package com.c23ps008.opet.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import com.c23ps008.opet.ml.PetImageAnalysis
import com.google.gson.Gson
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import retrofit2.HttpException
import java.lang.Integer.min
import java.nio.ByteBuffer
import java.nio.ByteOrder


data class ErrorResponse(
    val error: Boolean,
    val message: String?,
)

val classLabels = listOf(
    "Abyssinian",
    "american_bulldog",
    "american_pit_bull_terrier",
    "basset_hound",
    "beagle",
    "Bengal",
    "Birman",
    "Bombay",
    "boxer",
    "British_Shorthair",
    "chihuahua",
    "Egyptian_Mau",
    "english_cocker_spaniel",
    "english_setter",
    "german_shorthaired",
    "great_pyrenees",
    "havanese",
    "japanese_chin",
    "keeshond",
    "leonberger",
    "Maine_Coon",
    "miniature_pinscher",
    "newfoundland",
    "Persian",
    "pomeranian",
    "pug",
    "Ragdoll",
    "Russian_Blue",
    "saint_bernard",
    "samoyed",
    "scottish_terrier",
    "shiba_inu",
    "Siamese",
    "Sphynx",
    "staffordshire_bull_terrier",
    "wheaten_terrier",
    "yorkshire_terrier"
)

fun createErrorResponse(e: HttpException): ErrorResponse {
    val errorJSONString = e.response()?.errorBody()?.string()
    return Gson().fromJson(errorJSONString, ErrorResponse::class.java)
}

fun cropOneToOneRatio(context: Context, uri: Uri): Bitmap? {
    val originalBitmap = BitmapFactory.decodeStream(context.contentResolver.openInputStream(uri))

    val width = originalBitmap.width
    val height = originalBitmap.height

    // Calculate the crop square dimensions
    val cropSize = min(width, height)
    val xOffset = (width - cropSize) / 2
    val yOffset = (height - cropSize) / 2

    // Create the cropped bitmap

    return Bitmap.createBitmap(
        originalBitmap,
        xOffset,
        yOffset,
        cropSize,
        cropSize
    )
}

fun classifyPetImage(context: Context, bitmap: Bitmap, inputSize: Int): String {
    val resizedBitmap = Bitmap.createScaledBitmap(bitmap, inputSize, inputSize, true)

    val model = PetImageAnalysis.newInstance(context)

    // Creates inputs for reference.
    val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 299, 299, 3), DataType.FLOAT32)
    val byteBuffer = ByteBuffer.allocateDirect(4 * inputSize * inputSize * 3)
    byteBuffer.order(ByteOrder.nativeOrder())

    val intValues = IntArray(inputSize * inputSize)
    resizedBitmap.getPixels(
        intValues,
        0,
        resizedBitmap.width,
        0,
        0,
        resizedBitmap.width,
        resizedBitmap.height
    )

    var pixel = 0
    for (i in 0 until inputSize) {
        for (j in 0 until inputSize) {
            val value = intValues[pixel++]
            byteBuffer.putFloat(((value shr 16 and 0xFF) * (1f / 1)))
            byteBuffer.putFloat(((value shr 8 and 0xFF) * (1f / 1)))
            byteBuffer.putFloat(((value and 0xFF) * (1f / 1)))
        }
    }

    inputFeature0.loadBuffer(byteBuffer)

    // Runs model inference and gets result.
    val outputs = model.process(inputFeature0)
    val outputFeature0 = outputs.outputFeature0AsTensorBuffer

    val confidences = outputFeature0.floatArray

    var maxPos = 0
    var maxConfidences = 0f
    for (i in confidences.indices) {
        if (confidences[i] > maxConfidences) {
            maxConfidences = confidences[i]
            maxPos = i
        }
    }

    val result = classLabels[maxPos]
    Log.d("TESTS", "classifyPetImage: $result with confidences: $maxConfidences")

    return result

    // Releases model resources if no longer used.
    @Suppress("UNREACHABLE_CODE")
    model.close()
}