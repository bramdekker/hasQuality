#! /usr/bin/python3
"""
    Naam: Bram Dekker
    UvANetID: 11428279
    Studie: Informatica

    Dit python script combineert twee functies uit exercises.py om bestanden te
    lezen en te printen.
"""

import sys
from exercises import list_to_string, read_file

"""Leest een bestand in, slaat de regels intern op als een
lijst van lijsten en print de regels zoals ze in het bestand staan."""
if len( sys.argv ) >= 2:
    list_to_string( read_file( sys.argv[1] ) )
