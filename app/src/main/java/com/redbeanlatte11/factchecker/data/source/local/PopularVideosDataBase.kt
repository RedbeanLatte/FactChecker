package com.redbeanlatte11.factchecker.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.redbeanlatte11.factchecker.data.Video

/**
 * The Room Database that contains the Video table.
 *
 * Note that exportSchema should be true in production databases.
 */
@Database(entities = [Video::class], version = 2, exportSchema = false)
@TypeConverters(ThumbnailsConverter::class, StringListConverter::class, LocalizedConverter::class)
abstract class PopularVideosDataBase : RoomDatabase() {

    abstract fun videoDao(): VideosDao
}