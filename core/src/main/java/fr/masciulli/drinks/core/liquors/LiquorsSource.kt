package fr.masciulli.drinks.core.liquors

import rx.Observable

interface LiquorsSource {
    val liquors: Observable<List<Liquor>>
}

