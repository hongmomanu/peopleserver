(ns peopleserver.controller.home

  (:import (net.sourceforge.pinyin4j PinyinHelper)
           (net.sourceforge.pinyin4j.format HanyuPinyinToneType HanyuPinyinOutputFormat)
           (java.text SimpleDateFormat)
           (java.util  Date Calendar)
           )
  (:use compojure.core org.httpkit.server)
  (:use [taoensso.timbre :only [trace debug info warn error fatal]])
  (:require [peopleserver.db.core :as db]
            [peopleserver.layout :as layout]
            [noir.response :as resp]
            [clj-time.local :as l]
            [clj-time.core :as t]
            [pjstadig.utf8 :as putf8]
            [clj-time.format :as f]
            [clojure.data.json :as json]
            [peopleserver.public.websocket :as websocket]
            )
  )


(defn loadDataFire [roomno sortcode area]



  (future (doseq [channel (keys @websocket/channel-hub)]

            (send! channel (json/write-str
                             {:roomno roomno
                              :area area
                              :sortcode sortcode
                              :type 0
                              }
                             )
              false)
            ))

  ;(future
  (info (str "loadDatafire   " roomno ":" sortcode ":" area))
  ;(println "ok")
  (layout/render "hello.html")

  )

(defn changeTipFire [type roomno content]

  ;;(println "changeTipFire:" (str(putf8/utf8-str content)))

  (future (doseq [channel (keys @websocket/channel-hub)]

            (send! channel (json/write-str
                             {:roomno roomno
                              :type type
                              :content content
                              }
                             )
              false)
            ))

  (info "changeTipFire:2222")
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

  (info "changeVoiceTimesFire")
  (resp/json {:success true})


  )
(defn changeShowLinesFire [lines]

  (future (doseq [channel (keys @websocket/channel-hub)]

            (send! channel (json/write-str
                             {
                               :type 4
                               :showlines lines
                               }
                             )
              false)
            ))

  (info "changeShowLinesFire" lines)

  (resp/json {:success true})


  )
(defn changeVoiceSpeedFire [speed area sptype]


  (future (doseq [channel (keys @websocket/channel-hub)]

            (send! channel (json/write-str
                             {
                               :type 5
                               :area area
                               :sptype sptype
                               :speed speed
                               }
                             )
              false)
            ))

  (info "changeVoiceSpeedFire" speed)

  (resp/json {:success true})


  )
(defn clearsreen [num]

  (future (doseq [channel (keys @websocket/channel-hub)]

            (send! channel (json/write-str
                             {
                               :type 8
                               :num num
                               }
                             )
              false)
            ))

  (info "clearsreen" num)

  (resp/json {:success true})


  )
(defn updatesystem [type]

  (future (doseq [channel (keys @websocket/channel-hub)]

            (send! channel (json/write-str
                             {
                               :type (if (= type "big") 6 7)

                               }
                             )
              false)
            ))

  (info "updatesystem" type)
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
(defn getnewestwaitingstatus [area]
  (let [
         df   (new SimpleDateFormat "yyyy-MM-dd")
         today (new Date)
         cal (Calendar/getInstance)
         test (.add cal Calendar/DAY_OF_MONTH 1)
         todaystr (.format df (.getTime today))
         tomorrorstr (.format df (->(.getTime cal)(.getTime)))
         ]

    (resp/json (db/getnewestwaitingstatus area todaystr tomorrorstr))
    #_(resp/json [{:name "D" :value "D301"}])
    )

  )
(defn getbigscreendata[linenos area]

  (let [
         ;;time (l/local-now)
         ;;custom-formatter (f/formatter "yyyy-MM-dd")
         ;;todaystr (f/unparse custom-formatter time)

         df   (new SimpleDateFormat "yyyy-MM-dd")
         ;;time (l/local-now)

         time1 (.format df (.getTime (new Date)))
         ]
    #_(resp/json (db/getbigscreendata linenos time1 area))
    ;sicktype varchar(1), section varchar(10), patname varchar(50), roomno varchar(10) ,showno varchar(10),  sortno int,stateflag varchar(2),checkdt datetime
    (resp/json [{:sortcode 1 :sicktype "m" :section "section" :patname "王小明" :roomname "彩超11F"
                 :roomno "12" :showno "A001" :sortno 1 :linenos 1 :stateflag "rd" :checkdt "2015-05-27 10:59:59"}
                {:sortcode 2 :sicktype "m" :section "section" :patname "王小明" :roomname "彩超11F"
                 :roomno "12" :showno "A002" :sortno 1 :linenos 2 :stateflag "rd" :checkdt "2015-05-27 10:59:59"}
                ])
    )

  )
(defn getdatabysortcodeandtype [sortcode type]
  (resp/json (db/getdatabysortcodeandtype sortcode type))
  #_(resp/json [{:sortcode 45175 :sicktype "m" :section "section" :patname "王小明1" :roomname "彩超11F"
                 :roomno "12" :showno "A001" :sortno 1 :linenos 1 :stateflag type :checkdt "2015-05-27 10:59:59"}
                ])
  )
(defn getdatabysortcode [sortcode]

  (resp/json (db/getdatabysortcode sortcode))
  #_(resp/json [{:sortcode 45190 :sicktype "m" :section "section" :patname "司马相如" :roomname "1室"
                 :roomno "12" :showno "A002" :sortno 1 :linenos 1 :stateflag "rd" :checkdt "2015-05-27 10:59:59"}
                ])

  )
