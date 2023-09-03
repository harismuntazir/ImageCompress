package hm.bytefy.imagecompress.tools

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import java.io.InputStream
class Utils(private val context: Context) {
    fun getFileSize(uri: Uri): String {
        val inputStream = context.contentResolver.openInputStream(uri)
        val sizeInBytes = inputStream?.available()?.toLong()
        inputStream?.close()

        return when {
            sizeInBytes == null -> "Unknown size"
            sizeInBytes < 1024 -> "$sizeInBytes B"
            sizeInBytes < 1024 * 1024 -> String.format("%.2f KB", sizeInBytes / 1024.0)
            else -> String.format("%.2f MB", sizeInBytes / (1024.0 * 1024.0))
        }
    }


    // get Image ID from Uri
    fun getImageId(uri: Uri): String? {
        val projection = arrayOf(MediaStore.Images.Media._ID)
        val cursor = context.contentResolver.query(uri, projection, null, null, null)

        try {
            cursor?.let {
                if (it.moveToFirst()) {
                    val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                    return it.getString(columnIndex)
                }
            }
        } finally {
            cursor?.close()
        }
        return null
    }
}