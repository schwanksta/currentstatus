(ns currentstatus-fm
  (:require [dommy.template :as template])
  (:use [jayq.core :only [$ append]]
        [clojure.string :only [split]])
  (:use-macros [jayq.macros :only [let-ajax]]))

(def body js/document.body)

(def container ($ :div#container))

(defn get-url-for-item [item]
  (.-expanded_url (first (.-urls (.-entities item)))))

(defn get-youtube-id [item]
  (last (split (get-url-for-item item) "/") ))

(defn get-youtube-embed [item]
  (template/node
   [:iframe 
    {:width "560"
     :height "315"
     :src (str "http://www.youtube.com/embed/" (get-youtube-id item))
     :frameborder "0"
     }]))


(let-ajax [data 
           {:url "http://search.twitter.com/search.json?q=current%20status%20youtu.be&include_entities=true"
            :dataType :jsonp}]
  (append
     container
     (template/node
      [:ul
       (for [r (.-results data)]
         [:li (get-youtube-embed r)])])))