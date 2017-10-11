package fr.masciulli.drinks.core.drinks

import io.reactivex.Single

interface DrinksSource {
    fun getDrinks(): Single<List<Drink>>
    fun getDrink(id: String): Single<Drink>
}

