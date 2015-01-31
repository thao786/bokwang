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
            [bokwang.user :as u]
            [bokwang.lib :as l]))

(defn store-file
	"return the file name of uploaded file.
	if same name existed, add _2"
	[file filename upload-folder-path]
	(let [upload-file (File. (str upload-folder-path filename))
			upload-folder (File. upload-folder-path)]
		(if (FileUtils/directoryContains upload-folder upload-file)
			;there's already a file wt the same name
			(if (FileUtils/contentEquals file upload-file)
				filename
				(let [file-name-only (FilenameUtils/getBaseName filename)
						file-extention (FilenameUtils/getExtension filename)
						new-file-name-only (str file-name-only "_2")
						new-file-name (str new-file-name-only "." file-extention)
						dummy (FileUtils/copyFile file 
									(File. (str upload-folder-path new-file-name)))]
					new-file-name))
			(do (FileUtils/copyFile file upload-file)
				filename))))

(defn cke-store-img
	"upload image to articles folder, return the link"
	[request]
	(if-let [file (-> request :params :file :tempfile)]
		(let [filename (-> request :params :file :filename)
				src (str l/image-host "/articles/" (store-file file filename l/article-upload-img))]
			(str "<img src=\"" src "\">"))
		"Could not get file from request. Please try again."))

(defn store-doc
	"store in postgres"
	[request title level content category now]
	(let [	random (SecureRandom.)
			doc-id (str "doc-" (.toString (BigInteger. 50 random) 32))
			conn 	(DriverManager/getConnection l/bokwang-db-url)
			query 	"INSERT INTO doc (doc_id, title, content, uploader, category, level, upload_date, update_date) 
					VALUES(?, ?, ?, ?, ?, ?, ?, ?)"
			stmt 	(.prepareStatement conn query)
			stmt 	(doto stmt
						(.setString 1 doc-id)
						(.setString 2 title)
						(.setString 3 content)
						(.setString 4 (ses/get-cache-req request :user-id))
						(.setString 5 category)
						(.setInt 6 level)
						(.setDate 7 now)
						(.setDate 8 now))
			dummy 	(.executeUpdate stmt)
			dummy (.close conn)]
		doc-id))

(defn edit-doc
	"store in postgres"
	[request doc-id title level content category now]
	(let [	conn 	(DriverManager/getConnection l/bokwang-db-url)
			query 	(str "UPDATE doc SET title = ?, content = ?, category = ?, level = ?, update_date = ? where doc_id = '" doc-id "'")
			stmt 	(.prepareStatement conn query)
			stmt 	(doto stmt
						(.setString 1 title)
						(.setString 2 content)
						(.setString 3 category)
						(.setInt 4 level)
						(.setDate 5 now))
			dummy 	(.executeUpdate stmt)
			dummy (.close conn)]
		"edit"))

(defn check-category [categories]
	(try
		(reduce (fn [a b] (str a " " b)) categories)
    	(catch Exception e categories)))

(defn check-file [file]
	(if (= "" (file :filename))
		false
		(if (> (file :size) 0)
			true 
			false)))

(defn handle-single-upload-file
	"expect a file map from html client, store in filesystem and update database"
	[doc-id file]
	(if (check-file file)
		(let [filename (store-file (file :tempfile) (file :filename) l/article-upload-files)
			query 	(str "INSERT INTO upload_files (doc_id, name) VALUES('" doc-id "', ?)")
			conn 	(DriverManager/getConnection l/bokwang-db-url)
			stmt 	(.prepareStatement conn query)
			stmt 	(doto stmt (.setString 1 filename))
			dummy 	(.executeUpdate stmt)
			dummy (.close conn)]
		filename)))

(defn handle-upload-files [doc-id files]
	(condp = (type files) 
		clojure.lang.PersistentVector
			(map #(handle-single-upload-file doc-id %) files)
		clojure.lang.PersistentArrayMap 
			(handle-single-upload-file doc-id files)
		"void"))

(defn delete-doc [doc-id]
	(let [query 	(str "delete from doc where doc_id=?")
			conn 	(DriverManager/getConnection l/bokwang-db-url)
			stmt 	(.prepareStatement conn query)
			stmt 	(doto stmt (.setString 1 doc-id))
			dummy 	(.executeUpdate stmt)

			query 	(str "delete from upload_files where doc_id=?")
			stmt 	(.prepareStatement conn query)
			stmt 	(doto stmt (.setString 1 doc-id))
			dummy 	(.executeUpdate stmt)
			dummy (.close conn)]
		"OK"))

(defn delete-attachment [doc-id attachement-name]
	(let [query 	(str "delete from upload_files where doc_id=? and name=?")
			conn 	(DriverManager/getConnection l/bokwang-db-url)
			stmt 	(.prepareStatement conn query)
			stmt 	(doto stmt 
						(.setString 1 doc-id)
						(.setString 2 attachement-name))
			dummy 	(.executeUpdate stmt)
			dummy (.close conn)]
		"OK"))

(defn handle-doc
	"store in postgres"
	[request]
	(let [params (request :params)
			title (params :title)
			level (u/check-level (params :level))
			content (params :content)
			category (check-category (params :category))
			now 	(java.sql.Date. (.getTime (java.util.Date.)))]
		(if-let [doc-id ((request :params) :doc_id)]
			(do (edit-doc request doc-id title level content category now)
				(doall (handle-upload-files doc-id (params :files)))
					{:status 302
			   		:headers {"Location" (str "http://lotus-zen.com/edit-doc/" doc-id)}
			   		:body (r/render "private/edit-document.html" {:doc-id doc-id})})

			(let [doc-id (store-doc request title level content category now)]
				(do (doall (handle-upload-files doc-id (params :files)))
						{:status 302
				   		:headers {"Location" (str "http://lotus-zen.com/edit-doc/" doc-id)}
				   		:body (r/render "private/edit-document.html" {:doc-id doc-id})})))))





















