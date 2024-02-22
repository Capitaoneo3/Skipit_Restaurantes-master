package br.com.app5m.skipitrestaurantes.helper

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
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


/**
 * Useful
 *
 * An class of Wesley Costa, for Android Studio 3.4.1 or newer
 *
 * @author Android version Wesley Costa
 * @since Version 4.1.0
 * @Created  06/2019 - 04/10/2019
 *
 * @Requered Java 8 && Androidx, daimajia
 */
class Useful(private val context: Context) {
    private var alertDialog: AlertDialog? = null
    private val currentPhotoPath: String? = null
    fun openLoading() {
        try {
            val builder = AlertDialog.Builder(
                context
            )
            alertDialog = builder.create()
            val inflater: LayoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view: View = inflater.inflate(R.layout.loading, null)
            alertDialog?.setView(view)
            alertDialog?.window?.setBackgroundDrawableResource(R.color.trasparent)
            alertDialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            alertDialog?.setCanceledOnTouchOutside(false)
            alertDialog?.setCancelable(false)
            alertDialog?.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun closeLoading() {
        if (alertDialog != null) {
            alertDialog?.dismiss()
        }
    }

    companion object {
        const val HOME = 1
        const val NORMAL = 0
        private const val ALLOWED_CHARACTERS = "0123456789qwertyuiopasdfghjklzxcvbnm"

        /**
         * Este metodo constroi uma actionBar personalizada
         *
         * Params: activity, bar, title, type
         *
         * type 0 é pra qualquer activity
         * type 1 é pra apenas pra home activity
         *
         * você pode modifica-lo conforme a queira
         */
        /*  fun setActionBar(activity: Activity, bar: ActionBar, title: String?, type: Int) {
            val view: View = activity.getLayoutInflater().inflate(R.layout.toolbar, null)
            val params = ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT, Gravity.CENTER
            )
            val textTitle: TextView = view.findViewById<TextView>(R.id.textTitle)
            textTitle.setText(title)
            bar.setCustomView(view, params)
            when (type) {
                0 -> {
                    bar.setDisplayHomeAsUpEnabled(true)
                    bar.setHomeAsUpIndicator(
                        activity.getResources().getDrawable(R.drawable.ic_left_large, null)
                    )
                }
                1 -> bar.setDisplayHomeAsUpEnabled(false)
            }
            bar.setDisplayShowTitleEnabled(false)
            bar.setDisplayShowCustomEnabled(true)
        }*/

        /*    fun setActionBar(activity: Activity, bar: ActionBar, title: String?) {
            val view: View = activity.getLayoutInflater().inflate(R.layout.toolbar, null)
            val params = ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT, Gravity.CENTER
            )
            val textTitle: TextView = view.findViewById<TextView>(R.id.textTitle)
            textTitle.setText(title)
            bar.setCustomView(view, params)
            bar.setDisplayShowTitleEnabled(false)
            bar.setDisplayShowCustomEnabled(true)
        }*/
        open fun calculateNoOfColumns(
            context: Context,
            columnWidthDp: Float
        ): Int { // For example columnWidthdp=180
            val displayMetrics = context.resources.displayMetrics
            val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
            return (screenWidthDp / columnWidthDp + 0.5).toInt() // +0.5 for correct rounding to int.
        }


        fun startFragment(fragment: Fragment, fragmentManager: FragmentManager) {
            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
        }

      /*  fun startFragmentPager(fragment: Fragment, fragmentManager: FragmentManager) {
            fragmentManager.beginTransaction().replace(R.id.pager, fragment).commit()
        }*/

        fun startFragmentOnback(fragment: Fragment, fragmentManager: FragmentManager) {
            fragmentManager.beginTransaction().replace(R.id.container, fragment)
                .addToBackStack(null).commit()
        }
        fun startFragmentReload(fragmentkill:Fragment,fragmentnew: Fragment, fragmentManager: FragmentManager) {
            fragmentManager.beginTransaction().remove(fragmentkill).commit()
            fragmentManager.beginTransaction().replace(R.id.container, fragmentnew)
                .addToBackStack(null).commit()
        }

        /*
    *Faz a configura do bitmap e o retorna em um tamanho menor
    *porem mantendo uma boa qualidade
    */
        fun bmOptions(): BitmapFactory.Options {
            // Get the dimensions of the bitmap
            val bmOptions: BitmapFactory.Options = BitmapFactory.Options()
            bmOptions.inJustDecodeBounds = true

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false
            bmOptions.inSampleSize = 2
            bmOptions.inPurgeable = true
            return bmOptions
        }

        fun picturePath(context: Context, data: Intent): String {
            val selectedImage: Uri = data.getData()!!
            val filePathColumn = arrayOf<String>(MediaStore.Images.Media.DATA)
            val cursor =
                context.contentResolver.query(selectedImage, filePathColumn, null, null, null)
            cursor!!.moveToFirst()
            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
            val picturePath = cursor.getString(columnIndex)
            cursor.close()
            return picturePath
        }

        fun strongPassword(password: String, type: Int): Boolean {
            var resultado = false
            var achouNumero = false
            /*boolean achouMaiuscula = false;
        boolean achouMinuscula = false;
        boolean achouSimbolo = false;*/for (c in password.toCharArray()) {
                if (c >= '0' && c <= '9') {
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

        /*public boolean validateData(@NonNull String string) {
        boolean check1 = false;
        boolean check2 = false;
        boolean check3 = false;
        String[] separado = string.split("/");
        if (separado.length != 3) {
            return false;
        }
        int dia = Integer.parseInt(separado[0]);
        int mes = Integer.parseInt(separado[1]);
        int ano = Integer.parseInt(separado[2]);
        if (dia >= 1 && dia <= 31) check1 = true;
        if (mes >= 1 && mes <= 12) check2 = true;
        if (ano >= 1900 && mes <= 2050) check3 = true;
        return check1 && check2 && check3;
    }*/
        fun shake(view: View) {
            YoYo.with(Techniques.Shake).duration(400).repeat(1).playOn(view)
            view.requestFocus()
        }

        fun pulse(view: View?) {
            YoYo.with(Techniques.Pulse).duration(450).playOn(view)
        }

        fun titleCase(str: String?): String {
            if (str == null) return ""
            if (str.length < 1) return str
            val words = str.split("\\s").toTypedArray()
            val sb = StringBuilder()
            for (word in words) {
                sb.append(word.substring(0, 1).toUpperCase())
                    .append(word.substring(1).toLowerCase())
                sb.append(" ")
            }
            return sb.toString()
        }
        //    public static boolean isOnline(Context context) {
        //        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        //        return netInfo == null || !netInfo.isConnected();
        //    }
        /**
         * @param context
         * @param bitmap
         * @return
         * @throws IOException
         *
         * Faz a converção de bitmap para file
         */
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

        fun getRandomString(sizeOfRandomString: Int): String {
            val random = Random()
            val sb = StringBuilder(sizeOfRandomString)
            for (i in 0 until sizeOfRandomString) sb.append(
                ALLOWED_CHARACTERS[random.nextInt(
                    ALLOWED_CHARACTERS.length
                )]
            )
            return sb.toString()
        }

        fun getString(editText: EditText): String {
            return editText.getText().toString()
        }

        //recupera a data atual
        fun dataAtual(): String {
            var dataFormatada = ""
            val formataData = SimpleDateFormat("dd/MM/yyyy")
            val data = Date()
            dataFormatada = formataData.format(data)
            return dataFormatada
        }
    }
}