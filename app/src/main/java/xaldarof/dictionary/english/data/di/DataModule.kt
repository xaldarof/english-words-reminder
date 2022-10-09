package xaldarof.dictionary.english.data.di

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import xaldarof.dictionary.english.data.database.AppDatabase
import xaldarof.dictionary.english.data.dao.WordsDao
import javax.inject.Singleton

/**
 * @Author: Temur
 * @Date: 09/10/2022
 */

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideWordDao(@ApplicationContext context: Context): WordsDao {
        return AppDatabase.getDatabase(context).getWordsDao()
    }
}