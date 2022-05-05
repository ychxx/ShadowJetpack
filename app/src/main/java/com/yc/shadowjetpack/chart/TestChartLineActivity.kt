package com.yc.shadowjetpack.chart

import android.graphics.Color
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.yc.jetpacklib.base.YcBaseActivityPlus
import com.yc.jetpacklib.extension.*
import com.yc.jetpacklib.net.doFail
import com.yc.jetpacklib.net.doSuccess
import com.yc.jetpacklib.widget.chart.YcChartAxisYUnitRenderer
import com.yc.jetpacklib.widget.chart.YcChartFiniteLength
import com.yc.jetpacklib.widget.chart.YcChartMarkerView
import com.yc.jetpacklib.widget.chart.YcMarkerEntity
import com.yc.shadowjetpack.R
import com.yc.shadowjetpack.databinding.TestLineChartBinding

/**
 * Creator: yc
 * Date: 2021/7/16 14:27
 * UseDes:测试折线图
 */
class TestChartLineActivity : YcBaseActivityPlus<TestLineChartBinding>(TestLineChartBinding::inflate) {
    private val mViewModel: TestChartLineVB by ycViewModels()
    override fun TestLineChartBinding.initView() {
        mViewModel.getDataLine().observe {
            it.doSuccess {
                lcTest.refreshChartInfo(it)
                bcTest.refreshChartInfo(it)
                pcTest.refreshChartInfo(it)
            }.doFail {
                showToast(it.msg ?: "报错啦！！")
            }
        }
        lcTest.ycChartInitDefault()
        lcTest.rendererLeftYAxis = YcChartAxisYUnitRenderer(lcTest, "(m)")
        bcTest.ycChartBarInitDefault()
        pcTest.ycChartInitDefault()
        pcTest.renderer
    }

    private fun PieChart.refreshChartInfo(pieData: ChartEntity?) {
        if (pieData == null) {
            clear()
        } else {
            ycChartSetLineDataSet(pieData.pieEntryList)
        }
        ycChartRefresh()//刷新图表
    }

    private fun LineChart.refreshChartInfo(data: ChartEntity?) {
        if (data == null) {
            clear()
        } else {
            ycChartRefreshAxisYLabelCount(data.lineYMax)
            (marker as YcChartMarkerView).mUpdateData = {
                addData(YcMarkerEntity(R.drawable.shape_round_test_chart_mask_blue, "标示y:${data.lineEntryList[it].y}", R.color.blue), false)
                addData(YcMarkerEntity(R.drawable.shape_round_test_chart_mask_yellow, "标示x:${data.lineXList[it]}", R.color.yellow), false)
                addData(YcMarkerEntity(R.drawable.shape_round_test_chart_mask_red, "阈值:${data.lineLimitLineY}", R.color.jetpack_chart_limit_line_color))
            }
            (xAxis.valueFormatter as YcChartFiniteLength).mValues = data.lineXList
            ycChartSetLineDataSet(data.lineEntryList)
            ycChartSetLimitLine(data.lineLimitLineY)
        }
        ycChartRefresh()//刷新图表
    }

    private fun BarChart.refreshChartInfo(data: ChartEntity?) {
        if (data == null) {
            clear()
        } else {
            ycChartRefreshAxisYLabelCount(data.barYMax)
            (marker as YcChartMarkerView).mUpdateData = {
                addData(YcMarkerEntity(R.drawable.shape_round_test_chart_mask_blue, "标示y1:${data.barEntryList[it].y}", R.color.blue), false)
                addData(YcMarkerEntity(R.drawable.shape_round_test_chart_mask_blue, "标示y2:${data.barEntryList2[it].y}", R.color.red), false)
                addData(YcMarkerEntity(R.drawable.shape_round_test_chart_mask_red, "标示x:${data.barXList[it]}", R.color.yellow), false)
            }
            ycChartRefreshAxisXLabelCount(data.barXList.size, 7)
            (xAxis.valueFormatter as YcChartFiniteLength).mValues = data.barXList
            ycChartSetBarDataSet(data.barEntryList, ycGetColorRes(R.color.blue), 0, true)
            ycChartSetBarDataSet(data.barEntryList2, ycGetColorRes(R.color.red), 1)
        }
        ycChartRefresh()//刷新图表
    }
}