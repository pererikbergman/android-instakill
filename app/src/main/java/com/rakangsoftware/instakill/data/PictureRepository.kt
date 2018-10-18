package com.rakangsoftware.instakill.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment.DIRECTORY_PICTURES
import android.os.Environment.getExternalStoragePublicDirectory
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.rakangsoftware.instakill.utils.IO
import com.rakangsoftware.instakill.utils.UI
import java.io.File
import java.io.FileInputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class PictureRepository(context: Context) {

    private val applicationContext = context.applicationContext

    private val storage = FirebaseStorage.getInstance()
    private val databas = FirebaseFirestore.getInstance()

    fun save(picture: ByteArray, complete: (File) -> Unit = {}) {
        IO.execute {
            val file = saveLocally(picture)
            saveRemote(file, { downloadUrl ->
                UI.execute {
                    addToFeed(
                        FirebaseAuth.getInstance().currentUser?.displayName!!,
                        downloadUrl
                    )

                    addPicturePathToGallery(file)
                    complete(file)
                }
            }, FirebaseAuth.getInstance().currentUser?.uid!!)
        }
    }

    private fun addToFeed(name: String, downloadUrl: Uri) {
        val feedRef = databas.collection("feed")

        val value = HashMap<String, Any>()
        value.put("name", name)
        value.put("url", downloadUrl.toString())
        value.put("timestamp", FieldValue.serverTimestamp())

        feedRef.add(value).addOnSuccessListener {
            println("Done!")
        }
    }

    private fun saveRemote(file: File, complete: (Uri) -> Unit, uid: String) {
        val imageRef = storage.reference
            .child("images")
            .child(uid)
            .child(file.name)

        val stream = FileInputStream(file)
        val uploadTask = imageRef.putStream(stream)

        uploadTask.continueWithTask(
            Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                return@Continuation imageRef.downloadUrl

            }).addOnCompleteListener {
            if (it.isSuccessful) {
                complete(it.result)
            }
        }

    }

    private fun saveLocally(picture: ByteArray): File {
        // pictures location: /storage/emulated/0/Pictures/
        val pictures = getExternalStoragePublicDirectory(DIRECTORY_PICTURES).absolutePath

        // Subfolder: Instakill
        val instakill = "Instakill"

        // Filename: IMG_20181013_152955.jpg
        val date = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val filename = "IMG_$date.jpg"

        // Full path: /storage/emulated/0/Pictures/Instakill/IMG_20181013_152955.jpg
        val picturePath = "$pictures${File.separator}$instakill${File.separator}$filename"

        // /storage/emulated/0/Pictures/instakill/IMG_20181013_152955.jpg
        val file = File(picturePath)
            .also { it.parentFile.parentFile.mkdirs() }
            .also { it.parentFile.mkdirs() }
            .also { it.outputStream().write(picture) }
        return file
    }

    private fun addPicturePathToGallery(file: File) {
        applicationContext.sendBroadcast(
            Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).apply {
                data = Uri.fromFile(file)
            }
        )
    }
}