package com.nna.example.recyclerview

enum class ItemViewType(val value: Int) {
    Empty(0),
    Loading(1),
    Error(2),
    View(3),
    Paginating(4),
    PaginationEnd(5)
}
