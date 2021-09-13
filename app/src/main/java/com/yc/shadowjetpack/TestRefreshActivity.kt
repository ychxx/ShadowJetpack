package com.yc.shadowjetpack

import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import androidx.recyclerview.widget.DiffUtil
import com.yc.jetpacklib.data.entity.YcDataSourceEntity
import com.yc.jetpacklib.base.YcBaseActivityPlus
import com.yc.jetpacklib.base.YcBaseViewModel
import com.yc.jetpacklib.exception.YcException
import com.yc.jetpacklib.extension.ycLogESimple
import com.yc.jetpacklib.recycleView.YcPager
import com.yc.jetpacklib.recycleView.YcPagingDataAdapterPlus
import com.yc.jetpacklib.refresh.YcRefreshBaseUtil
import com.yc.jetpacklib.refresh.YcRefreshSpecialViewUtil
import com.yc.jetpacklib.release.YcSpecialViewCommon
import com.yc.jetpacklib.release.YcSpecialViewSmart
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
        fun getDataPagingData() = ycLaunch {
            YcPager.getPagerFlow(15, 15) { _, _ ->
                ycLogESimple("开始网络请求")
                delay(2000)
                if (mIsError) {
                    ycLogESimple("网络请求出错")
                    throw YcException("测试错误", 404)
                }
                val dataList = mutableListOf<ItemData>()
                for (i in 0 until 15) {
                    dataList.add(ItemData("item:${mId++}"))
                }
                ycLogESimple("网络请求成功")
                YcDataSourceEntity(dataList, 4)
            }.collect {
                _mGetData.postValue(it)
            }

        }
    }

    private val mViewModel: VM by ycViewModels()
    private val mAdapter = object : YcPagingDataAdapterPlus<ItemData, TestItemBinding>(TestItemBinding::inflate, ItemData.diffCallback) {
        override fun TestItemBinding.onUpdate(position: Int, data: ItemData) {
            btnTestItem.text = data.name
        }
    }
    private lateinit var mSpecialView: YcSpecialViewSmart
    private lateinit var mRefreshUtil: YcRefreshBaseUtil
    override fun TestRefreshActivityBinding.initView() {
        mSpecialView = YcSpecialViewSmart(rvTestRefresh, flRefresh)
        mRefreshUtil = YcRefreshSpecialViewUtil(this@TestRefreshActivity).build(mAdapter, slTestRefresh, rvTestRefresh, mSpecialView) {
            mRefreshCall = {
                mViewModel.getDataPagingData()
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
            mSpecialView.show(YcException("测试异常",400))
        }
        btnTestRefreshSpecialHide.setOnClickListener {
            mSpecialView.recovery()
        }
        btnTestRefreshFail.setOnClickListener {
            mViewModel.mIsError = true
        }
        btnTestRefreshSuccess.setOnClickListener {
            mViewModel.mIsError = false
        }
        btnTestRefreshEmpty.setOnClickListener {
            mRefreshUtil.mDataSourceChange = {
                false
            }
            mAdapter.ycSubmitData(PagingData.empty())
        }
        btnTestRefreshDataSours.setOnClickListener {
            mRefreshUtil.mDataSourceChange = {
                true
            }
            mRefreshUtil.startRefresh()
        }
        mViewModel.mGetData.observe {
            mAdapter.ycSubmitData(it)
        }
    }
}