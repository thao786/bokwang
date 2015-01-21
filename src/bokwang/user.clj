(ns bokwang.user
	(:import (java.io File)
			(java.sql DriverManager))
	(:use [clojure.java.shell :only [sh]])
	(:require [sodahead.render :as r]
            [clojure.java.io :as io]
            [bokwang.lib :as l]))

(defn edit-user [request user-id fname lname phone level alias comment email]
	(let [query (str "UPDATE users SET fname=?, lname=?, phone=?, level=?, alias=?, comment=?, email=? where userid = '" user-id "'")
			conn 	(DriverManager/getConnection l/bokwang-db-url)
			stmt 	(.prepareStatement conn query)
			stmt 	(doto stmt
						(.setString 1 fname)
						(.setString 2 lname)
						(.setString 3 phone)
						(.setInt 4 (Integer/parseInt level))
						(.setString 5 alias)
						(.setString 6 comment)
						(.setString 7 email))
			dummy 	(.executeUpdate stmt)
			dummy (.close conn)]
		"done"))

(defn store-user [request]
	(let [fname (-> request :params :fname)
			lname (-> request :params :lname)
			phone (-> request :params :phone)
			level(-> request :params :level)
			alias(-> request :params :alias)
			comment(-> request :params :comment)
			email (-> request :params :email)]
		(if-let [user-id (-> request :params :user_id)]
			(edit-user request user-id fname lname phone level alias comment email)
			"new user")))

(defn promote-admin
	"make this user an admin"
	[user-id]
	(let [query 	(str "UPDATE users SET role='admin'
						WHERE userid='" user-id "'")
				
			conn (DriverManager/getConnection l/bokwang-db-url)
			stmt (.createStatement conn)
			dummy (.executeUpdate stmt query)]
		(.close conn)))



