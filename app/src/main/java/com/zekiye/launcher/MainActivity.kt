package com.zekiye.launcher

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {
    companion object {
        private const val TAG = "ZEKIYE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val tv = TextView(this).apply {
            textSize = 16f
            setPadding(48, 96, 48, 96)
        }
        setContentView(LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            addView(tv)
        })

        val sb = StringBuilder("Zekiye debug\n\n")
        Log.d(TAG, "onCreate başladı")

        // 1. İzin kontrolü
        val granted = checkSelfPermission("com.termux.permission.RUN_COMMAND") ==
                PackageManager.PERMISSION_GRANTED
        sb.append("RUN_COMMAND izni: ${if (granted) "✅" else "❌"}\n")
        Log.d(TAG, "izin=$granted")

        // 2. Termux kurulu mu?
        val termuxInstalled = try {
            packageManager.getPackageInfo("com.termux", 0); true
        } catch (e: Exception) { false }
        sb.append("Termux kurulu: ${if (termuxInstalled) "✅" else "❌"}\n")

        if (!termuxInstalled) {
            sb.append("\n⚠ Termux bulunamadı!")
            tv.text = sb.toString()
            Handler(Looper.getMainLooper()).postDelayed({ finish() }, 5000)
            return
        }

        // 3. Intent gönder
        val scriptPath = "/data/data/com.termux/files/home/.shortcuts/tasks/zekiye.sh"
        val intent = Intent().apply {
            setClassName("com.termux", "com.termux.app.RunCommandService")
            action = "com.termux.RUN_COMMAND"
            putExtra("com.termux.RUN_COMMAND_PATH", "/data/data/com.termux/files/usr/bin/bash")
            putExtra("com.termux.RUN_COMMAND_ARGUMENTS", arrayOf(scriptPath))
            putExtra("com.termux.RUN_COMMAND_WORKDIR", "/data/data/com.termux/files/home")
            putExtra("com.termux.RUN_COMMAND_BACKGROUND", true)
            putExtra("com.termux.RUN_COMMAND_SESSION_ACTION", "0")
        }

        try {
            val component = startService(intent)
            Log.d(TAG, "startService=$component")
            sb.append("Intent: ${if (component != null) "✅ gönderildi" else "❌ null döndü"}\n")
            sb.append("\nScript: $scriptPath")
            sb.append("\n\nYeniden yükleyip Termux'u aç, logcat'i kontrol et.")
            Toast.makeText(this, "Komut gönderildi", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Log.e(TAG, "hata", e)
            sb.append("\n❌ HATA: ${e.message}")
            Toast.makeText(this, "Hata: ${e.message}", Toast.LENGTH_LONG).show()
        }

        tv.text = sb.toString()
        Handler(Looper.getMainLooper()).postDelayed({ finish() }, 4000)
    }
}
