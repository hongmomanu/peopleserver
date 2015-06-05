(ns peopleserver.controller.home

  (:import (net.sourceforge.pinyin4j PinyinHelper)
           (net.sourceforge.pinyin4j.format HanyuPinyinToneType HanyuPinyinOutputFormat)
           )
  (:use compojure.core org.httpkit.server)
  (:require [peopleserver.db.core :as db]
            [peopleserver.layout :as layout]
            [noir.response :as resp]
            [clj-time.local :as l]
            [clj-time.core :as t]
            [clj-time.format :as f]
            [clojure.data.json :as json]
            [peopleserver.public.websocket :as websocket]
            )
  )


(defn loadDataFire [roomno]

  (future (doseq [channel (keys @websocket/channel-hub)]
    ;;(println "ok")
    (send! channel (json/write-str
                     {:roomno roomno
                      :type 0
                      }
                     )
      false)
    ))
  (layout/render "about.html")
    #_(resp/json {:success true})
  )

(defn changeTipFire [type roomno content]


  (future (doseq [channel (keys @websocket/channel-hub)]
    (println channel)
    (send! channel (json/write-str
                     {:roomno roomno
                      :type type
                      :content content
                      }
                     )
      false)
    ))
  (resp/json {:success true})


  )

(defn changeVoiceTimesFire [totaltimes]


  (future (doseq [channel (keys @websocket/channel-hub)]

    (send! channel (json/write-str
                     {
                      :type 3
                      :totaltimes totaltimes
                      }
                     )
      false)
    ))
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
(defn getbigscreendata[linenos]

  (let [
         time (l/local-now)
         custom-formatter (f/formatter "yyyy-MM-dd")
         todaystr (f/unparse custom-formatter time)

         ]
    ;(resp/json (db/getbigscreendata linenos todaystr))
    ;sicktype varchar(1), section varchar(10), patname varchar(50), roomno varchar(10) ,showno varchar(10),  sortno int,stateflag varchar(2),checkdt datetime
   (resp/json [{:sortcode 1 :sicktype "m" :section "section" :patname "王小明1" :roomname "彩超11F"
                 :roomno "12" :showno "A001" :sortno 1 :linenos 1 :stateflag "rd" :checkdt "2015-05-27 10:59:59"}
                {:sortcode 2 :sicktype "m" :section "section" :patname "王小明2" :roomname "彩超11F"
                 :roomno "12" :showno "A002" :sortno 1 :linenos 2 :stateflag "rd" :checkdt "2015-05-27 10:59:59"}
                ])
    )

  )
(defn getroomdata [roomno]

  (let [
         time (l/local-now)
         custom-formatter (f/formatter "yyyy-MM-dd")
         todaystr (f/unparse custom-formatter time)

         ]
    ;(resp/json (db/getroomdata roomno todaystr))
    (resp/json [{:sortcode 1 :sicktype "m" :section "section" :roomname "彩超11F" :patname "王小明1"
                 :roomno "12" :showno "A001" :sortno 1 :linenos 1 :stateflag "ca" :checkdt "2015-05-27 10:59:59"}
                {:sortcode 2 :sicktype "m" :section "section" :patname "王小明2" :roomname "彩超11F"
                 :roomno "12" :showno "A002" :sortno 1 :linenos 2 :stateflag "rd" :checkdt "2015-05-27 10:59:59"}
                ])
    )

  )
(defn getbigscreenpasseddata [linenos]

  (let [
         time (l/local-now)
         custom-formatter (f/formatter "yyyy-MM-dd")
         todaystr (f/unparse custom-formatter time)

         ]
    ;(resp/json (db/getbigscreenpasseddata  linenos todaystr))
    ;sicktype varchar(1), section varchar(10), patname varchar(50), roomno varchar(10) ,showno varchar(10),  sortno int,stateflag varchar(2),checkdt datetime
    (resp/json [{:sortcode 3 :sicktype "m" :section "section" :patname "王小明3"
                 :roomno "12" :showno "A003" :sortno 1 :linenos 1 :stateflag "la" :checkdt "2015-05-27 10:59:59"}
                {:sortcode 2 :sicktype "m" :section "section" :patname "王小明2"
                 :roomno "12" :showno "A003" :sortno 1 :linenos 1 :stateflag "la" :checkdt "2015-05-27 10:59:59"}
                {:sortcode 4 :sicktype "m" :section "section" :patname "王小明4"
                 :roomno "12" :showno "A004" :sortno 1  :linenos 2 :stateflag "la" :checkdt "2015-05-27 10:59:59"}{:sortcode 4 :sicktype "m" :section "section" :patname "王小明4"
                 :roomno "12" :showno "A004" :sortno 1  :linenos 2 :stateflag "la" :checkdt "2015-05-27 10:59:59"}{:sortcode 4 :sicktype "m" :section "section" :patname "王小明4"
                 :roomno "12" :showno "A004" :sortno 1  :linenos 2 :stateflag "la" :checkdt "2015-05-27 10:59:59"}
                ])
    )

  )

