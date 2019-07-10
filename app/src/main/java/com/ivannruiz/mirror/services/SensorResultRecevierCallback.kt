package com.ivannruiz.mirror.services

import java.lang.Exception

interface SensorResultRecevierCallback <T> {
    fun onSuccess(data: T)
    fun onError(exception: Exception)
}