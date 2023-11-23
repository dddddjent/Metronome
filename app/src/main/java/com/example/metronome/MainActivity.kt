package com.example.metronome

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.lang.Error
import java.lang.NumberFormatException

class MainActivity : AppCompatActivity() {
    val Tag = "MainActivity"
    lateinit var soundManager: SoundManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        soundManager = SoundManager(this)

        val playButton = findViewById<Button>(R.id.play_button)
        playButton.setOnClickListener {
            soundManager.run()
            Log.i(Tag, "Start playing")
        }

        val stopButton = findViewById<Button>(R.id.stop_button)
        stopButton.setOnClickListener {
            soundManager.stop()
            Log.i(Tag, "Stop playing")
        }

        val setButton = findViewById<Button>(R.id.set_button)
        setButton.setOnClickListener {
            val text = findViewById<TextView>(R.id.bpm_text).text.toString()
            val bpm = try {
                text.toLong()
            } catch (e: NumberFormatException) {
                Toast.makeText(this, "Not an Integer!", Toast.LENGTH_SHORT).show()
                Log.e(Tag, "Not an Integer!")
                return@setOnClickListener
            }
            soundManager.interval = bpmToIntervalMS(bpm)
            if (soundManager.isRunning == true) {
                soundManager.stop()
                soundManager.run()
            }
            Toast.makeText(this, "bpm: $bpm", Toast.LENGTH_SHORT).show()
            Log.i(Tag, "bpm: $bpm")
        }
    }

    private fun bpmToIntervalMS(bpm: Long): Long {
        return 60000 / bpm
    }
}