package com.yc.jetpacklib.widget.chart

import android.content.Context
import android.graphics.Color
import android.graphics.DashPathEffect
import android.util.Log
import androidx.core.content.res.ResourcesCompat
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.yc.jetpacklib.R
import com.yc.jetpacklib.utils.YcResources
import com.yc.jetpacklib.utils.YcResources.getColorRes
import com.yc.jetpacklib.utils.YcResources.getDrawable
import com.yc.jetpacklib.utils.YcUI.getDisplayMetrics
import java.util.*

/**
 *
 */
object ChartHelper {
    @JvmStatic
    fun init(lineChart: LineChart, context: Context) {
        lineChart.setNoDataText(context.getString(R.string.every_lib_chart_data_empty))
        lineChart.setNoDataTextColor(getColorRes(R.color.every_lib_chart_x_y_text_color))
        lineChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(entry: Entry, highlight: Highlight) {
                lineChart.centerViewToAnimated(entry.x, entry.y, lineChart.data.getDataSetByIndex(highlight.dataSetIndex).axisDependency, 500)
            }

            override fun onNothingSelected() {
                Log.i("Nothing selected", "Nothing selected.")
            }
        })
        // 不使用描述文本相关信息
        lineChart.description.isEnabled = false
        lineChart.description.text = "描述"
        // 手势能否触摸图表
        lineChart.setTouchEnabled(true)
        //减速摩擦系数[0,1]之间，0立刻停止，1，自动转换为0.999f
        lineChart.dragDecelerationFrictionCoef = 0.9f

        // 将其设置为true以启用图表的拖动（用手指移动图表）（这不会影响缩放）。
        lineChart.isDragEnabled = true
        //将此设置为true以绘制网格背景，否则为false
        lineChart.setDrawGridBackground(false)
        //将其设置为true以允许在完全缩小时拖动图表曲面时突出显示。 默认值：true
        lineChart.isHighlightPerDragEnabled = true

        // 如果设置为true，则可以用2个手指同时缩放x和y轴，如果为false，则可以分别缩放x和y轴。 默认值：false
        lineChart.setPinchZoom(false) // 如果设置为true，则可以用2个手指同时缩放x和y轴，如果为false，则可以分别缩放x和y轴。 默认值：false
        lineChart.setScaleEnabled(false) //将其设置为true以在X轴和Y轴上为图表启用缩放（通过手势放大和缩小）（这不影响拖动）
        lineChart.isScaleXEnabled = false
        lineChart.isScaleYEnabled = false //禁止y轴放大和缩小
        // 设置背景颜色
        lineChart.setBackgroundResource(R.color.white)
        //使用指定的动画时间在x轴上动画显示图表。
        // lineChart.animateX(1500);
        //右侧y轴设置为不使用
        lineChart.axisRight.isEnabled = true

        //右侧y轴设置为透明的
        lineChart.axisRight.isEnabled = false
        //lineChart.getAxisRight().setTextColor(Color.TRANSPARENT);//y轴字的颜色
        //lineChart.getAxisRight().setAxisLineColor(Color.TRANSPARENT);
        //lineChart.getAxisRight().setSpaceTop(5f);//将顶部轴空间设置为整个范围的百分比。默认10f（即10%）
        //lineChart.getAxisRight().setDrawGridLines(true);    //将此设置为true，以便绘制该轴的网格线
        //lineChart.getAxisRight().setGranularityEnabled(false);  //在轴值间隔上启用/禁用粒度控制。
        //lineChart.getAxisRight().setXOffset(13f);
        //左侧侧y轴设置为透明的
        lineChart.axisLeft.axisLineColor = Color.TRANSPARENT //y轴颜色透明
        lineChart.axisLeft.axisMinimum = 0f // y轴最小值
        lineChart.axisLeft.typeface = ResourcesCompat.getFont(context, R.font.dinmittelschriftstd)
        lineChart.axisLeft.textColor = getColorRes(R.color.every_lib_chart_x_y_text_color) //y轴字的颜色
        lineChart.axisLeft.gridLineWidth = 0.5f
        lineChart.axisLeft.gridColor = getColorRes(R.color.every_lib_chart_y_grid_color)
        lineChart.axisLeft.setGridDashedLine(DashPathEffect(floatArrayOf(10f, 5f), 1f))
        //x周相关设置
