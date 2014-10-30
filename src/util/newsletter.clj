(ns util.newsletter
  (:import (MailChimp)
		 EmailCheck))

(def apikey "cb96bb2b28ee56a84658dccff219b570-us9")
(def listId "38f29c5b2f")
;us9-2a97a67d37-65aef36710@inbound.mailchimp.com


(def mail-client (MailChimp.))

(defn subscribe [params]
	(if (= "" (params :dummy))
		;"humans"
		(let [email 	(params :email)
				fname 	(params :fname)
				lname 	(params :lname)
				emailValidator 	(EmailCheck.)]
			(if (.emailCheck emailValidator email)
				(.subscribe mail-client apikey listId email fname lname)))
			

		;"from bots" tell logs
		(prn "from bots")))

