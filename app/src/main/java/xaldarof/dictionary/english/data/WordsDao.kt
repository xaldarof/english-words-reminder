package xaldarof.dictionary.english.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import xaldarof.dictionary.english.domain.WordEntity

@Dao
interface WordsDao {

    @Query("SELECT * FROM words_table ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomWord(): WordEntity


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWord(word: WordEntity)

    @Query("SELECT COUNT(body) FROM words_table")
    suspend fun getCount():Long
}