package hm.bytefy.imagecompress

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import hm.bytefy.imagecompress.ui.theme.ImageCompressTheme
import hm.bytefy.imagecompress.viewmodels.MainScreenViewModel


class MainScreen : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel: MainScreenViewModel = ViewModelProvider(this)[MainScreenViewModel::class.java]

        // Check if permission is granted
        viewModel.checkPermission(this)

        viewModel.permissionGranted.observe(this) { isGranted ->
            if (isGranted) {
                // If permission is granted, navigate to ChooseSettings Activity
                startActivity(Intent(this, ChooseSettings::class.java))
                finish()
            } else {
                // If permission is not granted, display the UI
                setContent {
                    ImageCompressTheme {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            RequestPermissions(viewModel)
                        }
                    }
                }
            }
        }
    }

}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun RequestPermissions(viewModel: MainScreenViewModel) {
    val isButtonClicked = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Button(onClick = { isButtonClicked.value = true }, modifier = Modifier.padding(8.dp)) {
            Text(
                text = "Request Permissions",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

        if (isButtonClicked.value) {
            RequestImageReadPermission { viewModel.onPermissionGranted() }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun RequestImageReadPermission(onPermissionGranted: () -> Unit) {
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            onPermissionGranted()
        } else {
            Toast.makeText(context, "Permission denied to read media images.", Toast.LENGTH_SHORT)
                .show()
        }
    }

    val permissionToRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

    val permissionStatus = remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                permissionToRequest
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    if (permissionStatus.value) {
        onPermissionGranted()
    } else {
        LaunchedEffect(Unit) {
            permissionLauncher.launch(permissionToRequest)
        }
    }
}




