package xaldarof.dictionary.english.domain.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import xaldarof.dictionary.english.domain.repositories.WordsRepository
import javax.inject.Singleton

/**
 * @Author: Temur
 * @Date: 09/10/2022
 */

@Module
@InstallIn(SingletonComponent::class)
interface DomainModule {

    @Singleton
    @Binds
    fun provideWordsRepository(wordsRepository: WordsRepository.Base): WordsRepository
}