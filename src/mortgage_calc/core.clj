;
;     Copyright (C) 2016 Christopher Birch
;
;     This program is free software: you can redistribute it and/or modify
;     it under the terms of the GNU Lesser General Public License as published by
;     the Free Software Foundation, either version 3 of the License, or
;     (at your option) any later version.
;
;     This program is distributed in the hope that it will be useful,
;     but WITHOUT ANY WARRANTY; without even the implied warranty of
;     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
;     GNU Lesser General Public License for more details.
;
;     You should have received a copy of the GNU Lesser General Public License
;     along with this program.  If not, see <http://www.gnu.org/licenses/>.
;

(ns mortgage-calc.core
  (require
    [aleph.http :as http]
    [bidi.bidi :refer [tag]]
    [bidi.ring :refer [make-handler] :as bidi]
    [hiccup.core :refer [html]]
    [yada.resources.webjar-resource :refer [new-webjar-resource]]
    [yada.yada :refer [handler resource swaggered] :as yada]
    [mortgage-calc.offset :refer [offset-mortgage-monthly-values]]
    [mortgage-calc.repayment :refer [monthly-mortgage-payment cost-of-mortgage]]
    [mortgage-calc.savings :refer [saving-goals regular-savings]])
  (:gen-class))


(defn get-offset-mortgage-chart-data [ctx]
  (let [json (get-in ctx [:parameters :body])
        principal (:principal json)
        interest-apr (:interest-apr json)
        term (:term-months json)
        payment (monthly-mortgage-payment principal interest-apr term)]
        (->>
          (:saving-goals json)
          (map #(:saving-amount %))
          (vec)
          (saving-goals)
          (regular-savings (:savings-starting-balance json) (:monthly-saving json))
          (offset-mortgage-monthly-values principal payment (/ interest-apr 12))
          (take term)
          (map #(vec [(:month %) (:offset-mortgage-balance %) (:repayment-mortgage-balance %) (:savings-balance %)]))
          )))


(def index
  {:id          :index
   :description "Home Page"
   :produces    [{:media-type #{"text/html"}
                  :charset    "UTF-8"}]
   :methods
                {:get
                 {:response
                  (fn [_]
                    (new java.io.File "resources/index.html"))}}
   })

(def mortgage-illustration
  {:id          :mortgage/offset
   :description "Take input variables for a mortgage of Principal, Term and Interest and return a list of [month, balance]"
   :produces    [{:media-type "application/json"
                  :charset    "UTF-8"}]
   :methods     {:post {:parameters {:body {:principal      Long
                                            :term-months    Long
                                            :interest-apr   Double
                                            :savings-starting-balance Long
                                            :monthly-saving Long
                                            :saving-goals   [{:saving-goal-description String
                                                              :saving-amount           Long}]
                                            }}
                        :consumes   #{"application/json"
                                      "application/x-www-form-urlencoded"
                                      "application/edn"}
                        :response   get-offset-mortgage-chart-data}}})

(defn routes
  [config]
  [""
   [
    ["/" (resource index)]
    ["/api" (-> ["/mortgage" [
                              ;["/hello" (resource hello)]
                              ["/repayment" (resource mortgage-illustration)]
                              ["/offset" (resource mortgage-illustration)]]]
                ;; Wrap this route structure in a Swagger
                ;; wrapper. This introspects the data model and
                ;; provides a swagger.json file, used by Swagger UI
                ;; and other tools.
                (swaggered
                  {:info     {:title       "Hello World!"
                              :version     "1.0"
                              :description "An API for calculating mortgages"}
                   :basePath "/api"})
                ;; Tag it so we can create an href to this API
                (tag :api))]

    ;; Swagger UI
    ["/swagger" (-> (new-webjar-resource "/swagger-ui" {:index-files ["index.html"]})
                    ;; Tag it so we can create an href to the Swagger UI
                    (tag :swagger))]

    ["/status" (resource
                 {:methods
                  {:get
                   {:produces "text/html"
                    :response (fn [ctx]
                                (html
                                  [:body
                                   [:div
                                    [:h2 "System properties"]
                                    [:table
                                     (for [[k v] (sort (into {} (System/getProperties)))]
                                       [:tr
                                        [:td [:pre k]]
                                        [:td [:pre v]]]
                                       )]]
                                   [:div
                                    [:h2 "Environment variables"]
                                    [:table
                                     (for [[k v] (sort (into {} (System/getenv)))]
                                       [:tr
                                        [:td [:pre k]]
                                        [:td [:pre v]]]
                                       )]]
                                   ]))}}})]

    ; This is a backstop. Always produce a 404 if we ge there. This
    ;; ensures we never pass nil back to Aleph.
    [true (handler nil)]
    ]])


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Starting...")
  (http/start-server
    (bidi/make-handler (routes nil))
    {:port 3000})
  (println "Started.")
  @(promise))
