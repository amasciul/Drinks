package fr.masciulli.drinks.core.liquors

import io.reactivex.Single

class LiquorsRepository(private val remoteSource: LiquorsSource) : LiquorsSource {
    private val cachedLiquors: MutableMap<String, Liquor> = LinkedHashMap()
    private var cached = false

    override fun getLiquors(): Single<List<Liquor>> {
        if (cached) {
            return Single.just(cachedLiquors.values.toList())
        }

        return remoteSource.getLiquors().doOnSuccess { cacheLiquors(it) }
    }

    override fun getLiquor(id: String): Single<Liquor> {
        val cachedLiquor = cachedLiquors[id]
        if (cachedLiquor != null) {
            return Single.just(cachedLiquor)
        }

        return remoteSource.getLiquor(id).doOnSuccess { cacheLiquors(listOf(it)) }
    }

    private fun cacheLiquors(liquors: List<Liquor>) {
        liquors.forEach {
            cachedLiquors[it.id] = it
        }
        cached = true
    }
}