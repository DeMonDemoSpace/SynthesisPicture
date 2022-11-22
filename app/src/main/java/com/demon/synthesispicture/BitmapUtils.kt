package com.demon.synthesispicture

import android.graphics.Bitmap
import android.graphics.Canvas

/**
 * @author admin
 * Created on 22/11/22.
 * E-mail 757454343@qq.com
 * Desc:
 */
object BitmapUtils {

    /**
     *
     * @param background  背景
     * @param foreground 前景
     * @param x  合成在背景上的x坐标
     * @param y  合成在背景上的y坐标
     * @param isRecycle 是否回收背景&前景Bitmap
     */
    fun synthesis(
        background: Bitmap?,
        foreground: Bitmap?,
        x: Int = 0,
        y: Int = 0,
        isRecycle: Boolean = true
    ): Bitmap? {
        var newBitmap: Bitmap? = null
        try {
            background ?: return null
            foreground ?: return null
            val bgWidth = background.width
            val bgHeight = background.height
            newBitmap = Bitmap.createBitmap(bgWidth, bgHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(newBitmap)
            canvas.drawBitmap(background, 0f, 0f, null)
            canvas.drawBitmap(foreground, x.toFloat(), y.toFloat(), null)
            canvas.save()
            canvas.restore()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (isRecycle) {
                background?.recycle()
                foreground?.recycle()
            }
        }
        return newBitmap
    }

}