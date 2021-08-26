package com.yc.jetpacklib.widget.chart;

import android.graphics.Canvas;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.renderer.XAxisRenderer;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;

/**
 * Creator: yc
 * Date: 2021/8/18 10:22
 * UseDes:
 * 最后一个label也绘制（非等间距）
 * 该自定义x轴只用于不缩放，x轴方法不滑动的图表，否则x轴label显示会有异常
 */
public class YcChartAxisXExtraLastRenderer extends XAxisRenderer {
    protected BarDataProvider mChartBar;
    protected LineDataProvider mChartLine;
    /**
     * x轴最后一个label的数据
     */
    protected float mLabelLastEntry = 0;
    /**
     * x轴是否多绘制一个label（当x轴最后两个点间距与其他两点间距，不相等时为true）
     */
    protected boolean mIsDrawLastLabel = false;

    public YcChartAxisXExtraLastRenderer(BarChart barChart) {
        super(barChart.getViewPortHandler(), barChart.getXAxis(), barChart.getTransformer(YAxis.AxisDependency.LEFT));
        mChartBar = barChart;
    }

    public YcChartAxisXExtraLastRenderer(LineChart lineChart) {
        super(lineChart.getViewPortHandler(), lineChart.getXAxis(), lineChart.getTransformer(YAxis.AxisDependency.LEFT));
        mChartLine = lineChart;
    }

    @Override
    protected void computeAxisValues(float min, float max) {
        int labelCount = mAxis.getLabelCount();
        double range = Math.abs(max - min);
        if (labelCount == 0 || range <= 0 || Double.isInfinite(range)) {
            mAxis.mEntries = new float[]{};
            mAxis.mCenteredEntries = new float[]{};
            mAxis.mEntryCount = 0;
            mIsDrawLastLabel = false;
            return;
        }
        //由于ui的x轴显示字符串，且x轴值都为整数；
        //所以间隔为等分向上取整
        double interval = Math.ceil(range / (labelCount - 1));
        //n代表要绘制的点数
        int n = mAxis.isCenterAxisLabelsEnabled() ? 1 : 0;
        // force label count
        if (mAxis.isForceLabelsEnabled()) {
            interval = (float) range / (float) (labelCount - 1);
            mAxis.mEntryCount = labelCount;
            if (mAxis.mEntries.length < labelCount) {
                // Ensure stops contains at least numStops elements.
                mAxis.mEntries = new float[labelCount];
            }
            float v = min;
            for (int i = 0; i < labelCount; i++) {
                mAxis.mEntries[i] = v;
                v += interval;
            }
            n = labelCount;
            // no forced count
        } else {
            double first = interval == 0.0 ? 0.0 : Math.ceil(min / interval) * interval;
            if (mAxis.isCenterAxisLabelsEnabled()) {
                first -= interval;
            }
            double last = interval == 0.0 ? 0.0 : Utils.nextUp(Math.floor(max / interval) * interval);
            double f;
            int i;
            if (interval != 0.0) {
                for (f = first; f <= last; f += interval) {
                    ++n;
                }
            }
            mAxis.mEntryCount = n;
            if (mAxis.mEntries.length < n) {
                // Ensure stops contains at least numStops elements.
                mAxis.mEntries = new float[n];
            }
            for (f = first, i = 0; i < n; f += interval, ++i) {
                if (f == 0.0) // Fix for negative zero case (Where value == -0.0, and 0.0 == -0.0)
                    f = 0.0;
                mAxis.mEntries[i] = (float) f;
            }
        }
        // set decimals
        if (interval < 1) {
            mAxis.mDecimals = (int) Math.ceil(-Math.log10(interval));
        } else {
            mAxis.mDecimals = 0;
        }
        if (mAxis.isCenterAxisLabelsEnabled()) {
            if (mAxis.mCenteredEntries.length < n) {
                mAxis.mCenteredEntries = new float[n];
            }
            float offset = (float) interval / 2f;
            for (int i = 0; i < n; i++) {
                mAxis.mCenteredEntries[i] = mAxis.mEntries[i] + offset;
            }
        }
        if ((n - 1) * interval < max) {
            double lastInterval = (range % interval / interval) * interval;
            mLabelLastEntry = (float) (mXAxis.mEntries[n - 1] + lastInterval);
            mIsDrawLastLabel = true;
        } else {
            mIsDrawLastLabel = false;
        }
    }

    /**
     * draws the x-labels on the specified y-position
     *
     * @param pos
     */
    @Override
    protected void drawLabels(Canvas c, float pos, MPPointF anchor) {
        final float labelRotationAngleDegrees = mXAxis.getLabelRotationAngle();
        boolean centeringEnabled = mXAxis.isCenterAxisLabelsEnabled();

        float[] positions = new float[mXAxis.mEntryCount * 2];
        for (int i = 0; i < positions.length; i += 2) {
            // only fill x values
            if (centeringEnabled) {
                positions[i] = mXAxis.mCenteredEntries[i / 2];
            } else {
                positions[i] = mXAxis.mEntries[i / 2];
            }
        }
        mTrans.pointValuesToPixel(positions);
        for (int i = 0; i < positions.length; i += 2) {
            float x = positions[i];
            if (mViewPortHandler.isInBoundsX(x)) {
                String label = mXAxis.getValueFormatter().getAxisLabel(mXAxis.mEntries[i / 2], mXAxis);
                if (mXAxis.isAvoidFirstLastClippingEnabled()) {
                    // avoid clipping of the last
                    if (i / 2 == mXAxis.mEntryCount - 1 && mXAxis.mEntryCount > 1) {
                        float width = Utils.calcTextWidth(mAxisLabelPaint, label);
                        if (width > mViewPortHandler.offsetRight() * 2 && x + width > mViewPortHandler.getChartWidth())
                            x -= width / 2;
                        // avoid clipping of the first
                    } else if (i == 0) {
                        float width = Utils.calcTextWidth(mAxisLabelPaint, label);
                        x += width / 2;
                    }
                }
                drawLabel(c, label, x, pos, anchor, labelRotationAngleDegrees);
            }
        }
        if (mIsDrawLastLabel) {
            String label = mXAxis.getValueFormatter().getAxisLabel(mLabelLastEntry, mXAxis);
            float[] lastPositions = new float[2];
            lastPositions[0] = mLabelLastEntry;
            mTrans.pointValuesToPixel(lastPositions);
            drawLabel(c, label, lastPositions[0], pos, anchor, labelRotationAngleDegrees);
        }
    }
}
