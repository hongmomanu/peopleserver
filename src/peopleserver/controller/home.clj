(ns peopleserver.controller.home

  (:import (net.sourceforge.pinyin4j PinyinHelper)
           (net.sourceforge.pinyin4j.format HanyuPinyinToneType HanyuPinyinOutputFormat)
           )
  (:use compojure.core org.httpkit.server)
  (:require [peopleserver.db.core :as db]
            [noir.response :as resp]
            [clj-time.local :as l]
            [clj-time.core :as t]
            [clj-time.format :as f]
            [clojure.data.json :as json]
            [peopleserver.public.websocket :as websocket]
            )
  )


(defn loadDataFire [roomno]

  (doseq [channel (keys @websocket/channel-hub)]
    ;;(println "ok")
    (send! channel (json/write-str
                     {:roomno roomno
                      }
                     )
      false)
    )
    (resp/json {:success true})
  )

(defn makepindetail [item]
  (let [
         t3  (new HanyuPinyinOutputFormat)
         test  (.setToneType  t3 HanyuPinyinToneType/WITH_TONE_NUMBER)
         data (PinyinHelper/toHanyuPinyinStringArray item t3)
         ]
    (if (= (count data) 0)  item (get data 0))
    )
  )
(defn maketexttopinyin [text]


    (resp/json (clojure.string/join  (map #(str (makepindetail %)) text) ))



  )
(defn getbigscreendata[sortno]

  (let [
         time (l/local-now)
         custom-formatter (f/formatter "yyyy-MM-dd")
         todaystr (f/unparse custom-formatter time)

         ]
    (resp/json (db/getbigscreendata sortno todaystr))
    )

  )

