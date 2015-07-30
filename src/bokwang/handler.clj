;lein new compojure bokwang
;nohup lein ring server &
; ps aux | grep server
(ns bokwang.handler
	(:import (java.io File)
		(org.apache.commons.io FileUtils))

	(:require [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [sodahead.render :as r]
            [clojure.java.io :as io]
            (ring.middleware [multipart-params :as mp])))


(defroutes app-routes
	(GET "/" [] (r/render "index.html"))
	(GET "/index.html" [] (r/render "index.html"))

	(GET "/programs.html" [] (r/render "programs.html"))

	(GET "/meditation.html" [] (r/render "meditation.html"))
    (GET "/meditation-structure.html" [] (r/render "meditation-structure.html")) 
    (GET "/meditation-how.html" [] (r/render "meditation-how.html"))
	(GET "/meditation-class.html" [] (r/render "meditation-class.html"))
	(GET "/meditation-class-zi.html" [] (r/render "meditation-class-zi.html"))
	(GET "/meditation-class-zen-tool.html" [] (r/render "meditation-class-zen-tool.html"))
	(GET "/meditation-class-meditate-circle.html" [] (r/render "meditation-class-meditate-circle.html"))


    (GET "/qigong.html" [] (r/render "qigong.html"))
	(GET "/qigong-class.html" [] (r/render "qigong-class.html"))

    (GET "/instructor-training-about.html" [] (r/render "instructor-training-about.html"))
    (GET "/instructor-training-lv1.html" [] (r/render "instructor-training-lv1.html"))
    (GET "/instructor-training-advanced.html" [] (r/render "instructor-training-advanced.html"))

	(GET "/food-healing.html" [] (r/render "food-healing.html"))

	(GET "/reflection-focusing.html" [] (r/render "reflection-focusing.html"))
	(GET "/reflection-focusing-abt-focusing.html" [] (r/render "reflection-focusing-abt-focusing.html"))
	(GET "/reflection-focusing-attitude.html" [] (r/render "reflection-focusing-attitude.html"))

	(GET "/buddhist-study.html" [] (r/render "buddhist-study.html"))

    (GET "/hae-kwang.html" [] (r/render "hae-kwang.html"))
	(GET "/about-taego.html" [] (r/render "about-taego.html"))
	(GET "/about-become-priest.html" [] (r/render "about-become-priest.html"))
	(GET "/gallery.html" [] (r/render "gallery.html"))

    (GET "/zen.html" [] (r/render "zen.html"))
    (GET "/zen-prayers.html" [] (r/render "zen-prayers.html"))

	(GET "/contact.html" [] (r/render "contact.html"))
	(GET "/calendar.html" [] (r/render "calendar.html"))
	(GET "/donation.html" [] (r/render "donation.html"))

	(GET "/image/:id" [id] (io/file (str "resources/image/" id)))
	(GET "/csssheet.css" [id] (io/file "resources/csssheet.css"))

	(route/resources "/")
	(route/not-found (r/render "404.html")))

(def app
	(handler/site app-routes))
