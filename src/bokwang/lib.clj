(ns bokwang.lib)


;(def static-file-host "http://lotus-zen.com/file")
(def static-file-host "")

;(def host "http://lotus-zen.com")
(def host "http://localhost:3000")

(def bootstrap-host "http://lotus-zen.com/bootstrap")

;(def image-host "http://lotus-zen.com/image")
(def image-host "image")


(def fb-verify-server "http://lotus-zen.com/fb-info.php")

(def db-server "50.116.53.36")
(def server1-conn {:pool {} :spec {:host "50.116.53.36" :port 6379}})


(def bokwang-db-url "jdbc:postgresql://50.116.53.36:5432/bokwang?user=thao&password=fall2010")

(def article-upload-img "/home/thao/bokwang/resources/image/articles/")
(def article-upload-files "/home/thao/bokwang/article-upload/")

(def view-doc-path (str host "/doc/"))
(def edit-doc-path (str host "/edit-doc/"))
(def edit-user-path (str host "/edit-user/"))

(def uploaded-file-path "http://lotus-zen.com/article-upload/")




