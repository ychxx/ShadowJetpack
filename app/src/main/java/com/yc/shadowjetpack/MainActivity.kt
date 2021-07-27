package com.yc.shadowjetpack

import android.content.Intent
import android.os.Bundle
import com.yc.jetpacklib.base.YcBaseActivityPlus
import com.yc.jetpacklib.extension.ycInitLinearLayoutManage
import com.yc.jetpacklib.recycleView.YcRecyclerViewAdapter
import com.yc.jetpacklib.recycleView.YcViewHolder
import com.yc.shadowjetpack.databinding.ActivityMainBinding
import com.yc.shadowjetpack.databinding.TestItemBinding

class MainActivity : YcBaseActivityPlus<ActivityMainBinding>(ActivityMainBinding::inflate) {

    val mAdapter: YcRecyclerViewAdapter<String, TestItemBinding> by lazy {
        object : YcRecyclerViewAdapter<String, TestItemBinding>(TestItemBinding::inflate) {
            override fun onUpdate(holder: YcViewHolder<TestItemBinding>, position: Int, data: String) {
                holder.viewBinding.btnTestItem.text = data
                holder.viewBinding.btnTestItem.setOnClickListener {
                    when (position) {
                        0 -> {
                            startActivity(Intent(this@MainActivity, TestShowLoadingActivity::class.java))
                        }
                        1 -> {
                            startActivity(Intent(this@MainActivity, TestSpecialReleaseActivity::class.java))
                        }
                    }
                }
            }
        }
    }

    override fun ActivityMainBinding.initView() {
        rv.ycInitLinearLayoutManage()
        rv.adapter = mAdapter
        mAdapter.addData("测试加载框")
        mAdapter.addData("测试替换布局")
        mAdapter.addData("")

    }
}