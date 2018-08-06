(ns re-frame-and-jekyll.core
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [re-frame-and-jekyll.events :as events]
            [re-frame-and-jekyll.components.hello-from-re-frame :refer [hello-from-re-frame]]
            [re-frame-and-jekyll.subs :as subs]
            [re-frame-and-jekyll.config :as config]))

(defn dev-setup []
  (when config/debug?
    (enable-console-print!)
    (println "dev mode")))

(defn ^:export init []
  (re-frame/dispatch-sync [::events/initialize-db])
  (dev-setup)
  (when-let [node @(re-frame/subscribe [::subs/node-by-id "hello-from-re-frame"])]
    (reagent/render [hello-from-re-frame] node)))
