(defproject org.cyverse/clj-cas "2.0.0"
  :description "A CAS Client library written in Clojure."
  :url "http://www.cyverse.org"
  :license {:name "BSD"
            :url "http://cyverse.org/sites/default/files/iPLANT-LICENSE.txt"}
  :scm {:connection "scm:git:git@github.com:cyverse-archive/clj-cas.git"
        :developerConnection "scm:git:git@github.com:cyverse-archive/clj-cas.git"
        :url "git@github.com:cyverse-archive/clj-cas.git"}
  :pom-addition [:developers
                 [:developer
                  [:url "https://github.com/orgs/cyverse-archive/teams/iplant-devs"]]]
  :classifiers [["javadoc" :javadoc]
                ["sources" :sources]]
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/tools.logging "0.4.0"]
                 [com.cemerick/url "0.1.1"]
                 [org.jasig.cas.client/cas-client-core "3.4.1"
                  :exclusions [javax.servlet/servlet-api]]
                 [ring/ring-core "1.6.3"]]
  :plugins [[jonase/eastwood "0.2.5"]]
  :repositories [["sonatype-nexus-snapshots"
                  {:url "https://oss.sonatype.org/content/repositories/snapshots"}]]
  :deploy-repositories [["sonatype-nexus-staging"
                         {:url "https://oss.sonatype.org/service/local/staging/deploy/maven2/"}]])
