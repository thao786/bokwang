(ns bokwang.category
	(:import (java.sql.Date)
			(java.security SecureRandom)
			(java.math BigInteger)
			(java.sql DriverManager))
	(:use [clojure.java.shell :only [sh]])
	(:require [sodahead.render :as r]
            [clojure.java.io :as io]
            [bokwang.util :as util]
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
			cat-id (util/make-sense-str cat)
			conn 	(DriverManager/getConnection l/bokwang-db-url)
			stmt 	(.prepareStatement conn query)
			stmt 	(doto stmt (.setString 1 cat-id)
						(.setString 2 cat))
			dummy 	(.executeUpdate stmt)
			dummy (.close conn)]
		(r/render "private/category-control.html")))

(defn store-category-doc [doc-id cat-ids]
	(if cat-ids
		(let [query 	(str "INSERT INTO category (doc_id, category_id) VALUES(?, ?)")
				conn 	(DriverManager/getConnection l/bokwang-db-url)
				cat-id-vector (if (= clojure.lang.PersistentVector (type cat-ids))
								cat-ids
								(vector cat-ids))]
			(doall 
				(doseq [cat-id cat-id-vector]
					(let [stmt 	(.prepareStatement conn query)
							stmt 	(doto stmt (.setString 1 doc-id)
								(.setString 2 cat-id))]
						(.executeUpdate stmt)))
				(.close conn)))))

(defn get-category-name-from-id [category-id]
	(let [query 	(str "select category from category_list where id='" category-id "'")
			conn (DriverManager/getConnection l/bokwang-db-url)
			stmt (.createStatement conn)
			result (first (resultset-seq (.executeQuery stmt query)))
			dummy (.close conn)]
		(result :category)))

(defn get-category-from-doc-id [doc-id]
	(let [query 	(str "select category_id from category where doc_id='" doc-id "'")
			conn (DriverManager/getConnection l/bokwang-db-url)
			stmt (.createStatement conn)
			result (resultset-seq (.executeQuery stmt query))
			dummy (.close conn)
			category-id-list (distinct (map #(% :category_id) result))]
		(map #(get-category-name-from-id %) category-id-list)))







