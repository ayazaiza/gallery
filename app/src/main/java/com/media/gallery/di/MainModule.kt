package com.media.gallery.di

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.net.wifi.WifiManager
import android.os.Build
import android.text.format.Formatter
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.media.gallery.config.AppConstants
import com.media.gallery.data.data_source.PlayerRoomDatabase
import com.media.gallery.data.repository.MediaFileFetcherRepoImpl
import com.media.gallery.data.repository.SharedPreferenceRepoImpl
import com.media.gallery.data.repository.ViewModelStrResRepoImpl
import com.media.gallery.domain.extensions.getSharedPrefs
import com.media.gallery.domain.repository.MediaFileFetcherRepo
import com.media.gallery.domain.repository.SharedPreferenceRepo
import com.media.gallery.domain.repository.ViewModelStrResRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainModule {

    @RequestedPermissionsArray
    @Singleton
    @Provides
    fun providerArrayPermissions(): Array<String> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_MEDIA_LOCATION
            )
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(
                Manifest.permission.READ_MEDIA_AUDIO,
                Manifest.permission.READ_MEDIA_VIDEO,
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.ACCESS_MEDIA_LOCATION
            )
        } else {
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }
    }

    @Singleton
    @Provides
    fun provideSharedPreference(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPrefs(key = AppConstants.SHARED_PREFS_KEY)
    }

    @Singleton
    @Provides
    fun provideSharedPrefRepo(
        @ApplicationContext context: Context,
        sharedPreferences: SharedPreferences
    ): SharedPreferenceRepo {
        return SharedPreferenceRepoImpl(context = context, sharedPreferences = sharedPreferences)
    }

    @Singleton
    @Provides
    fun provideRoomData(@ApplicationContext context: Context): PlayerRoomDatabase {
        return Room.databaseBuilder(
            context,
            PlayerRoomDatabase::class.java,
            PlayerRoomDatabase.DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }


    @Singleton
    @Provides
    fun provideGalleryFetcherRepo(
        sharedPreferenceRepo: SharedPreferenceRepo,
        playerRoomDatabase: PlayerRoomDatabase,
        @ApplicationContext context: Context
    ): MediaFileFetcherRepo {
        return MediaFileFetcherRepoImpl(
            sharedPreferenceRepo = sharedPreferenceRepo,
            playerRoomDatabase = playerRoomDatabase,
            context = context
        )
    }

    @Singleton
      @Provides
      fun provideViewModelStrRes(@ApplicationContext context: Context): ViewModelStrResRepo {
          return ViewModelStrResRepoImpl(context = context)
      }

    /* @Singleton
   @Provides
   fun provideAdmobAdsRepo(@ApplicationContext context: Context): AdMobAdsRepo {
       return AdMobAdsRepoImpl(context)
   }

   @Singleton
   @Provides
   fun providePort(): Int = 8088

   @Singleton
   @Provides
   fun provideServer(port: Int): SimpleSever {
       return SimpleSever.newInstance(port)
   }*/

    @Suppress("DEPRECATION")
    @IpAddress
    @Singleton
    @Provides
    fun getIpAddress(@ApplicationContext context: Context): String {
        val wm =
            context.applicationContext.getSystemService(AppCompatActivity.WIFI_SERVICE) as WifiManager
        return Formatter.formatIpAddress(
            wm.connectionInfo
                .ipAddress
        )
    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RequestedPermissionsArray

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IpAddress
