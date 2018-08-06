(ns re-frame-and-jekyll.subs
  (:require [re-frame.core :as re-frame]
            [goog.object :as object]))

(defn ->array
  [js-coll]
  (when js-coll
    (.from js/Array js-coll)))

(defn matches-node-or-children-id?
  [watched-id added-node]
  (or (when (= watched-id (object/get added-node "id"))
        added-node)
      (when-let [children (seq (->array (object/get added-node "children")))]
        (some (partial matches-node-or-children-id? watched-id) children))))

(defn mutations-handler
  [app-db watched-id mutation-list]
  (let [added-node (some (fn [mutation]
                           (->> (object/get mutation "addedNodes")
                                ->array
                                (some (partial matches-node-or-children-id? watched-id))))
                         mutation-list)
        removed-node (some (fn [mutation]
                             (->> (object/get mutation "removedNodes")
                                  ->array
                                  (some (partial matches-node-or-children-id? watched-id))))
                           mutation-list)]
    (cond added-node (swap! app-db update :node-by-id assoc watched-id added-node)
          ;; addition takes precedence over removal
          removed-node (swap! app-db update :node-by-id dissoc watched-id))))

(re-frame/reg-sub-raw
  ::node-by-id
  (fn [app-db [_ watched-id]]
    (swap! app-db update :node-by-id assoc watched-id (.getElementById js/document watched-id))
    (let [observer (doto (js/MutationObserver. (partial mutations-handler app-db watched-id))
                     (.observe (object/get js/document "body")
                               (clj->js {:attributes false
                                         :childList true
                                         :subtree true})))]
      (reagent.ratom/make-reaction
        (fn [] (get-in @app-db [:node-by-id watched-id]))
        :on-dispose (fn [] (.disconnect observer))))))
