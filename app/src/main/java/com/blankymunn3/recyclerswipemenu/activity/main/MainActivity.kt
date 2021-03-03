package com.blankymunn3.recyclerswipemenu.activity.main

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import com.blankymunn3.recyclerswipemenu.R
import com.blankymunn3.recyclerswipemenu.adapter.SwipeMenuRVAdapter
import com.blankymunn3.recyclerswipemenu.databinding.ActivityMainBinding
import com.blankymunn3.recyclerswipemenu.util.*
import java.util.*

class MainActivity : BaseActivity() {
    private val binding by binding<ActivityMainBinding>(R.layout.activity_main)
    private val viewModel by GetViewModel(MainViewModel::class.java)
    lateinit var adapter: SwipeMenuRVAdapter
    lateinit var swipeHelperCallback: SwipeHelperCallback

    private val swipeRVTitle: MutableList<String> = ArrayList()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this@MainActivity
        binding.viewModel = viewModel
        swipeHelperCallback = SwipeHelperCallback().apply {
            setClamp(this@MainActivity)
        }
        for (i in 0..10) {
            swipeRVTitle.add("SwipeMenu$i")
        }
        viewModel.list.postValue(swipeRVTitle)
        adapter = SwipeMenuRVAdapter()

        val itemTouchHelper = ItemTouchHelper(swipeHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.rvSwipeMenu)

        binding.rvSwipeMenu.apply {
            layoutManager = GridLayoutManager(this@MainActivity, 1)
            adapter = this@MainActivity.adapter
            setOnTouchListener { _, _ ->
                swipeHelperCallback.removePreviousClamp(this)
                false
            }
        }

        viewModel.list.observe(this@MainActivity, {
            if (it.isNotEmpty()) {
                adapter.setData(it)
            }
        })
        adapter.onSwipeMenuItemClickListener(object : SwipeMenuRVAdapter.SwipeMenuItemClickListener {
            override fun swipeMenuDeleteClick(position: Int) {
                if (swipeHelperCallback.buttonsState == SwipeHelperCallback.ButtonsState.RIGHT_VISIBLE && swipeHelperCallback.currentPosition == position) ShowSnackBar.showSnackBar(binding.rvSwipeMenu,
                    "${viewModel.list.value!![position]} Delete")
            }

            override fun swipeMenuModifyClick(position: Int) {
                if (swipeHelperCallback.buttonsState == SwipeHelperCallback.ButtonsState.LEFT_VISIBLE && swipeHelperCallback.currentPosition == position) ShowSnackBar.showSnackBar(binding.rvSwipeMenu,
                    "${viewModel.list.value!![position]} Delete")
            }

        })
    }
}