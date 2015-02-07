(ns bokwang.util
	(:import (java.sql.Date)
			(java.security SecureRandom)
			(java.math BigInteger)
			(java.sql DriverManager))
	(:use [clojure.java.shell :only [sh]]))

(defn make-sense-str [long-title]
	(let [short-title (if (< (.length long-title) 50) 
					long-title
					(subs long-title 0 50))
			filter-title (.replaceAll short-title "[^a-zA-Z0-9 ]" "")
			title (.replaceAll filter-title " " "_")
			now (System/currentTimeMillis)]
		(str title now)))

(defn concat-list [l]
	(if (> (count l) 0)
        (reduce #(str %1 " " %2) l)
        ""))


