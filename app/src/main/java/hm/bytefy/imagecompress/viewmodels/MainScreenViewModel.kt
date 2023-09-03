package hm.bytefy.imagecompress.viewmodels

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainScreenViewModel : ViewModel() {
    val permissionGranted = MutableLiveData(false)
    val imageSelected = MutableLiveData(false)

    fun onPermissionGranted() {
        permissionGranted.value = true
    }

    fun onImageSelected() {
        imageSelected.value = true
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun checkPermission(context: Context) {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
        permissionGranted.value = ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }

}
