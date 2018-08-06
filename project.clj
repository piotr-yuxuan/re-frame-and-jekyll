(defproject re-frame-and-jekyll "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.10.0-alpha6"]
                 [org.clojure/clojurescript "1.10.339"]
                 [reagent "0.8.2-SNAPSHOT"]
                 [re-frame "0.10.5"]]
  :npm {:dependencies [[animate.css "3.7.0"]]}
  :plugins [[lein-cljsbuild "1.1.7"]
            [lein-shell "0.5.0"]]
  :min-lein-version "2.5.3"
  :license {:name "GNU GPL v3+"
            :url "http://www.gnu.org/licenses/gpl-3.0.en.html"}
  :source-paths ["src"]
  :clean-targets ^{:protect false} ["resources/public/js/compiled/app.js"
                                    "figwheel_server.log"
                                    "target"]
  :figwheel {:css-dirs ["resources/public/css"]}
  :profiles {:dev {:dependencies [[binaryage/devtools "0.9.10"]
                                  [day8.re-frame/re-frame-10x "0.3.3-react16"] ;; keep "x.y.z-react16" suffix
                                  [cider/piggieback "0.3.8"]]
                   :plugins [[lein-figwheel "0.5.17-SNAPSHOT"]]}}
  :repl-options {:nrepl-middleware [cider.piggieback/wrap-cljs-repl]}
  :cljsbuild {:builds [{:id "dev"
                        :source-paths ["src"]
                        :figwheel {:on-jsload "re-frame-and-jekyll.core/mount-root"}
                        :compiler {:main re-frame-and-jekyll.core
                                   :output-to "resources/public/js/compiled/app.js"
                                   :output-dir "resources/public/js/compiled/out"
                                   :asset-path "js/compiled/out"
                                   :source-map-timestamp true
                                   :closure-defines {"re_frame.trace.trace_enabled_QMARK_" true}
                                   :preloads [devtools.preload day8.re-frame-10x.preload]
                                   :external-config {:devtools/config {:features-to-install :all}}}}
                       {:id "min"
                        :source-paths ["src"]
                        :compiler {:main re-frame-and-jekyll.core
                                   :output-to "resources/public/js/compiled/app.js"
                                   :optimizations :advanced
                                   :closure-defines {goog.DEBUG false}
                                   :pretty-print false}}]}
  :aliases {"compile" ["do"
                       ["cljsbuild" "once" "min"]
                       ["shell" "cp" "-r" "resources/public/js/compiled/app.js" "docs/js/compiled/app.js"]]})
