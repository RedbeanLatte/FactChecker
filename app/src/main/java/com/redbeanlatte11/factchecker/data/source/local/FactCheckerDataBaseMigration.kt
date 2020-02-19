package com.redbeanlatte11.factchecker.data.source.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class FactCheckerDataBaseMigration {

    companion object {

        val MIGRATION_2_3 = object : Migration(2, 3) {

            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""
                    CREATE TABLE new_Channels (
                        kind TEXT NOT NULL,
                        etag TEXT NOT NULL,
                        id TEXT PRIMARY KEY NOT NULL,
                        createdAt TEXT,
                        title TEXT NOT NULL,
                        description TEXT NOT NULL,
                        publishedAt TEXT NOT NULL,
                        thumbnails TEXT NOT NULL, 
                        localized TEXT NOT NULL,
                        country TEXT DEFAULT 'KR',
                        viewCount INTEGER NOT NULL,
                        commentCount INTEGER NOT NULL,
                        subscriberCount INTEGER NOT NULL,
                        hiddenSubscriberCount INTEGER NOT NULL,
                        videoCount INTEGER NOT NULL
                    )
                    """.trimIndent())
                database.execSQL("""
                    INSERT INTO new_Channels (
                        kind, etag, id, 
                        createdAt, title, description, 
                        publishedAt, thumbnails, localized,
                        country, viewCount, commentCount, 
                        subscriberCount, hiddenSubscriberCount, videoCount
                    )
                    SELECT kind, etag, id, 
                        createdAt, title, description, 
                        publishedAt, thumbnails, localized,
                        country, viewCount, commentCount, 
                        subscriberCount, hiddenSubscriberCount, videoCount FROM Channels
                    """.trimIndent())
                database.execSQL("DROP TABLE Channels")
                database.execSQL("ALTER TABLE new_Channels RENAME TO Channels")
            }
        }
    }
}