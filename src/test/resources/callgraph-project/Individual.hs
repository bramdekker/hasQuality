--
-- Name: Bram Dekker
-- UvANetID: 11428279
-- Study: Informatica
--
-- This program defines different existing Haskell functions in terms of other
-- functions like foldr, foldl and scanl.
module Individual

where

import qualified Puzzles (pLength', pOr')
import Generic

-- Every element in this list is ignored and for every element in this list the
-- variable n is increased by one.
iLength' :: [a] -> Int
iLength' = Puzzles.pLength'

-- Every element in the list is or'ed with n, n is initially false.
iOr' :: [Bool] -> Bool
iOr' = Puzzles.pOr'

-- Every element in the list is compared to q. n is initially false, but
-- becomes true, if any element is the same as q.
iElem' :: Eq a => a -> [a] -> Bool
iElem' q = elem' q

-- This function starts with the rightmost element of the list, applies the
-- function f to it and stores it in front of the list. Then it goes to the
-- next element and does the same.
iMap' :: (a -> b) -> [a] -> [b]
iMap' f = map' f

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
getIndex' :: (Eq a, Num a, Num b, Enum a) => [b] -> a -> b
getIndex' xs q = snd $ foldl (\i (j, y) -> if j == q then (j, y) else i)
                 (0, 0) (zip [0,1..] xs)

-- This function checks if the first and last element are the same. If that is
-- true then the second and second-last element are compared.
isPalindrome :: Eq a => [a] -> Bool
isPalindrome x = x == reverse(x)

-- This function returns a infinite list with fibonacci numbers.
fibonacci :: [Int]
fibonacci = scanl (+) 0 (1:fibonacci)

-- This specifies the List type. It is a function that translates an index to a
-- value.
type List = (Int) -> Int

-- This function generates a List from an integer list. This function is just
-- to test the addList function.
getList :: [Int] -> List
getList lst = \i -> lst !! i

-- This function adds elements to the type List = (Int) -> Int. It puts the
-- element to be added at the specified index. 
addList :: List -> (Int) -> Int -> List
addList lst ind elm nind | nind == ind = elm
                         | nind || ind = lst nind
                         | nind > ind = lst (nind - 1)
