package com.nna.example

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nna.example.databinding.ItemMainMenuBinding

class MainMenuAdapter(
    val onItemClickListener: (ExampleItem) -> Unit
): RecyclerView.Adapter<MainMenuAdapter.MainMenuViewHolder>() {
    private val menuList = mutableListOf<ExampleItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainMenuViewHolder {
        val itemViewBinding = ItemMainMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainMenuViewHolder(itemViewBinding)
    }

    override fun getItemCount(): Int {
        return menuList.size
    }

    override fun onBindViewHolder(holder: MainMenuViewHolder, position: Int) {
        val item = menuList[position]
        holder.apply {
            btTitle.text = item.title
            btTitle.setOnClickListener { onItemClickListener(item) }
        }
    }

    fun setItems(items: List<ExampleItem>) {
        menuList.addAll(items)
        notifyDataSetChanged()
    }

    class MainMenuViewHolder(binding: ItemMainMenuBinding): RecyclerView.ViewHolder(binding.root) {
        val btTitle = binding.btTitle
    }
}