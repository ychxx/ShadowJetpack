package com.yc.shadowjetpack

import android.content.Intent
import com.yc.jetpacklib.base.YcBaseActivityPlus
import com.yc.jetpacklib.extension.ycInitLinearLayoutManage
import com.yc.jetpacklib.recycleView.YcRecyclerViewAdapterPlus
import com.yc.shadowjetpack.chart.TestChartLineActivity
import com.yc.shadowjetpack.databinding.ActivityMainBinding
import com.yc.shadowjetpack.databinding.TestItemBinding

data class Item(val content: String, val code: Int)
class MainActivity : YcBaseActivityPlus<ActivityMainBinding>(ActivityMainBinding::inflate) {
    private val mAdapter by YcRecyclerViewAdapterPlus.ycLazyInit(TestItemBinding::inflate) { data: Item ->
        btnTestItem.text = data.content
        btnTestItem.setOnClickListener {
            when (data.code) {
                0 -> {
                    startActivity(Intent(this@MainActivity, TestShowLoadingActivity::class.java))
                }
                1 -> {
                    startActivity(Intent(this@MainActivity, TestSpecialReleaseActivity::class.java))
                }
                2 -> {
                    startActivity(Intent(this@MainActivity, TestSpecialReleaseActivity2::class.java))
                }
                3 -> {
                    startActivity(Intent(this@MainActivity, TestRefreshActivity::class.java))
                }
                4 -> {
                    startActivity(Intent(this@MainActivity, TestWidgetActivity::class.java))
                }
                5 -> {
                    startActivity(Intent(this@MainActivity, TestChartLineActivity::class.java))
                }
            }
        }
    }
//    val mAdapter: YcRecyclerViewAdapter<Item, TestItemBinding> by lazy {
//        object : YcRecyclerViewAdapter<Item, TestItemBinding>(TestItemBinding::inflate) {
//            override fun onUpdate(holder: YcViewHolder<TestItemBinding>, position: Int, data: Item) {
//
//            }
//        }
//    }

    override fun ActivityMainBinding.initView() {
        rv.ycInitLinearLayoutManage()
        rv.adapter = mAdapter
        mAdapter.addData(Item("测试加载框", 0))
        mAdapter.addData(Item("测试替换布局", 1))
        mAdapter.addData(Item("测试替换布局2", 2))
        mAdapter.addData(Item("测试刷新", 3))
        mAdapter.addData(Item("测试自定义View", 4))
        mAdapter.addData(Item("测试折线图", 5))
    }
}