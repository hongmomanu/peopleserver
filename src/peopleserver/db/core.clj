(ns peopleserver.db.core
  (:use korma.core
        [korma.db :only [defdb with-db]])
  (:require
    [yesql.core :refer [defqueries]]

    [clojure.java.io :as io]))

(def db-store (str (.getName (io/file ".")) "/site.db"))

(def datapath (str (.getName (io/file ".")) "/"))

(defn get-config-prop []
  (let [filename (str datapath "server.config")]
    (read-string (slurp filename))
    )
  )

(def db-spec
  {:classname   "org.h2.Driver"
   :subprotocol "h2"
   :subname     db-store
   :make-pool?  true
   :naming      {:keys   clojure.string/lower-case
                 :fields clojure.string/upper-case}})

(def db-sqlserver {:classname "net.sourceforge.jtds.jdbc.Driver"
                   :subprotocol "jtds:sqlserver"
                   :subname (let [prop (get-config-prop)
                                  sqlserver (:sqlserver prop)
                                  ]
                              (str (:address sqlserver) ";user="
                                (:user sqlserver ) ";password=" (:pass sqlserver)
                              ))
                   })

(defqueries "sql/queries.sql" {:connection db-spec})

(defn getbigscreendata [sortnum time]

  (with-db db-sqlserver
    (exec-raw ["select  * from si_sort where stateflag=? and sortno>? and checkdt>=? order by sortno " ["rd" sortnum time]] :results)
    )

  )


