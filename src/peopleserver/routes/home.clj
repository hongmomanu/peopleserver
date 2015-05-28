(ns peopleserver.routes.home
  (:require [peopleserver.layout :as layout]
            [peopleserver.controller.home :as home]
            [compojure.core :refer [defroutes GET]]
            [ring.util.http-response :refer [ok]]
            [clojure.java.io :as io]))

(defn home-page []
  (layout/render
    "home.html" {:docs (-> "docs/docs.md" io/resource slurp)}))

(defn about-page []
  (layout/render "about.html"))

(defroutes home-routes
  (GET "/" [] (home-page))
  (GET "/getbigscreendata" [linenos] (home/getbigscreendata linenos))
  (GET "/getbigscreenpasseddata" [linenos] (home/getbigscreenpasseddata linenos))
  (GET "/maketexttopinyin" [text] (home/maketexttopinyin text))



  (GET "/loadDataFire" [roomno]
    (home/loadDataFire roomno)
    )
  (GET "/changeTipFire" [type roomno content]
     (home/changeTipFire type roomno content)
    )
  (GET "/getroomdata" [roomno]
    (home/getroomdata roomno)
    )
  (GET "/about" [] (about-page)))

