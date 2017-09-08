package fr.masciulli.drinks.core

import rx.Observable

interface DrinksSource {
    val drinks: Observable<List<Drink>>
}

