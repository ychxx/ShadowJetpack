package com.yc.shadowjetpack

import android.annotation.SuppressLint
import androidx.annotation.IntDef
import androidx.annotation.StringDef
import androidx.paging.*
import com.yc.jetpacklib.data.entity.YcDataSourceEntity
import com.yc.jetpacklib.base.YcBaseActivityPlus
import com.yc.jetpacklib.base.YcBaseViewModel
import com.yc.jetpacklib.base.YcRepository
import com.yc.jetpacklib.data.constant.YcNetErrorCode
import com.yc.jetpacklib.exception.YcException
import com.yc.jetpacklib.extension.showToast
import com.yc.jetpacklib.extension.ycFlow
import com.yc.jetpacklib.extension.ycInitLinearLayoutManage
import com.yc.jetpacklib.extension.ycLogESimple
import com.yc.jetpacklib.net.YcResult
import com.yc.jetpacklib.net.doFail
import com.yc.jetpacklib.net.doSuccess
import com.yc.jetpacklib.recycleView.YcRecyclerViewAdapter
import com.yc.jetpacklib.refresh.YcRefreshSpecialUtil
import com.yc.jetpacklib.release.YcSpecialState
import com.yc.jetpacklib.release.YcSpecialViewSmart
import com.yc.shadowjetpack.databinding.TestItemBinding
import com.yc.shadowjetpack.databinding.TestRefreshActivityBinding
import kotlinx.coroutines.delay

/**
 * Creator: yc
 * Date: 2021/7/16 14:27
 * UseDes:
 */
@IntDef(RequestDataType.NORMAL, RequestDataType.EMPTY, RequestDataType.EMPTY_ERROR, RequestDataType.ERROR)
@kotlin.annotation.Retention(AnnotationRetention.SOURCE)
annotation class RequestDataType {
    companion object {
        /**
         * 正常的数据
         */
        const val NORMAL = 1

        /**
         * 空数据
         */
        const val EMPTY = 2

        /**
         * 空数据错误
         */
        const val EMPTY_ERROR = 3

        /**
         * 请求失败
         */
        const val ERROR = 4
    }
}
@SuppressLint("SetTextI18n")
class TestRefreshActivity2 : YcBaseActivityPlus<TestRefreshActivityBinding>(TestRefreshActivityBinding::inflate) {

    data class ItemData(val name: String)
    class Repository : YcRepository() {
        var mId = 0

        fun getData(@RequestDataType type: Int, pageIndex: Int, pageSize: Int) = ycFlow<YcDataSourceEntity<ItemData>> {
            ycLogESimple("开始网络请求")
            delay(3000)
            when (type) {
                RequestDataType.NORMAL -> {
                    val dataList = mutableListOf<ItemData>()
                    for (i in 0 until pageSize) {
                        dataList.add(ItemData("item:${mId++} pageIndex:$pageIndex"))
                    }
                    ycLogESimple("网络请求成功")
                    send(YcResult.Success(YcDataSourceEntity(dataList, 4)))
                }
                RequestDataType.EMPTY -> {
                    ycLogESimple("网络请求空数据成功")
                    send(YcResult.Success(YcDataSourceEntity(null, 0)))
                }
                RequestDataType.EMPTY_ERROR -> {
                    ycLogESimple("网络请求空数据异常")
                    throw YcException("测试网络请求错误2", 404, YcNetErrorCode.DATE_NULL_ERROR)
                }
                RequestDataType.ERROR -> {
                    ycLogESimple("网络请求出错")
                    throw YcException("测试网络请求错误2", 404)
                }
            }
        }
    }

    class VM(val mRepository: Repository = Repository()) : YcBaseViewModel() {
        fun getPageProxy(@RequestDataType type: Int, pageIndex: Int, pageSize: Int) = mRepository.getData(type, pageIndex, pageSize)
    }


    private val mViewModel: VM by ycViewModels()
    private val mAdapter by YcRecyclerViewAdapter.ycLazyInitApply<ItemData, TestItemBinding>(TestItemBinding::inflate) {
        mOnUpdate2 = { position: Int, data: ItemData ->
            btnTestItem.text = "${data.name}- $position"
        }
    }

    @RequestDataType
    private var mRequestDataType: Int = RequestDataType.NORMAL
    private lateinit var mRefreshUtil: YcRefreshSpecialUtil
    override fun TestRefreshActivityBinding.initView() {
        rvTestRefresh.adapter = mAdapter
        rvTestRefresh.ycInitLinearLayoutManage()
        mRefreshUtil = YcRefreshSpecialUtil(this@TestRefreshActivity2, slTestRefresh, mAdapter, YcSpecialViewSmart(rvTestRefresh, flRefresh))
        mRefreshUtil.mRefreshAndLoadMore = { isRefresh: Boolean, pageIndex: Int, pageSize: Int ->
            mViewModel.getPageProxy(mRequestDataType, pageIndex, pageSize).ycCollect {
                it.doSuccess {
                    this@TestRefreshActivity2.mAdapter.addAllData(it.data, isRefresh)
                }.doFail {
                    showToast(it.msg ?: "请求错误")
                }
            }
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
        }
        btnTestSetRequestEmptyError.setOnClickListener {
            mRequestDataType = RequestDataType.EMPTY_ERROR
        }
        btnTestRefreshEmpty.setOnClickListener {
            mAdapter.clearData()
        }
        btnTestRefreshDataSours.setOnClickListener {
            mRefreshUtil.startRefresh()
        }

    }
}