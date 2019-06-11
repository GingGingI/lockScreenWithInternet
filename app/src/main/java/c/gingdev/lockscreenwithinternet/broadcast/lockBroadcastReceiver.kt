package c.gingdev.lockscreenwithinternet.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import c.gingdev.lockscreenwithinternet.lockScreen.testLockScreen

import c.gingdev.lockscreenwithinternet.lockScreenMode
import c.gingdev.lockscreenwithinternet.lockScreenPrefName
import java.text.SimpleDateFormat
import java.util.*

class lockBroadcastReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val pref = context?.getSharedPreferences(lockScreenPrefName, 0)
        val lockScreenMode = pref?.getBoolean(lockScreenMode, false) ?: false

        when(intent?.action) {
            Intent.ACTION_SCREEN_ON ->{
                Log.i("BroadcastReceiver", "On")
            }
            Intent.ACTION_SCREEN_OFF ->  if (lockScreenMode) {
            val hour = getHour()
            Log.i("BroadcastReceiver", "ON At $hour")

            val i = Intent(context, testLockScreen::class.java)
                .apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_ANIMATION)
                }
            context?.startActivity(i)
        }
            Intent.ACTION_BOOT_COMPLETED -> {
                Log.i("BroadcastReceiver", "BOOT")
            }
        }
    }

    private fun getHour(): Int =
        SimpleDateFormat("HH", Locale.KOREA).format(Date(System.currentTimeMillis())).toInt()
}