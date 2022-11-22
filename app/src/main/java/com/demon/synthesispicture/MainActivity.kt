package com.demon.synthesispicture

import android.Manifest
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.demon.qfsolution.QFHelper
import com.demon.qfsolution.utils.openPhotoAlbum
import com.permissionx.guolindev.PermissionX
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private var bgFile: String? = null
    private var fgFile: String? = null

    private val tvBg by lazy {
        findViewById<TextView>(R.id.tvBg)
    }
    private val tvFront by lazy {
        findViewById<TextView>(R.id.tvFg)
    }
    private val tvX by lazy {
        findViewById<TextView>(R.id.tvX)
    }
    private val tvY by lazy {
        findViewById<TextView>(R.id.tvY)
    }
    private val etX by lazy {
        findViewById<TextView>(R.id.etX)
    }
    private val etY by lazy {
        findViewById<TextView>(R.id.etY)
    }
    private val iv by lazy {
        findViewById<ImageView>(R.id.ivSynthesis)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        QFHelper.init(this)

        findViewById<Button>(R.id.btnBg).setOnClickListener {
            lifecycleScope.launch {
                bgFile = openPhotoAlbum<String>()
                tvBg.text = bgFile
            }
        }

        findViewById<Button>(R.id.btnFg).setOnClickListener {
            lifecycleScope.launch {
                fgFile = openPhotoAlbum<String>()
                tvFront.text = fgFile
            }
        }



        findViewById<Button>(R.id.btnSynthesis).setOnClickListener {

            getPermission {
                if (bgFile == null || fgFile == null) {
                    Toast.makeText(this@MainActivity, "请先选择背景or前景图～", Toast.LENGTH_SHORT).show()
                    return@getPermission
                }

                if (etX.text.isNullOrEmpty() || etY.text.isNullOrEmpty()) {
                    Toast.makeText(this@MainActivity, "请选输入合成坐标～", Toast.LENGTH_SHORT).show()
                    return@getPermission
                }

                lifecycleScope.launch {
                    val x = etX.text.toString().toInt()
                    val y = etY.text.toString().toInt()
                    BitmapUtils.synthesis(
                        BitmapFactory.decodeFile(bgFile),
                        BitmapFactory.decodeFile(fgFile),
                        x,
                        y
                    )?.run {
                        iv.setImageBitmap(this)
                    }
                }
            }
        }
    }

    fun getPermission(callback: () -> Unit) {
        PermissionX.init(this)
            .permissions(Manifest.permission.READ_EXTERNAL_STORAGE)
            .request { allGranted, _, _ ->
                if (!allGranted) {
                    Toast.makeText(this@MainActivity, "请先给予存储权限～", Toast.LENGTH_SHORT).show()
                } else {
                    callback.invoke()
                }
            }
    }
}