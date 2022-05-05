package com.yc.jetpacklib.extension

import android.content.Context
import android.graphics.Color
import android.graphics.DashPathEffect
import android.util.Log
import com.github.mikephil.charting.charts.*
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.yc.jetpacklib.R
import com.yc.jetpacklib.utils.YcResources
import com.yc.jetpacklib.utils.YcUI
import com.yc.jetpacklib.widget.chart.*

/**
 * 图表相关扩展方法
 */
/**
 * 折线图的默认初始化
 * @receiver BarChart
 */
fun LineChart.ycChartInitDefault() {
    ycChartBaseInit()
    xAxis.labelCount = 7
    xAxis.valueFormatter = YcChartFiniteLength()
    setXAxisRenderer(YcChartAxisXExtraLastRenderer(this))
    ycChartSetScaleEnabled(false)
    renderer = YcChartLineRenderer(this)
    marker = YcChartMarkerView(this)//单击图表后，显示的提示框
    ycChartAxisYLeftValueInt()//左侧y轴上显示数据为正整数
}

/**
 * 柱状图的默认初始化
 * @receiver BarChart
 */
fun BarChart.ycChartBarInitDefault() {
    ycChartBaseInit()
    xAxis.valueFormatter = YcChartFiniteLength()//x轴限制最长显示文字
    ycChartSetScaleEnabled(false)
    renderer = YcChartBarRenderer(this)
    marker = YcChartMarkerView(this)//单击图表后，显示的提示框
    ycChartAxisYLeftValueInt()//左侧y轴上显示数据为正整数

}

/**
 * 默认配置初始化
 * @receiver BarLineChartBase<*>
 */
fun BarLineChartBase<*>.ycChartBaseInit() {
    this.axisLeft.ycChartAxisYLeftInit(context)
    this.xAxis.ycChartAxisXInit(context)
    setNoDataText(context.getString(R.string.jetpack_chart_data_empty))
    setNoDataTextColor(ycGetColorRes(R.color.jetpack_chart_x_y_text_color))
    setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
        override fun onValueSelected(entry: Entry, highlight: Highlight) {
            centerViewToAnimated(entry.x, entry.y, data.getDataSetByIndex(highlight.dataSetIndex).axisDependency, 500)
        }

        override fun onNothingSelected() {
            Log.i("Nothing selected", "Nothing selected.")
        }
    })
    // 不使用描述文本相关信息
    description.isEnabled = false
    legend.isEnabled = false //关闭显示label
    // 设置背景颜色
    setBackgroundResource(R.color.white)
    axisRight.isEnabled = false//右侧y轴设置为不使用
    animateXY(0, 0)
    ycChartRefresh()
}

/**
 * 默认配置初始化
 * @receiver BarLineChartBase<*>
 */
fun PieChart.ycChartInitDefault() {
    setExtraOffsets(5f, 10f, 5f, 5f)
    setNoDataText(context.getString(R.string.jetpack_chart_data_empty))
    setNoDataTextColor(ycGetColorRes(R.color.jetpack_chart_x_y_text_color))
    // 不使用描述文本相关信息
    renderer = YcPieChartRenderer(this)
    description.isEnabled = false
    legend.isEnabled = false //关闭显示label
    setBackgroundResource(R.color.white)
    setDrawHoleEnabled(true)
    setHoleColor(Color.WHITE)
    setHoleRadius(58f)
    setTransparentCircleRadius(61f)
    setTransparentCircleColor(Color.WHITE)
    setTransparentCircleAlpha(110)
    setHighlightPerTapEnabled(false)
    setRotationEnabled(false)
    setDrawCenterText(false)
    setDrawEntryLabels(false)
}

/**
 * 将y轴上的显示为整数
 * @receiver YAxis
 */
fun BarLineChartBase<*>.ycChartAxisYLeftValueInt() {
    axisLeft.ycChartAxisYLeftValueInt()
}

/**
 * 将y轴上的显示为整数
 * @receiver YAxis
 */
