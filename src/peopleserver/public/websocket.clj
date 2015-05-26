(ns peopleserver.public.websocket
      (:use org.httpkit.server)
  (:require
            [clojure.data.json :as json]


            )
)

(def channel-hub (atom {}))


(defn handler [request]
  (with-channel request channel
    ;; Store the channel somewhere, and use it to sent response to client when interesting event happened
    ;;(swap! channel-hub assoc channel nil)
    (on-receive channel (fn [data]

                          (swap! channel-hub assoc channel request)
                          (println data)
                               ;(println request)
                              ;(send! channel data)
                              ))
    (on-close channel (fn [status]
                        ;; remove from hub when channel get closed

                        (println channel " disconnected. status: "  )
                        (swap! channel-hub dissoc channel)

                        ))))




(defn start-server [port]
  (run-server handler {:port port :max-body 16388608 :max-line 16388608})
  )


;(future (loop []
;          (println (keys @channel-hub))
;          (doseq [channel (keys @channel-hub)]
;            (println "ok")
;            (send! channel (json/write-str
;                                  {:happiness (rand 10)})
;              false)
;            )
;          (Thread/sleep 5000)
;          (recur)))







