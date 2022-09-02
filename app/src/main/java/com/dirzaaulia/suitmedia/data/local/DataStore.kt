package com.dirzaaulia.suitmedia.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.dirzaaulia.suitmedia.DATA_STORE
import com.dirzaaulia.suitmedia.util.replaceIfNull
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStore @Inject constructor(
  @ApplicationContext private val context: Context
) {

  private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = DATA_STORE
  )

  private val _name = stringPreferencesKey("name")

  /**
   * Name
   */
  val nameFlow: Flow<String> = context.dataStore.data
    .catch {
      if (it is IOException) {
        it.printStackTrace()
        emit(emptyPreferences())
      } else {
        throw it
      }
    }
    .map { preferences ->
      preferences[_name].replaceIfNull()
    }

  suspend fun setNameToDataStore(name: String) {
    context.dataStore.edit { preferences ->
      preferences[_name] = name
    }
  }
}