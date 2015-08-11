(ns peopleserver.routes.home
  (:require [peopleserver.layout :as layout]
            [peopleserver.controller.home :as home]
            [compojure.core :refer [defroutes GET]]
            [ring.util.http-response :refer [ok]]
            [clojure.java.io :as io]))

(defn home-page []
  ;(ok  {:success true})
  (layout/render "hello.html")
  #_(layout/render
    "home.html" {:docs (-> "docs/docs.md" io/resource slurp)})
  )

(defn about-page []
  (layout/render "about.html"))

(defroutes home-routes
  (GET "/" [] (home-page))
  (GET "/getbigscreendata" [linenos area] (home/getbigscreendata linenos area))
  (GET "/getbigscreendataupdate" [sortcode] (home/getbigscreendataupdate sortcode))
  (GET "/getbigscreenpasseddata" [linenos area] (home/getbigscreenpasseddata linenos area))
  (GET "/getbigscreenpasseddataupdate" [sortcode] (home/getbigscreenpasseddataupdate sortcode))
  (GET "/getnewestwaitingstatus" [area] (home/getnewestwaitingstatus area))


  (GET "/maketexttopinyin" [text] (home/maketexttopinyin text))



  (GET "/loadDataFire" [roomno sortcode area]
    ;(home/loadDataFire roomno sortcode area)
    (home/loadDataFireNew roomno sortcode area)
    )
  (GET "/changeTipFire" [type roomno content]
     (home/changeTipFire type roomno content)
    )
  (GET "/getdatabysortcodeandtype" [sortcode type]
     (home/getdatabysortcodeandtype sortcode type)
    )

  (GET "/getdatabysortcode" [sortcode ]
     (home/getdatabysortcode sortcode)
    )

  (GET "/changeVoiceTimesFire" [totaltimes]
    (home/changeVoiceTimesFire  totaltimes)
    )

  (GET "/changeVoiceSpeedFire" [speed area sptype]
    (home/changeVoiceSpeedFire  speed area sptype)
    )
  (GET "/updatesystem" [type]
    (home/updatesystem  type)
    )
  (GET "/changeShowLinesFire" [lines]
    (home/changeShowLinesFire  lines)
    )
  (GET "/clearsreen" [num]
    (home/clearsreen  num)
    )

  (GET "/getroomdata" [roomno]
    (home/getroomdata roomno)
    )
  (GET "/about" [] (about-page)))

