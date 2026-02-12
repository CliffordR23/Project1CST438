package com.example.project_1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.project_1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // binding will hold references to every view in activity_main.xml
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) // CALL super or else it'll break...

        binding = ActivityMainBinding.inflate(layoutInflater) // creates the view objects
        setContentView(binding.root) // attach to ui screen
    }

}
