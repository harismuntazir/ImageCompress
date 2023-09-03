package hm.bytefy.imagecompress.viewmodels

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ChooseSettingsViewModel : ViewModel() {
    val imageUri = MutableLiveData<Uri>()

    fun onImagePicked(uri: Uri) {
        imageUri.value = uri
    }
}
