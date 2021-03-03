package com.blankymunn3.recyclerswipemenu.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.blankymunn3.recyclerswipemenu.databinding.RecyclerItemSwipeMenuBinding
import com.blankymunn3.recyclerswipemenu.util.DiffCallback

class SwipeMenuRVAdapter(var list: List<String> = emptyList()): RecyclerView.Adapter<SwipeMenuRVAdapter.ViewHolder>() {
    private lateinit var swipeMenuItemClickListener: SwipeMenuItemClickListener

    interface SwipeMenuItemClickListener {
        fun swipeMenuDeleteClick(position: Int)
        fun swipeMenuModifyClick(position: Int)
    }

    fun onSwipeMenuItemClickListener(swipeMenuItemClickListener: SwipeMenuItemClickListener) {
        this.swipeMenuItemClickListener = swipeMenuItemClickListener
    }

    fun setData(newData: List<String>) {
        val diffResult = DiffUtil.calculateDiff(DiffCallback(this.list, newData))
        this.list = newData
        diffResult.dispatchUpdatesTo(this@SwipeMenuRVAdapter)
    }

    inner class ViewHolder(private val binding: RecyclerItemSwipeMenuBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(title: String) {
            binding.tvSwipeMenuTitle.text = title
            binding.btnDiagnosticHistoryDelete.setOnClickListener {
                swipeMenuItemClickListener.swipeMenuDeleteClick(absoluteAdapterPosition)
            }
            binding.btnDiagnosticHistoryModify.setOnClickListener {
                swipeMenuItemClickListener.swipeMenuModifyClick(absoluteAdapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SwipeMenuRVAdapter.ViewHolder =
        ViewHolder(RecyclerItemSwipeMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: SwipeMenuRVAdapter.ViewHolder, position: Int) = holder.bind(list[position])

    override fun getItemCount(): Int = list.size
}