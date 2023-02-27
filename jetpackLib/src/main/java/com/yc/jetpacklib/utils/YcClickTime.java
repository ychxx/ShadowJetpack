package com.yc.jetpacklib.utils;


/**
 *
 */
public class YcClickTime {
    private static long clickTime;
    /**
     * 判断是否是无效的点击，防止抖动点击
     *
     * @return true无效，false有效
     */
    public static boolean isInvalidClick() {
        if (System.currentTimeMillis() - clickTime < 200) {
            clickTime = System.currentTimeMillis();
            return true;
        } else {
            return false;
        }
    }
}
