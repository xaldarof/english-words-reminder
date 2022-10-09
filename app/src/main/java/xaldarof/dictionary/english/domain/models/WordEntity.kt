package xaldarof.dictionary.english.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "words_table")
data class WordEntity(
    @PrimaryKey
    val body: String,
    val userKnows: Boolean
)