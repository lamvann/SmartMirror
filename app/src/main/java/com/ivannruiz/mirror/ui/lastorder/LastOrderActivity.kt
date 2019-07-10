package com.ivannruiz.mirror.ui.lastorder

import android.os.Bundle
import android.util.Log
import android.view.View
import com.ivannruiz.mirror.BaseActivity
import com.ivannruiz.mirror.R
import com.ivannruiz.mirror.utils.Constants
import com.pubnub.api.PubNub
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import kotlinx.android.synthetic.main.activity_last_order.*
import java.util.*

class LastOrderActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_last_order)
        hideTopBar()
        Thread(Runnable { confirmation() }).start()
    }

    private fun confirmation() {
        Thread.sleep(4500)
        runOnUiThread {
            confirmation.visibility = View.VISIBLE
            payments.visibility = View.GONE
        }
    }
}