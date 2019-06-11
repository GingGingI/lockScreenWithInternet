package c.gingdev.lockscreenwithinternet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CompoundButton
import android.widget.Toast
import c.gingdev.lockscreenwithinternet.service.lockScreenService
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity()
    , CompoundButton.OnCheckedChangeListener {

    private val pref by lazy(LazyThreadSafetyMode.NONE)
    { getSharedPreferences(lockScreenPrefName, 0) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startService()
        setSwitch()
    }

    private fun startService() {
        val i = Intent(applicationContext, lockScreenService::class.java)
        startService(i)
    }

    private fun setSwitch() {
        lockScreenSwitch.isChecked = pref.getBoolean(lockScreenMode, false)
        lockScreenSwitch.setOnCheckedChangeListener(this)
    }

    //    Switch Listener
    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        when(buttonView) {
            lockScreenSwitch -> {
                pref.edit().apply {
                    putBoolean(lockScreenMode, isChecked)
                }.run {
                    apply()
                    Toast.makeText(this@MainActivity, "Lock Scrrend Enabled = $isChecked", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}
