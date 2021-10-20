package com.yc.shadowjetpack

import android.annotation.SuppressLint
import androidx.paging.*
import com.yc.jetpacklib.data.entity.YcDataSourceEntity
import com.yc.jetpacklib.base.YcBaseActivityPlus
import com.yc.jetpacklib.base.YcBaseViewModel
import com.yc.jetpacklib.base.YcRepository
import com.yc.jetpacklib.exception.YcException
import com.yc.jetpacklib.extension.showToast
import com.yc.jetpacklib.extension.ycInitLinearLayoutManage
import com.yc.jetpacklib.extension.ycLogESimple
import com.yc.jetpacklib.net.YcResult
import com.yc.jetpacklib.net.doFail
import com.yc.jetpacklib.net.doSuccess
import com.yc.jetpacklib.recycleView.YcRecyclerViewAdapter
import com.yc.jetpacklib.recycleView.YcRefreshResult
import com.yc.jetpacklib.refresh.YcRefreshSpecialRecycleViewUtil
import com.yc.jetpacklib.release.YcSpecialViewSmart
import com.yc.shadowjetpack.databinding.TestItemBinding
import com.yc.shadowjetpack.databinding.TestRefreshActivityBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect

/**
 * Creator: yc
 * Date: 2021/7/16 14:27
 * UseDes:
 */
@SuppressLint("SetTextI18n")
class TestRefreshActivity2 : YcBaseActivityPlus<TestRefreshActivityBinding>(TestRefreshActivityBinding::inflate) {
    data class ItemData(val name: String)
    class Repository : YcRepository() {
        var mId = 0
        var mIsError = false
        fun getData(pageIndex: Int, pageSize: Int) = ycFlow<YcDataSourceEntity<ItemData>> {
            ycLogESimple("开始网络请求")
            delay(6000)
            if (mIsError) {
                ycLogESimple("网络请求出错")
                throw YcException("测试错误", 404)
            }
            val dataList = mutableListOf<ItemData>()
            for (i in 0 until pageSize) {
                dataList.add(ItemData("item:${mId++} pageIndex:$pageIndex"))
            }
            ycLogESimple("网络请求成功")
            send(YcResult.Success(YcDataSourceEntity(dataList, 4)))
        }
    }

    class VM(val mRepository: Repository = Repository()) : YcBaseViewModel() {
        var mIsError = false
            set(value) {
                field = value
                mRepository.mIsError = field
            }

        fun getPageProxy(pageIndex: Int, pageSize: Int) = mRepository.getData(pageIndex, pageSize)
    }


    private val mViewModel: VM by ycViewModels()
    private val mAdapter by YcRecyclerViewAdapter.ycLazyInitApply<ItemData, TestItemBinding>(TestItemBinding::inflate) {
        mOnUpdate2 = { position: Int, data: ItemData ->
            btnTestItem.text = "${data.name}- $position"
        }
    }

    private lateinit var mRefreshUtil: YcRefreshSpecialRecycleViewUtil
    override fun TestRefreshActivityBinding.initView() {
        rvTestRefresh.adapter = mAdapter
        rvTestRefresh.ycInitLinearLayoutManage()
        mRefreshUtil = YcRefreshSpecialRecycleViewUtil(this@TestRefreshActivity2, slTestRefresh, mAdapter, YcSpecialViewSmart(rvTestRefresh, flRefresh))
        mRefreshUtil.mRefreshAndLoadMore = { isRefresh: Boolean, pageIndex: Int, pageSize: Int ->
            mViewModel.getPageProxy(pageIndex, pageSize).ycCollect {
                it.doSuccess {
                    mAdapter.addAllData(it.data!!, isRefresh)
                }.doFail {
                    showToast(it.msg ?: "请求错误")
                }
            }
        }
        btnTestRefreshSpecialShow.setOnClickListener {
            mRefreshUtil.mSpecialViewSimple.show(YcException("测试异常", 400))
        }
        btnTestRefreshSpecialHide.setOnClickListener {
            mRefreshUtil.mSpecialViewSimple.recovery()
        }
        btnTestRefreshFail.setOnClickListener {
            mViewModel.mIsError = true
        }
        btnTestRefreshSuccess.setOnClickListener {
            mViewModel.mIsError = false
        }
        btnTestRefreshEmpty.setOnClickListener {
            mAdapter.clearData()
        }
        btnTestRefreshDataSours.setOnClickListener {
            mRefreshUtil.startRefresh()
        }
    }

}