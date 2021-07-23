package com.yc.jetpacklib.utils

import com.yc.jetpacklib.R
import com.yc.jetpacklib.utils.YcRegularUtil.ycRegular
import java.util.regex.Pattern

/**
 * Creator: yc
 * Date: 2021/7/15 14:44
 * UseDes:用于正则表达式的
 */
object YcRegularUtil {
    /**
     * 只能是字母
     */
    const val ONLY_NUM_AND_LETTER = "^[A-Za-z0-9]"

    /**
     * 只能是纯汉字+数字
     */
    const val ONLY_NUM_AND_CHINESE = "^[0-9\u4e00-\u9fa5]"

    /**
     * 只能是纯汉字+字母
     */
    const val ONLY_CHINESE_AND_LETTER = "^[A-Za-z\u4e00-\u9fa5]"

    /**
     * 只能是纯汉字
     */
    const val ONLY_CHINESE = "^[\u4e00-\u9fa5]"

    /**
     * 只能是汉字或字母或数字
     */
    const val ONLY_CHINESE_OR_NUM_OR_LETTER = "^[A-Za-z0-9\u4e00-\u9fa5]"

    /**
     * 只能是纯汉字或汉字+字母或汉字+数字
     */
    const val ONLY_CHINESE_AND_NUM_OR_CHINESE_AND_LETTER_OR_CHINESE =
        "^(?![0-9]+$)(?![a-zA-Z]+$)([A-Za-z\u4e00-\u9fa5]+|^[0-9\u4e00-\u9fa5]+|^[\u4e00-\u9fa5]+)"

    /**
     * 手机号
     */
    const val PHONE = "^1[3|4|5|6|7|8][0-9]\\d{8}$"

    /**
     * 身份证号
     */
    const val ID_CARD = "^\\d{15}$|^\\d{17}[0-9Xx]$"

    @JvmStatic
    fun String.ycRegular(regular: String): Boolean = Pattern.matches(regular, this)
}