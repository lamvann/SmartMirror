package com.ivannruiz.mirror.ui.facerecognition

import android.util.Base64
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
//import com.google.api.client.util.Base64
//import com.google.api.gax.core.FixedCredentialsProvider
//import com.google.auth.oauth2.AccessToken
//import com.google.auth.oauth2.ServiceAccountCredentials
//import com.google.cloud.automl.v1beta1.*
import com.google.gson.GsonBuilder
//import com.google.protobuf.ByteString
import com.ivannruiz.mirror.data.source.face.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayInputStream
import java.nio.charset.Charset
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.graphics.Matrix
import com.google.android.gms.common.util.IOUtils.toByteArray
import java.io.ByteArrayOutputStream


class FaceRecognitionViewModel : AbstractViewModel() {

    companion object {
        private const val REST_CLASSIFIER =
            true // flag to decide if we should use REST (true) or SDK (false) classifier.

        private const val PROJECT = "smart-mirror-8a2a6"
        private const val LOCATION = "us-central1"
        private const val MODEL = "ICN6634804269763061406"
        private const val SERVICE_ACCOUNT_JSON = "{\n" +
                "  \"type\": \"service_account\",\n" +
                "  \"project_id\": \"smart-mirror-8a2a6\",\n" +
                "  \"private_key_id\": \"5ab114c4a89cc37f1a5793d9e589e413a60d3666\",\n" +
                "  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCjPWgDSogRTR9+\\netPrPGRcpAuzxpETXu+7uOReAQ9YWAa0pd/x2Jf3JvpGmmiAmF0k/C1ZlT2ajhl8\\neHgC5hOlN5thGSUY7KGdR4q2HcdcmaAmmD+pNPSn2hpsfoLbRxgJXuTiCQEeeikh\\n9NS3x6xZF0PR+FdKRon/5UxFM/+UkTMPC1gcr+gBP1GxINHVAoQnfZcTA87jxD71\\n1vmg9ib8LEoAcdrtvXMAyLqqDuQS8InFkPobTnsB1skCNApq53jb20qztMfOziWW\\nOOn6JZOq+ae1AboZW9kUmsVLs1uycCW/2PWx4reX6j5O7504Z5dKhGPF6kub6uO0\\nfJ9wdufbAgMBAAECggEACpRya8VUJb5YUOvRVTa4Gevb8AUOeJ8TAAb8tz3dfOJ2\\ngoMZseSa2D7snfTV/xMX0KchNC1Odq82L18u4lbtHHXBvwW+U6LKkZVz31fQv5St\\n9USxtdvs7bSnyzLr4kKsnttuD6RU5vM0zmmZsrAqKZbltaqO+A1GDUiA0R9oEQ8C\\nqwtG1YxJ09Jv5DZnTzIzlSBO9pmqL0EkOhhGeBvhz9kpRs1ShDANhYz+1yr54S6v\\nl1gDhvtJJ2t8CLClWRkI0f0B8wEQmFbzPzx1UECzUzpMfEHT59pi8yIXShgFP95G\\nRzONEoWOCWFjV2wqFqJqHcwjNckZn4jwIecOpsf20QKBgQDcsHSo5man8nXxTx41\\nRTn+s1NzuBhHdkUHxRvxD1kQsk43+sbJTSGveYuSpEpqaNqPjPD5oIIUrSrCX6tJ\\n6kBpr/MNJ+XfLOA64AqxoMnH1mGleK/PC5RyGMS+4WSECQNgb+xKMwbDEVLmF55z\\ndwKnRPk9WsiwNcI+QseTEN1yMwKBgQC9W8svy5Ee64NM0PmOj2ZE5wCSioVm6arZ\\nAkrqPRO6fyxhC+0P71aMcJbf2A9EYpAxOEdRhFUTH+XBErcoSQbBXKZi9t0YpKSg\\nv9vS94o6tr+dWFOYjiCSAJGt9mzPa5CuxLqai/gEDaxRaI2BX7odHdEj1xKpTOFr\\nO7PlSQwbuQKBgQDMm0i9o0d/5FqXDIxsRS98xhPUENVpw+xCROwf9ePUiAve+ME+\\ntyVJBD50CZ/4whgIyVpNhhO0ScyAA6TSVb28fuWvx9LmtDt8OmWPxAvwHAHSIW+W\\nXR0XH4Ghm2TOyXB5A6umK2LUjgY2z6UXVjp+jIMr8DRqspzRjqZr4lH9YwKBgQCn\\nFaxATDuNMPr2eR77cAUpnzueEqSLnnumaKN59NLHqRebuk4/1UxlN6OpeJhgGyho\\nheRIRaBb87VjnAQJhAZ18C7Q/EszR3QMc76gdNR/4mlJiXqaDi8nJWFmQx7YF23f\\nCLAvVnpN+VKnsr3J/pHWx0yXd1t8D96IJHIkS0Rn4QKBgHDPXibu10wGgUVgYWXC\\nwUr3a8OhhN6osLSKGg5Bbe0GAW5Zwth4pmwzn9H+X4aDgLTGUUu8pzi001rNe7DP\\nUNKc7JMLL1ooQ+S4GkRkuNtQ73YzqSKKweZhNxSJ92i/N7z14kxcgzD85F39Gj9a\\nwDJA1ckFpVHtb8QC7/DV9TdF\\n-----END PRIVATE KEY-----\\n\",\n" +
                "  \"client_email\": \"auto-ml@smart-mirror-8a2a6.iam.gserviceaccount.com\",\n" +
                "  \"client_id\": \"109164674808518314436\",\n" +
                "  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n" +
                "  \"token_uri\": \"https://oauth2.googleapis.com/token\",\n" +
                "  \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n" +
                "  \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/auto-ml%40smart-mirror-8a2a6.iam.gserviceaccount.com\"\n" +
                "}";
    }

//    private val mServiceCredentials = ServiceAccountCredentials
//        .fromStream(ByteArrayInputStream(SERVICE_ACCOUNT_JSON.toByteArray(Charset.defaultCharset())))
//        .createScoped(mutableListOf("https://www.googleapis.com/auth/cloud-platform"))

