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

(ns mortgage-calc.savings-test
  (:require [clojure.test :refer :all]
            [mortgage-calc.savings :refer :all]))

; Savings plan

; Set a collection of savings goals to be spent
; Set the starting savings value
; Set the amount saved each month
; Get a sequence of monthly savings values asuming savings goals spent when reached

(deftest savings-plan-test
  (testing "Given a goal of 1000, 2000 can the infinite sequence yield 0?"
    (is (= (take 4 (saving-goals [1000, 2000])) '(1000 2000 0 0))))
  (testing "Given goals of 3000, 2500 and 1500 with 1000 saved each month, are my monthly balances: 1000, 2000, 0, 1000, 2000, 500, 0, 1000, 2000, 3000"
    (is (= (take 10 (regular-savings 0 1000 (saving-goals [3000 2500 1500]))) '(1000 2000 0 1000 2000 500 0 1000 2000 3000)))))

