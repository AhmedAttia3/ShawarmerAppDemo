package com.shawarmer.app.di.module

import android.content.Context
import androidx.room.Room
import com.shawarmer.app.data.storage.local.db.AppDB
import com.shawarmer.app.data.storage.local.db.AppDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {
    @Provides
    fun provideChannelDao(appDatabase: AppDB): AppDao {
        return appDatabase.channelDao()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDB {
        return Room.databaseBuilder(
            appContext,
            AppDB::class.java,
            "shawarmer"
        ).allowMainThreadQueries().build()
    }
}