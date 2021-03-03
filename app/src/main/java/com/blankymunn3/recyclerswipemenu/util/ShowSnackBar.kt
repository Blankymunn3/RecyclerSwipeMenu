package com.blankymunn3.recyclerswipemenu.util

import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.blankymunn3.recyclerswipemenu.R
import com.google.android.material.snackbar.Snackbar

object ShowSnackBar {
    fun showSnackBar(view: View, msg: String) {

        val snackBar = Snackbar.make(view, msg, Snackbar.LENGTH_SHORT)
        snackBar.setTextColor(ContextCompat.getColor(view.context, R.color.white))
        val snackBarView: View = snackBar.view
        val params = snackBarView.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.TOP
        snackBarView.layoutParams = params
        snackBar.show()
    }
}