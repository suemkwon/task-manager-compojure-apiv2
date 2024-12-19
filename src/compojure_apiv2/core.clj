(ns compojure-apiv2.core
  (:require [compojure.api.sweet :refer :all]
            [ring.adapter.jetty :as jetty]
            [schema.core :as s]
            [ring.util.http-response :refer :all]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.util.response :as response]
            [cheshire.core :as json])
  (:gen-class))

;; Feature flags
(def feature-flags (atom {"task-categories" {:id "task-categories"
                                             :name "Task Categories"
                                             :description "Enable task categorization feature"
                                             :enabled true}
                          "task-priorities" {:id "task-priorities"
                                             :name "Task Priorities"
                                             :description "Enable task priority levels"
                                             :enabled true}}))

(defn feature-enabled? [flag-id]
  (get-in @feature-flags [flag-id :enabled] true))

(defn toggle-feature! [flag-id]
  (when (contains? @feature-flags flag-id)
    (swap! feature-flags update-in [flag-id :enabled] not)
    (get-in @feature-flags [flag-id])))

(defn get-all-flags []
  (vals @feature-flags))

;; Task schema with conditional fields based on feature flags
(defn task-schema []
  (merge {:id s/Int
          :title s/Str
          :description (s/maybe s/Str)
          :completed s/Bool}
         (when (feature-enabled? "task-categories")
           {:category (s/maybe s/Str)})
         (when (feature-enabled? "task-priorities")
           {:priority (s/enum "low" "medium" "high")})))

;; In-memory task storage
(defonce tasks (atom (sorted-map)))

;; Generate unique ID
(defn generate-id []
  (if (empty? @tasks)
    1
    (inc (apply max (keys @tasks)))))

;; Task CRUD operations
(defn create-task! [task]
  (let [id (generate-id)
        new-task (assoc task :id id)]
    (swap! tasks assoc id new-task)
    new-task))

(defn update-task! [id updates]
  (when (contains? @tasks id)
    (swap! tasks update id merge updates)
    (get @tasks id)))

(defn delete-task! [id]
  (when (contains? @tasks id)
    (swap! tasks dissoc id)
    true))

(defn get-task [id]
  (get @tasks id))

(defn get-all-tasks []
  (vals @tasks))

;; Schemas for API documentation
(s/defschema FeatureFlag
  {:id s/Str
   :name s/Str
   :description s/Str
   :enabled s/Bool})

(s/defschema Task
  (task-schema))

;; API routes
(def app
  (-> (api
       {:swagger
        {:ui "/swagger-ui"
         :spec "/swagger.json"
         :data {:info {:title "Task Manager API"
                       :description "Task Management API with Feature Flags"
                       :version "1.0.0"}}}}

       (context "/api" []
         :tags ["api"]

         (context "/tasks" []
           :tags ["tasks"]

           (GET "/" []
             :return [Task]
             :summary "Get all tasks"
             (ok (get-all-tasks)))

           (POST "/" []
             :return Task
             :body [task (dissoc (task-schema) :id)]
             :summary "Create a new task"
             (ok (create-task! task)))

           (GET "/:id" []
             :path-params [id :- s/Int]
             :return (s/maybe Task)
             :summary "Get a task by ID"
             (if-let [task (get-task id)]
               (ok task)
               (not-found {:error "Task not found"})))

           (PUT "/:id" []
             :path-params [id :- s/Int]
             :body [task Task]
             :return (s/maybe Task)
             :summary "Update a task"
             (if-let [updated-task (update-task! id task)]
               (ok updated-task)
               (not-found {:error "Task not found"})))

           (DELETE "/:id" []
             :path-params [id :- s/Int]
             :return {:message s/Str}
             :summary "Delete a task"
             (if (delete-task! id)
               (ok {:message "Task deleted successfully"})
               (not-found {:error "Task not found"}))))

         (context "/feature-flags" []
           :tags ["feature-flags"]

           (GET "/" []
             :return [FeatureFlag]
             :summary "Get all feature flags"
             (ok (get-all-flags)))

           (POST "/:id/toggle" []
             :path-params [id :- s/Str]
             :return (s/maybe FeatureFlag)
             :summary "Toggle a feature flag"
             (if-let [updated-flag (toggle-feature! id)]
               (ok updated-flag)
               (not-found {:error "Feature flag not found"})))))

        ;; Serve the static frontend
       (undocumented
        (GET "/" []
          (response/resource-response "index.html" {:root "public"}))))
      (wrap-resource "public")))

;; Server functions
(defn start-server [port]
  (jetty/run-jetty app {:port port :join? false}))

(defn -main [& args]
  (let [port (Integer/parseInt (or (System/getenv "PORT") "3000"))]
    (println (str "Starting server on port " port))
    (start-server port)))