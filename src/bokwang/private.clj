(ns bokwang.private
	(:import (java.io File)
			(java.security SecureRandom)
			(java.math BigInteger))
	(:use [clojure.java.shell :only [sh]])
	(:require [sodahead.render :as r]
            [clojure.java.io :as io]
            [bokwang.lib :as l]))

(def random (SecureRandom.))
(def cookie-period 30)

(defn first-view-get [request]
	(if-let [zen-cookie (-> request :cookie :zen)]
		;visited before
		(str "hello old friend")

		;1st visit, there is no zen cookie
		(r/render "private/login-form.html")))


(defn first-view-post-fbID
	"this function only get called to send new fb-session tokens
	coming from a form, post request"
	[request]
	(if-let [fb-session (-> request :params :fb_session)]
		;check if fb-session is valid, then assign a zen session token
		(let [url (str l/fb-verify-server "?fb-session=" fb-session)
				fb-info-str (:out (sh "curl" "-X" "GET" url))
				fb-info (read-string fb-info-str)]
			(if-let [fb-id (fb-info :fb-id)]
				;assign a zen cookie
				(let [gen-random (.toString (BigInteger. 100 random) 32)
					;get st like zen-cookie-eb66rg9f1cfbug4s6knr
					gen-random-cookie (str "zen-cookie-" gen-random) 

					;store in database
					]
					;send cookie and render view 
					gen-random-cookie)

				"there is something wrong with your facebook session ID. Please try again."))

		;fb ID doesnt work
		"there is something wrong with your facebook session ID. Please try again."))


(defn first-vew-get
	"depends on the session-id, render the correct view"
	[session-id] 
	"")

; {"fbm_278376725619348" {:value "base_domain=.lotus-zen.com"}, "ring-session" {:value "f15467ce-2dd2-453b-8514-3ce74ff296fc"}, 
; "PHPSESSID" {:value "hjvovvkd8u2jejr1h9l1v2rvg5"}, 
; "fbsr_278376725619348" 
; 	{:value "v7tie5Isr5R-FZXZyVgp-CSjtP9Ut72TEpLFoEiGvRI.eyJhbGdvcml0aG0iOiJITUFDLVNIQTI1NiIsImNvZGUiOiJBUUFBU3UzTm5ubkhfeGtZTXFyNnVPZGNUQ2g5Vl9mMnAwNDRiNl9rU3BqNXRmLTl3ekFoSFdZX2NpLUJMNTdSdE9FS29oVktpQ1R3QTkzdnZOVHplcTdSWDhTejBrNzNVX0kzNEZRWU1BYTBCMHRQVml1SEI4dU9wNWVvUENESjBENWwyekpmQlBFZmxXYlgyTVpfSFdsYzVBTlZmQkNiNmhwcWVrM3pvaE43bGxfLVl1TnE1N1FITGJ6eDJQU1NnNHhTTnphMFZNRXkyNGJCOE9Nc0d6MjN2dGVIQm9LZFVQWHpveFk2UTExMHdlX2prTlZEbU9KQUdvZmVtZlVSRjZuYzNYYjBvVVlkbFhNYnFQU2JNZmFSSFlZWTAwYnQ4Sk9IYzZHUkNSMFRaaUd2X2YxOUxHaHp1UDB4NEpWZ0luN3Mxa05GS25BQVFBN0FabEJ2bzhkeiIsImlzc3VlZF9hdCI6MTQxOTIxMDYwNiwidXNlcl9pZCI6Ijc5MzU4NTkwNDAzMzk1MSJ9"}}