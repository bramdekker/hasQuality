"""
Naam: Bram Dekker
UvAnetID: 11428279
Studie: Informatica
Dit programma bestaat uit functies die geschreven moesten worden voor de
individuele opdracht van Python voor het vak Programmeertalen. Het doel hiervan
is om te oefenen met het schrijven van Python code.
"""
import collections


def opgave1(mylist):
    """Test of de meegegeven reeks van n integers uit 1 t/m n bestaat."""
    return [a for a in range(1, len(mylist) + 1)] == mylist


def opgave2(mylist):
    """Generator die een reeks van n integers als input neemt en alle integers
    1 t/m n produceert die geen deel zijn van die reeks."""
    return (n for n in range(1, len(mylist) + 1) if n not in mylist)


def opgave3a(filename):
    """Maak een lijst voor iedere regel in het bestand en maak hier een lijst
    van."""
    with open(filename) as f:
        return [[int(a) for a in elem.split()] for elem in f]


def opgave3b(mylist):
    """Print een lijst van lijsten van integers, één lijst per regel en
    gescheiden door een spatie."""
    print('\n'.join(' '.join(map(str, lst)) for lst in mylist))


def opgave3(filename):
    """Zet intern een bestand om in een lijst van lijsten en print dit regel
    voor regel, gescheiden door een spatie."""
    opgave3b(opgave3a(filename))


def sum_nested_it(mylist):
    """Geeft de som van alle integers in het meegegeven argument."""
    total_sum = 0
    while mylist:
        sublist = mylist.pop(0)

        if isinstance(sublist, collections.Iterable):
            mylist = sublist + mylist
        else:
            total_sum += sublist
    return total_sum
