package com.example.project_1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.project_1.databinding.ActivityVerificationBinding

class VerificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVerificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val number = intent.getStringExtra("number")
        val valid = intent.getBooleanExtra("valid", false)
        val country = intent.getStringExtra("country")
        val location = intent.getStringExtra("location")
        val carrier = intent.getStringExtra("carrier")

        binding.textNumber.text = "Number: $number"
        binding.textValid.text = "Valid: $valid"
        binding.textCountry.text = "Country: $country"
        binding.textLocation.text = "Location: $location"
        binding.textCarrier.text = "Carrier: $carrier"

        binding.buttonBack.setOnClickListener {
            finish() // This returns to the previous activity (MainActivity/HomeFragment)
        }
    }
}