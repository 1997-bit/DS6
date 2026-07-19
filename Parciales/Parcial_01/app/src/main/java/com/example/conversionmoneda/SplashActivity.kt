package com.example.conversionmoneda

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.conversionmoneda.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Aplicar animación de entrada al logo
        val slideIn = AnimationUtils.loadAnimation(this, R.anim.slide_in_logo)
        binding.ivLogoSplash.startAnimation(slideIn)

        // Aplicar animación al texto
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in_text)
        binding.tvSplashText.startAnimation(fadeIn)

        // Ir a MainActivity después de 3 segundos
        binding.root.postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }, 3000)
    }
}