package com.example.project

import android.widget.ImageButton
import androidx.core.graphics.drawable.DrawableCompat

fun changeButtonCollor(button : ImageButton, color : Int) {
    val drawable = button.drawable
    DrawableCompat.setTint(drawable, color)
    button.setImageDrawable(drawable)
}