(defn getbigscreendataupdate[sortcode]

  (let [
        ;; time (l/local-now)
         ;;custom-formatter (f/formatter "yyyy-MM-dd")
         ;;todaystr (f/unparse custom-formatter time)

         df   (new SimpleDateFormat "yyyy-MM-dd")
         ;;time (l/local-now)

         time1 (.format df (.getTime (new Date)))

         ]
    (resp/json (db/getbigscreendataupdate sortcode))
    ;sicktype varchar(1), section varchar(10), patname varchar(50), roomno varchar(10) ,showno varchar(10),  sortno int,stateflag varchar(2),checkdt datetime
    #_(resp/json [{:sortcode 1 :sicktype "m" :section "section" :patname "王小明1" :roomname "彩超11F"
                 :roomno "12" :showno "A001" :sortno 1 :linenos 1 :stateflag "rd" :checkdt "2015-05-27 10:59:59"}
                {:sortcode 2 :sicktype "m" :section "section" :patname "王小明2" :roomname "彩超11F"
                 :roomno "12" :showno "A002" :sortno 1 :linenos 2 :stateflag "rd" :checkdt "2015-05-27 10:59:59"}
                ])
    )

  )
(defn getroomdata [roomno]

  (let [
         df   (new SimpleDateFormat "yyyy-MM-dd")
         ;;time (l/local-now)

         time1 (.format df (.getTime (new Date)))
         ;;custom-formatter (f/formatter "yyyy-MM-dd")
         ;;custom-formatter-hh (f/formatter "yyyy-MM-dd HH:mm:ss")
         ;;todaystr (f/unparse custom-formatter time)
         ;;todaystrhh (f/unparse custom-formatter-hh time)

         ]
    ;(println "ssss:"     time1)
    (resp/json (db/getroomdata roomno time1))
    #_(resp/json [{:sortcode 1 :sicktype "m" :section "section" :roomname "彩超11F" :patname "王小明1"
                 :roomno "12" :showno "A001" :sortno 1 :linenos 1 :stateflag "rd" :checkdt "2015-05-27 10:59:59"}
                {:sortcode 2 :sicktype "m" :section "section" :patname "王小明2" :roomname "彩超11F"
                 :roomno "12" :showno "A002" :sortno 1 :linenos 2 :stateflag "ca" :checkdt "2015-05-27 10:59:59"}{:sortcode 2 :sicktype "m" :section "section" :patname "王小明2" :roomname "彩超11F"
                 :roomno "12" :showno "A003" :sortno 1 :linenos 2 :stateflag "rd" :checkdt "2015-05-27 10:59:59"}{:sortcode 2 :sicktype "m" :section "section" :patname "王小明2" :roomname "彩超11F"
                 :roomno "12" :showno "A004" :sortno 1 :linenos 2 :stateflag "rd" :checkdt "2015-05-27 10:59:59"}{:sortcode 2 :sicktype "m" :section "section" :patname "王小明2" :roomname "彩超11F"
                 :roomno "12" :showno "A005" :sortno 1 :linenos 2 :stateflag "rd" :checkdt "2015-05-27 10:59:59"}{:sortcode 2 :sicktype "m" :section "section" :patname "王小明2" :roomname "彩超11F"
                 :roomno "12" :showno "A006" :sortno 1 :linenos 2 :stateflag "rd" :checkdt "2015-05-27 10:59:59"}
                ])
    )

  )
(defn getbigscreenpasseddata [linenos area]

  (let [
         ;;time (l/local-now)
         ;;custom-formatter (f/formatter "yyyy-MM-dd")
         ;;todaystr (f/unparse custom-formatter time)

         df   (new SimpleDateFormat "yyyy-MM-dd")
         ;;time (l/local-now)

         time1 (.format df (.getTime (new Date)))

         ]
    (resp/json (db/getbigscreenpasseddata  linenos time1 area))
    ;sicktype varchar(1), section varchar(10), patname varchar(50), roomno varchar(10) ,showno varchar(10),  sortno int,stateflag varchar(2),checkdt datetime
    #_(resp/json [{:sortcode 3 :sicktype "m" :section "section" :patname "王小明3"
                 :roomno "12" :showno "A003" :sortno 1 :linenos 1 :stateflag "la" :checkdt "2015-05-27 10:59:59"}
                {:sortcode 2 :sicktype "m" :section "section" :patname "王小明2"
                 :roomno "12" :showno "A003" :sortno 1 :linenos 1 :stateflag "la" :checkdt "2015-05-27 10:59:59"}
                {:sortcode 4 :sicktype "m" :section "section" :patname "王小明4"
                 :roomno "12" :showno "A004" :sortno 1  :linenos 2 :stateflag "la" :checkdt "2015-05-27 10:59:59"}{:sortcode 4 :sicktype "m" :section "section" :patname "王小明4"
                 :roomno "12" :showno "A004" :sortno 1  :linenos 2 :stateflag "la" :checkdt "2015-05-27 10:59:59"}{:sortcode 4 :sicktype "m" :section "section" :patname "王小明4"
                 :roomno "12" :showno "A004" :sortno 1  :linenos 2 :stateflag "la" :checkdt "2015-05-27 10:59:59"}{:sortcode 4 :sicktype "m" :section "section" :patname "王小明4"
                 :roomno "12" :showno "A004" :sortno 1  :linenos 2 :stateflag "la" :checkdt "2015-05-27 10:59:59"}
                ])
    )

  )
(defn getbigscreenpasseddataupdate [sortcode]

  (let [
         time (l/local-now)
         custom-formatter (f/formatter "yyyy-MM-dd")
         todaystr (f/unparse custom-formatter time)

         ]
    (resp/json (db/getbigscreenpasseddataupdate  sortcode ))
    ;sicktype varchar(1), section varchar(10), patname varchar(50), roomno varchar(10) ,showno varchar(10),  sortno int,stateflag varchar(2),checkdt datetime
    #_(resp/json [{:sortcode 3 :sicktype "m" :section "section" :patname "王小明3"
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

