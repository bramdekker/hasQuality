#! /usr/bin/python3
"""
    Naam: Bram Dekker
    UvANetID: 11428279
    Studie: Informatica

    Dit programma beschrijft verschillende functies die dienen om te oefenen
    met de programmeertaal Python. Er is geprobeerd om de functies zo pythonic
    mogelijk te maken.
"""

import sys
import collections

def check_list( lst ):
    """Check of een lijst van n elementen van 1 tot en met n loopt."""
    return True if [ a for a in range(1, len( lst ) + 1 ) ] == lst else False

def gen_list( lst ):
    """Geeft alle elementen tussen 1 en n die niet in de lijst van lengte n
    zitten"""
    return ( n for n in range( 1, len( lst ) + 1 ) if n not in lst )

def read_file( path ):
    """Maakt van elke regel van de file een lijst en stopt al deze lijsten in
    een andere lijst"""
    with open( path ) as f:
        return [ [ int( a ) for a in elem.split() ] for elem in f ]
    f.close()

def list_to_string( lst ):
    """Van elke lijst uit een lijst van lijsten worden alle elementen op die op
    één regel staan geprint gescheiden door een spatie."""
    [ [ print( a, end=" " ) for a in elem ] and print() for elem in lst ]

def sum_nested_it( lst ):
    """Geeft de som van alle integers in het meegegeven argument."""
    total_sum = 0
    while lst:
        sublist = lst.pop( 0 )

        if isinstance( sublist, collections.Iterable ):
            lst = sublist + lst
        else:
            total_sum += sublist
    return total_sum
