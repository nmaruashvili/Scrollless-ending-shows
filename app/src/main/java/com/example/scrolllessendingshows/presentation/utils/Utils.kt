package com.example.scrolllessendingshows.presentation.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import kotlin.math.abs

abstract class LinearEndlessScrollListener : RecyclerView.OnScrollListener() {
    abstract fun requestNextPage()

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        if (dy > 0) {
            val lastItemPosition: Int
            val lastVisibleItemPosition: Int
            (recyclerView.layoutManager as LinearLayoutManager).let { lm ->
                lastItemPosition = lm.itemCount - 1
                lastVisibleItemPosition = lm.findLastVisibleItemPosition()
            }
            if (lastItemPosition - lastVisibleItemPosition < 10) {
                requestNextPage()
            }
        }
    }
}

fun RecyclerView.scrollToTop() {
    scrollToPosition(0)
}

fun Fragment.closeKeyboard(context: Context, view: View) {
    val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun AppBarLayout.setOnCollapseListener(block: (Boolean) -> Unit) {
    addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
        block.invoke(abs(verticalOffset) == appBarLayout.totalScrollRange)
    })
}