package com.ivannruiz.mirror.ui.facerecognition

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import com.google.firebase.ml.vision.face.FirebaseVisionFace
import com.ivannruiz.mirror.data.source.face.*
import com.ivannruiz.mirror.ui.CezarActivity
import com.ivannruiz.mirror.ui.lastorder.LastOrderActivity
import com.ivannruiz.mirror.ui.recommendations.RecommendationsActivity
import com.ivannruiz.mirror.ui.reservations.ReservationsActivity
import com.ivannruiz.mirror.utils.Constants
import com.ivannruiz.mirror.utils.convertToByteArray
import com.pubnub.api.PubNub
import com.pubnub.api.callbacks.SubscribeCallback
import com.pubnub.api.models.consumer.PNStatus
import com.pubnub.api.models.consumer.pubsub.PNMessageResult
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult
import java.io.IOException
import java.nio.ByteBuffer
import java.util.*


class FaceRecognitionActivity : AbstractActivity() {

    fun obtainViewModel() = obtainViewModel(FaceRecognitionViewModel::class.java)

    private var mCameraSource: MLCameraSource? = null
    private var mLastUserName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideTopBar()
        setObservers()
    }

    private fun setObservers() {
        obtainViewModel().subscribeClassifications()
            .observe(this, Observer<Resource<FaceClassification, Throwable>> { resource ->
                when (resource) {
                    is LoadingResource -> {
                        System.out.println("Classifying...")
                    }
                    is SuccessResource -> {
                        System.out.println("SuccessResource : " + resource.data)
                        val faceId = resource.data.faceId
                        val name = resource.data.name
                        val score = resource.data.confidence
                        Log.e("Face Recognition" , "Name = $name")
                        if(mLastUserName != name) {
                            mLastUserName = name
                            when {
                                name == "David" && score > 0.65 -> goToRecommendationsActivity()
                                name =="Alessandra" && score > 0.65  -> goToReservationsActivity()
                                name =="Ivann"  && score > 0.65 -> goToCheckoutActivity()
                                name =="Allexey"  && score > 0.65 -> goToCheckoutActivity()
                                name =="Cezar"  && score > 0.6 -> cezarActivity()
//                                name =="Otro"  && score > 0.6 -> goToCheckoutActivity()
//                                name =="Otro2"  && score > 0.6 -> goToCheckoutActivity()
//                                name =="Otro3"  && score > 0.6 -> goToCheckoutActivity()
                            }
                        }
                    }
                    is ErrorResource -> {
                        System.out.println("ErrorResource : " + resource.data)
                        resource.errorData?.printStackTrace()
                    }
                }
            })
    }

    private fun goToRecommendationsActivity(){
        Intent(this, RecommendationsActivity::class.java).also { startActivity(it) }
        finish()
    }

    private fun goToReservationsActivity(){
        Intent(this, ReservationsActivity::class.java).also { startActivity(it) }
        finish()
    }

    private fun goToCheckoutActivity(){
        Intent(this, LastOrderActivity::class.java).also { startActivity(it) }
        finish()
    }

    private fun cezarActivity(){
        Intent(this, CezarActivity::class.java).also { startActivity(it) }
        finish()
    }


    /**
     * Creates and starts the camera.
     */
    override fun createCameraSource() {
        mCameraSource = MLCameraSource(this, mGraphicOverlay)
        mCameraSource!!.setMachineLearningFrameProcessor(FaceDetector(object : FaceDetector.DetectorCallback {
            override fun onSuccess(
                frameData: ByteBuffer,
                results: List<FirebaseVisionFace>,
                frameMetadata: FrameMetadata
            ) {
                if (results.isEmpty()) {
                    // No faces in frame, so clear frame of any previous faces.
                    mGraphicOverlay.clear()
                } else {
                    // We have faces
                    results.forEach { face ->
                        val existingFace = mGraphicOverlay.find(face.trackingId) as FaceGraphic?
                        if (existingFace == null) {
                            // A new face has been detected.
                            val faceGraphic = FaceGraphic(face.trackingId, mGraphicOverlay)
                            mGraphicOverlay.add(faceGraphic)

                            // Lets try and find out who this face belongs to
                            obtainViewModel().classify(face.trackingId, frameData.convertToByteArray(frameMetadata))
                        } else {
                            // We have an existing face, update its position in the frame.
                            existingFace.updateFace(face)
                        }
                    }

                    mGraphicOverlay.postInvalidate()

                }
            }

            override fun onFailure(exception: Exception) {
                exception.printStackTrace()
            }
        }))
    }

    /**
     * Starts or restarts the camera source, if it exists.
     */
    override fun startCameraSource() {
        checkGooglePlayServices()

        if (mCameraSource != null) {
            try {
                mCameraPreview.start(mCameraSource!!, mGraphicOverlay)
            } catch (e: IOException) {
                Log.e("FaceRecognition", "Unable to start camera source.", e)
                mCameraSource!!.release()
                mCameraSource = null
            }
        }
    }

    /**
     * Releases the resources associated with the camera source.
     */
    override fun releaseCameraSource() {
        if (mCameraSource != null) {
            mCameraSource!!.release()
            mCameraSource = null
        }
    }
}