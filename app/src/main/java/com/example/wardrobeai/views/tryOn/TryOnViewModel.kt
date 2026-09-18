package com.example.wardrobeai.views.tryOn

import com.example.wardrobeai.firebase.clothing.ClothingFirestoreRepository
import com.example.wardrobeai.firebase.outfit.OutfitFirestoreRepository
import com.example.wardrobeai.firebase.services.AuthService
import com.example.wardrobeai.models.clothing.ClosetOrganiserModel
import com.example.wardrobeai.models.outfit.OutfitModel
import com.example.wardrobeai.models.outfit.OutfitStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class TryOnViewModel @Inject constructor(
    private val authService: AuthService,
    private val clothingRepo: ClothingFirestoreRepository,
    private val outfitRepo: OutfitFirestoreRepository,
    private val outfitStore: OutfitStore
) : ViewModel() {

    private val _clothingItems = MutableStateFlow<List<ClosetOrganiserModel>>(emptyList())

    val clothingItems: StateFlow<List<ClosetOrganiserModel>> = _clothingItems.asStateFlow()

    private val _savedOutfits = MutableStateFlow<List<OutfitModel>>(emptyList())

    val savedOutfits: StateFlow<List<OutfitModel>> = _savedOutfits.asStateFlow()

    private val _isSaving = MutableStateFlow(false)

    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    init { loadData() }


    fun loadData() {
        val uid = authService.currentUserId
        if (uid.isBlank()) return
        viewModelScope.launch {
            try {
                _clothingItems.value = clothingRepo.getAll(uid)
                _savedOutfits.value  = outfitRepo.getAll(uid)
            } catch (e: Exception) {
                Timber.e(e, "TryOnViewModel: failed to load data")
            }
        }
    }


    fun saveOutfit(name: String, items: List<ClosetOrganiserModel>) {
        if (items.isEmpty()) return
        _isSaving.value = true
        viewModelScope.launch {
            try {
                val outfit = OutfitModel(
                    title         = name,
                    description   = "Created in Virtual Try-On",
                    clothingItems = items.toMutableList()
                )
                outfitStore.create(outfit)
                val uid = authService.currentUserId
                if (uid.isNotBlank()) {
                    outfitRepo.upsert(uid, outfit)
                    _savedOutfits.value = outfitRepo.getAll(uid)
                } else {
                    _savedOutfits.value = _savedOutfits.value + outfit
                }
            } catch (e: Exception) {
                Timber.e(e, "TryOnViewModel: saveOutfit failed")
            } finally {
                _isSaving.value = false
            }
        }
    }
}