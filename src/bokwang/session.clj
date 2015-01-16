(ns bokwang.session
	(:import (java.security SecureRandom)
			(java.math BigInteger))
	(:use [clojure.java.shell :only [sh]])
	(:require [sodahead.render :as r]
            [bokwang.lib :as l]
            [taoensso.carmine :as car :refer (wcar)]))

(def server1-conn {:pool {} :spec {:host "50.116.53.36" :port 6379}})
(defmacro wcar* [& body] `(car/wcar server1-conn ~@body))


(defn cache
	"merge this map to the one associated wt this cookie in redis"
	[cookie data-map]
	(let [cache (wcar* (car/get cookie))
		new-cache (merge cache data-map)]
		(wcar* (car/set cookie new-cache))))

(defn get-cache-req [request attr]
	(let [cookie (((request :cookies) "zen") :value)]
		(if-let [cached-map (wcar* (car/get cookie))]
			(cached-map attr)
			"couldn't get user-id from redis")))














