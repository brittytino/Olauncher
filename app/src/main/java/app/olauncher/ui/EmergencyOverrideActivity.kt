package app.olauncher.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import app.olauncher.R
import app.olauncher.data.AppRestrictionDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EmergencyOverrideActivity : AppCompatActivity() {

    private var count = 0
    private val targetText = "i'm a loser"
    private val targetCount = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emergency_override)

        val typingInput = findViewById<EditText>(R.id.typingInput)
        val countDown = findViewById<TextView>(R.id.countDown)
        val packageName = intent.getStringExtra("packageName")

        typingInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().endsWith(targetText)) {
                    count++
                    countDown.text = "$count/$targetCount"
                    s?.clear()
                    if (count >= targetCount) {
                        if (packageName != null) {
                            unlockApp(packageName)
                        }
                        finish()
                    }
                }
            }
        })
    }

    private fun unlockApp(packageName: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val dao = AppRestrictionDatabase.getDatabase(applicationContext).appRestrictionDao()
            dao.getRestriction(packageName)?.let {
                it.lastOverrideAt = System.currentTimeMillis()
                dao.update(it)
            }
        }
    }
}
