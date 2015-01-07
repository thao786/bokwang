(ns bokwang.role
	(:import (java.io File)
			(java.sql DriverManager))
	(:use [clojure.java.shell :only [sh]])
	(:require [sodahead.render :as r]
            [clojure.java.io :as io]
            [bokwang.lib :as l]))

(defn promote-admin
	"make this user an admin"
	[user-id]
	(let [query 	(str "UPDATE users SET role='admin'
						WHERE userid='" user-id "'")
				
			conn (DriverManager/getConnection l/bokwang-db-url)
			stmt (.createStatement conn)
			dummy (.executeUpdate stmt query)]
		(.close conn)))






