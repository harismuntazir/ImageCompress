package hm.bytefy.imagecompress.tools

import android.content.Context
import android.net.Uri
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.Toast
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import kotlin.math.sqrt

class CompressImage(private val context: Context) {
    fun compress(originalImageUri: Uri, retainQuality: Int, outputSizeKb: Int): Boolean {
        var isCompressed = false
        try {
            context.contentResolver.openInputStream(originalImageUri).use { inputStream ->
                val originalBitmap = BitmapFactory.decodeStream(inputStream)
                    ?: throw IllegalArgumentException("Could not decode image.")

                ByteArrayOutputStream().use { byteArrayOutputStream ->
                    originalBitmap.compress(Bitmap.CompressFormat.JPEG, retainQuality, byteArrayOutputStream)

                    var byteCount = byteArrayOutputStream.size() / 1024
                    var scaledBitmap = originalBitmap
                    while (byteCount > outputSizeKb) {
                        val scale = sqrt((outputSizeKb / byteCount).toDouble()).toFloat()

                        if (scaledBitmap.width * scale <= 0 || scaledBitmap.height * scale <= 0) {
                            throw IllegalArgumentException("Invalid dimensions after scaling.")
                        }

                        scaledBitmap = Bitmap.createScaledBitmap(
                            scaledBitmap,
                            (scaledBitmap.width * scale).toInt(),
                            (scaledBitmap.height * scale).toInt(),
                            true
                        )
                        byteCount = (scaledBitmap.allocationByteCount) / 1024
                    }

                    val compressedFile = File(context.cacheDir, "compressed_image.jpg")
                    FileOutputStream(compressedFile).use { fileOutputStream ->
                        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, retainQuality, fileOutputStream)
                    }

                    Toast.makeText(context, "Image Compressed", Toast.LENGTH_LONG).show()
                    isCompressed = true
                }
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Compression Failed", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
        return isCompressed
    }


}


