package fr.masciulli.drinks.drink

import fr.masciulli.drinks.BasePresenter
import fr.masciulli.drinks.BaseView
import fr.masciulli.drinks.core.drinks.Drink

interface DrinkContract {
    interface View : BaseView {
        fun showDrink(drink: Drink)
        fun showError()
        fun showShareDrink(drink: Drink)
    }

    interface Presenter : BasePresenter {
        fun openShareDrink()
    }
}