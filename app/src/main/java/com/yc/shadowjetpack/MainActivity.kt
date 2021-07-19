package com.yc.shadowjetpack

import android.content.Intent
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
                holder.viewBinding.btnTestItem.setOnClickListener {
                    when (position) {
                        0 -> {
                            startActivity(Intent(this@MainActivity, TestShowLoadingActivity::class.java))
                        }
                    }
                }
            }
        }
    }

    override fun ActivityMainBinding.initView() {
        rv.ycInitLinearLayoutManage()
        rv.adapter = mAdapter
        mAdapter.addData("")
        mAdapter.addData("")
        mAdapter.addData("")

    }
}