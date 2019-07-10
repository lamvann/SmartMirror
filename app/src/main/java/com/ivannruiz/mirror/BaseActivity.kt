package com.ivannruiz.mirror

import android.content.Intent
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.ivannruiz.mirror.ui.facerecognition.FaceRecognitionActivity
import com.ivannruiz.mirror.ui.lastorder.LastOrderActivity
import com.ivannruiz.mirror.ui.recommendations.RecommendationsActivity
import com.ivannruiz.mirror.ui.reservations.ReservationsActivity
import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import java.util.*
import com.ivannruiz.mirror.ui.voyage.VoyageActivity
import com.ivannruiz.mirror.utils.Constants
import com.ivannruiz.mirror.utils.Constants.MIN_DISTANCE
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult

abstract class BaseActivity : AppCompatActivity() {
    val pnConfiguration = PNConfiguration().apply {
        subscribeKey = "sub-c-395f5f16-6d49-11e9-b50c-aee189a56456"
        publishKey = "pub-c-0470fe00-110d-4da6-856c-db50fabca594"
        isSecure = false
    }
    val pubnub = PubNub(pnConfiguration)
    var sensorFlag = true

    override fun onStop() {
        super.onStop()
        pubnub.unsubscribe()
            .channels(Arrays.asList("awesomeChannel")) // unsubscribe from channels
            .execute()
    }

    fun goToVoyage() {
        Intent(this, VoyageActivity::class.java).also { startActivity(it) }
    }


    fun hideTopBar() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        supportActionBar?.hide()
    }

    fun <T : ViewModel> FragmentActivity.obtainViewModel(viewModelClass: Class<T>) =
        ViewModelProviders.of(this, ViewModelFactory.getInstance(application as MirrorApplication)).get(viewModelClass)

    override fun onResume() {
        super.onResume()
        sensorFlag = true
        val subscribeCallback = object : SubscribeCallback() {
            override fun status(pubnub: PubNub, status: PNStatus) {
            }

            override fun message(pubnub: PubNub, message: PNMessageResult) {
                if(!sensorFlag) return
//                Log.e("sensor", message.message.toString())
                val proximity = message.message.asFloat
                if (proximity < MIN_DISTANCE && this@BaseActivity is VoyageActivity) {
                    Log.e("sensor", message.message.toString())
                    sensorFlag = false
                    goToFaceRecog()
                } else if (proximity > MIN_DISTANCE && this@BaseActivity !is VoyageActivity) {
                    Log.e("sensor", message.message.toString())
                    sensorFlag = false
                    Thread.sleep(2000)
                    goToVoyage()
                }
            }

            override fun presence(pubnub: PubNub, presence: PNPresenceEventResult) {
            }
        }

        pubnub.addListener(subscribeCallback)
        pubnub.subscribe()
            .channels(Arrays.asList("awesomeChannel")) // subscribe to channels
            .execute()
    }

    private fun goToFaceRecog() =
        Intent(this, FaceRecognitionActivity::class.java).also {
            startActivity(it)
        }

}