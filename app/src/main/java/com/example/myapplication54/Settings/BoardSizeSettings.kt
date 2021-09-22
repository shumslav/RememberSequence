package com.firestu.biggiwins.Settings

import android.content.Context

class BoardSizeSettings(context: Context) {

    private companion object{
        const val BOARD_SIZE = "BoardSize"
        const val SIZE = "Size"
    }

    private val boardSize = context.getSharedPreferences(BOARD_SIZE,Context.MODE_PRIVATE)

    var size:Int
    get() {return boardSize.getInt(SIZE,0)}
    set(value) {boardSize.edit().putInt(SIZE,value).apply()}
}