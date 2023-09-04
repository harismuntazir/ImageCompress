package hm.bytefy.imagecompress.tools

import android.content.Context
import android.net.Uri
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.Toast
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

                var byteCount = originalBitmap.byteCount / 1024
                var scaledBitmap = originalBitmap
                while (byteCount > outputSizeKb) {
                    val scale = sqrt((outputSizeKb.toDouble() / byteCount))

                    val newWidth = (scaledBitmap.width * scale).toInt()
                    val newHeight = (scaledBitmap.height * scale).toInt()

                    if (newWidth <= 0 || newHeight <= 0) {
                        throw IllegalArgumentException("Invalid dimensions after scaling.")
                    }

                    scaledBitmap = Bitmap.createScaledBitmap(
                        scaledBitmap,
                        newWidth,
                        newHeight,
                        true
                    )

                    byteCount = scaledBitmap.allocationByteCount / 1024
                }

                val compressedFile = File(context.cacheDir, "compressed_image.jpg")
                FileOutputStream(compressedFile).use { fileOutputStream ->
                    scaledBitmap.compress(Bitmap.CompressFormat.JPEG, retainQuality, fileOutputStream)
                }

                Toast.makeText(context, "Image Compressed", Toast.LENGTH_SHORT).show()
                isCompressed = true
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Compression Failed", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
        return isCompressed
    }

}


