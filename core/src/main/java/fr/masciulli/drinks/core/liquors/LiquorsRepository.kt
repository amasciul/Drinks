package fr.masciulli.drinks.core.liquors

import rx.Observable

class LiquorsRepository(private val remoteSource: LiquorsSource) : LiquorsSource {
    override val liquors: Observable<List<Liquor>>
        get() = remoteSource.liquors
}