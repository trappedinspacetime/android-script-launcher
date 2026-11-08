package com.zekiye.launcher

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast

class MainActivity : Activity() {
    companion object {
        private const val TAG = "ZEKIYE_DEBUG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "=== onCreate başladı ===")

        try {
            // 1. İzin kontrolü
            val granted = checkSelfPermission("com.termux.permission.RUN_COMMAND") ==
                    PackageManager.PERMISSION_GRANTED
            Log.i(TAG, "RUN_COMMAND izni=$granted")

            // 2. Termux görünürlüğü
            val termuxVisible = try {
                packageManager.getPackageInfo("com.termux", 0)
                Log.i(TAG, "Termux görünür=✅")
                true
            } catch (e: Exception) {
                Log.e(TAG, "Termux görünmüyor", e)
                false
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

            Log.i(TAG, "startService çağrılıyor...")
            val component = startService(intent)
            Log.i(TAG, "startService sonucu=$component")
            
            Toast.makeText(this, "Sonuç: $component", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Log.e(TAG, "=== FATAL HATA ===", e)
            Toast.makeText(this, "Hata: ${e.message}", Toast.LENGTH_LONG).show()
        }

        // 2 saniye sonra kapan
        Handler(Looper.getMainLooper()).postDelayed({ finish() }, 2000)
    }
}
