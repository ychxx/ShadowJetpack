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
import com.yc.jetpacklib.net.YcResult
import com.yc.jetpacklib.recycleView.YcPager
import com.yc.jetpacklib.recycleView.YcPagingDataAdapterChange
import com.yc.jetpacklib.refresh.YcRefreshSpecialPagingUtil
import com.yc.jetpacklib.release.YcSpecialState
import com.yc.shadowjetpack.databinding.TestItemBinding
import com.yc.shadowjetpack.databinding.TestRefreshActivityBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.parcelize.Parcelize

/**
 * Creator: yc
 * Date: 2021/7/16 14:27
 * UseDes:
 */
@SuppressLint("SetTextI18n")
class TestRefreshActivity : YcBaseActivityPlus<TestRefreshActivityBinding>(TestRefreshActivityBinding::inflate) {
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
        var mIsError = false
        private val _mGetData = MutableLiveData<PagingData<ItemData>>()
        val mGetData: LiveData<PagingData<ItemData>> get() = _mGetData
        fun getDataPagingData(@RequestDataType type: Int) = ycLaunch {
            YcPager.getPagerFlow(15, 15) { _, _ ->
                ycLogESimple("开始网络请求")
                delay(2000)
                when (type) {
                    RequestDataType.NORMAL -> {
                        val dataList = mutableListOf<ItemData>()
                        for (i in 0 until 15) {
                            dataList.add(ItemData("item:${mId++}"))
                        }
                        ycLogESimple("网络请求成功")
                        YcDataSourceEntity(dataList, 4)
                    }
                    RequestDataType.EMPTY -> {
                        ycLogESimple("网络请求空数据成功")
                        YcDataSourceEntity(null, 0)
                    }
                    RequestDataType.EMPTY_ERROR -> {
                        ycLogESimple("网络请求空数据异常")
                        throw YcException("测试网络请求错误1", 404, YcNetErrorCode.DATE_NULL_ERROR)
                    }
                    RequestDataType.ERROR -> {
                        ycLogESimple("网络请求出错")
                        throw YcException("测试网络请求错误2", 404)
                    }
                    else -> {
                        ycLogESimple("请求状态未设置")
                        YcDataSourceEntity(null, 0)
                    }
                }
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

    //    private val mAdapter2 by YcPagingDataAdapterChange.ycLazyInit(TestItemBinding::inflate, ItemData.diffCallback) {
//        btnTestItem.text = "${data.name}- $position"
//    }
    //    private val mAdapter2 = object : YcPagingDataAdapterChange<ItemData, TestItemBinding>(TestItemBinding::inflate, ItemData.diffCallback) {
//        override fun TestItemBinding.onUpdate(position: Int, data: ItemData) {
//
//        }
//    }
    private lateinit var mRefreshUtil: YcRefreshSpecialPagingUtil<ItemData>

    @RequestDataType
    private var mRequestDataType: Int = RequestDataType.NORMAL
    override fun TestRefreshActivityBinding.initView() {
        rvTestRefresh.adapter = mAdapter
        rvTestRefresh.ycInitLinearLayoutManage()
        mRefreshUtil = YcRefreshSpecialPagingUtil<ItemData>(this@TestRefreshActivity).build(mAdapter, slTestRefresh, rvTestRefresh, flRefresh) {
            mRefreshCall = {
                mViewModel.getDataPagingData(mRequestDataType)
            }
            //无需要对刷新失败/成功做处理，会显示toast
//            mRefreshResult = {
//                it.doSuccess {
//                    ycLogESimple("Refresh$it")
//                }.doFail { error ->
//                    ycLogESimple("Refresh$error")
//                }
//            }
            //用于判断数据源是否需要改变，如果没有改变则不需设置（例如搜索列表的关键字改变）
//            mDataSourceChange = {
//                false
//            }
            //无需要对加载更多失败/成功做处理，会显示toast
//            mLoadMoreResult = {
//                it.doFail {
//                    ycLogESimple("LoadMore$it")
//                }
//            }
        }
        btnTestRefreshSpecialShow.setOnClickListener {
            mRefreshUtil.mSpecialViewSimple.show(YcSpecialState.DATA_EMPTY_ERROR, YcException("测试异常", 400))
        }
        btnTestRefreshSpecialHide.setOnClickListener {
            mRefreshUtil.mSpecialViewSimple.recovery()
        }
        btnTestRequestFail.setOnClickListener {
            mRequestDataType = RequestDataType.ERROR
        }
        btnTestRequestSuccess.setOnClickListener {
            mRequestDataType = RequestDataType.NORMAL
        }
        btnTestSetRequestEmpty.setOnClickListener {
            mRequestDataType = RequestDataType.EMPTY
            mRefreshUtil.setDataSourceRefresh()
        }
        btnTestSetRequestEmptyError.setOnClickListener {
            mRequestDataType = RequestDataType.EMPTY_ERROR
            mRefreshUtil.setDataSourceRefresh()
        }

        btnTestRefreshEmpty.setOnClickListener {
            mRefreshUtil.acClearPagingData()
        }
        btnTestRefreshDataSours.setOnClickListener {

            mRefreshUtil.startRefresh()
        }
        mViewModel.mGetData.observe {
            mRefreshUtil.acSetPagingData(it)
        }
    }

}