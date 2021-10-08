package com.yc.jetpacklib.recycleView

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.yc.jetpacklib.R
import com.yc.jetpacklib.utils.YcResources
import com.yc.jetpacklib.utils.YcUI
import com.yc.jetpacklib.utils.ycDp

/**
 * Creator: yc
 * Date: 2021/9/1 18:05
 * UseDes:分割线
 * //TODO 很久以前的代码（暂时先用着）
 */
class YcDecoration(private var mSpace: Int = 10.ycDp()) : ItemDecoration() {
    private var mLineColor = YcResources.getColorRes(R.color.transparent) //0xFFE9F7FF;
    private val mIsHasColor = false //是否有颜色
    private val mIsHasBorder = false //边缘是否需要分割线；线性布局的上下/左右，表格布局的四周
    private var mPaint: Paint? = null

    init {
        mPaint = Paint()
        mPaint!!.color = mLineColor
    }

    fun setSpaceDp(space: Int) {
        mSpace = YcUI.dpToPx(space.toFloat())
    }

    fun setSpacePx(space: Int) {
        mSpace = space
    }

    fun setLineColor(lineColor: Int) {
        mLineColor = lineColor
        mPaint!!.color = mLineColor
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        if (!mIsHasColor) return
        val layoutManager = parent.layoutManager
        val childCount = parent.childCount //返回 可见 的item数量
        if (layoutManager is LinearLayoutManager) { //线性布局
//            ((LinearLayoutManager) layoutManager).getOrientation()
            // 遍历每个Item，分别获取它们的位置信息，然后再绘制对应的分割线
            @RecyclerView.Orientation val orientation = layoutManager.orientation
            for (i in 0 until childCount) {
                // 每个Item的位置
                drawLinear(parent.getChildAt(i), c, orientation)
            }
        } else if (layoutManager is StaggeredGridLayoutManager) { //表格式布局
            // 遍历每个Item，分别获取它们的位置信息，然后再绘制对应的分割线
            for (i in 0 until childCount) {
                // 每个Item的位置
                drawStaggeredGrid(parent.getChildAt(i), c)
            }
        }
    }

    private fun drawLinear(child: View, c: Canvas, @RecyclerView.Orientation orientation: Int) {
        if (orientation == RecyclerView.HORIZONTAL) {
            if (child.left - mSpace <= 0) c.drawRect((child.left - mSpace).toFloat(), 0f, mSpace.toFloat(), child.bottom.toFloat(), mPaint!!)
            c.drawRect(child.right.toFloat(), child.top.toFloat(), (child.right + mSpace).toFloat(), child.bottom.toFloat(), mPaint!!)
        } else if (orientation == RecyclerView.VERTICAL) {
            if (child.top - mSpace <= 0) c.drawRect(0f, (child.top - mSpace).toFloat(), child.right.toFloat(), mSpace.toFloat(), mPaint!!)
            c.drawRect(child.left.toFloat(), child.bottom.toFloat(), child.right.toFloat(), (child.bottom + mSpace).toFloat(), mPaint!!)
        }
    }

    private fun drawStaggeredGrid(child: View, c: Canvas) {
        c.drawRect((child.left - mSpace).toFloat(), (child.top - mSpace).toFloat(), (child.right + mSpace).toFloat(), mSpace.toFloat(), mPaint!!) //上
        c.drawRect(
            (child.left - mSpace).toFloat(), child.bottom.toFloat(), (child.right + mSpace).toFloat(), (child.bottom + mSpace).toFloat(),
            mPaint!!
        ) //下
        c.drawRect(
            (child.left - mSpace).toFloat(), (child.top - mSpace).toFloat(), child.left.toFloat(), (child.bottom + mSpace).toFloat(),
            mPaint!!
        ) //左
        c.drawRect(
            child.right.toFloat(), (child.top - mSpace).toFloat(), (child.right + mSpace).toFloat(), (child.bottom + mSpace).toFloat(),
            mPaint!!
        ) //右
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        val layoutManager = parent.layoutManager
        val position = parent.getChildAdapterPosition(view)
        if (layoutManager is StaggeredGridLayoutManager) {//表格式布局(瀑布流)
            itemOffsetsStaggeredGrid(outRect, position, layoutManager.spanCount)
        } else if (layoutManager is GridLayoutManager) {//表格式布局(高相等，或宽相等，一行一行的)
            itemOffsetsGridLayoutManager(outRect, position, layoutManager.spanCount, layoutManager.orientation)
        } else if (layoutManager is LinearLayoutManager) { //线性布局
            @RecyclerView.Orientation val orientation = layoutManager.orientation
            if (layoutManager.itemCount != 1) { //当RecyclerView里的数据只有一个时，不用分割线
                itemOffsetsLinear(outRect, orientation, position)
            }
        }
    }

    private fun itemOffsetsLinear(outRect: Rect, @RecyclerView.Orientation orientation: Int, position: Int) {
        if (mIsHasBorder) {
            if (position == 0) {
                if (orientation == RecyclerView.HORIZONTAL) {
                    outRect.left = mSpace
                } else if (orientation == RecyclerView.VERTICAL) {
                    outRect.top = mSpace
                }
            }
            if (orientation == RecyclerView.HORIZONTAL) {
                outRect.right = mSpace
            } else if (orientation == RecyclerView.VERTICAL) {
                outRect.bottom = mSpace
            }
        } else {
            if (position == 0) {
                return
            }
            if (orientation == RecyclerView.HORIZONTAL) {
                outRect.left = mSpace
            } else if (orientation == RecyclerView.VERTICAL) {
                outRect.top = mSpace
            }
        }
    }

    private fun itemOffsetsGridLayoutManager(outRect: Rect, position: Int, verticalNum: Int, @RecyclerView.Orientation orientation: Int) {
        if (position / verticalNum <= 0) {
            return
        }
        if (orientation == RecyclerView.HORIZONTAL) {
            outRect.left = mSpace
        } else if (orientation == RecyclerView.VERTICAL) {
            outRect.top = mSpace
        }
    }

    private fun itemOffsetsStaggeredGrid(outRect: Rect, position: Int, verticalNum: Int) {
        //设置列间距
        val itemReallySumSpace: Int = if (mIsHasBorder) {
            //设置行间距
            if (position < verticalNum) {
                outRect.top = mSpace
            }
            outRect.bottom = mSpace
            ((verticalNum + 1) * mSpace * 1.0 / verticalNum).toInt()
        } else {
            //设置行间距
            if (position >= verticalNum) {
                outRect.top = mSpace
            }
            ((verticalNum - 1) * mSpace * 1.0 / verticalNum).toInt()
        }
        if (itemReallySumSpace == 0) return
        var left = mSpace
        var right = itemReallySumSpace - left
        val horizontalPosition = position % verticalNum
        for (i in 0 until horizontalPosition) {
            left = mSpace - right
            right = itemReallySumSpace - left
        }
        outRect.left = left
        outRect.right = right
    }

}
