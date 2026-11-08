package com.zekiye.launcher

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : Activity() {
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

        val sb = StringBuilder()
        val logFile = File(getExternalFilesDir(null), "zekiye-debug.txt")
        val ts = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        sb.append("[$ts] Zekiye başladı\n\n")

        // 1. İzin kontrolü
        val granted = checkSelfPermission("com.termux.permission.RUN_COMMAND") ==
                PackageManager.PERMISSION_GRANTED
        sb.append("RUN_COMMAND izni: ${if (granted) "✅" else "❌"}\n")

        // 2. Termux kurulu mu / görünüyor mu?
        val termuxInfo = try {
            packageManager.getPackageInfo("com.termux", 0)
            "✅ (${termuxInfo.versionName})"
        } catch (e: Exception) {
            "❌ GÖRÜNMÜYOR — <queries> eksik olabilir"
        }
        sb.append("Termux görünürlüğü: $termuxInfo\n")

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
            sb.append("Intent sonucu: ${if (component != null) "✅ ${component.className}" else "❌ NULL (service başlatılamadı)"}\n")
            Toast.makeText(this, "Gönderildi", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            sb.append("❌ HATA: ${e.message}\n")
            Toast.makeText(this, "Hata: ${e.message}", Toast.LENGTH_LONG).show()
        }

        sb.append("\nScript: $scriptPath")
        sb.append("\nLog: ${logFile.absolutePath}")

        // Dosyaya yaz
        logFile.writeText(sb.toString())

        // Ekranda da göster
        tv.text = sb.toString()

        // 5 saniye sonra kapan
        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
            finish()
        }, 5000)
    }
}
