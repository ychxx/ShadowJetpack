package com.yc.jetpacklib.init

import android.app.Application
import android.content.res.Resources
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.yc.jetpacklib.BuildConfig
import com.yc.jetpacklib.R
import com.yc.jetpacklib.extension.YcLogExt
import com.yc.jetpacklib.refresh.YcFooterAdapter
import com.yc.jetpacklib.refresh.YcRefreshHeaderView
import com.yc.jetpacklib.widget.pickerview.YcPickerColor
import okhttp3.Interceptor
import org.xutils.x

/**
 * Creator: yc
 * Date: 2021/6/3 13:54
 * UseDes:
 */

class YcJetpack private constructor() {
    companion object {
        const val OTHER_BASE_URL = "other_base_url"

        @JvmStatic
        val mInstance = YcJetpack2Holder.holder
    }

    private object YcJetpack2Holder {
        @JvmStatic
        val holder = YcJetpack()
    }

    /**
     * 加载网络图片失败时显示的图片
     */
    var mImgIdResFail: Int = R.drawable.yc_img_loading

    /**
     * 加载网络图片加载时显示的图片
     */
    var mImgIdResLoading: Int = R.drawable.yc_img_loading


    /**
     * 请求成功返回的code
     */
    var mNetSuccessCode: Int? = 200

    /**
     * retrofit的过滤器
     */
    val mInterceptor: MutableList<Interceptor> = mutableListOf()
    var mDefaultBaseUrl = ""
    lateinit var mApplication: Application

    fun init(app: Application) {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, _ -> YcRefreshHeaderView(context) }
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, _ -> YcFooterAdapter(context) }
        mApplication = app
        //Logger初始化
        Logger.addLogAdapter(AndroidLogAdapter())
        x.Ext.init(mApplication)
        x.Ext.setDebug(BuildConfig.DEBUG)
    }

    fun setBaseUrl(baseUrl: String = "") {
        mDefaultBaseUrl = baseUrl
    }

    /**
     * 添加过滤器
     */
    fun addInterceptor(interceptor: Interceptor) {
        mInterceptor.add(interceptor)
    }

    /**
     * 是否显示logger
     */
    fun setLog(isShow: Boolean) {
        YcLogExt.mIsShowLogger = isShow
    }

    fun getResources(): Resources = mApplication.resources

    var mPickerColor = YcPickerColor(
        R.color.common_bg, R.color.common_bg, R.color.every_lib_blue, R.color.transparent, R.color.every_lib_black_333A40
    )

    fun setPickerColorInt(
        submitColor: Int = R.color.every_lib_blue, textColorCenter: Int = R.color.every_lib_black_333A40,
        cancelColor: Int = R.color.transparent, dividerColor: Int = R.color.common_bg, titleBgColor: Int = R.color.common_bg
    ) {
        this.mPickerColor = YcPickerColor(dividerColor, titleBgColor, submitColor, cancelColor, textColorCenter)
    }
}