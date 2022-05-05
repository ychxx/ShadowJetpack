package com.yc.shadowjetpack.chart

import com.yc.jetpacklib.base.YcRepository
import com.yc.jetpacklib.net.YcResult
import kotlinx.coroutines.flow.Flow
import com.yc.jetpacklib.extension.ycFlow
/**
 * Creator: yc
 * Date: 2021/8/17 15:36
 * UseDes:
 */
class TestChartLineRepository : YcRepository() {
    private val mChartJsonToEntity by lazy { ChartJsonToEntity() }

    fun getData(): Flow<YcResult<ChartEntity?>> = ycFlow {
        val xList = mutableListOf<String>()
        val yList = mutableListOf<Float>()

        for (i in 0 until 5) {
            if ((i + 1) / 10 <= 0) {
                xList.add("08.0${i + 1}")
            } else {
                xList.add("08.${i + 1}")
            }
            yList.add(((i + 2) * 32f) % 50)
        }
        val json = ChartJson(xList, yList)
        send(YcResult.Success(mChartJsonToEntity.map(json)))
    }
}