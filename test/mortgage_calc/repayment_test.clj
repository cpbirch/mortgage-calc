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

(ns mortgage-calc.repayment-test
  (:require [clojure.test :refer :all]
            [mortgage-calc.repayment :refer :all]))

(deftest monthly-mortgage-payment-equation-test
         (testing "Does a 1 + APR of 6% to the power of 180 = 0.01227?"
                  (is (= (format "%.3f" (one-plus-rate-pow-term 0.06 180)) "2.454")))
         (testing "Does the average monthly compound interest for 2.454 = 0.008439"
                  (is (= (format "%.6f" (avg-compound-int 0.06 2.454)) "0.008439")))
         (testing "Does the mortgage payment for £100,000 = £843.86?"
                  (is (= (format "%.2f" (monthly-mortgage-payment 100000 0.06 180)) "843.86")))
         )

; Calculating off-set mortgage benefits

; Work out the standard repayment amount
; Calculate the total paid back
; Work out the difference with the principal to see the total cost

(deftest total-interest-paid-test
         (testing "Does a £100,000 mortgage over 180 months at 6% APR cost £51,894.80?"
                  (is (= (format "%.2f" (cost-of-mortgage 180 843.86 100000)) "51894.80"))))
