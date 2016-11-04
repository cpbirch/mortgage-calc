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

(ns mortgage-calc.offset-test
  (:require [clojure.test :refer :all]
            [mortgage-calc.offset :refer :all]
            [mortgage-calc.savings :refer :all]))

; Using the calculated repayment amount M, deduct it from the principal P, then add the interest of (P - savings)
; repeat for the term

(deftest offset-mortgage-monthly-values-test
  (testing "Given P=100000, M=843.86, APR=6% and savings at £0 per month with one goal of £10000"
    (let [mortgage-values (offset-mortgage-monthly-values 100000 843.86 (/ 0.06 12) (regular-savings 0 0 (saving-goals [0])))]
      (is (<= (:mortgage-balance (nth mortgage-values 180) 0))))))

; Principal £200,000
; Interest 2.1% APR
; Term 22 years
;
; Savings Rate £2,000
; Starting Savings £3,000
;
; Linn Plan1 £4,500
; Safari Plan1 £1,750
; Safari Plan2 £5,250
; Linn Plan2 £4,500
; Kitchen Plan £50,000

; ((6.5 / 100 / 12) * 200000) / (1 - ((1 + (6.5 / 100 / 12)) ^ (-30 * 12)))


