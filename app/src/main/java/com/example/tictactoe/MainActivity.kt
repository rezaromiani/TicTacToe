package com.example.tictactoe

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.navigation.findNavController
import com.example.tictactoe.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onBackPressed() {
        val id = findNavController(binding.navHostFragment.id).currentDestination?.id
        if (id == R.id.gameFragment) {
            AlertDialog.Builder(this)
                .setTitle("Warning")
                .setMessage("Do you want to exit the game?")
                .setNegativeButton(R.string.no, null)
                .setPositiveButton(
                    R.string.yes
                ) { _, _ -> this@MainActivity.bp() }
                .show()
        } else {
            super.onBackPressed()
        }
    }

    private fun bp() {
        super.onBackPressed()
    }
}