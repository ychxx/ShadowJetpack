package com.yc.shadowjetpack

import android.content.Intent
import android.os.Bundle
import androidx.compose.ui.platform.setContent
import com.yc.jetpacklib.base.YcBaseActivityPlus
import com.yc.jetpacklib.extension.ycInitLinearLayoutManage
import com.yc.jetpacklib.recycleView.YcRecyclerViewAdapter
import com.yc.jetpacklib.recycleView.YcViewHolder
import com.yc.shadowjetpack.databinding.ActivityMainBinding
import com.yc.shadowjetpack.databinding.TestItemBinding

data class Item(val content: String, val code: Int)
class MainActivity : YcBaseActivityPlus<ActivityMainBinding>(ActivityMainBinding::inflate) {
    val mAdapter: YcRecyclerViewAdapter<Item, TestItemBinding> by lazy {
        object : YcRecyclerViewAdapter<Item, TestItemBinding>(TestItemBinding::inflate) {
            override fun onUpdate(holder: YcViewHolder<TestItemBinding>, position: Int, data: Item) {
                holder.viewBinding.btnTestItem.text = data.content
                holder.viewBinding.btnTestItem.setOnClickListener {
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
                    }
                }
            }
        }
    }

    override fun ActivityMainBinding.initView() {
        rv.ycInitLinearLayoutManage()
        rv.adapter = mAdapter
        mAdapter.addData(Item("测试加载框", 0))
        mAdapter.addData(Item("测试替换布局", 1))
        mAdapter.addData(Item("测试替换布局2", 2))
        mAdapter.addData(Item("测试刷新", 3))
    }
}