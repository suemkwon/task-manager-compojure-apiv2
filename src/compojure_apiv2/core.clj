(ns compojure-apiv2.core
  (:require [compojure.api.sweet :refer :all]
            [ring.adapter.jetty :as jetty]
            [schema.core :as s]
            [ring.util.http-response :refer :all])
  (:gen-class))

;; Task schema
(s/defschema Task 
  {:id s/Int
   :title s/Str
   :description (s/maybe s/Str)
   :completed s/Bool})

;; In-memory task storage
(defonce tasks (atom (sorted-map)))

;; Generate unique ID
(defn generate-id []
  (if (empty? @tasks)
    1
    (inc (apply max (keys @tasks)))))

;; Create task
(defn create-task! [task]
  (let [id (generate-id)
        new-task (assoc task :id id)]
    (swap! tasks assoc id new-task)
    new-task))

;; Update task
(defn update-task! [id updates]
  (swap! tasks update id merge updates)
  (get @tasks id))

;; API routes
(def app
  (api
    {:swagger
     {:ui "/swagger-ui"
      :spec "/swagger.json"
      :data {:info {:title "Task Manager API"
                    :description "Simple Task Management API"}}}}
    
    (context "/tasks" []
      :tags ["tasks"]
      
      (POST "/" []
        :return Task
        :body [task (s/maybe Task)]
        :summary "Create a new task"
        (ok (create-task! (dissoc task :id))))
      
      (GET "/" []
        :return [Task]
        :summary "List all tasks"
        (ok (vals @tasks)))
      
      (GET "/:id" []
        :path-params [id :- s/Int]
        :return Task
        :summary "Get a specific task"
        (if-let [task (get @tasks id)]
          (ok task)
          (not-found {:error "Task not found"})))
      
      (PUT "/:id" []
        :path-params [id :- s/Int]
        :body [updates Task]
        :return Task
        :summary "Update a task"
        (if (get @tasks id)
          (ok (update-task! id (dissoc updates :id)))
          (not-found {:error "Task not found"})))
      
      (DELETE "/:id" []
        :path-params [id :- s/Int]
        :return {:message s/Str}
        :summary "Delete a task"
        (if (get @tasks id)
          (do
            (swap! tasks dissoc id)
            (ok {:message "Task deleted successfully"}))
          (not-found {:error "Task not found"}))))))

;; Server start function
(defn start-server [& args]
  (jetty/run-jetty app {:port 3000 :join? false}))

;; Main function
(defn -main [& args]
  (println "Starting server on port 3000")
  (start-server))
