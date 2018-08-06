(ns re-frame-and-jekyll.events
  (:require [re-frame.core :as re-frame]
            [re-frame-and-jekyll.db :as db]))

(re-frame/reg-event-fx
  ::initialize-db
  (fn [{:keys [db]} _]
    {:db db/default-db}))
