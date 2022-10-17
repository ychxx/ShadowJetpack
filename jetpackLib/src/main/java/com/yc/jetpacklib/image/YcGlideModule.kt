package com.yc.jetpacklib.image

import android.content.Context
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.Headers
import com.bumptech.glide.request.RequestOptions
import com.google.common.collect.Multimap
import com.yc.jetpacklib.init.YcJetpack

/**
 * Creator: yc
 * Date: 2021/6/4 17:41
 * UseDes:
 */
@GlideModule
class YcGlideModule : AppGlideModule() {
    //禁止解析Manifest文件,可以提升初始化速度，避免一些潜在错误
    override fun isManifestParsingEnabled(): Boolean {
        return false
    }

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        val options: RequestOptions = RequestOptions()
            .placeholder(YcJetpack.mInstance.mImgIdResLoading)
            .error(YcJetpack.mInstance.mImgIdResFail)
        builder.setDefaultRequestOptions(options)
    }
}