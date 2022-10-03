package xaldarof.dictionary.english.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import xaldarof.dictionary.english.domain.UnSeenWordEntity
import xaldarof.dictionary.english.domain.WordEntity

@Database(
    entities = [WordEntity::class, UnSeenWordEntity::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getWordsDao(): WordsDao

    companion object {
        fun getDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, "db")
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}