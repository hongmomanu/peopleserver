(defproject peopleserver "0.1.0-SNAPSHOT"

  :description "FIXME: write description"
  :url "http://example.com/FIXME"

  :dependencies [[org.clojure/clojure "1.7.0-RC1"]
                 [selmer "0.8.2"]
                 [com.taoensso/timbre "3.4.0"]
                 [com.taoensso/tower "3.0.2"]
                 [markdown-clj "0.9.66"]
                 [environ "1.0.0"]
                 [compojure "1.3.4"]
                 [ring/ring-defaults "0.1.5"]
                 [ring/ring-session-timeout "0.1.0"]
                 [metosin/ring-middleware-format "0.6.0"]
                 [metosin/ring-http-response "0.6.2"]
                 [bouncer "0.3.2"]
                 [prone "0.8.2"]
                 [org.clojure/tools.nrepl "0.2.10"]
                 [ring-server "0.4.0"]
                 [ragtime "0.3.8"]
                 [org.clojure/java.jdbc "0.3.7"]
                 [instaparse "1.4.0"]
                 [yesql "0.5.0-rc2"]
                 [net.sourceforge.jtds/jtds "1.3.1"]

                 [org.clojure/data.json "0.2.6"]
                 [http-kit "2.1.16"]
                 [clj-time "0.8.0"]
                 [com.belerweb/pinyin4j "2.5.0"]
                 [cheshire "5.5.0"]


                 [pjstadig/utf8 "0.1.0"]

                 [lib-noir "0.9.1"]
                 [korma "0.4.0"]
                 [com.oracle/ojdbc6 "11.2.0.3"]
                 [com.h2database/h2 "1.4.182"]]

  :min-lein-version "2.0.0"
  :uberjar-name "peopleserver.jar"
  :jvm-opts ["-server"]

;;"-Dfile.encoding=GBK"
;;enable to start the nREPL server when the application launches
;:env {:repl-port 7001}

  :main peopleserver.core

  :plugins [[lein-ring "0.9.1"]
            [lein-environ "1.0.0"]
            [lein-ancient "0.6.5"]
            [ragtime/ragtime.lein "0.3.8"]]
  

  
  :ring {:handler peopleserver.handler/app
         :init    peopleserver.handler/init
         :destroy peopleserver.handler/destroy
         :uberwar-name "peopleserver.war"}

  
  :ragtime
  {:migrations ragtime.sql.files/migrations
   :database "jdbc:h2:./site.db"}


  :repositories [
                  ["java.net" "http://download.java.net/maven/2"]
                  ["nexus" "https://code.lds.org/nexus/content/groups/main-repo"]
                  ["sonatype" {:url "http://oss.sonatype.org/content/repositories/releases"
                               ;; If a repository contains releases only setting
                               ;; :snapshots to false will speed up dependencies.
                               :snapshots false
                               ;; Disable signing releases deployed to this repo.
                               ;; (Not recommended.)
                               :sign-releases false
                               ;; You can also set the policies for how to handle
                               ;; :checksum failures to :fail, :warn, or :ignore.
                               :checksum :fail
                               ;; How often should this repository be checked for
                               ;; snapshot updates? (:daily, :always, or :never)
                               :update :always
                               ;; You can also apply them to releases only:
                               :releases {:checksum :fail :update :always}}]
                  ;; Repositories named "snapshots" and "releases" automatically
                  ;; have their :snapshots and :releases disabled as appropriate.
                  ;; Credentials for repositories should *not* be stored
                  ;; in project.clj but in ~/.lein/credentials.clj.gpg instead,
                  ;; see `lein help deploying` under "Authentication".
                  ]
  
  
  
  
  :profiles
  {:uberjar {:omit-source true
             :env {:production true}
             
             :aot :all}
   :dev {:dependencies [[ring-mock "0.1.5"]
                        [ring/ring-devel "1.3.2"]
                        [pjstadig/humane-test-output "0.7.0"]
                        ]
         :source-paths ["env/dev/clj"]
         
         
         
         :repl-options {:init-ns peopleserver.core}
         :injections [(require 'pjstadig.humane-test-output)
                      (pjstadig.humane-test-output/activate!)]
         :env {:dev true}}})
