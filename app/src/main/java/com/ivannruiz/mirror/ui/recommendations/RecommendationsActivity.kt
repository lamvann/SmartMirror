package com.ivannruiz.mirror.ui.recommendations


import android.os.Bundle
import android.util.Log
import com.ivannruiz.mirror.BaseActivity
import com.ivannruiz.mirror.R
import com.ivannruiz.mirror.utils.Constants
import com.pubnub.api.PubNub
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import java.util.*

class RecommendationsActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recommendations)
        hideTopBar()
    }
}