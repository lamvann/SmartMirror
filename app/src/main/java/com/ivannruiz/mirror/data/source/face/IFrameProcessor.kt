package com.ivannruiz.mirror.data.source.face

import java.nio.ByteBuffer

interface IFrameProcessor {

    fun process(data: ByteBuffer, frameMetadata: FrameMetadata)

    fun stop()

}