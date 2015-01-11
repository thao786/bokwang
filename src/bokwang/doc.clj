(ns bokwang.doc
	(:import (java.io File)
			(java.sql.Date)
			(java.security SecureRandom)
			(java.math BigInteger)
			(java.sql DriverManager)
			(org.apache.commons.io FileUtils))
	(:use [clojure.java.shell :only [sh]])
	(:require [sodahead.render :as r]
            [clojure.java.io :as io]
            [bokwang.lib :as l]))

(defn store-doc
	"store in postgres"
	[request]
	(let [params (request :params)
			title (params :title)
			content (params :content)
			file (-> params :file :tempfile)
			category (params :category)
			now 	(java.sql.Date. (.getTime (java.util.Date.)))
			random (SecureRandom.)
			doc-id (str "doc-" (.toString (BigInteger. 50 random) 32))

			conn 	(DriverManager/getConnection l/bokwang-db-url)
			query 	"INSERT INTO doc (doc_id, title, content, uploader, category, level, upload_date) 
					VALUES(?, ?, ?, ?, ?, ?, ?)"
			stmt 	(.prepareStatement conn query)
			stmt 	(doto stmt
						(.setString 1 doc-id)
						(.setString 2 title)
						(.setString 3 content)
						(.setString 4 "thao")
						(.setString 5 category)
						(.setInt 6 4)
						(.setDate 7 now))
			dummy 	(.executeUpdate stmt)
			dummy (.close conn)]
		(str request)))








(defn a [request]
	(let [file (-> request :params :file :tempfile)]
		(FileUtils/copyFile file (File. "/home/thao/sqlite.clj"))))
