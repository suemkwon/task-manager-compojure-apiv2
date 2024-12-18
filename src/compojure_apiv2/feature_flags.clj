(ns compojure-apiv2.feature-flags
  (:require [schema.core :as s]))

;; Feature flag schema
(s/defschema FeatureFlag
  {:id s/Str
   :name s/Str
   :description s/Str
   :enabled s/Bool})

;; In-memory feature flags storage
(defonce feature-flags (atom {}))

;; Initialize default feature flags
(swap! feature-flags assoc
       "task-categories" {:id "task-categories"
                          :name "Task Categories"
                          :description "Enable task categorization feature"
                          :enabled false}
       "task-priorities" {:id "task-priorities"
                          :name "Task Priorities"
                          :description "Enable task priority levels"
                          :enabled true})

;; Feature flag functions
(defn feature-enabled? [flag-id]
  (get-in @feature-flags [flag-id :enabled] false))

(defn toggle-feature! [flag-id]
  (when (contains? @feature-flags flag-id)
    (swap! feature-flags update-in [flag-id :enabled] not)
    (get-in @feature-flags [flag-id])))

(defn get-all-flags []
  (vals @feature-flags))