package com.yc.shadowjetpack

import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.recyclerview.widget.DiffUtil
import com.yc.jetpacklib.data.entity.YcDataSourceEntity
import com.yc.jetpacklib.base.YcBaseActivityPlus
import com.yc.jetpacklib.base.YcBaseViewModel
import com.yc.jetpacklib.data.constant.YcNetErrorCode
import com.yc.jetpacklib.exception.YcException
import com.yc.jetpacklib.extension.ycInitLinearLayoutManage
import com.yc.jetpacklib.extension.ycLogESimple
import com.yc.jetpacklib.recycleView.YcPager
import com.yc.jetpacklib.recycleView.YcPagingDataAdapterChange
import com.yc.jetpacklib.refresh.YcRefreshSpecialPagingUtil
import com.yc.jetpacklib.release.YcSpecialState
import com.yc.shadowjetpack.databinding.TestItemBinding
import com.yc.shadowjetpack.databinding.TestRefreshActivityBinding
import com.yc.shadowjetpack.databinding.TestRemovePagingDataActivityBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.parcelize.Parcelize

/**
 * Creator: yc
 * Date: 2021/7/16 14:27
 * UseDes:
 */
@SuppressLint("SetTextI18n")
class TestRemovePagingDataActivity : YcBaseActivityPlus<TestRemovePagingDataActivityBinding>(TestRemovePagingDataActivityBinding::inflate) {
    @Parcelize
    data class ItemData2(val name: String, val name2: String) : Parcelable {
        companion object {
            val diffCallback = object : DiffUtil.ItemCallback<ItemData2>() {
                override fun areItemsTheSame(oldItem: ItemData2, newItem: ItemData2): Boolean {
                    return oldItem == newItem
                }

                override fun areContentsTheSame(oldItem: ItemData2, newItem: ItemData2): Boolean {
                    return oldItem == newItem
                }
            }
        }
    }

    class VM : YcBaseViewModel() {
        var mId = 0
        private val _mGetData = MutableLiveData<PagingData<ItemData2>>()
        val mGetData: LiveData<PagingData<ItemData2>> get() = _mGetData
        fun getDataPagingData(@RequestDataType type: Int) = ycLaunch {
            YcPager.getPagerFlow(15, 15) { _, _ ->
                ycLogESimple("开始网络请求")
                val dataList = mutableListOf<ItemData2>()
                for (i in 0 until 15) {
                    dataList.add(ItemData2("item:${mId++}", "12"))
                }
                YcDataSourceEntity(dataList, 1)
            }.cachedIn(viewModelScope).collect {
                _mGetData.postValue(it)
            }
        }
    }

    private val mViewModel: VM by ycViewModels()
    private val mAdapter: YcPagingDataAdapterChange<ItemData2, TestItemBinding> by YcPagingDataAdapterChange.ycLazyInitApply(
        TestItemBinding::inflate, ItemData2.diffCallback
    ) {
        mItemClick = {

        }
        mItemClick2 = { data: ItemData2, position: Int ->

        }
        mOnUpdate = {

        }
        mOnUpdate3 = { holder, data: ItemData2 ->
            btnTestItem.text = "${data.name}- ${holder.bindingAdapter}"
            btnTestItem.setOnClickListener {
                removeItem(this@TestRemovePagingDataActivity.lifecycle, data)
            }
        }
    }

    private lateinit var mRefreshUtil: YcRefreshSpecialPagingUtil<ItemData2>

    @RequestDataType
    private var mRequestDataType: Int = RequestDataType.NORMAL
    override fun TestRemovePagingDataActivityBinding.initView() {
        rvTestRefresh.adapter = mAdapter
        rvTestRefresh.ycInitLinearLayoutManage()
        mRefreshUtil = YcRefreshSpecialPagingUtil<ItemData2>(this@TestRemovePagingDataActivity).build(mAdapter, slTestRefresh, rvTestRefresh, flRefresh) {
            mRefreshCall = {
                mViewModel.getDataPagingData(mRequestDataType)
            }
        }
        btnTestDelete.setOnClickListener {
            mRefreshUtil.mSpecialViewSimple.show(YcSpecialState.DATA_EMPTY_ERROR, YcException("测试异常", 400))
        }

        mViewModel.mGetData.observe {
            mRefreshUtil.acSetPagingData(it)
        }
    }

}