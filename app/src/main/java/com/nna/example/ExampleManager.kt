package com.nna.example

import android.content.Context
import android.content.Intent
import com.nna.example.recyclerview.RecyclerViewPaginationActivity

object ExampleManager {
    val examples = listOf(
        ExampleItem("RecyclerView", RecyclerViewPaginationActivity::createIntent),
    )
}

data class ExampleItem(
    val title: String,
    val intentProvider: (context: Context) -> Intent
)