fun YAxis.ycChartAxisYLeftValueInt() {
    valueFormatter = object : ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            val str = value.toString() + ""
            return if (str.isEmpty()) {
                str
            } else str.substring(0, str.indexOf("."))
            //设置自己的返回位数
        }
    }
}

/**
 * 初始化Y轴配置
 * @receiver YAxis
 * @param context Context
 */
fun YAxis.ycChartAxisYLeftInit(context: Context) {
    //左侧侧y轴设置为透明的
    axisLineColor = Color.TRANSPARENT //y轴颜色透明
    axisMinimum = 0f // y轴最小值
    typeface = context.ycGetFont(R.font.dinmittelschriftstd)
    textColor = context.ycGetColorRes(R.color.jetpack_chart_x_y_text_color) //y轴字的颜色
    //辅助线
    gridLineWidth = 0.5f
    gridColor = context.ycGetColorRes(R.color.jetpack_chart_grid_color)
    //虚线样式的辅助线
    setGridDashedLine(DashPathEffect(floatArrayOf(10f, 5f), 1f))
    //由于xAxis.setTextSize()里有进行了*mMetrics.density,所以要除去scaledDensity
    textSize = context.ycGetDimension(R.dimen.yc_chart_axis_text_size) / YcUI.getDisplayMetrics().scaledDensity
}

/**
 * 初始化x轴配置
 * @receiver XAxis
 * @param context Context
 */
fun XAxis.ycChartAxisXInit(context: Context) {
    //将其设置为true以启用绘制该轴的网格线。
    setDrawGridLines(false)
    //x轴在下方
    position = XAxis.XAxisPosition.BOTTOM
//        axisMinimum = -0.5f //从负0.5开始，使得左侧留一些空
//        axisMaximum = 0.5f
    setDrawAxisLine(false)//不绘制垂直辅助线
//        granularity =5f //放大时为轴设置最小间隔。轴不允许低于该限制。这可用于在放大时避免标签重复
//    if (xShowLabelCount != null)
//        setLabelCount(xShowLabelCount, false)   //设置X轴显示多少个,true为强制显示labelCount个，如果不是可以少于labelCount个，但不能大于labelCount个
    labelRotationAngle = 0f //文字倾斜
    typeface = context.ycGetFont(R.font.dinmittelschriftstd)
    textColor = context.ycGetColorRes(R.color.jetpack_chart_x_y_text_color) //x轴字的颜色
    //由于setTextSize()里有进行了*mMetrics.density,所以要除去scaledDensity
    textSize = context.ycGetDimension(R.dimen.yc_chart_axis_text_size) / YcUI.getDisplayMetrics().scaledDensity
}

/**
 * 能否缩放
 * @receiver BarLineChartBase<*>
 * @param enabled Boolean
 */
fun BarLineChartBase<*>.ycChartSetScaleEnabled(enabled: Boolean) {
    // 如果设置为true，则可以用2个手指同时缩放x和y轴，如果为false，则可以分别缩放x和y轴。 默认值：false
    setPinchZoom(enabled) // 如果设置为true，则可以用2个手指同时缩放x和y轴，如果为false，则可以分别缩放x和y轴。 默认值：false
    setScaleEnabled(enabled) //将其设置为true以在X轴和Y轴上为图表启用缩放（通过手势放大和缩小）（这不影响拖动）
    isDoubleTapToZoomEnabled = enabled //禁止双击屏幕缩放
}

/**
 * 刷新数据
 * @receiver BarLineChartBase<*>
 */
fun BarLineChartBase<*>.ycChartRefresh() {
    notifyDataSetChanged()
    invalidate() //刷新
}

/**
 * 刷新数据
 * @receiver BarLineChartBase<*>
 */
fun PieRadarChartBase<*>.ycChartRefresh() {
    notifyDataSetChanged()
    invalidate() //刷新
}

