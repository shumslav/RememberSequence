package com.firestu.biggiwins.Settings

import android.content.Context

class GameSettings(context: Context) {

    private companion object{
        private const val GAME_SETTINGS = "GameSettings"
        private const val MAX_LEVEL = "MaxLevel"
    }

    private val gameSettings = context.getSharedPreferences(GAME_SETTINGS,Context.MODE_PRIVATE)

    var maxLevel:Int
    get() {return gameSettings.getInt(MAX_LEVEL,1)}
    set(value) {gameSettings.edit().putInt(MAX_LEVEL,value).apply()}
}