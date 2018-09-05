(ns scalabotlib.initfile
  (:require
             [clojure-ini.core :refer [read-ini]]
             ))

(gen-class
  :name scalabotlib.initfile
  :methods [#^{:static true} [getValue [String] String]])

(def ini (atom nil))

(defn getConfig []
  (if @ini
    @ini
    (reset! ini (read-ini "bot.ini"))))

(defn -getValue [val]
  (get (getConfig) val))


