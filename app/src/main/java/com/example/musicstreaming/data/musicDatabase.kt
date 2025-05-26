package com.example.musicstreaming.data

import android.content.Context
import androidx.room.*
import androidx.room.RoomDatabase
import com.example.musicstreaming.model.Song
import com.example.musicstreaming.model.Playlist

@Database(entities = [Song::class, Playlist::class], version = 1)
@TypeConverters(Converters::class)
abstract class MusicDatabase : RoomDatabase() {
    abstract fun songDao(): SongDao
    abstract fun playlistDao(): PlaylistDao

    companion object {
        @Volatile
        private var INSTANCE: MusicDatabase? = null

        fun getDatabase(context: Context): MusicDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    MusicDatabase::class.java,
                    "music_database"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
