package com.yc.shadowjetpack

import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.recyclerview.widget.DiffUtil
import com.yc.jetpacklib.base.YcBaseActivityPlus
import com.yc.jetpacklib.base.YcBaseViewModel
import com.yc.jetpacklib.data.constant.YcNetErrorCode
import com.yc.jetpacklib.data.entity.YcDataSourceEntity
import com.yc.jetpacklib.exception.YcException
import com.yc.jetpacklib.extension.ycInitLinearLayoutManage
import com.yc.jetpacklib.extension.ycLogESimple
import com.yc.jetpacklib.recycleView.YcPager
import com.yc.jetpacklib.recycleView.YcPagingDataAdapterChange
import com.yc.jetpacklib.refresh.YcRefreshSpecialPagingUtil
import com.yc.jetpacklib.release.YcSpecialState
import com.yc.shadowjetpack.databinding.TestItemBinding
import com.yc.shadowjetpack.databinding.TestRefreshActivity3Binding
import com.yc.shadowjetpack.databinding.TestRefreshActivityBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.parcelize.Parcelize

/**
 * Creator: yc
 * Date: 2021/7/16 14:27
 * UseDes:
 */
class TestRefreshActivity3 : YcBaseActivityPlus<TestRefreshActivity3Binding>(TestRefreshActivity3Binding::inflate) {
    @Parcelize
    data class ItemData(val name: String) : Parcelable {
        companion object {
            val diffCallback = object : DiffUtil.ItemCallback<ItemData>() {
                override fun areItemsTheSame(oldItem: ItemData, newItem: ItemData): Boolean {
                    return oldItem == newItem
                }

                override fun areContentsTheSame(oldItem: ItemData, newItem: ItemData): Boolean {
                    return oldItem == newItem
                }
            }
        }
    }

    class VM : YcBaseViewModel() {
        var mId = 0
        private val _mGetData = MutableLiveData<PagingData<ItemData>>()
        val mGetData: LiveData<PagingData<ItemData>> get() = _mGetData
        fun getDataPagingData() = ycLaunch {
            mId++
            YcPager.getPagerFlow(15, 15) { _, _ ->
                ycLogESimple("开始网络请求", "TAG")
                delay(2000)
                val dataList = mutableListOf<ItemData>()
                for (i in 0 until 15) {
                    dataList.add(ItemData("item:${mId}-$i"))
                }
                ycLogESimple("网络请求成功", "TAG")
                YcDataSourceEntity(dataList, 4)
            }.cachedIn(viewModelScope).collect {
                _mGetData.postValue(it)
            }
        }
    }

    private val mViewModel: VM by ycViewModels()
    private val mAdapter by YcPagingDataAdapterChange.ycLazyInitApply(TestItemBinding::inflate, ItemData.diffCallback) {
        mItemClick = {

        }
        mItemClick2 = { data: ItemData, position: Int ->

        }
        mOnUpdate = {

        }
        mOnUpdate2 = { position: Int, data: ItemData ->
            btnTestItem.text = "${data.name}- $position"
        }
    }
    private lateinit var mRefreshUtil: YcRefreshSpecialPagingUtil<ItemData>
    private var testId = 0

    override fun TestRefreshActivity3Binding.initView() {
        rvTestRefresh.adapter = mAdapter
        rvTestRefresh.ycInitLinearLayoutManage()
        mRefreshUtil = YcRefreshSpecialPagingUtil<ItemData>(this@TestRefreshActivity3).build(mAdapter, slTestRefresh, rvTestRefresh, flRefresh) {
            mRefreshCall = {
                ycLogESimple("333-获取", "TAG$testId")
                mViewModel.getDataPagingData()
            }
        }
        btnReplace.setOnClickListener {
            ycLogESimple("11", "TAG$testId")
            mRefreshUtil.startRefresh(true)
        }
        mViewModel.mGetData.observe {
            ycLogESimple("444-替换", "TAG$testId")
            mRefreshUtil.acSetPagingData(it)
        }
    }

}