    private val errorHandler = CoroutineExceptionHandler { _, throwable ->
        mResult.postValue(ErrorResource(throwable))
    }

    private val mResult = MutableLiveData<Resource<FaceClassification, Throwable>>()

    fun subscribeClassifications(): LiveData<Resource<FaceClassification, Throwable>> {
        return mResult
    }

//    private var accessToken: AccessToken? = null

//    init {
//        Thread {
//            accessToken = mServiceCredentials.accessToken
//            if (accessToken == null) {
//                accessToken = mServiceCredentials.refreshAccessToken()
//            }
//        }.start()
//    }

    fun classify(faceId: Int, imageBytes: ByteArray) {
        classifyUsingRetrofit(faceId, imageBytes)
    }

    private fun classifyUsingRetrofit(faceId: Int, imageBytes: ByteArray) {
        launch(errorHandler) {
            // Show loading indicator while we wait for the request.
            mResult.value = LoadingResource(null)

            /*// Build the body of our request, essentially the image to be classified.
            val body = CloudAutoMLModel(
                Payload(
                    MlImage(
                        String(
                            Base64.encodeBase64(imageBytes)
                        )
                    )
                )
            )

            // Define the authentication credentials and make the API request
            val response = getRESTService().classify(
                "Bearer ${accessToken?.tokenValue}",
                PROJECT, LOCATION, MODEL, body
            ).await()

            System.out.println("Response : " + response.payload?.size + " : " + response.payload?.firstOrNull()?.displayName)

            if (response.payload?.isNotEmpty() == true) {
                // We have a prediction!
                var predictedName: String? = null
                var predictedConfidence: Double? = null

                response.payload.forEach { entry ->
                    if (entry.displayName != null) {
                        predictedName = entry.displayName
                        predictedConfidence = entry.classification?.score
                    }
                }

                if (predictedName != null && predictedConfidence != null) {
                    // We had an actual name returned
                    mResult.postValue(
                        SuccessResource(
                            FaceClassification(
                                faceId,
                                predictedName!!,
                                predictedConfidence!!
                            )
                        )
                    )
                } else {
                    // No name was returned, this is an unknown face.
                    mResult.postValue(ErrorResource(null))
                }
            } else {
                // There were no payloads returned, possible error or unknown face.
                mResult.postValue(ErrorResource(null))
            }*/


//            val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
//
//            val matrix = Matrix()
//
//            matrix.postRotate(270F)
//
//            val scaledBitmap = Bitmap.createScaledBitmap(bitmap, 640, 480, true)
//
//            val rotatedBitmap =
//                Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.width, scaledBitmap.height, matrix, true)
//
////            val base64Image = String(Base64.encode(imageBytes, Base64.DEFAULT))
//
//            val stream = ByteArrayOutputStream()
//            rotatedBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
//            val byteArray = stream.toByteArray()
//            rotatedBitmap.recycle()
            val base64Image = String(Base64.encode(rotateSamsungImage(imageBytes), Base64.DEFAULT))

            System.out.println("base64Image : " + base64Image);

            val body = MachineBoxService.FaceDetectionRequest(base64Image);

            val response = getMachineBoxRestService().faceDetection(body).await();
            System.out.println("Response : " + response.faces?.size + " : " + response.faces?.firstOrNull()?.name)

            if (response.faces?.isNotEmpty() == true) {
                // We have a prediction!
                var predictedName: String? = null
                var predictedConfidence: Double? = null

                response.faces.forEach { entry ->
                    if (entry.name != null) {
                        predictedName = entry.name
                        predictedConfidence = entry?.confidence
                    }
                }

                if (predictedName != null && predictedConfidence != null) {
                    // We had an actual name returned
                    mResult.postValue(
                        SuccessResource(
                            FaceClassification(
                                faceId,
                                predictedName!!,
                                predictedConfidence!!
                            )
                        )
                    )
                } else {
                    // No name was returned, this is an unknown face.
                    mResult.postValue(ErrorResource(null))
                }
            } else {
                // There were no payloads returned, possible error or unknown face.
                mResult.postValue(ErrorResource(null))
            }

        }
    }

    private fun getMachineBoxRestService(): MachineBoxService {
        val gsonFactory = GsonConverterFactory
            .create(GsonBuilder().create())

        val networkClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

        return Retrofit.Builder()
            .baseUrl("http://192.168.43.71:8080")
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(gsonFactory)
            .client(networkClient)
            .build()
            .create(MachineBoxService::class.java)

    }

    private fun rotateSamsungImage(byteArray: ByteArray) : ByteArray {
        val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)

        val matrix = Matrix()

        matrix.postRotate(90F)

        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, 640, 480, true)

        val rotatedBitmap =
            Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.width, scaledBitmap.height, matrix, true)

//            val base64Image = String(Base64.encode(imageBytes, Base64.DEFAULT))

        val stream = ByteArrayOutputStream()
        rotatedBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val byteArrayRotated = stream.toByteArray()
        rotatedBitmap.recycle()
        return byteArrayRotated
    }

}