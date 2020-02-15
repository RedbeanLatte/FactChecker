package com.redbeanlatte11.factchecker.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.redbeanlatte11.factchecker.data.Channel
import com.redbeanlatte11.factchecker.data.Video

/**
 * The Room Database that contains the Video table and Channel table.
 *
 * Note that exportSchema should be true in production databases.
 */
@Database(entities = [Video::class, Channel::class], version = 3, exportSchema = false)
@TypeConverters(ThumbnailsConverter::class, StringListConverter::class, LocalizedConverter::class)
abstract class FactCheckerDataBase : RoomDatabase() {

    abstract fun videoDao(): VideosDao

    abstract fun channelDao(): ChannelsDao
}
