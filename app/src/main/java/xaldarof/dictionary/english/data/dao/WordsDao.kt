package xaldarof.dictionary.english.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import xaldarof.dictionary.english.domain.models.UnSeenWordEntity
import xaldarof.dictionary.english.domain.models.WordEntity

@Dao
interface WordsDao {

    @Query("SELECT * FROM words_table WHERE userKnows = 0 ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomWord(): WordEntity


    @Query("SELECT * FROM unseen_table ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomUnSeedWord(): UnSeenWordEntity


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWord(word: WordEntity)


    @Query("SELECT COUNT(body) FROM words_table")
    suspend fun getCount(): Long


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToUnRead(unSeenWordEntity: UnSeenWordEntity)


    @Query("DELETE FROM unseen_table")
    suspend fun clearUnSeenWords()


    @Query("SELECT COUNT(body) FROM unseen_table")
    suspend fun getUnSeenWordsCount(): Int

    @Query("UPDATE words_table SET userKnows=1 WHERE body=:body")
    suspend fun updateKnow(body: String)


}