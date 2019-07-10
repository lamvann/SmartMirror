package com.ivannruiz.mirror.data.source.face


import kotlinx.coroutines.Deferred
import retrofit2.http.Body
import retrofit2.http.POST

interface MachineBoxService {

    @POST("/facebox/check")
    fun faceDetection(
        @Body body: FaceDetectionRequest
    ): Deferred<FaceDetectionResponse>

    //data class Score(val score: Double)
    data class FaceDetectionRequest(val base64: String)

    data class FaceMatch(val id: String?, val name: String?, val matched: Boolean?, val confidence: Double)
    data class FaceDetectionResponse(val success: Boolean, val facesCount: Int?, val faces: List<FaceMatch>?)
}