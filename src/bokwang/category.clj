(ns bokwang.category
	(:import (java.sql.Date)
			(java.security SecureRandom)
			(java.math BigInteger)
			(java.sql DriverManager))
	(:use [clojure.java.shell :only [sh]])
	(:require [sodahead.render :as r]
            [clojure.java.io :as io]
            [bokwang.doc :as doc]
            [bokwang.lib :as l]))

(defn delete-category-list [cat-id]
	(let [query 	(str "delete from category_list where id=?")
			conn 	(DriverManager/getConnection l/bokwang-db-url)
			stmt 	(.prepareStatement conn query)
			stmt 	(doto stmt (.setString 1 cat-id))
			dummy 	(.executeUpdate stmt)
			dummy (.close conn)]
		"OK"))

(defn add-category-list [cat]
	(let [query 	(str "INSERT INTO category_list (id, category) VALUES(?, ?)")
			cat-id (doc/make-sense-str cat)
			conn 	(DriverManager/getConnection l/bokwang-db-url)
			stmt 	(.prepareStatement conn query)
			stmt 	(doto stmt (.setString 1 cat-id)
						(.setString 2 cat))
			dummy 	(.executeUpdate stmt)
			dummy (.close conn)]
		(r/render "private/category-control.html")))

(defn store-category-doc2 [doc-id cat-ids]
	(let [query 	(str "INSERT INTO category (doc_id, category_id) VALUES(?, ?)")
			conn 	(DriverManager/getConnection l/bokwang-db-url)]
		(doall 
			(doseq [cat-id cat-ids]
				(let [stmt 	(.prepareStatement conn query)
						stmt 	(doto stmt (.setString 1 doc-id)
							(.setString 2 cat-id))]
					(.executeUpdate stmt)))
			(.close conn))))

(defn store-category-doc [doc-id cat-ids]
	(let [query 	(str "INSERT INTO category (doc_id, category_id) VALUES(?, ?)")
			conn 	(DriverManager/getConnection l/bokwang-db-url)
			stmt 	(.prepareStatement conn query)
			stmt 	(doto stmt (.setString 1 doc-id)
						(.setString 2 cat-ids))
			dummy 	(.executeUpdate stmt)
			dummy (.close conn)]
		67))










