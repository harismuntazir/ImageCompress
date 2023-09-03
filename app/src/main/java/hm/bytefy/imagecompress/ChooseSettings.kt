package hm.bytefy.imagecompress

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import hm.bytefy.imagecompress.tools.CompressImage
import hm.bytefy.imagecompress.ui.theme.ImageCompressTheme
import hm.bytefy.imagecompress.viewmodels.ChooseSettingsViewModel


class ChooseSettings : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel: ChooseSettingsViewModel =
            ViewModelProvider(this)[ChooseSettingsViewModel::class.java]

        // Compose content
        setContent {
            ImageCompressTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Existing settings UI
                    SettingsScreens(applicationContext)
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreens(context: Context) {
    // image Uri
    var originalImage: Uri? = null
    // handle the image opening
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            originalImage = it
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title "Settings"
        Text(
            text = "Settings",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Box {
            Button(
                onClick = {
                    imagePicker.launch("image/*")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(text = "Open Image")

            }
        }

        // Slider value
        var sliderValue by remember { mutableStateOf(0.75f) }

        // Title "Retain Quality" with slider percentage
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp) // Reduced padding
        ) {
            Text(
                text = "Retain Quality:",
                fontWeight = FontWeight.Bold
            )
            Text(text = "${(sliderValue * 100).toInt()}%", fontWeight = FontWeight.Bold)
        }

        // Slider
        Slider(
            value = sliderValue,
            onValueChange = { sliderValue = it },
            valueRange = 0f..1f,
            steps = 100,
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp) // Reduced padding
        )

        // Title "Enter The Output Size You Need"
        Text(
            text = "Enter The Output Size You Need",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // Input Field for Size with default value "200"
        var sizeInput by remember { mutableStateOf("200") }
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Size:")
            TextField(
                value = sizeInput,
                onValueChange = { sizeInput = it },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier
                    .width(100.dp)
                    .padding(8.dp)
            )
            Text(text = "KB")
        }

        // Button "Compress"
        Button(
            onClick = {
                if (originalImage != null) {
                    val quality = (sliderValue * 100).toInt()
                    val outputSizeInt = sizeInput.toIntOrNull() ?: 0
                    // Directly call your non-Composable ImageCompress function
                    val isCompressed = CompressImage(context).compress(originalImage!!, quality, outputSizeInt)

                    if (isCompressed) {
                        // Open the compressed image in a new activity
                        val intent = Intent(context, ShowCompressed::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        intent.putExtra("imageUri", originalImage.toString())
                        context.startActivity(intent)
                    }
                } else {
                    Toast.makeText(context, "Please select an image first.", Toast.LENGTH_SHORT)
                        .show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(text = "Compress")
        }
    }
}



