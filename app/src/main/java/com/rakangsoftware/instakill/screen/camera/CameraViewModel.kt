package com.rakangsoftware.instakill.screen.camera

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.rakangsoftware.instakill.data.PictureRepository
import com.rakangsoftware.instakill.utils.SingleLiveEvent

class CameraViewModel(val pictureRepository: PictureRepository) : ViewModel() {

    val pictureSaved = MutableLiveData<String>()

    val takePicture = SingleLiveEvent<Boolean>()

    fun onTakePicture() {
        takePicture.value = true
    }

    fun savePicture(jpeg: ByteArray?) {
        jpeg?.let {
            pictureRepository.save(it) {file->
                pictureSaved.value = file.name
            }
        }
    }

}