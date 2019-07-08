package dev.williamreed.letsrun.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dev.williamreed.letsrun.R

/**
 * Main (God) Activity
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
