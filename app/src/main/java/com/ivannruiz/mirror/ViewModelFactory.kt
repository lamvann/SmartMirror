package com.ivannruiz.mirror

import android.annotation.SuppressLint
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ivannruiz.mirror.ui.facerecognition.FaceRecognitionViewModel
import com.ivannruiz.mirror.ui.voyage.VoyageViewModel

class ViewModelFactory private constructor(
    private val application: MirrorApplication
    // include repositories
) : ViewModelProvider.NewInstanceFactory() {
    //
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(VoyageViewModel::class.java) ->
                    VoyageViewModel(application)
                isAssignableFrom(FaceRecognitionViewModel::class.java) ->
                    FaceRecognitionViewModel()
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T

    companion object {

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(application: MirrorApplication) =
            INSTANCE ?: synchronized(ViewModelFactory::class.java) {
                INSTANCE ?: ViewModelFactory(
                    application
                )
                    .also { INSTANCE = it }
            }

//        fun providePromoRepository(context: Context): PromoRepository {
//            return PromoRepository
//                .getInstance(PromoRemoteDataSource(context, PromoFirebaseController(), PromoRetrofitController()))
//        }

        @VisibleForTesting
        fun destroyInstance() {
            INSTANCE = null
        }
    }
}