//        CustomAxisX formatter = new CustomAxisX(list);
//        xAxis.setTypeface(mTfLight);f

        //将其设置为true以启用绘制该轴的网格线。
        lineChart.xAxis.setDrawGridLines(false)
        //x轴在下方
        lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        lineChart.xAxis.axisMinimum = -0.5f //从负0.5开始，使得左侧留一些空
        lineChart.xAxis.setDrawAxisLine(false)
        lineChart.xAxis.granularity = 1f //设置x轴间距
        lineChart.xAxis.labelCount = 12 //设置
        lineChart.xAxis.labelRotationAngle = 0f //文字倾斜
        lineChart.xAxis.typeface = ResourcesCompat.getFont(context, R.font.dinmittelschriftstd)
        lineChart.xAxis.textColor = getColorRes(R.color.every_lib_chart_x_y_text_color) //x轴字的颜色
        var textSize = context.resources.getDimensionPixelSize(R.dimen.every_lib_chart_x_axis_text_size).toFloat()
        textSize /= getDisplayMetrics().scaledDensity
        lineChart.xAxis.textSize = textSize

        //lineChart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        //lineChart.getLegend().setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        //lineChart.getLegend().setDrawInside(true);
        lineChart.legend.isEnabled = false //关闭显示label
        lineChart.animateXY(0, 0)
        lineChart.notifyDataSetChanged()
        lineChart.invalidate() //刷新
    }

    @JvmStatic
    fun setLineDataSet(lineChart: LineChart, dataList: List<Double>, index: Int) {
        val entries = ArrayList<Entry>()
        for (i in dataList.indices) {
            entries.add(Entry(i.toFloat(), dataList[i].toFloat()))
        }
        val dataSet: LineDataSet
        if (lineChart.data != null && lineChart.data.dataSetCount > index) {
            dataSet = lineChart.data.getDataSetByIndex(index) as LineDataSet
            dataSet.values = entries
        } else {
            dataSet = LineDataSet(entries, "")
            dataSet.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
            dataSet.axisDependency = YAxis.AxisDependency.LEFT
            //set.setColor(color);//设置线的颜色
            dataSet.lineWidth = 1.5f //设置线的宽度
            //顶点圆
            dataSet.circleRadius = 4.5f //设置顶点的半径
            dataSet.setDrawValues(false) //关闭顶点上方的文字
            //dataSet.setValueTextColor(YcResources.getColor(R.color.chart_value_text_color));
            //dataSet.setValueTypeface(ResourcesCompat.getFont(App.getInstance(), R.font.dinmittelschriftstd));
            dataSet.circleSize = 2.5f
            dataSet.setCircleColor(getColorRes(R.color.every_lib_chart_value_circle_color)) //设置顶点的颜色,由于重新Renderer这边设置颜色可能失效
            //dataSet.setCubicIntensity(0.1f);
            //set.setHighlightEnabled(false);//关闭指引线宽度
            //set.setHighlightLineWidth(1f); //指引线宽度
            dataSet.highLightColor = getColorRes(R.color.every_lib_chart_high_light_color) //指引线的颜色
            dataSet.enableDashedHighlightLine(10f, 5f, 1f)
            //开启填充（关闭后会极大提升性能）
            dataSet.setDrawFilled(true)
            dataSet.fillDrawable = getDrawable(R.drawable.yc_selector_chart_fill)
            dataSet.fillAlpha = 100
            dataSet.setDrawHorizontalHighlightIndicator(false)
            lineChart.lineData.addDataSet(dataSet)
        }
    }

    /*
    * 扬尘监控
    * */
    @JvmStatic
    fun setLineDataSetThreshold(lineChart: LineChart, threshold: Float, xStart: Float, xEnd: Float, index: Int) {
        val entries = ArrayList<Entry>()
        entries.add(Entry(xStart, threshold))
        entries.add(Entry(xEnd, threshold))
        val dataSet: LineDataSet
        if (lineChart.data != null && lineChart.data.dataSetCount > index) {
            dataSet = lineChart.data.getDataSetByIndex(index) as LineDataSet
            dataSet.values = entries
        } else {
            dataSet = LineDataSet(entries, "")
            dataSet.mode = LineDataSet.Mode.LINEAR
            dataSet.axisDependency = YAxis.AxisDependency.LEFT
            dataSet.enableDashedLine(10f, 5f, 1f)
            dataSet.color = YcResources.getColorRes(R.color.every_lib_chart_threshold_line_color) //设置线的颜色
            dataSet.lineWidth = 1f //设置线的宽度
            dataSet.setDrawCircles(false)
            dataSet.setDrawCircleHole(false)
            dataSet.setDrawValues(false)
            //指引线
            dataSet.isHighlightEnabled = false //关闭指引线宽度
            //开启填充（关闭后会极大提升性能）
            dataSet.setDrawFilled(false)
            lineChart.lineData.addDataSet(dataSet)
        }
    }


    @JvmStatic
    fun init(barChart: BarChart, context: Context) {
        barChart.setNoDataText(context.getString(R.string.every_lib_chart_data_empty))
        barChart.setNoDataTextColor(getColorRes(R.color.every_lib_chart_x_y_text_color))
        barChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(entry: Entry, highlight: Highlight) {
                barChart.centerViewToAnimated(entry.x, entry.y, barChart.data.getDataSetByIndex(highlight.dataSetIndex).axisDependency, 500)
            }

            override fun onNothingSelected() {
                Log.i("Nothing selected", "Nothing selected.")
            }
        })
        barChart.setPinchZoom(false) // 如果设置为true，则可以用2个手指同时缩放x和y轴，如果为false，则可以分别缩放x和y轴。 默认值：false
        barChart.setScaleEnabled(false) //将其设置为true以在X轴和Y轴上为图表启用缩放（通过手势放大和缩小）（这不影响拖动）
        barChart.isScaleXEnabled = false
        barChart.isScaleYEnabled = false //禁止y轴放大和缩小
        barChart.isDoubleTapToZoomEnabled = false //禁止双击屏幕缩放
        // 不使用描述文本相关信息
        barChart.description.isEnabled = false
        barChart.description.text = "描述"
        // 手势能否触摸图表
        barChart.setTouchEnabled(true)
        //减速摩擦系数[0,1]之间，0立刻停止，1，自动转换为0.999f
        barChart.dragDecelerationFrictionCoef = 0.9f
        // 将其设置为true以启用图表的拖动（用手指移动图表）（这不会影响缩放）。
        barChart.isDragEnabled = true
        //将此设置为true以绘制网格背景，否则为false
        barChart.setDrawGridBackground(false)
        //将其设置为true以允许在完全缩小时拖动图表曲面时突出显示。 默认值：true
        barChart.isHighlightPerDragEnabled = true
        barChart.setBackgroundResource(R.color.white)// 设置背景颜色
        //使用指定的动画时间在x轴上动画显示图表。
        //lineChart.animateX(1500);
        //右侧y轴设置为不使用
        barChart.axisRight.isEnabled = false
        //lineChart.getAxisRight().setTextColor(Color.TRANSPARENT);//y轴字的颜色
        //lineChart.getAxisRight().setAxisLineColor(Color.TRANSPARENT);
        //lineChart.getAxisRight().setSpaceTop(5f);//将顶部轴空间设置为整个范围的百分比。默认10f（即10%）
        //lineChart.getAxisRight().setDrawGridLines(true);    //将此设置为true，以便绘制该轴的网格线
        //lineChart.getAxisRight().setGranularityEnabled(false);  //在轴值间隔上启用/禁用粒度控制。
        //lineChart.getAxisRight().setXOffset(13f);
        //左侧侧y轴设置为透明的
        barChart.axisLeft.axisLineColor = Color.TRANSPARENT //y轴颜色透明
        barChart.axisLeft.axisMinimum = 0f // y轴最小值
        barChart.axisLeft.typeface = ResourcesCompat.getFont(context, R.font.dinmittelschriftstd)
        barChart.axisLeft.textColor = getColorRes(R.color.every_lib_chart_x_y_text_color) //y轴字的颜色
        barChart.axisLeft.gridLineWidth = 0.5f
        barChart.axisLeft.gridColor = getColorRes(R.color.every_lib_chart_y_grid_color)
        barChart.axisLeft.setGridDashedLine(DashPathEffect(floatArrayOf(10f, 5f), 1f))
        //x周相关设置
