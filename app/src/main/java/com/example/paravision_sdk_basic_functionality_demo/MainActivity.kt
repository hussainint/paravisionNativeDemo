package com.example.paravision_sdk_basic_functionality_demo

import ai.paravision.sdk.android.ParavisionFaceSDK
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
        fun onClickBtn()
        {
            Toast.makeText(this,"Clicked on Button",Toast.LENGTH_LONG).show();
            Log.d("sdk_test", "About to build SDK")
            val sdk = ParavisionFaceSDK.Builder(applicationContext)
                .faceDetectionOptions(arrayOf(
                    ParavisionFaceSDK.FaceDetectionOption.BOUNDING_BOX,
                    ParavisionFaceSDK.FaceDetectionOption.QUALITY,
                    ParavisionFaceSDK.FaceDetectionOption.LANDMARKS,
                    ParavisionFaceSDK.FaceDetectionOption.EMBEDDING
                ))
                .sdkMode(ParavisionFaceSDK.SDKMode.NORMAL)
                .build()
            Log.d("sdk_test", "Finished building SDK")

            // Process a full image
            val thomasBitmap = BitmapFactory.decodeResource(resources, R.drawable.thomas)
            val thomasResult = sdk.processFullImage(thomasBitmap, arrayOf(
                ParavisionFaceSDK.FaceDetectionOption.BOUNDING_BOX,
                ParavisionFaceSDK.FaceDetectionOption.QUALITY,
                ParavisionFaceSDK.FaceDetectionOption.LANDMARKS,
                ParavisionFaceSDK.FaceDetectionOption.EMBEDDING
            ), true)
            Log.d("sdk_test_thomas_result", thomasResult.toString())

            // Get bounding boxes from an image
            val thomasBoundingBoxResult = sdk.getBoundingBoxes(thomasBitmap)
            Log.d("sdk_test_getBoundingBoxes", thomasBoundingBoxResult.toString())

            // Get landmarks from an image
            val thomasLandmarksResult = sdk.getLandmarks(thomasBitmap)
            Log.d("sdk_test_getLandmarks", thomasLandmarksResult.toString())

            // Get embedding out of an image
            val thomasGetEmbeddingResult = sdk.getEmbeddings(thomasBitmap)
            Log.d("sdk_test_getEmbedding", thomasGetEmbeddingResult.toString())

            // Taking two images of the same person "Jourdan" for comparison
            val jourdan1Bitmap = BitmapFactory.decodeResource(resources, R.drawable.jourdan1)
            val jourdan1Result = sdk.processFullImage(jourdan1Bitmap, arrayOf(
                ParavisionFaceSDK.FaceDetectionOption.BOUNDING_BOX,
                ParavisionFaceSDK.FaceDetectionOption.QUALITY,
                ParavisionFaceSDK.FaceDetectionOption.LANDMARKS,
                ParavisionFaceSDK.FaceDetectionOption.EMBEDDING
            ), true)

            val jourdan2Bitmap = BitmapFactory.decodeResource(resources, R.drawable.jourdan2)
            val jourdan2Result = sdk.processFullImage(jourdan2Bitmap, arrayOf(
                ParavisionFaceSDK.FaceDetectionOption.BOUNDING_BOX,
                ParavisionFaceSDK.FaceDetectionOption.QUALITY,
                ParavisionFaceSDK.FaceDetectionOption.LANDMARKS,
                ParavisionFaceSDK.FaceDetectionOption.EMBEDDING
            ), true)

            val jourdanEmbedding = jourdan1Result?.mostProminentFaceIdx?.let { jourdan1Result?.faces?.get(it)?.embedding }
            val jourdanEmbedding2 = jourdan2Result?.mostProminentFaceIdx?.let { jourdan2Result?.faces?.get(it)?.embedding }

            // Take both "Jourdan" embeddings and get the similarity score, a value between 1 and 4
            // A value of 2.789293418934410 and above will have a robust 1 in 100,000 False Match Rate
            val jourdanSimilarity = jourdanEmbedding?.let { jourdanEmbedding2?.let { it1 ->
                sdk.getSimilarityScore(it,
                    it1
                )
            } }
            Log.d("sdk_test_jourdan_test_similarity_result", jourdanSimilarity.toString())

            // Thomas vs. Jourdan similarity check
            val thomasEmbedding = thomasGetEmbeddingResult?.first()?.let { thomasGetEmbeddingResult?.first() }
            val jourdanThomasSimilarity = jourdanEmbedding?.let { thomasEmbedding?.let { it1 ->
                sdk.getSimilarityScore(it,
                    it1
                )
            } }
            Log.d("sdk_test_jourdan_thomas_test_similarity_result", jourdanThomasSimilarity.toString())

        }
}