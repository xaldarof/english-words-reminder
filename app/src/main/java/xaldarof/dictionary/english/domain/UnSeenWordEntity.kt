package xaldarof.dictionary.english.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "unseen_table")
data class UnSeenWordEntity(@PrimaryKey val body: String)