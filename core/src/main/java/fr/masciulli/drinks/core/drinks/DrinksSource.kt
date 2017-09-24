package fr.masciulli.drinks.core.drinks

import rx.Observable

interface DrinksSource {
    fun getDrinks(): Observable<List<Drink>>
    fun getDrink(id: String): Observable<Drink>
}

