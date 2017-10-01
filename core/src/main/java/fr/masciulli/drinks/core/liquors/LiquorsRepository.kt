package fr.masciulli.drinks.core.liquors

import rx.Observable

class LiquorsRepository(private val remoteSource: LiquorsSource) : LiquorsSource {
    private val cachedLiquors: MutableMap<String, Liquor> = LinkedHashMap()
    private var cached = false

    override fun getLiquors(): Observable<List<Liquor>> {
        if (cached) {
            return Observable.just(cachedLiquors.values.toList())
        }

        return remoteSource.getLiquors().doOnNext { cacheLiquors(it) }
    }

    override fun getLiquor(id: String): Observable<Liquor> {
        val cachedLiquor = cachedLiquors[id]
        if (cachedLiquor != null) {
            return Observable.just(cachedLiquor)
        }

        return remoteSource.getLiquor(id).doOnNext { cacheLiquors(listOf(it)) }
    }

    private fun cacheLiquors(liquors: List<Liquor>) {
        liquors.forEach {
            cachedLiquors[it.id] = it
        }
        cached = true
    }
}