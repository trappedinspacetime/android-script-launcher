package com.zekiye.launcher

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val scriptPath = "/data/data/com.termux/files/home/.shortcuts/tasks/zekiye.sh"

        val intent = Intent().apply {
            setClassName(
                "com.termux",
                "com.termux.app.RunCommandService"
            )
            action = "com.termux.RUN_COMMAND"
            putExtra("com.termux.RUN_COMMAND_PATH", "/data/data/com.termux/files/usr/bin/bash")
            putExtra("com.termux.RUN_COMMAND_ARGUMENTS", arrayOf(scriptPath))
            putExtra("com.termux.RUN_COMMAND_WORKDIR", "/data/data/com.termux/files/home")
            putExtra("com.termux.RUN_COMMAND_BACKGROUND", true)
            putExtra("com.termux.RUN_COMMAND_SESSION_ACTION", "0")
        }

        try {
            startService(intent)
            Toast.makeText(this, "Zekiye çalışıyor ✨", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Hata: Termux bulunamadı", Toast.LENGTH_LONG).show()
        }

        // Kendini hemen kapat, sadece bir köprü görevi gördü
        finish()
    }
}
