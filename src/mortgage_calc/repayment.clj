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

(ns mortgage-calc.repayment
  (:require [clojure.math.numeric-tower :as math]))

(defn one-plus-rate-pow-term [apr months]
  (->
    (/ apr 12)
    (+ 1)
    (math/expt months)))

(defn avg-compound-int [apr rate-powered]
  (->
    (/ apr 12)
    (* rate-powered)
    (/ (- rate-powered 1))))

(defn monthly-mortgage-payment [principal apr months]
  (->>
    (one-plus-rate-pow-term apr months)
    (avg-compound-int apr)
    (* principal)))

(defn cost-of-mortgage [months payment principal]
  (- (* payment months) principal))

