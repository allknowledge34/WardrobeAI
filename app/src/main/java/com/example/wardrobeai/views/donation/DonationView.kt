package com.example.wardrobeai.views.donation

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.example.wardrobeai.ui.theme.ClosetOrganiserTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DonationView : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ClosetOrganiserTheme {
                DonationScreen(onBack = { finish() })
            }
        }
    }
}