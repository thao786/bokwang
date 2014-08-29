(ns sodahead.prep
	(:require 	[sodahead.parse :as p]
				[clojure.java.io :as io]))

(defn get-file-content
	"strip off quote and get the file's content"
	[file-name-within-quote]
	(let 	[err-msg (str "Cannot find " name ". Please make sure its name is surounded by double quote and can be found by slurp.")
			len 	(count file-name-within-quote)]
		(cond
			(= 0 len) ""

			(< len 3) err-msg

			:else
			(let 	[name 	(subs file-name-within-quote 1 
							(dec (count file-name-within-quote)))
					content 	(try (slurp (io/resource name)) (catch Exception e nil))]
				(if content 
					content nil)))))

(defn get-included
	"recursively retrieve all included files and inject plain concated content"
	[original-text]
	(loop 	[text original-text]
		(if-let [includeToken 		(re-find #"%include[\s]*\{" text)]
			(let [include-pos 		(.indexOf text includeToken)
				matching-sign-pos	(p/getClosingBrac text include-pos "{" "}")
				text-length 		(count text)
					missing-Err-Msg 	"No include matching sign % found"
				]
				(if (neg? matching-sign-pos)
					(str 	(subs text 0 include-pos) 
							missing-Err-Msg
							(subs text include-pos text-length))
					(let [first-brac-pos 	(.indexOf text "{" include-pos)
						file-list-start 	(inc first-brac-pos)
						file-list-string 	(.trim (subs text file-list-start matching-sign-pos))
						file-names 			(.split file-list-string "[ \n\t]+")
						files-content-vector		(map get-file-content file-names)
						files-dump 			(apply str files-content-vector)
						]
						(recur (str 	(subs text 0 include-pos)
								files-dump
								(subs text (inc matching-sign-pos) text-length))))))
			text)))

;produce (try code (catch Exception e (str e "" code)))
(defn wrap-exception [code]
	(str " (try " code " (catch Exception e))\n"))

(defn morph-into-code
	"depends on the type of code block, wrap it in appropriate handler"
	[single-raw-data-chunk]
	(let [type 	(single-raw-data-chunk :type)
		data 	(single-raw-data-chunk :content)
		data-length 	(count data)
		escaped-data 	(.replace data "\\" "\\\\")]
		(cond
			(= type "text")
			(let [escaped-quote-data (.replace escaped-data "\"" "\\\"")]
				(str " (str \"" escaped-quote-data "\")\n"))

			(= type "var")
			(let [variable (subs data 1 data-length)]
				(str variable))

			(= type "expr")
			(let [function (subs data 1 data-length)]
				(wrap-exception function))

			(= type "bloc")
			(let 	[code 	(subs data 2 (dec data-length))
					do-bloc 	(str " (do " code ")")]
				(wrap-exception do-bloc)))))

(defn get-require-code
	"get code as it is, don't wrap in try catch"
	[require-block]
	(let [type (require-block :type)
		data (require-block :content)]
		(cond
			(= type "expr")
			(subs data 1 (count data))

			(= type "bloc")
			(subs data 2 (dec (count data)))

			:else
			(throw (Exception. "Sodahead Exception: require code must be in a block or expression.")))))

(defn get-def-str 
	"return the string (def variable value)"
	[params key-word]
	(let [value 	(params key-word)
		 variable	(name key-word)]
		(str " (def " variable " " value ")\n")))

(defn mk-defs
	"generate independent def expression for each parameters"
	[params]
	(if-let [key-list (keys params)]
		(let [key-vector 	(vec key-list)
			 def-vector 	(map (partial get-def-str params) key-vector)]
			(apply str def-vector))))

(defn wrap-do
	"group together imported libraries 
	then wrap all following code blocks in a huge function"
	[code-vector]
	(str "(defn render [params]\n(let [blocks ["
		(apply str code-vector) "]]"
		"\n(apply str blocks)))"))