/**
 * 设置y轴最大值
 * @receiver YAxis
 * @param maxY Float            显示数据的最大值(非y轴上的最大值)
 * @param yLabelCount Int        y轴分为多少块
 * @param surplusScale Float    上方多余的空白比例
 * @param isIntMax Boolean      是否需要强制为整数
 */
fun BarLineChartBase<*>.ycChartRefreshAxisYLabelCount(maxY: Float, yLabelCount: Int = 5, surplusScale: Float = 1.5f, isIntMax: Boolean = true) {
    axisLeft.ycChartRefreshAxisYLabelCount(maxY, yLabelCount, surplusScale, isIntMax)
}

/**
 * 设置y轴最大值
 * @receiver YAxis
 * @param maxY Float            实际数据的最大值(非y轴label最大数据)
 * @param yLabelCount Int       y轴label显示数量
 * @param blankScale Float      上方留白比例
 * @param isIntMax Boolean      是否需要强制为整数
 */
fun YAxis.ycChartRefreshAxisYLabelCount(maxY: Float, yLabelCount: Int = 5, blankScale: Float = 1.5f, isIntMax: Boolean = true) {
    axisMaximum = when {
        maxY < yLabelCount -> {
            yLabelCount.toFloat()
        }
        isIntMax -> {
            ((maxY * blankScale).toInt()).toFloat()
        }
        else -> {
            maxY * blankScale
        }
    }
    this.labelCount = yLabelCount
}

/**
 * 根据数据刷新图表的x轴label
 * @receiver BarLineChartBase<*>
 * @param dataSizeX Int         x轴数据的数量
 * @param xShowLabelCount Int?  x轴最多显示数量（默认使用之前设置的）
 */
fun BarLineChartBase<*>.ycChartRefreshAxisXLabelCount(dataSizeX: Int, xLabelCount: Int = dataSizeX, xShowLabelCount: Int? = null) {
    val maxShowLabel = if (xShowLabelCount != null) {
        xAxis.setLabelCount(xShowLabelCount, true)
        xShowLabelCount
    } else {
        xAxis.labelCount
    }
    xAxis.axisMinimum = -0.5f//设置x轴最小值
    if (maxShowLabel >= dataSizeX) {
        xAxis.axisMaximum = dataSizeX - 0.5f
        setVisibleXRange(0f, xLabelCount.toFloat())
    } else {
        xAxis.axisMaximum = dataSizeX - 0.35f //设置x轴最大值(总数+0.35，从0开始)；0.35为右侧边距，条形柱占0.3，左右边距分别为0.35
        setVisibleXRange(0f, xLabelCount + 0.5f) //多展示半个
    }
}

/**
 * 水平方向添加一条限制线
 * @receiver BarLineChartBase<*>
 * @param dataY Float   数据
 * @param lineIndex Int 标示（用于区分第几条）
 * @param color Int     颜色
 * @param label String  显示的提示文字
 */
fun BarLineChartBase<*>.ycChartSetLimitLine(
    dataY: Float,
    lineIndex: Int = 0,
    color: Int = YcResources.getColorRes(R.color.jetpack_chart_limit_line_color),
    label: String = ""
) {
    axisLeft.ycChartSetLimitLine(dataY, lineIndex, color, label)
}

/**
 * 水平方向添加一条限制线
 * @receiver YAxis
 * @param dataY Float   数据
 * @param lineIndex Int 标示（用于区分第几条）
 * @param color Int     颜色
 * @param label String  显示的提示文字
 */
fun YAxis.ycChartSetLimitLine(
    dataY: Float,
    lineIndex: Int = 0,
    color: Int = YcResources.getColorRes(R.color.jetpack_chart_limit_line_color),
    label: String = ""
) {
    val yLimitLine = LimitLine(dataY, label)
    yLimitLine.lineColor = color
    yLimitLine.textColor = color
    yLimitLine.textSize = YcResources.getDimension(R.dimen.yc_chart_axis_text_size) / YcUI.getDisplayMetrics().scaledDensity
    if (limitLines.size > lineIndex) {
        limitLines[lineIndex] = yLimitLine
    } else {
        limitLines.add(lineIndex, yLimitLine)
    }
}


