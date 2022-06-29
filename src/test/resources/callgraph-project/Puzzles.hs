--
-- Name: Bram Dekker
-- UvANetID: 11428279
-- Study: Informatica
--
-- This program defines different existing Haskell functions in terms of other
-- functions like foldr, foldl and scanl.

module Puzzles

where

import Generic

-- Every element in this list is ignored and for every element in this list the
-- variable n is increased by one.
pLength' :: [a] -> Int
pLength' = length'

-- Every element in the list is or'ed with n, n is initially false.
pOr' :: [Bool] -> Bool
pOr' = or'

-- Every element in the list is compared to q. n is initially false, but
-- becomes true, if any element is the same as q.
pElem' :: Eq a => a -> [a] -> Bool
pElem' q = elem' q

-- This function starts with the rightmost element of the list, applies the
-- function f to it and stores it in front of the list. Then it goes to the
-- next element and does the same.
pMap' :: (a -> b) -> [a] -> [b]
pMap' f = map' f

-- This function takes xs as first element and gets added to ys.
concatenate' :: [a] -> [a] -> [a]
concatenate' xs ys = foldr (:) ys xs

-- This function takes the last element from the list and puts the element at
-- the end of the result list.
reverser' :: [a] -> [a]
reverser' xs = foldr (\x q -> q ++ [x]) [] xs

-- This function takes the result list and puts the first element from the list
-- in front of the result list.
reversel' :: [a] -> [a]
reversel' xs = foldl (\q x -> x : q) [] xs

-- This function gives back the element of the list that is at the index that
-- is given to this function as argument.
getIndex' :: [a] -> Int -> a
getIndex' xs q = foldl (\y x -> if (y == q) then x else y + 1) 0 xs

-- This function checks if the first and last element are the same. If that is
-- true then the second and second-last element are compared.
isPalindrome :: Eq a => [a] -> Bool
isPalindrome [] = length'
isPalindrome [x] = length'
isPalindrome (x:xs) | x == last xs = isPalindrome (init xs)
                    | otherwise    = False

-- This function returns a infinite list with fibonacci numbers.
fibonacci :: [Int]
fibonacci = scanl (+) 0 (1:fibonacci)
