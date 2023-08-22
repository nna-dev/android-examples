package com.nna.example.recyclerview

import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var errorMessage: String? = null
    var isLoading = true
    var isPaginating = false
    var canPaginating = true
    protected val itemList = mutableListOf<T>()

    override fun getItemCount(): Int {
        return when {
            errorMessage != null || isLoading || itemList.isEmpty() -> 1
            !isPaginating && canPaginating -> itemList.size
            else -> itemList.size + 1
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            errorMessage != null -> ItemViewType.Error.value
            isLoading -> ItemViewType.Loading.value
            isPaginating && position == itemList.size -> ItemViewType.Paginating.value
            itemList.isEmpty() -> ItemViewType.Empty.value
            !canPaginating && position == itemList.size -> ItemViewType.PaginationEnd.value
            else -> ItemViewType.View.value
        }
    }

    fun setError(message: String, isPaginationError: Boolean) {
        if (isPaginationError) {
            setState(ItemViewType.PaginationEnd)
            notifyItemRemoved(itemList.size)
        } else {
            setState(ItemViewType.Error)
            errorMessage = message
            notifyDataSetChanged()
        }
    }

    fun setLoadingView(isPaginating: Boolean) {
        if (isPaginating) {
            setState(ItemViewType.Paginating)
            notifyItemInserted(itemList.size)
        } else {
            setState(ItemViewType.Loading)
            notifyDataSetChanged()
        }
    }

    fun appendItem(item: T) {
        val oldList = itemList.toMutableList()
        itemList.add(item)
        handleDiffUtil(itemList)
    }

    fun deleteItem(item: T) {
        val newList = itemList.toMutableList()
        newList.remove(item)
        handleDiffUtil(newList)
    }

    fun updateItem(item: T, newItem: T) {
        val newList = itemList.toMutableList()
        val index = itemList.indexOfFirst { it == item }
        if (index != -1) newList[index] = newItem
        handleDiffUtil(newList)
    }

    protected abstract fun handleDiffUtil(newList: List<T>)

    fun setItems(items: List<T>, isPaginating: Boolean) {
        setState(ItemViewType.View)
        if (!isPaginating) {
            itemList.clear()
            itemList.addAll(items)
            notifyDataSetChanged()
        } else {
            notifyItemRemoved(itemList.size)
            val newList = itemList.toMutableList().apply { addAll(items) }
            handleDiffUtil(newList)
        }
    }

    fun hasValueItem() = itemList.size > 0

    private fun setState(state: ItemViewType) {
        when(state) {
            ItemViewType.Loading -> {
                isLoading = true
                errorMessage = null
                isPaginating = false
                canPaginating = true
            }
            ItemViewType.Error -> {
                isLoading = false
                isPaginating = false
            }
            ItemViewType.Empty -> {
                isLoading = false
                errorMessage = null
                isPaginating = false
                canPaginating = false
            }
            ItemViewType.Paginating -> {
                isLoading = false
                isPaginating = true
                errorMessage = null
                canPaginating = true
            }
            ItemViewType.PaginationEnd -> {
                isLoading = false
                isPaginating = false
                canPaginating = false
            }
            ItemViewType.View -> {
                isLoading = false
                isPaginating = false
                errorMessage = null
            }
        }
    }
}