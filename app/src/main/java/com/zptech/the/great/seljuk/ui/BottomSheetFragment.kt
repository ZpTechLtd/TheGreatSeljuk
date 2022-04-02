package com.zptech.the.great.seljuk.ui

import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.view.*
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.zptech.the.great.seljuk.BuildConfig
import com.zptech.the.great.seljuk.R
import com.zptech.the.great.seljuk.databinding.FragmentBottomSheetBinding
import com.zptech.the.great.seljuk.isPermissionGranted
import com.zptech.the.great.seljuk.model.RingTone
import com.zptech.the.great.seljuk.requestPermission
import java.io.*


class BottomSheetFragment(
    private val ringTone: RingTone
) : DialogFragment() {

    private lateinit var binding: FragmentBottomSheetBinding
    private var root: String =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            .toString() + "/Islamic RingTones/"
    private var isShareClick: Boolean = false
    private var isDownloadClick: Boolean = false
    private lateinit var file: File

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_bottom_sheet, container, false)
        binding.title.text = ringTone.ringToneTitle

        file = File(root + "${ringTone.ringToneTitle}.mp3")

        isShareClick = false

        binding.lnRingtone.setOnClickListener {

            if (canWriteSettings()) {
                if (isPermissionGranted(this!!.activity!!)) {
                    if (file.exists()) {
                        setAsRingtone(file, RingtoneManager.TYPE_RINGTONE)
                    } else {
                        file = this!!.copyFileToExternalStorage()!!
                        if (file.exists()) {
                            setAsRingtone(file, RingtoneManager.TYPE_RINGTONE)
                        }
                    }
                } else {
                    requestPermission(this!!.activity!!)
                }
            }

        }
        binding.lnNotification.setOnClickListener {
            if (canWriteSettings()) {
                if (isPermissionGranted(this!!.activity!!)) {
                    if (file.exists()) {
                        setAsRingtone(file, RingtoneManager.TYPE_NOTIFICATION)
                    } else {
                        file = this!!.copyFileToExternalStorage()!!
                        if (file.exists()) {
                            setAsRingtone(file, RingtoneManager.TYPE_NOTIFICATION)
                        }
                    }
                } else {
                    requestPermission(this!!.activity!!)
                }
            }
        }

        binding.lnAlarm.setOnClickListener {

            if (canWriteSettings()) {
                if (isPermissionGranted(this!!.activity!!)) {
                    if (file.exists()) {
                        setAsRingtone(file, RingtoneManager.TYPE_ALARM)
                    } else {
                        file = this!!.copyFileToExternalStorage()!!
                        if (file.exists()) {
                            setAsRingtone(file, RingtoneManager.TYPE_ALARM)
                        }
                    }
                } else {
                    requestPermission(this!!.activity!!)
                }
            }

        }
        binding.lnStorage.setOnClickListener {
            if (isPermissionGranted(this!!.activity!!)) {
                if (!file.exists()) {
                    isDownloadClick = true
                    copyFileToExternalStorage()
                } else {
                    Toast.makeText(activity, "RingTone downloaded.", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                requestPermission(this!!.activity!!)
            }
        }

        binding.lnShare.setOnClickListener {
            if (isPermissionGranted(this!!.activity!!)) {
                if (!file.exists()) {
                    isShareClick = true
                    copyFileToExternalStorage()
                } else {
                    share(root + "${ringTone.ringToneTitle}.mp3", "audio/*")
                }
            } else {
                requestPermission(this!!.activity!!)
            }


        }

        return binding.root
    }

    fun copyFileToExternalStorage(): File? {
        val pathSDCard =
            root + ringTone.ringToneTitle + ".mp3"
        File(
            root
        ).mkdirs()

        try {
            val inputStream: InputStream = resources.openRawResource(ringTone.ringToneFile)
            var out: FileOutputStream? = null
            out = FileOutputStream(pathSDCard)
            val buff = ByteArray(1024)
            var read = 0
            try {
                while (inputStream.read(buff).also({ read = it }) > 0) {
                    out.write(buff, 0, read)
                }
            } finally {
                inputStream.close()
                out.close()
            }


            if (isShareClick) {
                share(pathSDCard, "audio/*")
                isShareClick = false
            } else {
                if (isDownloadClick) {
                    isDownloadClick = false
                    Toast.makeText(activity, "RingTone save successfully", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return File(pathSDCard)
    }

    fun share(path: String?, mimeType: String?) {
        try {
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.type = mimeType
            val file = File(path)
            val uri: Uri = FileProvider.getUriForFile(
                this!!.activity!!,
                BuildConfig.APPLICATION_ID.toString() + ".fileprovider",
                file
            )
            intent.putExtra(Intent.EXTRA_STREAM, uri)
            startActivity(intent)
        } catch (exception: IllegalStateException) {
            Toast.makeText(context, "Exception sharing...", Toast.LENGTH_SHORT).show()
        } catch (exception: IllegalArgumentException) {
            Toast.makeText(context, "Exception sharing...", Toast.LENGTH_SHORT).show()
        } catch (exception: ActivityNotFoundException) {
            Toast.makeText(context, "Exception sharing...", Toast.LENGTH_SHORT).show()
        } catch (exception: NullPointerException) {
            Toast.makeText(context, "Exception sharing...", Toast.LENGTH_SHORT).show()
        }
    }


    private fun setAsRingtone(k: File, typeRingtone: Int) {
        val values = ContentValues()
        values.put(MediaStore.MediaColumns.TITLE, k.name)
        values.put(
            MediaStore.MediaColumns.MIME_TYPE,
            getMIMEType(k.absolutePath)
        )
        values.put(MediaStore.MediaColumns.SIZE, k.length())
        values.put(MediaStore.Audio.Media.ARTIST, R.string.app_name)

        if (typeRingtone == RingtoneManager.TYPE_RINGTONE) {
            values.put(MediaStore.Audio.Media.IS_RINGTONE, true)
        } else if (typeRingtone == RingtoneManager.TYPE_ALARM) {
            values.put(MediaStore.Audio.Media.IS_ALARM, true)
        } else if (typeRingtone == RingtoneManager.TYPE_NOTIFICATION) {
            values.put(MediaStore.Audio.Media.IS_NOTIFICATION, true)
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val newUri: Uri =
                activity!!.contentResolver.insert(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    values
                )!!
            try {
                activity!!.contentResolver.openOutputStream(newUri).use { os ->
                    val size = k.length().toInt()
                    val bytes = ByteArray(size)
                    try {
                        val buf = BufferedInputStream(FileInputStream(k))
                        buf.read(bytes, 0, bytes.size)
                        buf.close()
                        os!!.write(bytes)
                        os.close()
                        os.flush()
                    } catch (e: IOException) {
                    }
                }
            } catch (ignored: Exception) {
            }
            RingtoneManager.setActualDefaultRingtoneUri(
                activity,
                typeRingtone,
                newUri
            )
            Toast.makeText(activity, "Set Successfully", Toast.LENGTH_SHORT).show()
        }
    }

    fun getMIMEType(url: String?): String? {
        var mType: String? = null
        val mExtension = MimeTypeMap.getFileExtensionFromUrl(url)
        if (mExtension != null) {
            mType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(mExtension)
        }
        return mType
    }

    fun canWriteSettings(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(context)) {
                val intent =
                    Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
                intent.data = Uri.parse("package:" + context!!.packageName)
                context!!.startActivity(intent)
                return false
            } else {
                return true
            }
        } else {
            return true
        }
    }

    override fun onResume() {
        super.onResume()
        val width = (resources.displayMetrics.widthPixels * 0.90).toInt()
        val window: Window? = dialog!!.window
        window!!.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
        window.setGravity(Gravity.CENTER)
    }
}