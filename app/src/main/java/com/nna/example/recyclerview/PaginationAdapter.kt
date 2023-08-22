package com.nna.example.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.nna.example.R
import com.nna.example.databinding.ItemPaginationEmptyBinding
import com.nna.example.databinding.ItemPaginationEndBinding
import com.nna.example.databinding.ItemPaginationErrorBinding
import com.nna.example.databinding.ItemPaginationLoadingBinding
import com.nna.example.databinding.ItemPaginationPagingBinding
import com.nna.example.databinding.ItemPaginationViewBinding

class PaginationAdapter: BaseAdapter<RecyclerItem>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ItemViewType.Loading.value -> LoadingViewHolder(
                ItemPaginationLoadingBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                )
            )
            ItemViewType.Error.value -> ErrorViewHolder(
                ItemPaginationErrorBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                )
            )
            ItemViewType.Empty.value -> EmptyViewHolder(
                ItemPaginationEmptyBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                )
            )
            ItemViewType.Paginating.value -> PagingViewHolder(
                ItemPaginationPagingBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                )
            )
            ItemViewType.PaginationEnd.value -> PagingEndViewHolder(
                ItemPaginationEndBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                )
            )
            else -> ItemViewHolder(ItemPaginationViewBinding.inflate(layoutInflater, parent, false))
        }
    }

    override fun handleDiffUtil(newList: List<RecyclerItem>) {
        val diffUtil = PaginationDiffUtilCallback(itemList, newList)
        val diffResults = DiffUtil.calculateDiff(diffUtil, true)
        itemList.clear()
        itemList.addAll(newList)
        diffResults.dispatchUpdatesTo(this)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            ItemViewType.View.value -> {
                (holder as ItemViewHolder).bind(itemList[position])
            }
        }
    }

    class ItemViewHolder(binding: ItemPaginationViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val title = binding.title
        val btBookmark = binding.btBookmark
        val btMore = binding.btMore

        fun bind(data: RecyclerItem) {
            title.text = data.title
            btBookmark.setImageResource(if (data.isBookmarked) R.drawable.ic_bookmark else R.drawable.ic_bookmark_border)
        }
    }

    class ErrorViewHolder(binding: ItemPaginationErrorBinding) :
        RecyclerView.ViewHolder(binding.root)

    class LoadingViewHolder(binding: ItemPaginationLoadingBinding) :
        RecyclerView.ViewHolder(binding.root)

    class EmptyViewHolder(binding: ItemPaginationEmptyBinding) :
        RecyclerView.ViewHolder(binding.root)

    class PagingViewHolder(binding: ItemPaginationPagingBinding) :
        RecyclerView.ViewHolder(binding.root)

    class PagingEndViewHolder(binding: ItemPaginationEndBinding) :
        RecyclerView.ViewHolder(binding.root)
}

class PaginationDiffUtilCallback(
    private val oldList: List<RecyclerItem>,
    private val newList: List<RecyclerItem>
): DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        if (oldList[oldItemPosition].id != newList[newItemPosition].id) return false
        if (oldList[oldItemPosition].title != newList[newItemPosition].title) return false
        if (oldList[oldItemPosition].isBookmarked != newList[newItemPosition].isBookmarked) return false
        return true
    }

}