package com.yc.shadowjetpack

import android.content.Intent
import com.yc.jetpacklib.base.YcBaseActivityPlus
import com.yc.jetpacklib.extension.ycCreateResultLauncher
import com.yc.jetpacklib.extension.ycInitLinearLayoutManage
import com.yc.jetpacklib.recycleView.YcRecyclerViewAdapter
import com.yc.jetpacklib.ui.YcScanQrcodeActivity
import com.yc.shadowjetpack.chart.TestChartLineActivity
import com.yc.shadowjetpack.databinding.ActivityMainBinding
import com.yc.shadowjetpack.databinding.TestItemBinding
import com.yc.shadowjetpack.socket.TestSocketActivity

data class Item(val content: String, val code: Int)
class MainActivity : YcBaseActivityPlus<ActivityMainBinding>(ActivityMainBinding::inflate) {
    private val mAdapter2 by YcRecyclerViewAdapter.ycLazyInitApply<Item, TestItemBinding>(TestItemBinding::inflate) {
        mOnUpdate = {

        }
        mItemClick = {

        }
    }
    private val mAdapter by YcRecyclerViewAdapter.ycLazyInit(TestItemBinding::inflate) { data: Item ->
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
                6 -> {
                    startActivity(Intent(this@MainActivity, TestPickerViewActivity::class.java))
                }
                7 -> {
                    startActivity(Intent(this@MainActivity, TestSocketActivity::class.java))
                }
                8 -> {
                    YcScanQrcodeActivity.newInstance(this@MainActivity, mLauncher, R.color.jetpack_black_scan_bg)
                }
                9 -> {
                    startActivity(Intent(this@MainActivity, TestRefreshActivity2::class.java))
                }
                10 -> {
                    startActivity(Intent(this@MainActivity, TestRemovePagingDataActivity::class.java))
                }
                11 -> {
                    startActivity(Intent(this@MainActivity, TestRefreshActivity3::class.java))
                }
                12 -> {
                    startActivity(Intent(this@MainActivity, TestGlideActivity::class.java))
                }
            }
        }
    }
    val mLauncher = ycCreateResultLauncher {

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
        mAdapter.addData(Item("???????????????", 0))
        mAdapter.addData(Item("??????????????????", 1))
        mAdapter.addData(Item("??????????????????2", 2))
        mAdapter.addData(Item("????????????", 3))
        mAdapter.addData(Item("???????????????View", 4))
        mAdapter.addData(Item("???????????????", 5))
        mAdapter.addData(Item("??????Pickerview?????????", 6))
        mAdapter.addData(Item("??????Socket", 7))
        mAdapter.addData(Item("????????????", 8))
        mAdapter.addData(Item("????????????2", 9))
        mAdapter.addData(Item("????????????pagingData", 10))
        mAdapter.addData(Item("????????????pagingData", 11))
        mAdapter.addData(Item("??????Glide", 12))
    }
}