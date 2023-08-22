package com.nna.example.recyclerview

data class RecyclerViewOperation<T>(
    val operationType: OperationType,
    val data: T
)

enum class OperationType {
    Insert,
    Delete,
    Update
}