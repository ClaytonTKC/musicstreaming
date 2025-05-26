package com.example.musicstreaming.service

import android.content.*
import android.os.IBinder

class ServiceConnectionHelper(
    private val context: Context,
    private val onServiceConnected: (MusicPlayerService) -> Unit
) {
    private var serviceBound = false
    private var service: MusicPlayerService? = null

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            val musicBinder = binder as MusicPlayerService.MusicBinder
            service = musicBinder.getService()
            serviceBound = true
            onServiceConnected(service!!)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            serviceBound = false
            service = null
        }
    }

    fun bindService() {
        val intent = Intent(context, MusicPlayerService::class.java)
        context.bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }

    fun unbindService() {
        if (serviceBound) {
            context.unbindService(connection)
            serviceBound = false
        }
    }

    fun getService(): MusicPlayerService? = service
}
