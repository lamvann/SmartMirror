package com.ivannruiz.mirror.services
//
//import android.app.Application
//import android.content.Intent
//import android.util.Log
//import androidx.localbroadcastmanager.content.LocalBroadcastManager
//import com.pubnub.api.PNConfiguration
//import com.pubnub.api.PubNub
//import com.pubnub.api.callbacks.SubscribeCallback
//import com.pubnub.api.models.consumer.PNStatus
//import com.pubnub.api.models.consumer.pubsub.PNMessageResult
//import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
//import java.util.*
//
//@Singleton
//class SensorService(val application: Application, val pubnub: PubNub) {
//    fun enableSensor() {
//
//        val subscribeCallback = object : SubscribeCallback() {
//            override fun status(pubnub: PubNub, status: PNStatus) {
//
//            }
//
//            override fun message(pubnub: PubNub, message: PNMessageResult) {
//                Log.e("sensor", message.message.toString())
//                val newIntent = Intent("distance").apply {
//                    putExtra("distance", message.message.toString())
//                }
//                LocalBroadcastManager.getInstance(application.applicationContext).sendBroadcast(newIntent)
//
//            }
//
//            override fun presence(pubnub: PubNub, presence: PNPresenceEventResult) {
//
//            }
//        }
//
//        pubnub.addListener(subscribeCallback)
//        pubnub.subscribe()
//            .channels(Arrays.asList("awesomeChannel")) // subscribe to channels
//            .execute()
//    }
//
//    companion object {
//
//        private var INSTANCE: SensorService? = null
//
//        @JvmStatic
//        fun getInstance(application: Application, pubnub: PubNub) =
//            INSTANCE ?: synchronized(SensorService::class.java) {
//                INSTANCE
//                    ?: SensorService(application, pubnub)
//                        .also { INSTANCE = it }
//            }
//        /**
//         * Used to force [getInstance] to create a new instance
//         * next time it's called.
//         */
//        @JvmStatic
//        fun destroyInstance() {
//            INSTANCE = null
//        }
//    }
//}
