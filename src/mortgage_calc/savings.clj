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

(ns mortgage-calc.savings)

(defn saving-goals [goals-coll]
  (lazy-seq
    (if-let [goal (first goals-coll)]
      (cons goal (saving-goals (rest goals-coll)))
      (cons 0 (rest (repeat 0))))))

(defn regular-savings [starting-bal monthly-addition goals]
  (lazy-seq
    (let [savings-bal (+ starting-bal monthly-addition)
          goal (first goals)]
      (if (<= goal savings-bal)
        (cons (- savings-bal goal) (regular-savings (- savings-bal goal) monthly-addition (rest goals)))
        (cons savings-bal (regular-savings savings-bal monthly-addition goals))))))