fun PieChart.ycChartSetLineDataSet(pieDataList: List<PieEntry>, formatter: ((PieEntry) -> String)? = null) {
    val dataSet = PieDataSet(pieDataList, "")
    dataSet.axisDependency = YAxis.AxisDependency.LEFT
    val colors = resources.getStringArray(R.array.yc_pic_chart_colors).map {
        Color.parseColor(it)
    }
    dataSet.colors = colors
    dataSet.setValueTextColors(colors)
    dataSet.yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
    dataSet.setDrawValues(true)
    if (data == null) {
        data = PieData(dataSet)
    } else {
        this.data.dataSet = dataSet
    }
    data.setValueFormatter(object : ValueFormatter() {
        override fun getPieLabel(value: Float, pieEntry: PieEntry): String {
            return formatter?.invoke(pieEntry) ?: super.getPieLabel(value, pieEntry)
        }
    })
    ycChartRefresh()
}

/**
 * 设置lineChart的dataSet
 * @receiver LineChart
 * @param lineDataList List<YcEntry>
 * @param index Int
 */
fun LineChart.ycChartSetLineDataSet(lineDataList: List<YcEntry>, index: Int = 0) {
    if (data == null) {
        data = LineData()
    }
    if (data.dataSetCount <= index) {
        val dataSet = LineDataSet(lineDataList, "")
        dataSet.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
        dataSet.axisDependency = YAxis.AxisDependency.LEFT
//        dataSet.color = color //设置颜色(//TODO YcChartLineRenderer里已经写死了，以后在改)
        dataSet.lineWidth = 1.5f //设置线的宽度
        //顶点圆
        dataSet.circleRadius = 4.5f //设置顶点的半径
        dataSet.setDrawValues(false) //关闭顶点上方的文字
        dataSet.setCircleColor(ycGetColorRes(R.color.jetpack_chart_draw_circle_color)) //设置顶点的颜色,由于重新Renderer这边设置颜色可能失效
        dataSet.highLightColor = ycGetColorRes(R.color.jetpack_chart_high_light_color) //指引线的颜色
        dataSet.enableDashedHighlightLine(10f, 5f, 1f)//指引线设置为虚线
        //开启填充（关闭后会极大提升性能）
        dataSet.setDrawFilled(true)
        dataSet.fillDrawable = ycGetDrawable(R.drawable.yc_selector_chart_fill)
        dataSet.fillAlpha = 100
        dataSet.setDrawHorizontalHighlightIndicator(false)
        lineData.addDataSet(dataSet)
    } else {
        val lineDataSet: LineDataSet = data.getDataSetByIndex(index) as LineDataSet
        lineDataSet.values = lineDataList
    }
}

/**
 * 设置BarChart的dataSet
 * @receiver BarChart
 * @param barDataList List<BarEntry>    数据
 * @param color Int                     颜色
 * @param index Int                     标示（用于区分第几条）
 * @param isHighlightEnabled Boolean    是否高亮(必须有一个是，否则 单击后不会显示提示框（即不显示ChartMarker）)
 * @param barWidth Float                设置柱状占1一个单位的比例
 */
fun BarChart.ycChartSetBarDataSet(barDataList: List<BarEntry>, color: Int, index: Int = 0, isHighlightEnabled: Boolean = false, barWidth: Float = 0.2f) {
    if (data == null) {
        data = BarData()
        data.barWidth = barWidth
    }
    if (data.dataSetCount <= index) {
        val dataSet = BarDataSet(barDataList, "" + index)
        dataSet.axisDependency = YAxis.AxisDependency.LEFT
        dataSet.color = color //设置颜色
        dataSet.setDrawValues(false) //关闭顶点上方的文字
        dataSet.isHighlightEnabled = isHighlightEnabled
        data.addDataSet(dataSet)
    } else {
        val dataSet: BarDataSet = data.getDataSetByIndex(index) as BarDataSet
        dataSet.values = barDataList
    }
}
