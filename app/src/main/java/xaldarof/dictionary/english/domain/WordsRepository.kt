package xaldarof.dictionary.english.domain

import xaldarof.dictionary.english.data.WordsDao

class WordsRepository(private val wordsDao: WordsDao) {

    suspend fun getRandomWord(): WordEntity {
        return wordsDao.getRandomWord()
    }
}