package com.ivannruiz.mirror.data.source.face

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class AbstractViewModel : ViewModel(), CoroutineScope {

    private val coroutineJob = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + coroutineJob

    override fun onCleared() {
        super.onCleared()

        coroutineJob.cancel()
    }
}