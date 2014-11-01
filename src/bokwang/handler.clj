;lein new compojure bokwang
;nohup lein ring server &
; ps aux | grep server
; ssh thao@50.116.53.36
(ns bokwang.handler
  (:require [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [sodahead.render :as r]
            [clojure.java.io :as io]
            [util.newsletter :as newsltr]))

(defroutes app-routes
	(GET "/" [] (r/render "index.html"))
	(GET "/programs" [] (r/render "programs.html"))
	(context "/meditation" []
	    (GET "/" [] (r/render "meditation.html")) 
	    (GET "/structure" [] (r/render "meditation-structure.html")) 
	    (GET "/how" [] (r/render "meditation-how.html"))
		(GET "/class" [] (r/render "meditation-class.html"))
		(GET "/class/zi" [] (r/render "meditation-class-zi.html"))
		(GET "/class/zen-tool" [] (r/render "meditation-class-zen-tool.html"))
		(GET "/class/meditate-circle" [] (r/render "meditation-class-meditate-circle.html")))
	(context "/qigong" []
	    (GET "/" [] (r/render "qigong.html")) 
		(GET "/class" [] (r/render "qigong-class.html")))
	(context "/qigong-instructor-training" []
	    (GET "/" [] (r/render "instructor-training-about.html"))
	    (GET "/level-1" [] (r/render "instructor-training-lv1.html"))
	    (GET "/advanced" [] (r/render "instructor-training-advanced.html"))) 
	(context "/food-healing" []
		(GET "/" [] (r/render "food-healing.html")))
	(context "/about" []
	    (GET "/" [] (r/render "about-bokwang-centre.html"))
	    (GET "/bo-kwang-centre" [] (r/render "about-bokwang-centre.html"))
	    (GET "/kwang" [] (r/render "about-kwang.html"))
		(GET "/taego" [] (r/render "about-taego.html"))
		(GET "/other-temples" [] (r/render "about-other-taego-temples.html"))
		(GET "/become-priest" [] (r/render "about-become-priest.html"))
		(GET "/gallery" [] (r/render "gallery.html")))
	(context "/zen" []	    
	    (GET "/" [] (r/render "zen.html"))
	    (GET "/prayers" [] (r/render "zen-prayers.html"))
	    (GET "/zen-self" [] (r/render "zen-self.html")))
	(GET "/contact" [] (r/render "contact.html"))
	(GET "/calendar" [] (r/render "calendar.html"))
	(GET "/donations" [] (r/render "donation.html"))
	(context "/faqs" []
	    (GET "/" [] (r/render "faqs.html")) 
	    (GET "/meditation" [] "profile")
	    (GET "/qigong" [] "posts")
		(GET "/general" [] "posts"))



	(POST "/subscribe" request (newsltr/subscribe (request :params)))
	(GET "/test1" [name] "wats up")
	(GET "/test2" [name] (clojure.java.io/file "/home/thao/Test2.pdf"))
	(GET "/test" [name] (r/render "test.html"))

	(GET "/image/:name" [name] (io/resource (str "image/" name)))
	(GET "/file/:name" [name] (io/resource name))
	(GET "/bootstrap/css/:name" [name] (io/resource (str "bootstrap/css/" name)))
	(GET "/bootstrap/js/:name" [name] (io/resource (str "bootstrap/js/" name)))
	(GET "/bootstrap/fonts/:name" [name] (io/resource (str "bootstrap/fonts/" name)))
	(route/resources "/")
	(route/not-found "under construction :((((((("))

(def app
	(handler/site app-routes))








