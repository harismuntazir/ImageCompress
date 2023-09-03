package hm.bytefy.imagecompress.compression

import android.content.Context
import android.net.Uri
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.Toast
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class CompressImage(private val context: Context) {
    fun compress(originalImageUri: Uri, retainQuality: Int, outputSizeKb: Int): Boolean {
        var isCompressed = false
        try {
            // Get InputStream from Uri and decode it to Bitmap
            val inputStream = context.contentResolver.openInputStream(originalImageUri)
            val originalBitmap = BitmapFactory.decodeStream(inputStream)

            // Compress the Bitmap to ByteArrayOutputStream
            val byteArrayOutputStream = ByteArrayOutputStream()
            originalBitmap?.compress(Bitmap.CompressFormat.JPEG, retainQuality, byteArrayOutputStream)

            // Check if the compressed size is less than outputSizeKb, otherwise scale down
            var scaledBitmap = originalBitmap
            var byteCount = byteArrayOutputStream.size() / 1024
            while (byteCount > outputSizeKb) {
                val scale = Math.sqrt((outputSizeKb / byteCount).toDouble()).toFloat()
                scaledBitmap = Bitmap.createScaledBitmap(
                    scaledBitmap!!,
                    (scaledBitmap.width * scale).toInt(),
                    (scaledBitmap.height * scale).toInt(),
                    true
                )
                byteCount = (scaledBitmap.allocationByteCount) / 1024
            }

            // Save the compressed/scaled Bitmap to a file
            val compressedFile = File(context.cacheDir, "compressed_image.jpg")
            val fileOutputStream = FileOutputStream(compressedFile)
            scaledBitmap?.compress(Bitmap.CompressFormat.JPEG, retainQuality, fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()

            // show toast
            Toast.makeText(context, "Image Compressed", Toast.LENGTH_LONG).show()
            isCompressed = true
        } catch (e: Exception) {
            Toast.makeText(context, "Compressed Failed", Toast.LENGTH_LONG).show()
            // Handle exceptions here
            e.printStackTrace()
        }
        return isCompressed
    }

}


