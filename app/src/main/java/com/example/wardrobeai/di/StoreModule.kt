package com.example.wardrobeai.di

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import com.example.wardrobeai.firebase.calendar.OutfitCalendarFirestoreRepository
import com.example.wardrobeai.models.ClosetSQLStore
import com.example.wardrobeai.models.OutfitJSONStore
import com.example.wardrobeai.models.clothing.ClothingStore
import com.example.wardrobeai.models.outfit.OutfitStore
import com.example.wardrobeai.preferences.LocationPreferencesRepository
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object StoreModule {

    @Provides
    @Singleton
    fun provideClothingStore(@ApplicationContext context: Context): ClothingStore {
        return ClosetSQLStore(context)
    }

    @Provides
    @Singleton
    fun provideClosetSQLStore(@ApplicationContext context: Context): ClosetSQLStore {
        return ClosetSQLStore(context)
    }

    @Provides
    @Singleton
    fun provideOutfitStore(@ApplicationContext context: Context): OutfitStore {
        return OutfitJSONStore(context)
    }

    @Provides @Singleton
    fun provideOutfitCalendarFirestoreRepository(
        firestore: FirebaseFirestore
    ): OutfitCalendarFirestoreRepository = OutfitCalendarFirestoreRepository(firestore)

    @Provides
    @Singleton
    fun provideLocationPreferencesRepository(@ApplicationContext context: Context): LocationPreferencesRepository {
        return LocationPreferencesRepository(context)
    }
}