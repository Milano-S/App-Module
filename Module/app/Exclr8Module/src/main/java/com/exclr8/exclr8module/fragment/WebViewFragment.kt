package com.exclr8.exclr8module.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.exclr8.exclr8module.R
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.*

private const val INPUT_FILE_REQUEST_CODE = 1
private const val TAG = "WebViewFragment"
class WebViewFragment : Fragment() {

    private var mCameraPhotoPath: String? = null
    private var mFilePathCallback: ValueCallback<Array<Uri>>? = null
    private val readStoragePermission = 11

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_web_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val webView = view.findViewById<WebView>(R.id.wvWebView)

        loadWebView(webView, "https://danielkummer.github.io/git-flow-cheatsheet/")
    }

    private fun loadWebView(webView: WebView, url: String) {
        val pb = view?.findViewById<ProgressBar>(R.id.pbWebView)
        webView.settings.apply {
            @SuppressLint("SetJavaScriptEnabled")
            javaScriptEnabled = true
            builtInZoomControls = true
            displayZoomControls = false
            supportZoom()
            useWideViewPort = true
            //Progress Bar
            webView.webViewClient = object : WebViewClient() {
                //Progress Bar
                override fun onPageFinished(view: WebView?, url: String?) {
                    pb?.isVisible = false
                }

                //Url Changes
                override fun doUpdateVisitedHistory(
                    view: WebView?, url: String?, isReload: Boolean
                ) {
                    super.doUpdateVisitedHistory(view, url, isReload)
                    Log.i(TAG, isReload.toString())
                    Log.i(TAG, "Current Url: " + url.toString())
                    //Toast.makeText(requireContext(), url.toString(), Toast.LENGTH_SHORT).show()
                }

                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    Log.i(TAG, "Loading Url: " + url.toString())
                    Toast.makeText(requireContext(), url.toString(), Toast.LENGTH_SHORT).show()
                    return super.shouldOverrideUrlLoading(view, url)
                }
            }
        }
        webView.loadUrl(url)
    }

    //Upload files from device inside WebView
    private fun fileAttach(WebView: WebView, mimeType: String) {
        WebView.webChromeClient = object : WebChromeClient() {
            override fun onShowFileChooser(
                webView: WebView, filePathCallback: ValueCallback<Array<Uri>>,
                fileChooserParams: FileChooserParams
            ): Boolean {
                if (mFilePathCallback != null) {
                    mFilePathCallback!!.onReceiveValue(null)
                }
                mFilePathCallback = filePathCallback

                val contentSelectionIntent = Intent(Intent.ACTION_GET_CONTENT)
                contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE)
                //contentSelectionIntent.type = "application/pdf"
                contentSelectionIntent.type = mimeType

                val chooserIntent = Intent(Intent.ACTION_CHOOSER)
                chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent)
                chooserIntent.putExtra(Intent.EXTRA_TITLE, "Document Selection")
                startActivityForResult(chooserIntent, INPUT_FILE_REQUEST_CODE)
                return true
            }
        }
    }

    //Download Files From Base64 Url
    private fun createAndSaveFileFromBase64Url(url: String): String {
        //Add requestLegacyExternalStorage to manifest
        requestPhonePermissions()
        val path: File =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val filetype: String = try {
            (url.substring(url.indexOf("/") + 1, url.indexOf(";")))
        } catch (e: Exception) {
            Log.i(TAG, e.message.toString())
            "svg"
        }
        val filename = System.currentTimeMillis().toString() + "." + filetype
        val file = File(path, filename)
        try {
            if (!path.exists()) {
                path.mkdirs()
                if (!file.exists()) {
                    file.createNewFile()
                }
            }
            val base64EncodedString = url.substring(url.indexOf(",") + 1)
            val decodedBytes = Base64.decode(base64EncodedString, Base64.DEFAULT)
            val os: OutputStream = FileOutputStream(file)
            os.write(decodedBytes)
            os.close()
            Toast.makeText(
                requireContext(),
                filetype.uppercase(Locale.ROOT) + " Downloaded",
                Toast.LENGTH_SHORT
            ).show()
        } catch (e: Exception) {
            Log.i(TAG, "Exception: " + e.message.toString())
            Toast.makeText(requireContext(), "Error Downloading File", Toast.LENGTH_SHORT).show()
        }
        return file.toString()
    }

    //Request Permissions
    private fun requestPhonePermissions() {
        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                readStoragePermission
            )
        }

        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                readStoragePermission
            )
        }

        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_NOTIFICATION_POLICY
            )
            != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_NOTIFICATION_POLICY),
                readStoragePermission
            )
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode != INPUT_FILE_REQUEST_CODE || mFilePathCallback == null) {
            super.onActivityResult(requestCode, resultCode, data)
            return
        }
        var results: Array<Uri>? = null
        if (resultCode == Activity.RESULT_OK) {
            if (data == null) {
                if (mCameraPhotoPath != null) {
                    results = arrayOf(Uri.parse(mCameraPhotoPath))
                }
            } else {
                val dataString = data.dataString
                if (dataString != null) {
                    results = arrayOf(Uri.parse(dataString))
                }
            }
        }
        mFilePathCallback!!.onReceiveValue(results)
        mFilePathCallback = null
        return
    }
}