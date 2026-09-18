package com.example.wardrobeai.di


import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.example.wardrobeai.firebase.clothing.ClothingFirestoreRepository
import com.example.wardrobeai.firebase.outfit.OutfitFirestoreRepository
import com.example.wardrobeai.firebase.services.AuthService
import com.example.wardrobeai.firebase.storage.ImageStorageRepository
import com.example.wardrobeai.firebase.calendar.OutfitCalendarFirestoreRepository

@EntryPoint
@InstallIn(SingletonComponent::class)
interface FirebaseEntryPoint {
    fun authService(): AuthService

    fun clothingFirestoreRepository(): ClothingFirestoreRepository

    fun outfitFirestoreRepository(): OutfitFirestoreRepository

    fun imageStorageRepository(): ImageStorageRepository

    fun outfitCalendarFirestoreRepository(): OutfitCalendarFirestoreRepository
}