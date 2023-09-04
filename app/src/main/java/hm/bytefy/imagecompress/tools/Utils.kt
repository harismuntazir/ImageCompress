package hm.bytefy.imagecompress.tools

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.provider.OpenableColumns
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

    // get file name from Uri
    fun getFileName(uri: Uri, context: Context): String? {
        var fileName: String? = null
        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            cursor.moveToFirst()
            fileName = cursor.getString(nameIndex)
        }
        return fileName
    }

}