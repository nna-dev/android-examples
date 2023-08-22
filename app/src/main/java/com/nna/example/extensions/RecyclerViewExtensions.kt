package com.nna.example.extensions

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.annotation.IntRange

fun RecyclerView.addVerticalItemSpacing(
    @IntRange(from = 0) vertical: Int = 0,
    @IntRange(from = 0) horizontal: Int = 0,
    ignoreFirst: Boolean = false,
    ignoreLast: Boolean = false
) {
    addItemDecoration(object : ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            outRect.top = vertical
            outRect.bottom = vertical
            outRect.left = horizontal
            outRect.right = horizontal
            if (ignoreFirst || ignoreLast) {
                val position = parent.getChildAdapterPosition(view)
                if (ignoreFirst && position == 0) outRect.top = 0
                if (ignoreLast && position == state.itemCount - 1) outRect.bottom = 0
            }
        }
    })
}