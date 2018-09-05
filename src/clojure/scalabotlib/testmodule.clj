(ns scalabotlib.testmodule
  )

(gen-class
  :name scalabotlib.testmodule.TestModuleClj
  :extends scalabotlib.testmodule.IrcModuleClj)

(def -helpBlurb "This is a test Help Blurb")

(defn -regex [_] "(cljtest .*)")

(defn -func [_ user, channel, message]
  (println ["Clojure: " message])
  "This is calling Clojure!")