package com.c23ps008.opet.utils

import android.content.ContentResolver
import android.content.Context
import android.content.IntentSender
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.location.Geocoder
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.core.graphics.drawable.toBitmap
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import coil.size.Scale
import com.c23ps008.opet.ml.PetImageAnalysis
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.gson.Gson
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import retrofit2.HttpException
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.lang.Integer.min
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.Locale


data class ErrorResponse(
    val error: Boolean,
    val message: String?,
)

val classLabels = listOf(
    "Abyssinian",
    "American Bulldog",
    "American Pit Bull Terrier",
    "Basset Hound",
    "Beagle",
    "Bengal",
    "Birman",
    "Bombay",
    "Boxer",
    "British Shorthair",
    "Chihuahua",
    "Egyptian Mau",
    "English Cocker Spaniel",
    "English Setter",
    "German Shorthaired",
    "Great Pyrenees",
    "Havanese",
    "Japanese Chin",
    "Keeshond",
    "Leonberger",
    "Maine Coon",
    "Miniature Pinscher",
    "Newfoundland",
    "Persian",
    "Pomeranian",
    "Pug",
    "Ragdoll",
    "Russian Blue",
    "Saint Bernard",
    "Samoyed",
    "Scottish Terrier",
    "Shiba Inu",
    "Siamese",
    "Sphynx",
    "Staffordshire Bull Terrier",
    "Wheaten Terrier",
    "Yorkshire Terrier"
)

suspend fun loadIcon(
    context: Context,
    url: String?,
    width: Int,
    height: Int,
): BitmapDescriptor? {
    return try {
        val loader = ImageLoader(context)
        val req = ImageRequest.Builder(context).data(url).size(width, height).scale(Scale.FILL).allowHardware(false).build()

        val result = (loader.execute(req) as SuccessResult).drawable
        val bitmap = result.toBitmap()

        val output = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)

        val paint = Paint().apply {
            isAntiAlias = true
            color = Color.RED
        }

        val radius = bitmap.width.coerceAtMost(bitmap.height) / 2f
        val centerX = bitmap.width / 2f
        val centerY = bitmap.height / 2f

        canvas.drawCircle(centerX, centerY, radius, paint)

        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)

        BitmapDescriptorFactory.fromBitmap(output)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun createErrorResponse(e: HttpException): ErrorResponse {
    val errorJSONString = e.response()?.errorBody()?.string()
    return Gson().fromJson(errorJSONString, ErrorResponse::class.java)
}

fun uriToFile(selectedImg: Uri, context: Context): File {
    val contentResolver: ContentResolver = context.contentResolver
    val myFile = createCustomTempFile(context)

    val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
    val outputStream: OutputStream = FileOutputStream(myFile)
    val buf = ByteArray(1024)
    var len: Int
    while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
    outputStream.close()
    inputStream.close()
    return myFile
}

fun createCustomTempFile(context: Context): File {
    val timestamp = System.currentTimeMillis()
    val imageFileName = "OPet_IMG_$timestamp.jpg"
    val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(imageFileName, ".jpg", storageDir)
}

@Suppress("DEPRECATION")
fun getAddressName(context: Context, lat: Double, lon: Double): String {
    val geocoder = Geocoder(context, Locale.getDefault())
    try {
        val list = geocoder.getFromLocation(lat, lon, 1)
        if (list != null && list.size != 0) {
            return list[0].getAddressLine(0)
        }
    } catch (e: Exception) {
        Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
    }
    return ""
}

@Suppress("DEPRECATION")
fun getCityName(context: Context, lat: Double, lon: Double): String {
    val geocoder = Geocoder(context, Locale.getDefault())
    try {
        val list = geocoder.getFromLocation(lat, lon, 1)
        if (list != null && list.size != 0) {
            val address = list[0]
            return address.adminArea
        }
    } catch (e: Exception) {
        Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
    }
    return ""
}

fun checkLocationSetting(
    context: Context,
    onDisabled: (IntentSenderRequest) -> Unit,
    onEnabled: () -> Unit,
) {
    val locationRequest =
        LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000).build()
    val client: SettingsClient = LocationServices.getSettingsClient(context)
    val builder: LocationSettingsRequest.Builder =
        LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
    val gpsSettingTask =
        client.checkLocationSettings(builder.build())
    gpsSettingTask.addOnSuccessListener {
        onEnabled()
    }
    gpsSettingTask.addOnFailureListener { exception ->
        if (exception is ResolvableApiException) {
            try {
                val intentSenderRequest = IntentSenderRequest.Builder(exception.resolution).build()
                onDisabled(intentSenderRequest)
            } catch (sendEx: IntentSender.SendIntentException) {
                Toast.makeText(context, "Error: ${sendEx.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
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

    model.close()

    return result
}