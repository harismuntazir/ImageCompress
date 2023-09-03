package hm.bytefy.imagecompress

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import hm.bytefy.imagecompress.ui.theme.ImageCompressTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

class ShowCompressed : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ImageCompressTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ImageComparisonScreen(
                        originalImage = R.drawable.image,
                        compressedImage = R.drawable.image,
                        originalSize = "2.5 MB",
                        compressedSize = "95.45 KB",
                        { finish() },
                        {finish() }
                    )

                }
            }
        }
    }
}

@Composable
fun ImageComparisonScreen(
    originalImage: Int, // Resource ID for original image
    compressedImage: Int, // Resource ID for compressed image
    originalSize: String, // Original image size
    compressedSize: String, // Compressed image size
    onBack: () -> Unit,
    onSave: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Original Image Row
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = originalImage),
                contentDescription = "Original Image",
                modifier = Modifier.fillMaxSize()
            )
            Text(text = "Size: $originalSize", modifier = Modifier.align(Alignment.TopCenter))
        }

        // Compressed Image Row
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = compressedImage),
                contentDescription = "Compressed Image",
                modifier = Modifier.fillMaxSize()
            )
            Text(text = "Size: $compressedSize", modifier = Modifier.align(Alignment.TopCenter))
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
            Button(onClick = { onSave }) {
                Text(text = "Save Image")
            }
        }
    }
}