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
            (ring.middleware [multipart-params :as mp])
            [util.newsletter :as newsltr]
            [bokwang.donate :as donate]
            [bokwang.private :as private]
            [bokwang.user :as user]
            [bokwang.class :as c]
            [bokwang.doc :as doc]))


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
	(context "/zen-reflection" []
		(GET "/" [] (r/render "reflection-focusing.html"))
		(GET "/about-focusing" [] (r/render "reflection-focusing-abt-focusing.html"))
		(GET "/focus-attitude" [] (r/render "reflection-focusing-attitude.html")))
	(context "/buddhist-study" []
		(GET "/" [] (r/render "buddhist-study.html")))
	(context "/teacher" []
	    (GET "/" [] (r/render "kwang.html"))
		(GET "/taego" [] (r/render "about-taego.html"))
		(GET "/become-priest" [] (r/render "about-become-priest.html"))
		(GET "/gallery" [] (r/render "gallery.html")))
	(context "/zen" []	    
	    (GET "/" [] (r/render "zen.html"))
	    (GET "/prayers" [] (r/render "zen-prayers.html")))
	(GET "/contact" [] (r/render "contact.html"))
	(GET "/calendar" [] (r/render "calendar.html"))
	(GET "/donations" [] (r/render "donation.html"))

	(GET "/donate" [] (str @donate/tokens))
	;(POST "/donate" [token] (swap! donate/tokens conj token))
	(POST "/donate" request (str (:params request)))

	(POST "/subscribe" request (newsltr/subscribe (request :params)))

	(GET "/cookie" request (str request))

	(GET "/login" [] (r/render "private/login-form.html"))

	(context "/private" []
		(GET "/" request (private/first-view-get request))
		(POST "/" request (private/log-in request))) ;this expect a fb-session-id in request
	  
	(GET  "/test/:id" request (str request))

	(GET  "/upload-doc" request (r/render "private/doc-upload.html"))



	(GET  "/upload" [] (r/render "file.html"))
	(mp/wrap-multipart-params 
    	(POST "/upload" request (doc/handle-doc request)))

	(GET "/doc/:doc_id" request (r/render "private/view-document.html" 
			{:doc-id (-> request :params :doc_id) :cookie (((request :cookies) "zen") :value)}))

	(GET "/edit-doc/:doc_id" [doc_id] (r/render "private/edit-document.html" {:doc-id doc_id}))

	(GET "/edit-user/:user_id" [user_id] (r/render "private/edit-user.html" {:user-id user_id}))
	(mp/wrap-multipart-params 
	    	(POST "/edit-user" request (user/handle-user request)))

	; (GET "/class-regis" request (r/render "private/class-create.html"))
	; (mp/wrap-multipart-params 
	;     	(POST "/class-regis" request (c/create-zen-class request)))

	; (GET "/class-regis/:class_id" request (r/render "private/class-registration.html" {:class-id (-> request :params :class_id)}))
	; (mp/wrap-multipart-params 
	;     	(POST "/class-regis/:class_id" request (c/add-class-student request)))

	(GET "/file/:name" [name] (io/resource name))
	(mp/wrap-multipart-params 
    	(POST "/file" request (doc/cke-store-img request)))

	(POST "/delete-doc" request (doc/delete-doc (-> request :params :doc_id)))
	(POST "/delete-attachment" request (doc/delete-attachment (-> request :params :doc_id) (-> request :params :file_name)))

	
	(route/resources "/")
	(route/not-found (r/render "404.html")))

(def app
	(handler/site app-routes))
