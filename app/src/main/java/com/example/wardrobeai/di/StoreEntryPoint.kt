package com.example.wardrobeai.di

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.example.wardrobeai.models.clothing.ClothingStore
import com.example.wardrobeai.models.outfit.OutfitStore

@EntryPoint
@InstallIn(SingletonComponent::class)
interface StoreEntryPoint {

    fun clothingStore(): ClothingStore

    fun outfitStore(): OutfitStore
}