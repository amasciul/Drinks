package fr.masciulli.drinks.core.liquors

import rx.Observable

interface LiquorsSource {
    fun getLiquors(): Observable<List<Liquor>>
    fun getLiquor(id: String): Observable<Liquor>
}

