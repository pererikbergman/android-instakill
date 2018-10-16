package com.rakangsoftware.instakill.utils

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors

val IO: Executor = Executors.newFixedThreadPool(2) // Read & Write
val UI: Executor = UiThreadExecutor() // Updating the UI

internal class UiThreadExecutor : Executor {
    private val mHandler = Handler(Looper.getMainLooper())

    override fun execute(command: Runnable) {
        mHandler.post(command)
    }
}
