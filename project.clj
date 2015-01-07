(defproject bokwang "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :java-source-paths ["src/java"]
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [compojure "1.1.8"]
                 [com.ecwid/ecwid-mailchimp "2.0.1.0"]
                 [com.google.guava/guava "18.0"]
                 [commons-logging/commons-logging "1.1.1"]
                 [commons-validator/commons-validator "1.4.0"]
                 [commons-io/commons-io "2.4"]
                 [com.netflix.denominator/denominator-cli "4.3.3"]
                 [com.google.code.gson/gson "2.3"]
                 [org.apache.httpcomponents/httpclient "4.3.5"]
                 [org.apache.httpcomponents/httpcore "4.3.2"]
                 [org.xerial/sqlite-jdbc "3.8.7"]
                 [org.postgresql/postgresql "9.3-1102-jdbc41"]
                 [com.taoensso/carmine "2.9.0"]
                 [commons-codec/commons-codec "1.10"]]
  :plugins [[lein-ring "0.8.11"]]
  :ring {:handler bokwang.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring-mock "0.1.5"]]}})


