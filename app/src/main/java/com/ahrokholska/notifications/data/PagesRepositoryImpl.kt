package com.ahrokholska.notifications.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.ahrokholska.notifications.domain.repositories.PagesRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "cache")

@Singleton
class PagesRepositoryImpl @Inject constructor(@ApplicationContext context: Context) :
    PagesRepository {
    private val dataStore = context.dataStore

    override suspend fun addPage() {
        withContext(Dispatchers.IO) {
            dataStore.edit {
                it[PAGE_KEY] = (it[PAGE_KEY] ?: 1) + 1
            }
        }
    }

    override suspend fun removePage() {
        withContext(Dispatchers.IO) {
            dataStore.edit {
                it[PAGE_KEY] = (it[PAGE_KEY] ?: 1) - 1
            }
        }
    }

    override suspend fun getLastPage(): Int? = withContext(Dispatchers.IO) {
        dataStore.data.first()[PAGE_KEY]
    }

    companion object {
        val PAGE_KEY = intPreferencesKey("PAGE_KEY")
    }
}