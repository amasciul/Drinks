package fr.masciulli.drinks.core.drinks

class DrinksRepository(private val remoteSource: DrinksSource) : DrinksSource {
    override fun getDrinks() = remoteSource.getDrinks()
    override fun getDrink(id: String) = remoteSource.getDrink(id)
}