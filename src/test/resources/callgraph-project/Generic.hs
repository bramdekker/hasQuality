module Generic where

import Individual as Ind

-- Every element in this list is ignored and for every element in this list the
-- variable n is increased by one.
length' :: [a] -> Int
length' = foldr (\_ n -> n + 1) 0

-- Every element in the list is or'ed with n, n is initially false.
or' :: [Bool] -> Bool
or' = Ind.iOr'

-- Every element in the list is compared to q. n is initially false, but
-- becomes true, if any element is the same as q.
elem' :: Eq a => a -> [a] -> Bool
elem' q = foldr (\x n -> n || (x == q)) False

-- This function starts with the rightmost element of the list, applies the
-- function f to it and stores it in front of the list. Then it goes to the
-- next element and does the same.
map' :: (a -> b) -> [a] -> [b]
map' f = foldr (\x xs -> f x : xs) []
