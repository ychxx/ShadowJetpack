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
import com.yc.shadowjetpack.web.TestWebViewActivity

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
                13 -> {
                    startActivity(Intent(this@MainActivity, TestSoundActivity::class.java))
                }
                14 -> {
                    startActivity(Intent(this@MainActivity, TestWebViewActivity::class.java))
                }
                15 -> {
                    startActivity(Intent(this@MainActivity, TestNetActivity::class.java))
                }
                16 -> {
                    startActivity(Intent(this@MainActivity, TestSendMssActivity::class.java))
                }
                17 -> {
                    startActivity(Intent(this@MainActivity, TestAliPayLivingBodyTestActivity::class.java))
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
        mAdapter.addData(Item("测试阿里活体检测", 17))
        mAdapter.addData(Item("测试webView", 14))
        mAdapter.addData(Item("测试请求", 15))
        mAdapter.addData(Item("测试加载框", 0))
        mAdapter.addData(Item("测试替换布局", 1))
        mAdapter.addData(Item("测试替换布局2", 2))
        mAdapter.addData(Item("测试刷新", 3))
        mAdapter.addData(Item("测试自定义View", 4))
        mAdapter.addData(Item("测试折线图", 5))
        mAdapter.addData(Item("测试Pickerview选择器", 6))
        mAdapter.addData(Item("测试Socket", 7))
        mAdapter.addData(Item("测试扫描", 8))
        mAdapter.addData(Item("测试刷新2", 9))
        mAdapter.addData(Item("动态删除pagingData", 10))
        mAdapter.addData(Item("快速替换pagingData", 11))
        mAdapter.addData(Item("测试Glide", 12))
        mAdapter.addData(Item("测试录音", 13))
        mAdapter.addData(Item("测试录音", 16))

    }
}