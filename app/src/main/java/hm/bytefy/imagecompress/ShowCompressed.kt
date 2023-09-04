package hm.bytefy.imagecompress

import android.content.ContentValues
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.net.toUri
import coil.annotation.ExperimentalCoilApi
import hm.bytefy.imagecompress.ui.theme.ImageCompressTheme
import java.io.File
import coil.compose.rememberImagePainter
import hm.bytefy.imagecompress.tools.Utils
import java.io.FileInputStream


class ShowCompressed : AppCompatActivity() {
    @OptIn(ExperimentalCoilApi::class)
    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Compare And Save"
        setContent {
            ImageCompressTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // get data ready
                    val orgImage = intent.getStringExtra("imageUri")?.let { Uri.parse(it) }
                    val compressedFile = File(cacheDir, "compressed_image.jpg").toUri()

                    val originalPainter = rememberImagePainter(orgImage)
                    val compressedPainter = rememberImagePainter(compressedFile)

                    val originalSize = Utils(this).getFileSize(orgImage!!)
                    val compressedSize = Utils(this).getFileSize(Uri.parse(compressedFile.toString()))

                    // Existing settings UI
                    ImageComparisonScreen(
                        originalPainter = originalPainter,
                        compressedPainter = compressedPainter,
                        originalSize = originalSize,
                        compressedSize = compressedSize,
                        onBack = { finish() },
                        onSave = { saveImage() }
                    )
                }
            }
        }
    }

    // save the image to the DCIM folder in ImageCompress folder
    private fun saveImage() {
        val compressedFile = File(cacheDir, "compressed_image.jpg")

        // get the original image name
        val originalImageName = Utils(this).getFileName(Uri.parse(intent.getStringExtra("imageUri")!!), this)

        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "com_${originalImageName}")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, "DCIM/ImageCompress")
        }

        contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)?.let { uri ->
            contentResolver.openOutputStream(uri).use { outputStream ->
                val compressedImageStream = FileInputStream(compressedFile)
                compressedImageStream.copyTo(outputStream!!)
                compressedImageStream.close()
                outputStream.close()
                Toast.makeText(applicationContext, "Image Saved", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}

@Composable
fun ImageComparisonScreen(
    originalPainter: Painter,
    compressedPainter: Painter,
    originalSize: String,
    compressedSize: String,
    onBack: () -> Unit,
    onSave: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = originalPainter,
                contentDescription = "Original Image",
                modifier = Modifier.fillMaxSize()
            )
            Text(
                text = "Size: $originalSize",
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .zIndex(1f),
                style = TextStyle(fontWeight = FontWeight.Bold)
            )
        }

        // For Compressed Image Row
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = compressedPainter,
                contentDescription = "Compressed Image",
                modifier = Modifier.fillMaxSize()
            )
            Text(
                text = "Size: $compressedSize",
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .zIndex(1f),
                style = TextStyle(fontWeight = FontWeight.Bold)
            )
        }

        // Buttons Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = onBack) {
                Text(text = "Back")
            }
            Button(onClick = onSave) {
                Text(text = "Save Image")
            }
        }
    }
}