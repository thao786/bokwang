(ns bokwang.private
	(:import (java.io File)
			(java.security SecureRandom)
			(java.math BigInteger)
			(java.sql DriverManager))
	(:use [clojure.java.shell :only [sh]])
	(:require [sodahead.render :as r]
            [clojure.java.io :as io]
            [bokwang.lib :as l]))

(def random (SecureRandom.))
(def cookie-valid-period 30)

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
					cookie (str "zen-cookie-" gen-random) 

					;store in database
					query 	(str "INSERT INTO session (cookie, userid) VALUES ('" 
									cookie "', '" fb-id "')")
					conn (DriverManager/getConnection l/bokwang-db-url)
					; stmt (.createStatement conn)
					; dummy (.executeUpdate stmt query)
					dummy (.close conn)
					]
					;send cookie and render view 
					conn)

				"there is something wrong with your facebook session ID. Please try again."))

		;fb ID doesnt work
		"there is something wrong with your facebook session ID. Please try again."))


(defn first-vew-get
	"depends on the session-id, render the correct view"
	[session-id]
	"")
