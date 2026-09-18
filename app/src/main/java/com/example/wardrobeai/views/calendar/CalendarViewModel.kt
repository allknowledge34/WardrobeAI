package com.example.wardrobeai.views.calendar

import com.example.wardrobeai.models.calendar.OutfitCalendarEntry
import com.example.wardrobeai.models.outfit.OutfitModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wardrobeai.firebase.calendar.OutfitCalendarFirestoreRepository
import com.example.wardrobeai.firebase.outfit.OutfitFirestoreRepository
import com.example.wardrobeai.firebase.services.AuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val authService: AuthService,
    private val calendarRepo: OutfitCalendarFirestoreRepository,
    private val outfitRepo: OutfitFirestoreRepository
) : ViewModel() {

    private val _calendarEntries = MutableStateFlow<Map<String, OutfitCalendarEntry>>(emptyMap())

    /** A map of date key strings to their assigned [OutfitCalendarEntry]. */
    val calendarEntries: StateFlow<Map<String, OutfitCalendarEntry>> = _calendarEntries

    private val _outfits = MutableStateFlow<List<OutfitModel>>(emptyList())
    val outfits: StateFlow<List<OutfitModel>> = _outfits

    private val _isLoading = MutableStateFlow(false)

    val isLoading: StateFlow<Boolean> = _isLoading

    init { refresh() }

    fun refresh() {
        val uid = authService.currentUserId
        if (uid.isBlank()) return
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _calendarEntries.value = calendarRepo.getAll(uid)
                _outfits.value = outfitRepo.getAll(uid)
            } catch (e: Exception) {
                Timber.e(e, "Calendar refresh failed")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun assignOutfit(dateKey: String, outfit: OutfitModel?, note: String = "") {
        val uid = authService.currentUserId
        if (uid.isBlank()) return
        viewModelScope.launch {
            try {
                if (outfit == null) {
                    calendarRepo.delete(uid, dateKey)
                    _calendarEntries.value = _calendarEntries.value - dateKey
                } else {
                    val entry = OutfitCalendarEntry(
                        dateKey = dateKey,
                        outfitId = outfit.id,
                        outfitTitle = outfit.title,
                        note = note
                    )
                    calendarRepo.upsert(uid, entry)
                    _calendarEntries.value = _calendarEntries.value + (dateKey to entry)
                }
            } catch (e: Exception) {
                Timber.e(e, "Assign outfit to calendar failed")
            }
        }
    }
}