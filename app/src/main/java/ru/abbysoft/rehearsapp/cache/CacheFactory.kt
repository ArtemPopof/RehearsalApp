package ru.abbysoft.rehearsapp.cache

class CacheFactory private constructor() {

    companion object {
        private val instance = InMemoryCache()

        fun getDefaultCacheInstance() : Cache {
            return instance
        }
    }
}