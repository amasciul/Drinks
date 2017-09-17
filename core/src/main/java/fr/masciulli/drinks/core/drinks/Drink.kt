package fr.masciulli.drinks.core.drinks

data class Drink(
        val name: String,
        val imageUrl: String,
        val history: String,
        val wikipedia: String,
        val instructions: String,
        val ingredients: List<String>
)
