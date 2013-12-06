(defproject org.iplantc/clj-cas "1.2.0-SNAPSHOT"
  :description "A CAS Client library written in Clojure."
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [com.cemerick/url "0.1.0"]
                 [org.jasig.cas.client/cas-client-core "3.2.0"
                  :exclusions [javax.servlet/servlet-api]]])
