(defproject compojure-apiv2 "0.1.0-SNAPSHOT"
  :description "Simple Task Manager API"
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [metosin/compojure-api "2.0.0-alpha31"]
                 [ring/ring-core "1.9.5"]
                 [ring/ring-jetty-adapter "1.9.5"]
                 [prismatic/schema "1.2.1"]
                 [metosin/ring-http-response "0.9.3"]
                 [cheshire "5.11.0"]]
  :main compojure-apiv2.core
  :source-paths ["src"]
  :ring {:handler compojure-apiv2.core/app})