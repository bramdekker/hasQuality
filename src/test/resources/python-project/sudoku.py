#! /usr/bin/python3
"""
    Naam: Bram Dekker
    UvANetID: 11428279
    Studie: Informatica

    Dit programma geeft een oplossing van de meegegeven sudoku, als er een
    geldige sudoku waarvoor een oplossing te vinden is, is meegegeven.
"""

import sys
import collections
import math
import copy
from exercises import *

def gen_faults( lst ):
    """Geeft alle elementen tussen 1 en n die niet in de lijst van lengte n
    zitten"""
    return [ n for n in range( 1, len( lst ) + 1 ) if n not in lst ]


def transpose( matrix ):
    """Transponeert een lijst van lijsten en geeft de getransponeerde matrix
    terug. Deze functie komt uit de opgave."""
    return [ list(a) for a in zip(*matrix) ]

def get_block( tple, n ):
    """Geeft het block terug waarin de meegegeven plaats zich bevindt."""
    return int( tple[0] / n ) * n + int( tple[1] / n )

def check_sudoku( rows, n ):
    """Checkt of elke rij, kolom en blok in de sudoku de cijfers 1 t/m n
    bevatten."""
    r_check = [ gen_faults( elem ) == [] for elem in rows ]
    c_check = [ gen_faults( elem ) == [] for elem in transpose( rows ) ]
    b_check = [ gen_faults( elem ) == [] for elem in [ [ rows[ int( m / n )
    * n + i ] [ ( m % n ) * n + j ]
    for i in range(n) for j in range(n) ] for m in range(n**2) ] ]
    return ( True and all( r_check ) and all( c_check ) and all( b_check ) )

"""Code voor script dat een bestand inleest, de regels intern opslaat als een
lijst van lijsten en de regels print zoals ze in het bestand staan."""
if len( sys.argv ) >= 2:
    rows = read_file( sys.argv[1] )
    columns = transpose( rows )
    if ( math.sqrt( len( rows ) ) % 1 != 0 ) or ( math.sqrt( len( columns ) )
    % 1 != 0 ):
        sys.exit("Give a valid sudoku as argument, please! Usage: ./sudoku "
        "sudoku.txt.")
    else:
        n = int( math.sqrt( len( rows ) ) )

    """Maakt een lijst van lijsten voor de blokken van de sudoku."""
    blocks = [ [ rows[ int( m / n ) * n + i ] [ ( m % n ) * n + j ]
    for i in range(n) for j in range(n) ] for m in range(n**2) ]

    """Maakt een lijst van lijsten die voor iedere 0 plaats in de sudoku
    waarden geeft die daar ingevuld kunnen worden.
    Something gies wrong here!"""
    search_tree = [ list( set( gen_faults( rows[ rows.index( elem ) ] ) ) &
    set( gen_faults( columns[ elem.index( a ) ] ) ) & set( gen_faults( blocks[
    get_block( ( rows.index( elem ), elem.index( a ) ), n ) ] ) ) )
    for elem in rows for a in elem if a == 0 ]


    """Guesses bevat alle verschillende mogelijkheden om alle 0 plaatsen in de
    sudoku in te vullen."""
    guesses = [ [] ]
    for u in search_tree:
        t = []
        for v in u:
            for i in guesses:
                t.append( i + [ v ] )
        guesses = t

    """Hier worden alle guesses ingevuld in de sudoku. Als er een oplossing is
    gevonden stopt het invullen en wordt de oplossing geprint."""
    backup = copy.deepcopy( rows )
    spot = 0
    for a in guesses:
        for elem in rows:
            for b in elem:
                if b == 0:
                    rows[ rows.index( elem ) ][ elem.index( b ) ] = a[spot]
                    spot += 1
        if check_sudoku( rows, n ):
            list_to_string( rows )
            solution = True
            break
        else:
            solution = False
            rows = copy.copy( backup )
            spot = 0

    if not solution:
        print( "No solution found, please enter a valid sudoku!")
