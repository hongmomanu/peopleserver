# peopleserver

FIXME

## Prerequisites

You will need [Leiningen][1] 2.0 or above installed.

[1]: https://github.com/technomancy/leiningen

## Running

To start a web server for the application, run:

    lein ring server"rd" roomno  time

    select top 3 * from si_sort where stateflag in ('ca','rd') and roomno=2030500 and checkdt>='2015-07-22' order by linenos

    loadFire
    #_(doseq [channel (keys @websocket/channel-hub)]

        (send! channel (json/write-str
                         {:roomno roomno
                          :area area
                          :sortcode sortcode
                          :type 0
                          }
                         )
          false)
        ;)
        )

        #_(resp/json {:success true})

        #_(future (doseq [channel (keys @websocket/channel-hub)]

            (send! channel (json/write-str
                             {
                              :type 3
                              :totaltimes totaltimes
                              }
                             )
              false)
            ))
             (future (doseq [channel (keys @websocket/channel-hub)]

                (send! channel (json/write-str
                                 {
                                  :type 5
                                  :area area
                                  :speed speed
                                  }
                                 )
                  false)
                ))

## License

Copyright © 2015 FIXME


http://localhost:3000/changeVoiceTimesFire?totaltimes=4
http://localhost:3000/updatesystem?type=big
http://localhost:3000/clearsreen?num=2030500

http://localhost:3000/changeVoiceSpeedFire?speed=30&area=2030200&sptype=1 (50正常语速，取值为0-100,sptype 0序号 1名字 2房间号)
http://localhost:3000/changeTipFire?type=1&roomno=2030500&content=ssssssss
http://localhost:3000/changeTipFire?type=2&roomno=200&content=wwwsss
http://d.hiphotos.baidu.com/zhidao/pic/item/0d338744ebf81a4c6a74d7cdd42a6059252da66b.jpg