(import 'java.io.File)


(defn cchange [folder]
	(doseq [ob (.listFiles folder)]
		(if (.isFile ob)
			;file renameTo(File dest)
			(let [wholename	(.getPath ob)]
				(if (re-matches #".*.html" wholename)
					;change to .php
					(let [newname 	(clojure.string/replace wholename #".html" ".php")]
						(do (prn newname)
							(.renameTo ob (File. newname))))))
			;folder
			(cchange ob))))



(cchange (File. "/home/thao/Downloads/p"))


