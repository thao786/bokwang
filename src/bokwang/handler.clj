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
	(GET "/meditation" [] (r/render "meditation.html"))
	(GET "/about" [] (r/render "about.html"))
	(GET "/gallery" [] (r/render "gallery.html"))

	(GET "/image/:name" [name] (io/resource (str "image/" name)))
	(GET "/file/:name" [name] (io/resource name))
	(GET "/bootstrap/css/:name" [name] (io/resource (str "bootstrap/css/" name)))
	(GET "/bootstrap/js/:name" [name] (io/resource (str "bootstrap/js/" name)))
	(GET "/bootstrap/fonts/:name" [name] (io/resource (str "bootstrap/fonts/" name)))
	(route/resources "/")
	(route/not-found "under construction"))

(def app
	(handler/site app-routes))
