(ns bokwang.private
	(:import (java.io File)
			(java.security SecureRandom)
			(java.math BigInteger)
			(java.sql DriverManager))
	(:use [clojure.java.shell :only [sh]])
	(:require [sodahead.render :as r]
            [bokwang.lib :as l]
            [taoensso.carmine :as car :refer (wcar)]
            [bokwang.session :as ses]))

(def server1-conn {:pool {} :spec {:host l/db-server :port 6379}})
(defmacro wcar* [& body] `(car/wcar server1-conn ~@body))

(def cookie-valid-period 30)

(defn get-userid
	"get the userid associated with this cookie from redis"
	[cookie]
	(if-let [result-map (wcar* (car/get cookie))]
		(result-map :user-id)))

(defn get-user-basic-info
	"get the user info map associated with this userid from table users"
	[userid]
	(let [query (str "select * from users where userid = '" userid "'")
			conn (DriverManager/getConnection l/bokwang-db-url)
			stmt (.createStatement conn)
			result (resultset-seq (.executeQuery stmt query))
			dummy (.close conn)]
		(if-let [record (first result)]
			record)))

(defn view-based-on-cookie
	"depends on the cookie, determine roles and render the correct view"
	[cookie]
	(if-let [user-id (get-userid cookie)]
		(if-let [user (get-user-basic-info user-id)]
			(do ;inject to redis
				(ses/cache cookie user)
				(if (= (:role user) "admin") 	;if admin, show admin view
					{:status 200
					 :cookies {"zen" {:value cookie}}
					 :body (r/render "private/view-admin.html" {:user user})}
					
					{:status 200
					 :cookies {"zen" {:value cookie}}
					 :body (r/render "private/view-member.html" {:user user})})))
		;no record wt such cookie, delete cookie and log in again
		{:status 200
		 :cookies {"zen" {:value "cookie" :max-age 1}}
		 :body (r/render "private/login-form.html")}))


(defn first-view-get [request]
	(if-let [zen-cookie-map ((request :cookies) "zen")]
		;has a cookie
		(view-based-on-cookie (zen-cookie-map :value))

		;1st visit, there is no zen cookie
		(r/render "private/login-form.html")))

(defn insert-fb-user [fb-info]
	(let [fb-id (str "fb-" (fb-info :fb-id))
		fb-profile (fb-info :fb-profile)
		fname (fb-info :fname)
		lname (fb-info :lname)
		query-insert-user (str "insert into users (userid, fb_profile, fname, lname) 
				values ('" fb-id "', '" fb-profile "', '" fname "', '" lname "')")
		conn (DriverManager/getConnection l/bokwang-db-url)
		stmt (.createStatement conn)
		dummy (.executeUpdate stmt query-insert-user)]
			(.close conn)))

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
				(let [random (SecureRandom.)
					gen-random (.toString (BigInteger. 100 random) 32)
					;get st like zen-cookie-eb66rg9f1cfbug4s6knr
					cookie (str "zen-cookie-" gen-random)
					user-id (str "fb-" fb-id)

					;store session in redis
					dummy (ses/cache cookie {:user-id user-id})

					;is this user in database?
					query-user-exist (str "SELECT userid from users where userid = '" user-id "'")
					conn (DriverManager/getConnection l/bokwang-db-url)
					stmt (.createStatement conn)
					existed-users (resultset-seq (.executeQuery stmt query-user-exist))
					dummy (.close conn)

					dummy (if (= 0 (count existed-users))
								(insert-fb-user fb-info))
					]

					(view-based-on-cookie cookie))	;send cookie and render view 

				"there is something wrong with your facebook session ID. Please try again."))

		;fb ID doesnt work
		"there is something wrong with your facebook session ID. Please try again."))





