package com.keyejxptwn.diffpatch

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sample_text.text = BuildConfig.VERSION_NAME
    }

    //增量更新点击
    fun update(view: View) {
        val newFile = File(getExternalFilesDir("apk"), "app.apk")
        val patchFile = File(getExternalFilesDir("apk"), "patch.apk")
        val result: Int = BsPatchUtils.patch(
            applicationInfo.sourceDir, newFile.getAbsolutePath(), patchFile.getAbsolutePath()
        )
        if (result == 0) install(newFile)

    }

    //安装
    private fun install(file: File) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val apkUri: Uri = FileProvider.getUriForFile(this, "$packageName.fileprovider", file)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive")
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive")
        }
        startActivity(intent)
    }


}
