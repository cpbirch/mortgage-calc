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

(ns mortgage-calc.offset)

(defn calculate-interest [principal monthly-rate]
  (if (> principal 0)
    (* principal monthly-rate)
    0))

(defn offset-mortgage-monthly-values
  ([principal payment monthly-apr regular-savings]
   (offset-mortgage-monthly-values 1 principal principal payment monthly-apr regular-savings))
  ([month offset-principal repayment-principal payment monthly-apr regular-savings]
    (lazy-seq
      (let [savings-bal (first regular-savings)
            offset-interest-amt (calculate-interest (- offset-principal savings-bal) monthly-apr)
            offset-mortgage-bal (+ offset-principal offset-interest-amt)
            repay-mortgage-bal (+ repayment-principal (calculate-interest repayment-principal monthly-apr))]
        (cons {:month month
               :offset-mortgage-balance offset-mortgage-bal
               :offset-interest offset-interest-amt
               :repayment-mortgage-balance repay-mortgage-bal
               :savings-balance savings-bal}
              (offset-mortgage-monthly-values (inc month) (- offset-mortgage-bal payment) (- repay-mortgage-bal payment) payment monthly-apr (rest regular-savings)))))))



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
; Kitchen Plan £100,000

; ((6.5 / 100 / 12) * 200000) / (1 - ((1 + (6.5 / 100 / 12)) ^ (-30 * 12)))
