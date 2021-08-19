package com.yc.shadowjetpack.chart

import androidx.lifecycle.liveData
import com.yc.jetpacklib.base.YcBaseViewModel
import kotlinx.coroutines.flow.collectLatest

/**
 * Creator: yc
 * Date: 2021/8/17 15:36
 * UseDes:
 */
class TestChartLineVB : YcBaseViewModel() {
    private val mTestChartLineRepository: TestChartLineRepository by lazy { TestChartLineRepository() }
    fun getDataLine() = liveData {
        mTestChartLineRepository.getData().ycHasLoading().collectLatest {
            emit(it)
        }
    }
    fun getDataBar() = liveData {
        mTestChartLineRepository.getData().ycHasLoading().collectLatest {
            emit(it)
        }
    }
}