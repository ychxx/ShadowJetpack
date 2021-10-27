package com.yc.jetpacklib.init

import android.app.Application
import android.content.Context
import android.content.res.Resources
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.yc.jetpacklib.R
import com.yc.jetpacklib.extension.YcLogExt
import com.yc.jetpacklib.refresh.YcRefreshFooterAdapter
import com.yc.jetpacklib.refresh.YcRefreshHeaderView
import com.yc.jetpacklib.release.YcSpecialViewConfigureBase
import com.yc.jetpacklib.release.YcSpecialViewConfigureImp
import com.yc.jetpacklib.widget.pickerview.YcPickerColor
import okhttp3.Interceptor
import org.xutils.x
import java.io.File

/**
 * Creator: yc
 * Date: 2021/6/3 13:54
 * UseDes:
 */

class YcJetpack private constructor() {
    companion object {
        const val OTHER_BASE_URL = "other_base_url"

        init {
            //设置全局的Header构建器
            SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, _ -> YcRefreshHeaderView(context) }
            //设置全局的Footer构建器
            SmartRefreshLayout.setDefaultRefreshFooterCreator { context, _ -> YcRefreshFooterAdapter(context) }
        }

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
    var mNetSuccessCode: MutableList<Int>? = mutableListOf(200)

    /**
     * retrofit的过滤器
     */
    val mInterceptor: MutableList<Interceptor> = mutableListOf()

    /**
     * 接口地址
     */
    var mDefaultBaseUrl = ""
    var mCreateSpecialViewBuildBase: ((context: Context) -> YcSpecialViewConfigureBase) = {
        YcSpecialViewConfigureImp(it)
    }


    /**
     * 默认保存文件夹路径
     */
    lateinit var mDefaultSaveDirPath: String

    lateinit var mApplication: Application

    fun init(app: Application, defaultSaveDirPath: String = app.filesDir.path + File.separator) {
        mApplication = app
        mDefaultSaveDirPath = defaultSaveDirPath
        //Logger初始化
        Logger.addLogAdapter(AndroidLogAdapter())
        x.Ext.init(mApplication)
    }

    /**
     * 是否显示logger
     */
    fun setLog(isShow: Boolean) {
        YcLogExt.mIsShowLogger = isShow
        x.Ext.setDebug(isShow)
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