package com.blankymunn3.recyclerswipemenu.util

import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.view.View
import android.view.WindowManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.blankymunn3.recyclerswipemenu.adapter.SwipeMenuRVAdapter
import kotlinx.android.synthetic.main.recycler_item_swipe_menu.view.*
import kotlin.math.max
import kotlin.math.min

class SwipeHelperCallback : ItemTouchHelper.Callback() {

    enum class ButtonsState {
        GONE, LEFT_VISIBLE, RIGHT_VISIBLE
    }

    var currentPosition: Int? = null
    private var previousPosition: Int? = null
    private var currentDx = 0f
    private var addDx = 0f
    private var plusClamp = 0f
    private var minusClamp = 0f

    var buttonsState = ButtonsState.GONE

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        currentDx = 0f
        previousPosition = viewHolder.absoluteAdapterPosition
        getDefaultUIUtil().clearView(getView(viewHolder))
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        viewHolder?.let {
            currentPosition = viewHolder.absoluteAdapterPosition
            getDefaultUIUtil().onSelected(getView(it))
            addDx = 0f
        }
    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            val view = getView(viewHolder)
            val isClamped = getTag(viewHolder)
            val x = clampViewPositionHorizontal(view, dX, isClamped, isCurrentlyActive)
            currentDx = x
            getDefaultUIUtil().onDraw(c, recyclerView, view, x, dY, actionState, isCurrentlyActive)
        }
    }

    private fun clampViewPositionHorizontal(
        view: View,
        dX: Float,
        isClamped: Boolean,
        isCurrentlyActive: Boolean
    ) : Float {
        val min: Float = -view.width.toFloat()/2
        val max: Float = +view.width.toFloat()*2
        addDx += dX
        val x = if (isClamped && addDx > 0) {
            if (isCurrentlyActive) {
                buttonsState = ButtonsState.GONE
                dX - minusClamp
            } else {
                buttonsState = ButtonsState.LEFT_VISIBLE
                minusClamp
            }
        } else if (isClamped && addDx < 0) {
            if (isCurrentlyActive) {
                buttonsState = ButtonsState.GONE
                dX - plusClamp
            } else {
                buttonsState = ButtonsState.RIGHT_VISIBLE
                plusClamp
            }
        } else {
            buttonsState = ButtonsState.GONE
            dX
        }

        return min(max(min, x), max)
    }

    private fun getView(viewHolder: RecyclerView.ViewHolder): View {
        return (viewHolder as SwipeMenuRVAdapter.ViewHolder).itemView.layout_rv_swipe_menu
    }


    override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
        return defaultValue * 10
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        val isClamped = getTag(viewHolder)
        setTag(viewHolder, !isClamped && (currentDx <= minusClamp || currentDx >= plusClamp))
        return 2f
    }

    private fun setTag(viewHolder: RecyclerView.ViewHolder, isClamped: Boolean) {
        viewHolder.itemView.tag = isClamped
    }

    private fun getTag(viewHolder: RecyclerView.ViewHolder) : Boolean {
        return viewHolder.itemView.tag as? Boolean ?: false
    }

    fun setClamp(activity: Activity) {
        val display = (activity.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        val width = display.width
        this.plusClamp = -(width.toFloat()/5)
        this.minusClamp = (width.toFloat()/5)
    }

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        return makeMovementFlags(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}
    fun removePreviousClamp(recyclerView: RecyclerView) {
        if (currentPosition == previousPosition)
            return
        previousPosition?.let {
            val viewHolder = recyclerView.findViewHolderForAdapterPosition(it) ?: return
            getView(viewHolder).translationX = 0f
            setTag(viewHolder, false)
            previousPosition = null
        }
    }
}