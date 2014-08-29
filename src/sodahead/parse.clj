(ns sodahead.parse)

(defn mkmap 
	"make a vector recording the position each time this token appear in text,
	starting from start-pos"
	[text start-pos token label]
	(loop 	[index 	start-pos
			res 	[]]
		(let 	[pos 	(.indexOf text token index)]
			(if (neg? pos)
				res
				(let 	[newMap 	{:type label :pos pos}]
					(recur (inc pos) (conj res newMap)))))))

(defn removeQuote 
	"remove all quote in string to avoid confusion, 
	patch with strings of x's of the same length to preserve position"
	[s]
	(loop 	[text 	s]         
		(let 	[token 	(re-find #"\\+\"" text)  ;")
				]
			(if token
				(let 	[freq 	(dec (count token))
						patch 	(if (odd? freq)
									;if odd no of \, get rid of all
									(apply str (repeat (count token) "x"))
									;if even only replace \ keep " 
									(clojure.string/replace token #"\\" "x"))
						token-begin 	(.indexOf text token)
						newText (str 	(subs text 0 token-begin)
										patch
										(subs text (+ token-begin (count patch)) 
													(count text)))]
					(recur newText))
				text))))

(defn getClosingBrac 
	"get the position of the closing bracket/parenthesis
	that matches brac (assuming they're not the same)
	if there's none, return -1. watch out for strings"
	[text-to-search start-pos brac close-brac]
	(let 	[text 		(removeQuote text-to-search)
			strVec 		(mkmap text start-pos "\"" "quote")
			bracVec 	(mkmap text start-pos brac "brac")
			closeVec	(mkmap text start-pos close-brac "close")
			group 		(sort-by :pos (concat strVec bracVec closeVec))]
		(if (< (count closeVec) 1)
			-1
			(loop 	[array 	(vec (rest group))
					weight 	1
					strzone -1]
				(let 	[cur 		(get array 0)
						type 		(cur :type)
						new-weight 	(cond
										;if this is within a string, do nothing
										(= strzone 1) weight
										(= type "brac") (inc weight)
										(= type "close") (dec weight)
										:else weight)
						new-strzone	(if (= "quote" type)
										(* -1 strzone)
										strzone)
						new-array 	(vec (rest array))]
					(cond
						(= new-weight 0) (cur :pos)
						(= 0 (count new-array)) -1

						:else
						(recur new-array new-weight new-strzone)))))))


;when starting a new text, look for: 
; 	%{ code block } (avoid } in string)
; 	%variable
; 	%( statement ) 
(defn chop 
	"partition the text into interleaving chunks (vector) of code and pure text"
	[original-text]
	(let  	[icon 		"%"
			brac 		"{"
			close-brac 	"}"] 
		(loop 	[text 	original-text
			 	res 	[]]
			(if-let [token	(re-find #"%[\(\{a-zA-Z\_\-\!\$\%\&\*\?\|][^ \s\(\)\{\}\[\]\@\\\~\`\,\.\"\<\>\']*"  ;"
										text)]	
				(let 	[token-begin 		(.indexOf text token)
						trail-text 			(subs text 0 token-begin)
						trail-text-length 	(count trail-text)
						trail-text-block 	{:type "text" :content trail-text}
						token-length 		(count token)
						text-length 		(count text)
						token-end 			(+ token-length trail-text-length)]

					(cond
						;a %{
						(= 1 (.indexOf token brac))
						(let 	[close-pos 	(inc (getClosingBrac text token-begin brac close-brac))
								code 		(subs text token-begin close-pos)
								code-block 	{:type "bloc" :content code}
								code-length (count code)
								code-end 	(+ code-length trail-text-length)
								newText 	(subs text code-end text-length)
								newRes 		(conj res trail-text-block code-block)]
							(recur newText newRes))

						;a %(
						(= 1 (.indexOf token "("))
						(let 	[close-pos 	(inc (getClosingBrac text token-begin "(" ")"))
								code 		(subs text token-begin close-pos)
								code-block 	{:type "expr" :content code}
								code-length (count code)
								code-end 	(+ code-length trail-text-length)
								newText 	(subs text code-end text-length)
								newRes 		(conj res trail-text-block code-block)]
							(recur newText newRes))

						;a variable
						:else
						(let 	[newblock 	{:type "var" :content token}
								newText 	(subs text token-end text-length)]
							(recur newText (conj res trail-text-block newblock)))))

				;no more valid % token, end of file
				(let 	[lastblock 	{:type "text" :content text}]
					(conj res lastblock))))))

(def sl (char 92)) ;slash \