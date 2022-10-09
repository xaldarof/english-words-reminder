package xaldarof.dictionary.english.domain.repositories

import xaldarof.dictionary.english.data.dao.WordsDao
import xaldarof.dictionary.english.domain.models.UnSeenWordEntity
import xaldarof.dictionary.english.domain.models.WordEntity
import javax.inject.Inject


interface WordsRepository {
    suspend fun getRandomWord(): WordEntity
    suspend fun getRandomUnSeedWord(): UnSeenWordEntity
    suspend fun insertWord(word: WordEntity)
    suspend fun getCount(): Long
    suspend fun insertToUnRead(unSeenWordEntity: UnSeenWordEntity)
    suspend fun clearUnSeenWords()
    suspend fun getUnSeenWordsCount(): Int
    suspend fun updateKnow(body: String)


    class Base @Inject constructor(private val wordsDao: WordsDao) : WordsRepository {
        override suspend fun getRandomWord(): WordEntity {
            return wordsDao.getRandomWord()
        }


        override suspend fun getRandomUnSeedWord(): UnSeenWordEntity {
            return wordsDao.getRandomUnSeedWord()
        }


        override suspend fun insertWord(word: WordEntity) {
            wordsDao.insertWord(word)
        }


        override suspend fun getCount(): Long {
            return wordsDao.getCount()
        }


        override suspend fun insertToUnRead(unSeenWordEntity: UnSeenWordEntity) {
            wordsDao.insertToUnRead(unSeenWordEntity)
        }


        override suspend fun clearUnSeenWords() {
            wordsDao.clearUnSeenWords()
        }


        override suspend fun getUnSeenWordsCount(): Int {
            return wordsDao.getUnSeenWordsCount()
        }

        override suspend fun updateKnow(body: String) {
            wordsDao.updateKnow(body)
        }
    }
}