package fr.masciulli.drinks.core
data class Liquor(
        val name: String,
        val imageUrl: String,
        val wikipedia: String,
        val history: String,
        val otherNames: List<String>
)
