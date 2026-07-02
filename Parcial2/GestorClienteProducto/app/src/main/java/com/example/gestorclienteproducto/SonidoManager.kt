package com.example.gestorclienteproducto

import android.content.Context
import android.media.MediaPlayer

object SonidoManager {

    private var sonidoClick: MediaPlayer? = null
    private var sonidoRegresar: MediaPlayer? = null

    fun inicializar(context: Context) {
        sonidoClick = MediaPlayer.create(context, R.raw.mousedown2)
        sonidoRegresar = MediaPlayer.create(context, R.raw.swishout)
    }

    fun reproducirClick() {
        sonidoClick?.start()
    }

    fun reproducirRegresar() {
        sonidoRegresar?.start()
    }

    fun liberar() {
        sonidoClick?.release()
        sonidoClick = null
        sonidoRegresar?.release()
        sonidoRegresar = null
    }
}