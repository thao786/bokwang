(ns bokwang.class
	(:import (java.io File)
			(java.sql.Date)
			(java.security SecureRandom)
			(java.math BigInteger)
			(java.sql DriverManager))
	(:use [clojure.java.shell :only [sh]])
	(:require [sodahead.render :as r]
            [clojure.java.io :as io]
            [bokwang.session :as ses]
            [bokwang.user :as u]
            [bokwang.lib :as l]))

(defn create-zen-class [request]
	(if-let [title (-> request :params :title)]
		(let [location (-> request :params :title)
				comment (-> request :params :comment)
				now (java.sql.Date. (.getTime (java.util.Date.)))
				random (SecureRandom.)
				class-id (str "class-" (.toString (BigInteger. 50 random) 32))

				query 	"INSERT INTO classes (id, title, location, creator, time, comment) 
						VALUES(?, ?, ?, ?, ?, ?)"
				conn 	(DriverManager/getConnection l/bokwang-db-url)		
				stmt 	(.prepareStatement conn query)
				stmt 	(doto stmt
							(.setString 1 class-id)
							(.setString 2 title)
							(.setString 3 "57 Gorevale Rd, Brampton")
							(.setString 4 "Bup Moon Pat")
							(.setDate 5 now)
							(.setString 6 comment))
				dummy 	(.executeUpdate stmt)
				dummy (.close conn)]
			(r/render "private/class-registration.html" 
				{:class-id class-id :title title :location location :time now}))))











