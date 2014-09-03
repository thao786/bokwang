;lein new compojure bokwang
;nohup lein ring server &
; ps aux | grep server
(ns bokwang.handler
  (:require [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [sodahead.render :as r]
            [clojure.java.io :as io]))

(defroutes app-routes
	(GET "/" [] (r/render "index.html"))
	(GET "/programs" [] (r/render "programs.html"))
	(context "/meditation" []
	    (GET "/" [] (r/render "meditation.html")) 
	    (GET "/how" [] (r/render "meditation-how.html")) 
		(GET "/class" [] (r/render "meditation-class.html")))
	(context "/about" []
	    (GET "/" [] (r/render "about.html"))
	    (GET "/buddhism" [] "posts")
	    (GET "/bo-kwang-centre" [] "profile")
	    (GET "/kwang" [] "posts")
		(GET "/pat" [] "posts")
		(GET "/taego" [] "")
		(GET "/taego-monks" [] ""))
	(GET "/gallery" [] (r/render "gallery.html"))
	(GET "/contact" [] (r/render "contact.html"))
	(GET "/donation" [] (r/render "donation.html"))
	(context "/faqs" []
	    (GET "/" [] (r/render "faqs.html")) 
	    (GET "/meditation" [] "profile")
	    (GET "/qigong" [] "posts")
		(GET "/general" [] "posts"))

	(GET "/image/:name" [name] (io/resource (str "image/" name)))
	(GET "/file/:name" [name] (io/resource name))
	(GET "/bootstrap/css/:name" [name] (io/resource (str "bootstrap/css/" name)))
	(GET "/bootstrap/js/:name" [name] (io/resource (str "bootstrap/js/" name)))
	(GET "/bootstrap/fonts/:name" [name] (io/resource (str "bootstrap/fonts/" name)))
	(route/resources "/")
	(route/not-found "under construction"))

(def app
	(handler/site app-routes))
