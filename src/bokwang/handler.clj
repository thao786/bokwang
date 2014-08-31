;lein new compojure bokwang
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
	    (GET "/why" [] "profile")
	    (GET "/how" [] "posts")
		(GET "/classes" [] "posts"))
	(context "/about" []
	    (GET "/" [] (r/render "about.html"))
	    (GET "/buddhism" [] "posts")
	    (GET "/bo-kwang-centre" [] "profile")
	    (GET "/kwang" [] "posts")
		(GET "/pat" [] "posts")
		(GET "/taego" [] "")
		(GET "/taego-monks" [] ""))
	(GET "/gallery" [] (r/render "gallery.html"))
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
