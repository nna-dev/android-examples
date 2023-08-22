package com.nna.example.recyclerview

import java.util.Objects

data class RecyclerItem(
    val id: String,
    val title: String,
    val isBookmarked: Boolean
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RecyclerItem) return false
        return this.id == other.id
    }

    override fun hashCode() = Objects.hashCode(id)
}
