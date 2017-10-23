package fr.masciulli.drinks.drink

import fr.masciulli.drinks.BaseViewModel
import fr.masciulli.drinks.core.drinks.Drink
import fr.masciulli.drinks.core.drinks.DrinksSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class DrinkViewModel(
        private val drinksSource: DrinksSource,
        private val drinkId: String
) : BaseViewModel {
    private var drinkDisposable: Disposable? = null

    val drink: BehaviorSubject<Drink> = BehaviorSubject.create()
    val error: BehaviorSubject<Throwable> = BehaviorSubject.create()
    val shareDrink: BehaviorSubject<Drink> = BehaviorSubject.create()

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
        this.drink.onNext(drink)
    }

    private fun errorLoadingDrink(throwable: Throwable) {
        error.onNext(throwable)
    }

    fun openShareDrink() {
        drink.value?.let { shareDrink.onNext(it) }
    }

    override fun stop() {
        drinkDisposable?.dispose()
    }
}