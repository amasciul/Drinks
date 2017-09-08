package fr.masciulli.drinks.core

import rx.Observable

interface LiquorsSource {
    val liquors: Observable<List<Liquor>>
}

