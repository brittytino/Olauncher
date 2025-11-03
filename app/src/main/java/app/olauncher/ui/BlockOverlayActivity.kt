package app.olauncher.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import app.olauncher.R

class BlockOverlayActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_block_overlay)

        val appName = intent.getStringExtra("appName")
        val packageName = intent.getStringExtra("packageName")
        val reason = intent.getStringExtra("reason")

        val appIcon = findViewById<ImageView>(R.id.appIcon)
        val appNameTextView = findViewById<TextView>(R.id.appName)
        val blockReasonTextView = findViewById<TextView>(R.id.blockReason)
        val overrideButton = findViewById<Button>(R.id.overrideButton)

        try {
            val icon = packageManager.getApplicationIcon(packageName)
            appIcon.setImageDrawable(icon)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        appNameTextView.text = appName
        blockReasonTextView.text = reason

        overrideButton.setOnClickListener {
            val intent = Intent(this, EmergencyOverrideActivity::class.java)
            intent.putExtra("packageName", packageName)
            startActivity(intent)
            finish()
        }
    }
}
