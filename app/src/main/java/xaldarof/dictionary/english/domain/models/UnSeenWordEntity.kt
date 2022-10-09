package xaldarof.dictionary.english.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "unseen_table")
data class UnSeenWordEntity(@PrimaryKey val body: String)