package fr.masciulli.drinks.core.liquors
data class Liquor(
        val id: String,
        val name: String,
        val imageUrl: String,
        val wikipedia: String,
        val history: String,
        val otherNames: List<String>
)
