package com.ivannruiz.mirror.ui.voyage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.ivannruiz.mirror.BaseActivity
import com.ivannruiz.mirror.R
import com.ivannruiz.mirror.ui.facerecognition.FaceRecognitionActivity
import com.pubnub.api.PubNub
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import java.util.*

class VoyageActivity : BaseActivity() {
    fun obtainViewModel() = obtainViewModel(VoyageViewModel::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voyage)
        obtainViewModel()
        hideTopBar()
    }
}