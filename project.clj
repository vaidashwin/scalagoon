(defproject scalagoon "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :plugins [[lein-scalac "0.1.0"]]
  :managed-dependencies [[org.scala-lang/scala-compiler "2.12.6"]]
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.scala-lang/scala-library "2.12.6"]
                 [org.scalaj/scalaj-http_2.12 "2.4.1"]
                 [com.typesafe.play/play-json_2.12 "2.6.7"]
                 [org.scala-lang.modules/scala-xml_2.12 "1.1.0"]
                 [io.replikativ/konserve "0.5.0-beta3"]
                 [clj-time "0.14.4"]
                 [clojure-ini "0.0.2"]]
  :repositories [["Typesafe Repository" "https://repo.typesafe.com/typesafe/releases/"]]
  :main ^:skip-aot app.Robot
  :aot [scalabotlib.db scalabotlib.ircmodule scalabotlib.testmodule scalabotlib.initfile]
  :target-path "target/%s"
  :source-paths ["src/clojure"]
  :scala-source-path "src"
  :prep-tasks ["compile" "scalac" ]
  :profiles {:uberjar {:aot :all}}
  :jvm-opts ["-Dfile.encoding=UTF8"])