//        CustomAxisX formatter = new CustomAxisX(list);
//        xAxis.setTypeface(mTfLight);

        //将其设置为true以启用绘制该轴的网格线。
        barChart.xAxis.setDrawGridLines(false)
        //x轴在下方
        barChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        barChart.xAxis.setDrawAxisLine(false)
        barChart.xAxis.labelCount = 6 //设置
        barChart.xAxis.axisMinimum = -0.5f //设置x轴最小值
        //        barChart.setVisibleXRange(0,7);
        barChart.xAxis.labelRotationAngle = 0f //文字倾斜
        barChart.xAxis.yOffset = 10f //x轴label(文字) 偏移量(上方)  必须调用setExtraTopOffset添加额外偏移量，否则会导致滑动时高度偏差问题
        barChart.extraTopOffset = 10f //额外的偏移量(上方)
        barChart.extraBottomOffset = 2f //额外的偏移量(下方) 添加偏移后会导致下方缺少一些像素
        barChart.xAxis.typeface = ResourcesCompat.getFont(context, R.font.dinmittelschriftstd)
        barChart.xAxis.textColor = getColorRes(R.color.every_lib_chart_x_y_text_color) //x轴字的颜色
        var textSize = context.resources.getDimensionPixelSize(R.dimen.every_lib_chart_x_axis_text_size).toFloat()
        textSize /= getDisplayMetrics().scaledDensity
        barChart.xAxis.textSize = textSize

        //lineChart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        //lineChart.getLegend().setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        //lineChart.getLegend().setDrawInside(true);
        barChart.legend.isEnabled = false //关闭显示label
        barChart.animateXY(0, 0)
        barChart.notifyDataSetChanged()
        barChart.invalidate() //刷新
    }

    @JvmStatic
    fun setBarDataSet(barChart: BarChart, dataList1: List<Double>, index: Int, color: Int, isHighlightEnabled: Boolean) {
        val entries = ArrayList<BarEntry>()
        for (i in dataList1.indices) {
            entries.add(BarEntry(i.toFloat(), dataList1[i].toFloat()))
        }
        val dataSet: BarDataSet
        if (barChart.data != null && barChart.data.dataSetCount > index) {
            dataSet = barChart.data.getDataSetByIndex(index) as BarDataSet
            dataSet.values = entries
        } else {
            dataSet = BarDataSet(entries, "")
            dataSet.axisDependency = YAxis.AxisDependency.LEFT
            //dataSet.setBarShadowColor(Color.argb(0, 0, 0, 0));
            dataSet.color = color //设置颜色
            // dataSet.setDrawIcons(false);
            dataSet.setDrawValues(false) //关闭顶点上方的文字
            dataSet.isHighlightEnabled = isHighlightEnabled
            //data.setValueFormatter(new StackedValueFormatter(false, "", 1));
            //data.setValueTextColor(Color.GREEN);
            barChart.data.addDataSet(dataSet)
        }
    }
}