package com.example.wardrobeai.views.calendar

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.example.wardrobeai.ui.theme.ClosetOrganiserTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CalendarView : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ClosetOrganiserTheme {
                CalendarScreen(onBack = { finish() })
            }
        }
    }
}