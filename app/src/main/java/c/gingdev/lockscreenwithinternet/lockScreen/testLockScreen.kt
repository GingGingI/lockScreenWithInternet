package c.gingdev.lockscreenwithinternet.lockScreen

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import c.gingdev.lockscreenwithinternet.R
import c.gingdev.lockscreenwithinternet.data.BusData
import c.gingdev.lockscreenwithinternet.retrofit.retrofits
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.lock_screen.*
import retrofit2.Call
import retrofit2.Response
import java.util.concurrent.TimeUnit

class testLockScreen: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        flagInit()
        setContentView(R.layout.lock_screen)
        setTextFromInternet()
    }

    private fun setTextFromInternet() {
        Log.e("text", "test")
        BusObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                TestTitle.text = it.data.time1 + " | " + it.data.station1
            }, {
                it.printStackTrace()
            })
    }

    override fun onResume() {
        super.onResume()
        Log.e("lifeCycle", "resume")
//        ThreadOn
    }

    override fun onPause() {
        super.onPause()
        Log.e("lifeCycle", "pause")
//        ThreadOff
    }

    @SuppressLint("NewApi")
    private fun flagInit() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.run {
            decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                    View.SYSTEM_UI_FLAG_FULLSCREEN)

            setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) setShowWhenLocked(true)
            else addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED)
        }
    }

    private fun Any?.notNull(f: ()-> Unit) {
        if (this != null) f()
    }
    private fun Any?.isNull(f: ()-> Unit) {
        if (this == null) f()
    }

    private val BusObservable =
        Observable.interval(0, 10, TimeUnit.SECONDS)
            .flatMap {
                return@flatMap Observable.create<BusData> { subscriber ->
                    retrofits.getInstance()
                        .getRetrofitService()
                        .getDatas()
                        .enqueue(object : retrofit2.Callback<BusData> {
                            override fun onFailure(call: Call<BusData>, t: Throwable) {
                                subscriber.onError(t)
                            }

                            override fun onResponse(call: Call<BusData>, response: Response<BusData>) {
                                response.body().notNull {
                                    Log.i("data", "${response.body()!!.data.time1}")
                                    subscriber.onNext(response.body()!!)
                                }
                            }
                        })
                }.subscribeOn(Schedulers.io())
            }
}