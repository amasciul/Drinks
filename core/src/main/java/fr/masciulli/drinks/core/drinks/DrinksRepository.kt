package fr.masciulli.drinks.core.drinks

import io.reactivex.Single

class DrinksRepository(private val remoteSource: DrinksSource) : DrinksSource {
    private val cachedDrinks: MutableMap<String, Drink> = LinkedHashMap()
    private var cached = false

    override fun getDrinks(): Single<List<Drink>> {
        if (cached) {
            return Single.just(cachedDrinks.values.toList())
        }
        return remoteSource.getDrinks().doOnSuccess { cacheDrinks(it) }
    }

    override fun getDrink(id: String): Single<Drink> {
        val cachedDrink = cachedDrinks[id]
        if (cachedDrink != null) {
            return Single.just(cachedDrink)
        }
        return remoteSource.getDrink(id).doOnSuccess { cacheDrinks(listOf(it)) }
    }

    private fun cacheDrinks(drinks: List<Drink>) {
        drinks.forEach {
            cachedDrinks[it.id] = it
        }
        cached = true
    }
}