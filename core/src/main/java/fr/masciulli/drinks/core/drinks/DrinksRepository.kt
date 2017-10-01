package fr.masciulli.drinks.core.drinks

import rx.Observable

class DrinksRepository(private val remoteSource: DrinksSource) : DrinksSource {
    private val cachedDrinks: MutableMap<String, Drink> = LinkedHashMap()
    private var cached = false

    override fun getDrinks(): Observable<List<Drink>> {
        if (cached) {
            return Observable.just(cachedDrinks.values.toList())
        }
        return remoteSource.getDrinks().doOnNext { cacheDrinks(it) }
    }

    override fun getDrink(id: String): Observable<Drink> {
        val cachedDrink = cachedDrinks[id]
        if (cachedDrink != null) {
            return Observable.just(cachedDrink)
        }
        return remoteSource.getDrink(id).doOnNext { cacheDrinks(listOf(it)) }
    }

    private fun cacheDrinks(drinks: List<Drink>) {
        cachedDrinks.clear()

        drinks.forEach {
            cachedDrinks[it.id] = it
        }
        cached = true
    }
}