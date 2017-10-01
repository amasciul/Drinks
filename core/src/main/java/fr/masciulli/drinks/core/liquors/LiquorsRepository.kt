package fr.masciulli.drinks.core.liquors

import rx.Observable

class LiquorsRepository(private val remoteSource: LiquorsSource) : LiquorsSource {
    override fun getLiquors(): Observable<List<Liquor>> = remoteSource.getLiquors()
    override fun getLiquor(id: String): Observable<Liquor> = remoteSource.getLiquor(id)
}