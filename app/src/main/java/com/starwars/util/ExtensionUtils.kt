package com.starwars.util

import android.graphics.Color
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.starwars.R

fun AppCompatActivity.showCustomSnackBar(view: View, message: String) {
    Snackbar.make(
        view,
        message,
        Snackbar.LENGTH_SHORT
    ).setBackgroundTint(ContextCompat.getColor(this, R.color.colorAccent))
        .setTextColor(Color.BLACK)
        .show()
}