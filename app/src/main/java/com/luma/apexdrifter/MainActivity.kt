package com.luma.apexdrifter

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.material.color.DynamicColors
import com.luma.apexdrifter.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DynamicColors.applyToActivityIfAvailable(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        WindowInsetsControllerCompat(window, window.decorView).apply {
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            hide(WindowInsetsCompat.Type.systemBars())
        }

        binding.playButton.setOnClickListener {
            animateCard()
        }

        binding.startButton.setOnClickListener {
            binding.statusText.text = "Velocity Rush Ready"
        }

        binding.root.post {
            animateCard()
        }
    }

    private fun animateCard() {
        binding.cardView.animate().alpha(1f).setDuration(400).start()
        ObjectAnimator.ofFloat(binding.carView, "translationX", -260f, 260f).apply {
            duration = 1200
            interpolator = DecelerateInterpolator()
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
            start()
        }
        binding.statusText.text = "Apex Drifter 3D • 60 FPS ready"
    }
}
