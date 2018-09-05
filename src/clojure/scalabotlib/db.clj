(ns scalabotlib.db

  (:require [konserve.filestore :refer [new-fs-store]]
            [konserve.core :as k]
            [clojure.core.async :refer [<!!, <!, go]]
            [clojure.string :refer [join]]
            [clj-time.core :as t]
            [clj-time.coerce :as c])
  )

(gen-class
  :name scalabotlib.db
  :methods [
            #^{:static true} [cacheValue [String String String] String]
            #^{:static true} [getCacheValueTimeout [String String] String]
            #^{:static true} [initStore [] void]])

;(def store (delay (<!! (new-fs-store "diosdb.tmp"))))

(def store (atom ()))

(defn -initStore []
  (reset! store (<!! (new-fs-store "diosdb.tmp"))))

(defn -cacheValue [cache key value]
  (let [kk (join "" [cache key])]
    (go
      (<! (k/assoc-in @store [kk :value] value))
      (<! (k/assoc-in @store [kk :date] (c/to-long (t/now))))))
  value)

(defn -getCacheValueTimeout [cache key]
  (let [kk (join "" [cache key])]
    (if-let [date (c/from-long (<!! (k/get-in @store [kk :date])))]
      (if (t/after? (t/now) (t/plus date (t/days 1)))
        nil
        (<!! (k/get-in @store [kk :value])))
      nil)))

