package fr.masciulli.drinks.core.drinks

import rx.Observable

interface DrinksSource {
    val drinks: Observable<List<Drink>>
}

