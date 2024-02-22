package br.com.app5m.skipitrestaurantes.helper

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.provider.MediaStore
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import br.com.app5m.skipitrestaurantes.R
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo

import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class MyUseFulKotlin {


    private var currentPhotoPath: String? = null

    /**
     * Useful
     *
     * @author Android version Wesley Costa
     * @since Version 1.0.0
     * @Created  01/2020 - 01/2020
     *
     * @Requered Androidx, daimajia
     */

    fun startFragment(frag: Fragment, fragmentManager: FragmentManager) {
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.container, frag)/*.addToBackStack(null)*/.commit()
    }

    fun startFragmentOnBack(frag: Fragment, fragmentManager: FragmentManager) {
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.container, frag).addToBackStack(null).commit()
    }

/*fun closeCurrentActToLogin(activity: Activity,context: Context){
    Preferences(context).clearUserData()
    Handler().postDelayed({

        activity?.let{
            val intent = Intent (it, StartAct2::class.java)
            it.startActivity(intent)
            it.finish()
        }
    }, 1500)
}*/


    fun openLoading(context: Context, alertDialog: AlertDialog) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.loading, null)
        alertDialog.setView(view)
        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.setCancelable(false)
        alertDialog.show()
    }
   /* fun openLoadingWaitingProducts(context: Context, alertDialog: AlertDialog) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.loading_products, null)
        alertDialog.setView(view)
        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.setCancelable(false)
        alertDialog.show()
    }*/

    fun closeLoading(alertDialog: AlertDialog) {
        if (alertDialog.isShowing) alertDialog.dismiss()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun setActionBar(activity: Activity, bar: ActionBar, title: String, type: Int){

/*   *//*     val view = activity.layoutInflater.inflate(R.layout.tool_bar, null)
        val params = ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT,
            ActionBar.LayoutParams.WRAP_CONTENT, Gravity.CENTER)*//*

      *//*  val textTitle = view.findViewById<TextView>(R.id.toolbarTitle)
        textTitle.text = title
        bar.setCustomView(view, params)*//*

//        if (type == 0) {
//            bar.setHomeAsUpIndicator(activity.resources.getDrawable(R.drawable.ic_arrow_left_green, null))
//        }*/

        bar.setDisplayShowTitleEnabled(false)
        bar.setDisplayShowCustomEnabled(true)
        bar.setDisplayHomeAsUpEnabled(true)

    }

   @SuppressLint("UseCompatLoadingForDrawables")
 /*   fun setActionBar(activity: Activity, bar: ActionBar, toolbarTitle: String){

        bar.setHomeAsUpIndicator(activity.resources.getDrawable(
            R.drawable.ic_arrow_back,
            null))

        bar.title = toolbarTitle
        bar.setDisplayShowTitleEnabled(true)
        bar.setDisplayShowCustomEnabled(true)
        bar.setDisplayHomeAsUpEnabled(true)


   }*/

    fun createLink(targetTextView: TextView, completeString: String,
                   partToClick: String, clickableAction: ClickableSpan?): TextView? {
        val spannableString = SpannableString(completeString)

        // make sure the String is exist, if it doesn't exist
        // it will throw IndexOutOfBoundException
        val startPosition = completeString.indexOf(partToClick)
        val endPosition = completeString.lastIndexOf(partToClick) + partToClick.length
        spannableString.setSpan(clickableAction, startPosition, endPosition,
            Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        targetTextView.text = spannableString
        targetTextView.movementMethod = LinkMovementMethod.getInstance()
        return targetTextView
    }

    fun strongPassword(password: String, type: Int): Boolean {
        var resultado = false
        var achouNumero = false
        /*boolean achouMaiuscula = false;
        boolean achouMinuscula = false;
        boolean achouSimbolo = false;*/for (c in password.toCharArray()) {
            if (c in '0'..'9') {
                achouNumero = true
            } /*else if (c >= 'A' && c <= 'Z') {
                achouMaiuscula = true;
            } else if (c >= 'a' && c <= 'z') {
                achouMinuscula = true;
            } else {
                achouSimbolo = true;
            }*/
            if (type == 1) resultado = achouNumero

            /*if (tipo == 2) resultado = achouMaiuscula;

            if (tipo == 3) resultado = achouMinuscula;

            if (tipo == 4) resultado = achouSimbolo;*/
        }
        return resultado
    }

    @Throws(IOException::class)
    private fun createImageFile(activity: Activity): File? {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName,  /* prefix */
            ".jpg",  /* suffix */
            storageDir /* directory */
        )

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.absolutePath
        return image
    }

    fun dispatchTakePictureIntent(activity: Activity, type: Int): String? {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(activity.packageManager) != null) {
            // Create the File where the photo should go
            var photoFile: File? = null
            try {
                photoFile = createImageFile(activity)
            } catch (ex: IOException) {
                // Error occurred while creating the File
                ex.printStackTrace()
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                val photoURI = FileProvider.getUriForFile(activity,
                    "br.com.app5m.appaloquadras",
                    photoFile)
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                activity.startActivityForResult(takePictureIntent, type)
            }
        }
        return currentPhotoPath
    }

    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    fun picturePath(context: Context, data: Intent?): String? {
        val selectedImage = data?.data
        val filePathColumn =
            arrayOf(MediaStore.Images.Media.DATA)
        val cursor =
            selectedImage?.let { context.contentResolver.query(it, filePathColumn, null, null, null) }
        cursor?.moveToFirst()
        val columnIndex = cursor?.getColumnIndex(filePathColumn[0])
        val picturePath = columnIndex?.let { cursor.getString(it) }
        cursor?.close()
        return picturePath
    }

    fun bmOptions(): BitmapFactory.Options {

        return BitmapFactory.Options().apply {
            inJustDecodeBounds = true
            inJustDecodeBounds = false
            inSampleSize = 3
        }

    }

    @Throws(IOException::class)
    fun storeOnCache(context: Context, bitmap: Bitmap): File {
        val cacheDir = context.cacheDir
        val file = File(cacheDir, generateRandomFilename())
        val out = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
        out.flush()
        out.close()
        return file
    }

    private fun generateRandomFilename(): String {
        return System.currentTimeMillis().toString() +
                (Math.random() * 10000.0).toInt() +
                "." +
                "jpg"
    }


    fun shake(view: View) {
        YoYo.with(Techniques.Shake).duration(400).repeat(1).playOn(view);
        view.requestFocus();
    }

    fun pulse(view: View) {
        YoYo.with(Techniques.Pulse).duration(450).playOn(view);
    }


}