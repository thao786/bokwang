(ns bokwang.search_doc
	(:import (java.io File)
			(java.sql.Date)
			(java.math BigInteger)
			(java.sql DriverManager))
	(:require [sodahead.render :as r]
            [bokwang.util :as util]
            [bokwang.session :as ses]
            [bokwang.private :as private]
            [bokwang.lib :as l]))

;{:category "", :update_date "", :uploader "", :content "", :submit "", :uload_date "", :search "yes", :title "", :level ""}
(defn handle-search [params]
	(str 9))