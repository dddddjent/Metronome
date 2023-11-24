package com.example.metronome

import android.media.AudioAttributes
import android.media.SoundPool
import android.util.Log
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

class SoundManager(val context: MainActivity) {
    private lateinit var mTask: Runnable
    private var soundPool: SoundPool
    private val soundID = HashMap<String, Int>()
    private val maxStreams = 5
    var interval = 1000L
    var bpm = 60L
    var isRunning = false
    private val executionService = Executors.newScheduledThreadPool(3)
    private var futureTask: ScheduledFuture<*>

    init {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build()
        soundPool = SoundPool.Builder()
            .setMaxStreams(maxStreams)
            .setAudioAttributes(audioAttributes)
            .build()

        soundID["s0"] = soundPool.load(context, R.raw.s0, 1)
        soundID["s1"] = soundPool.load(context, R.raw.s1, 1)

        initTask()

        futureTask =
            executionService.scheduleAtFixedRate(mTask, 1000, interval, TimeUnit.MILLISECONDS)
        futureTask.cancel(true)
    }

    fun initTask() {
        mTask = Runnable {
            soundID["s0"]?.let { soundPool.play(it, 1f, 1f, 0, 0, 1f) }
        }
    }

    fun run() {
        if (futureTask.isCancelled == true || futureTask.isDone == true) {
            futureTask =
                executionService.scheduleAtFixedRate(mTask, 0, interval, TimeUnit.MILLISECONDS)
            isRunning = true
            Log.d(context.Tag, "Task starts")
        } else {
            Log.d(context.Tag, "Task is already running")
        }
    }

    fun stop() {
        if (futureTask.isCancelled == false) {
            futureTask.cancel(true)
            isRunning = false
            Log.d(context.Tag, "Task is stopped")
        } else {
            Log.d(context.Tag, "Task is already not running")
        }
    }
}