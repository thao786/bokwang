(def pages [
	"index.html"
	"programs.html"
	"meditation.html"
	"meditation-structure.html"
	"meditation-how.html"
	"meditation-class.html"
	"meditation-class-zi.html"
	"meditation-class-zen-tool.html"
	"meditation-class-meditate-circle.html"
	"qigong.html"
	"qigong-class.html"
	"instructor-training-about.html" 
	"instructor-training-lv1.html" 
	"instructor-training-advanced.html"
	"food-healing.html"
	"reflection-focusing.html"
	"reflection-focusing-abt-focusing.html"
	"reflection-focusing-attitude.html"
	"buddhist-study.html"
	"hae-kwang.html"
	"about-taego.html"
	"about-become-priest.html"
	"gallery.html"
	"zen.html"
	"zen-prayers.html"
	"contact.html"
	"calendar.html"
	"donation.html"
	"404.html"
])

(def host "localhost:3000/")
(def local "/Users/thao/Downloads/pro/bokwang/static/")

;get all content and pipe into files

(defn gen
	(doseq [page pages]
	(let [page-path (str host page)
			local-path (str local page)
			content (:out (sh "curl" page-path))]
		(spit local-path content))))








