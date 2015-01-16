(ns bokwang.doc
	(:import (java.io File)
			(java.sql.Date)
			(java.security SecureRandom)
			(java.math BigInteger)
			(java.sql DriverManager)
			(org.apache.commons.io FilenameUtils)
			(org.apache.commons.io FileUtils))
	(:use [clojure.java.shell :only [sh]])
	(:require [sodahead.render :as r]
            [clojure.java.io :as io]
            [bokwang.session :as ses]
            [bokwang.lib :as l]))

(defn insert-article-img
	"return the file name of uploaded file.
	if same name existed, add -2"
	[file filename]
	(let [upload-file (File. (str l/article-upload-img filename))
			upload-folder (File. l/article-upload-img)
			existed-file (File. (str l/article-upload-img filename))]
		(if (FileUtils/directoryContains upload-folder existed-file)
			;there's already a file wt the same name
			(if (FileUtils/contentEquals file existed-file)
				filename
				(let [file-name-only (FilenameUtils/getBaseName filename)
						file-extention (FilenameUtils/getExtension filename)
						new-file-name-only (str file-name-only "_2")
						new-file-name (str new-file-name-only "." file-extention)
						dummy (FileUtils/copyFile file 
									(File. (str l/article-upload-img new-file-name)))]
					new-file-name))

			(do (FileUtils/copyFile file upload-file)
				filename))))

(defn cke-store-img
	"upload image to articles folder, return the link"
	[request]
	(if-let [file (-> request :params :file :tempfile)]
		(let [filename (-> request :params :file :filename)
				src (str l/image-host "/articles/" (insert-article-img file filename))]
			(str "<img src=\"" src "\">"))
		"Could not get file from request. Please try again."))

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
						(.setString 4 (ses/get-cache-req request :user-id))
						(.setString 5 category)
						(.setInt 6 4)
						(.setDate 7 now))
			dummy 	(.executeUpdate stmt)
			dummy (.close conn)]
		(ses/get-cache-req request :user-id)))



