package hm.bytefy.imagecompress

import android.net.Uri
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import hm.bytefy.imagecompress.ui.theme.ImageCompressTheme
import java.io.File
import coil.compose.rememberImagePainter
import hm.bytefy.imagecompress.tools.Utils


class ShowCompressed : AppCompatActivity() {
    @OptIn(ExperimentalCoilApi::class)
    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ImageCompressTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // get data ready
                    val orgImage = intent.getStringExtra("imageUri")?.let { Uri.parse(it) }
                    val compressedFile = File(cacheDir, "compressed_image.jpg").toURI()

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
                        onSave = { finish() }
                    )
                }
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
        // the size of original image
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            Text(text = "Size: $originalSize", modifier = Modifier.align(androidx.compose.ui.Alignment.TopCenter))
        }
        // Original Image Row
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(8.dp),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            Image(
                painter = originalPainter,
                contentDescription = "Original Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Text(text = "Size: $originalSize", modifier = Modifier.align(androidx.compose.ui.Alignment.TopCenter))
        }

        // Compressed Image Row
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(8.dp),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            Image(
                painter = compressedPainter,
                contentDescription = "Compressed Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Text(text = "Size: $compressedSize", modifier = Modifier.align(androidx.compose.ui.Alignment.TopCenter))
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