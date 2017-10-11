package fr.masciulli.drinks.drink

import fr.masciulli.drinks.core.drinks.Drink
import fr.masciulli.drinks.core.drinks.DrinksSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class DrinkPresenter(
        private val drinksSource: DrinksSource,
        private val view: DrinkContract.View,
        private val drinkId: String
) : DrinkContract.Presenter {
    private var drinkDisposable: Disposable? = null
    private var drink: Drink? = null

    override fun start() {
        drinkDisposable = drinksSource.getDrink(drinkId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { drinkLoaded(it) },
                        { errorLoadingDrink(it) }
                )
    }

    private fun drinkLoaded(drink: Drink) {
        this.drink = drink
        view.showDrink(drink)
    }

    private fun errorLoadingDrink(throwable: Throwable) {
        view.showError()
    }

    override fun openShareDrink() {
        drink?.let { view.showShareDrink(it) }
    }

    override fun stop() {
        drinkDisposable?.dispose()
    }
}