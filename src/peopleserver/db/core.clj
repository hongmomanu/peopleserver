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

(defn getbigscreendata [linenos time area]

  (with-db db-sqlserver
    (exec-raw ["select  * from si_sort where stateflag=? and area=? and linenos>? and checkdt>=? order by linenos " ["rd" area linenos time]] :results)
    )

  )
(defn getbigscreendataupdate [sortcode]

  (with-db db-sqlserver
    (exec-raw ["select  * from si_sort where stateflag=? and sortcode=?  " ["rd" sortcode]] :results)
    )

  )
(defn getroomdata [roomno time]

  (with-db db-sqlserver
    (exec-raw ["select top 3 * from si_sort where stateflag in (?,?) and roomno=? and checkdt>=? order by linenos  " ["ca" "rd" roomno  time]] :results)
    )

  )

(defn getnewestwaitingstatus [area today tomorrow]

  (with-db db-sqlserver
    (exec-raw ["select  left(showno,1) as name,max(showno) as value from si_sort where area=? and checkdt>? and checkdt<? and stateflag =? group by left(showno,1)  "
               [area today tomorrow "rd"  ]] :results)
    )

  )

(defn getbigscreenpasseddata [linenos time area]
  (with-db db-sqlserver
    (exec-raw ["select  * from si_sort where stateflag=? and area=? and linenos>? and checkdt>=? order by linenos " ["la" area linenos time ]] :results)
    )

  )
(defn getbigscreenpasseddataupdate [sortcode]
  (with-db db-sqlserver
    (exec-raw ["select  * from si_sort where stateflag=? and sortcode=?  " ["la" sortcode ]] :results)
    )

  )
(defn getdatabysortcodeandtype [sortcode type]
  (with-db db-sqlserver
    (exec-raw ["select  * from si_sort where stateflag=? and sortcode=?  " [type sortcode ]] :results)
    )
  )
(defn getdatabysortcode [sortcode ]
  (with-db db-sqlserver
    (exec-raw ["select  * from si_sort where  sortcode=?  " [ sortcode ]] :results)
    )
  )


