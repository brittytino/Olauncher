package app.olauncher.ui

import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import app.olauncher.R
import app.olauncher.data.AppRestriction
import app.olauncher.data.AppRestrictionDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RestrictionConfigActivity : AppCompatActivity() {

    private lateinit var appName: String
    private lateinit var packageName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restriction_config)

        appName = intent.getStringExtra("appName") ?: ""
        packageName = intent.getStringExtra("packageName") ?: ""

        findViewById<TextView>(R.id.appName).text = appName

        val timeLimitSlider = findViewById<SeekBar>(R.id.timeLimitSlider)
        val timeLimitLabel = findViewById<TextView>(R.id.timeLimitLabel)
        timeLimitSlider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                timeLimitLabel.text = "$progress minutes"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        val cooldownSlider = findViewById<SeekBar>(R.id.cooldownSlider)
        val cooldownLabel = findViewById<TextView>(R.id.cooldownLabel)
        cooldownSlider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                cooldownLabel.text = "$progress minutes"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        findViewById<Button>(R.id.saveButton).setOnClickListener {
            val restriction = AppRestriction(
                packageName = packageName,
                appName = appName,
                allowedDays = getSelectedDays(),
                maxContinuousMinutes = timeLimitSlider.progress,
                cooldownMinutes = cooldownSlider.progress,
                isActive = findViewById<Switch>(R.id.restrictionSwitch).isChecked,
                createdAt = System.currentTimeMillis(),
                overrideCount = 0,
                lastOverrideAt = null
            )
            CoroutineScope(Dispatchers.IO).launch {
                AppRestrictionDatabase.getDatabase(applicationContext).appRestrictionDao().insert(restriction)
                finish()
            }
        }
    }

    private fun getSelectedDays(): List<Int> {
        val days = mutableListOf<Int>()
        if (findViewById<CheckBox>(R.id.monday).isChecked) days.add(1)
        if (findViewById<CheckBox>(R.id.tuesday).isChecked) days.add(2)
        if (findViewById<CheckBox>(R.id.wednesday).isChecked) days.add(3)
        if (findViewById<CheckBox>(R.id.thursday).isChecked) days.add(4)
        if (findViewById<CheckBox>(R.id.friday).isChecked) days.add(5)
        if (findViewById<CheckBox>(R.id.saturday).isChecked) days.add(6)
        if (findViewById<CheckBox>(R.id.sunday).isChecked) days.add(7)
        return days
    }
}
