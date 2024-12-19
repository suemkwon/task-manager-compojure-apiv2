(ns hooks.compojure-api-sweet)

(defn params-bindings [params]
  (into {}
        (for [param params]
          [(symbol (name param)) {:tag :param}])))

(defmacro GET [& args]
  (params-bindings (filter symbol? args)))

(defmacro POST [& args]
  (params-bindings (filter symbol? args)))

(defmacro PUT [& args]
  (params-bindings (filter symbol? args)))

(defmacro DELETE [& args]
  (params-bindings (filter symbol? args)))