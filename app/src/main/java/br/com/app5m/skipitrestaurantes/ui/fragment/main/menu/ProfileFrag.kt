package br.com.app5m.skipitrestaurantes.ui.fragment.main.menu

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import br.com.app5m.skipitrestaurantes.BuildConfig
import br.com.app5m.skipitrestaurantes.R
import br.com.app5m.skipitrestaurantes.controller.UserControl
import br.com.app5m.skipitrestaurantes.controller.webservice.WSConstants
import br.com.app5m.skipitrestaurantes.controller.webservice.WSResult
import br.com.app5m.skipitrestaurantes.helper.Preferences
import br.com.app5m.skipitrestaurantes.helper.Useful
import br.com.app5m.skipitrestaurantes.helper.ValidationHelper
import br.com.app5m.skipitrestaurantes.model.user.User
import br.com.app5m.skipitrestaurantes.model.user.UserItem
import br.com.app5m.skipitrestaurantes.ui.dialog.AtentionMessageDialog
import br.com.app5m.skipitrestaurantes.ui.dialog.RightMessageDialog
import com.bumptech.glide.Glide
import com.github.rtoshiro.util.format.SimpleMaskFormatter
import com.github.rtoshiro.util.format.text.MaskTextWatcher
import com.isseiaoki.simplecropview.CropImageView
import com.isseiaoki.simplecropview.callback.CropCallback
import kotlinx.android.synthetic.main.fragment_menu.*
import kotlinx.android.synthetic.main.profile_fragment.*
import kotlinx.android.synthetic.main.profile_fragment.email
import kotlinx.android.synthetic.main.profile_fragment.name
import kotlinx.android.synthetic.main.profile_fragment.number
import kotlinx.android.synthetic.main.profile_fragment.password
import kotlinx.android.synthetic.main.profile_fragment.updateData
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ProfileFrag : Fragment()

{
    private var preferences: Preferences? = null
    private  var user = UserItem()
    private var currentPhotoPath: String? = null
    private var file: File? = null


    private lateinit var builder: AlertDialog.Builder
    private lateinit var alertDialog: AlertDialog

    override fun onResume() {
        super.onResume()
        onclicks()

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.profile_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var smf2 = SimpleMaskFormatter("(NN) NNNNN-NNNN")
        var mtw2 = MaskTextWatcher(number, smf2)
        number.addTextChangedListener(mtw2)


    }

    private fun onclicks() {
        preferences = Preferences(requireContext())
        user = preferences?.getUserData()!!

        if (preferences?.getLogin() == true) {
            updateView()
        }else{
            Glide.with(requireContext())
                .load(R.drawable.nophoto)
                .into(profile_image)
        }







        profile_image2.setOnClickListener {

            choosePhoto()

        }

        updateData.setOnClickListener {
            if (!validation()) return@setOnClickListener


            val user2 = UserItem()
            user2.id_user = user.id
            user2.nome = name.text.toString()
            user2.email = email.text.toString()
            user2.celular = number.text.toString()

            UserControl(requireContext(),   object : WSResult {

                override fun responseUser(user: User, type: String) {
                    super.responseUser(user, type)
                    if (user[0].status =="01"){
                        user[0].msg?.let { it1 -> dialogshowRight(it1) }
                        updateView()

                    }else{
                        user[0].msg?.let { it1 -> dialogshowAtention(it1) }
                    }
                }




                override fun error(error: String) {
                    super.error(error)
                    dialogshowAtention(error)
                }


            }).updateCadastro(user2)


        }
        updatePass.setOnClickListener {
            if (!ValidationHelper.password(password, requireContext())) return@setOnClickListener
            if (!ValidationHelper.coPassword(password, coPassword, requireContext())) return@setOnClickListener
            val user2 = UserItem()
            user2.id = user.id
            user2.password = password.text.toString()

            UserControl(requireContext(),   object : WSResult {
                override fun responseUser(user: User, type: String) {
                    super.responseUser(user, type)
                    if (user[0].status =="01"){
                        user[0].msg?.let { it1 -> dialogshowRight(it1) }
                        updateView()


                    }else{
                        user[0].msg?.let { it1 -> dialogshowAtention(it1) }
                    }

                }


                override fun error(error: String) {
                    super.error(error)
                    dialogshowAtention(error)
                }


            }).updatepassword(user2)
        }

    }

    private fun editPhoto(image: Bitmap) {
        val builder = AlertDialog.Builder(
            requireContext()
        )
        val alertEditPhoto = builder.create()
        val inflater =
            requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.dialog_edit_image, null)
        alertEditPhoto.setView(view)
        alertEditPhoto.window?.setBackgroundDrawableResource(android.R.color.transparent)
        alertEditPhoto.requestWindowFeature(Window.FEATURE_NO_TITLE)
        alertEditPhoto.setCanceledOnTouchOutside(true)
        alertEditPhoto.setCancelable(true)
        alertEditPhoto.show()

        //LinearLayout gallery = view.findViewById(R.id.gallery_ll);
        val camera = view.findViewById<ImageButton>(R.id.camera_btn)
        val save = view.findViewById<ImageButton>(R.id.save_btn)
        val left = view.findViewById<ImageButton>(R.id.left_btn)
        val rigth = view.findViewById<ImageButton>(R.id.rigth_btn)
        val civ: CropImageView = view.findViewById(R.id.cropImageView)
        civ.setCropMode(CropImageView.CropMode.SQUARE)
        civ.imageBitmap = image
        left.setOnClickListener { v: View? -> civ.rotateImage(CropImageView.RotateDegrees.ROTATE_M90D) }
        rigth.setOnClickListener { v: View? -> civ.rotateImage(CropImageView.RotateDegrees.ROTATE_90D) }
        save.setOnClickListener { v: View? ->
            civ.crop(civ.sourceUri).execute(object : CropCallback {
                override fun onSuccess(cropped: Bitmap) {
                    civ.save(cropped)
                    profile_image2.setImageBitmap(cropped)
                    try {
                        file = context?.let { Useful.storeOnCache(it, cropped) }
                        user.id?.let {
                    /*        file?.let { it1 ->
                                UserControl(requireContext(),   object : WSResult {
                                    override fun responseUser(user: User, type: String) {
                                        super.responseUser(user, type)
                                        if (user[0].status =="01"){
                                            user[0].msg?.let { it1 -> dialogshowRight(it1) }
                                            updateView(user)

                                        }else{
                                            user[0].msg?.let { it1 -> dialogshowAtention(it1) }
                                        }
                                    }


                                    override fun error(error: String) {
                                        super.error(error)
                                        dialogshowAtention(error)
                                    }


                                }).updateAvatar(it, it1)
                            }*/
                        }
                    } catch (e: IOException) {
                        //e.printStackTrace();
                    }
                }

                override fun onError(e: Throwable) {}
            })
            alertEditPhoto.dismiss()
        }
        camera.setOnClickListener { v: View? ->
            dispatchTakePictureIntent()
            alertEditPhoto.dismiss()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            var imagem: Bitmap? = null
            try {
                Log.d("caminho", "onActivityResult: $currentPhotoPath")
                //Valida tipo de seleção da imagem
                if (requestCode == SELECAO_GALERIA) {
                    imagem = BitmapFactory.decodeFile(
                        data?.let { Useful.picturePath(requireContext(), it) },
                        Useful.bmOptions()
                    )
                } else if (requestCode == CAMERA) {
                    imagem = BitmapFactory.decodeFile(currentPhotoPath, Useful.bmOptions())
                }
                //Valida imagem selecionada
                if (imagem != null) {

                    //Converte imagem em byte array
                    val baos = ByteArrayOutputStream()
                    imagem.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                    editPhoto(imagem)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    private fun choosePhoto() {
        val builder = AlertDialog.Builder(
            requireContext()
        )
        val alertPhoto = builder.create()
        val inflater =
            requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val dialog = inflater.inflate(R.layout.dialog_gallery_camera, null)
        alertPhoto.setView(dialog)
        alertPhoto.window?.setBackgroundDrawableResource(android.R.color.transparent)
        alertPhoto.requestWindowFeature(Window.FEATURE_NO_TITLE)
        alertPhoto.show()
        val gallery = dialog.findViewById<AppCompatImageButton>(R.id.gallery_ib)
        val camera = dialog.findViewById<AppCompatImageButton>(R.id.camera_ib)
        gallery.setOnClickListener { v: View? ->
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                requestPermissions( arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE), 1);


            }else{
                val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(i, SELECAO_GALERIA)
                alertPhoto.dismiss()
            }


        }
        camera.setOnClickListener { v: View? ->
            dispatchTakePictureIntent()
            alertPhoto.dismiss()
        }
    }
    @Throws(IOException::class)
    private fun createImageFile(context: Context?): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName,  /* prefix */
            ".jpg",  /* suffix */
            storageDir /* directory */
        )

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.absolutePath
        return image
    }
    private fun dispatchTakePictureIntent() {

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_DENIED){
            requestPermissions( arrayOf(
                Manifest.permission.CAMERA), 1);
        }else{
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            // Ensure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(requireContext().packageManager) != null) {
                Toast.makeText(requireContext(), "", Toast.LENGTH_SHORT).show()
                // Create the File where the photo should go
                var photoFile: File? = null
                try {
                    photoFile = createImageFile(context)
                } catch (ex: IOException) {
                    Log.d("problema", ex.toString())
                    // Error occurred while creating the File
                    ex.printStackTrace()
                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    val photoURI = FileProvider.getUriForFile(requireContext(),
                        BuildConfig.APPLICATION_ID + ".provider", photoFile)

                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, CAMERA)
                }
            }
        }




    }
    fun dialogshowAtention(message: String) {
        val dialog = AtentionMessageDialog(message)

        fragmentManager?.let { it1 -> dialog.show(it1, "BadMessageDialog") }
    }

    fun dialogshowRight(message: String) {
        val dialog = RightMessageDialog(message)

        fragmentManager?.let { it1 -> dialog.show(it1, "BadMessageDialog") }
    }
    private fun validation(): Boolean {
        if (!ValidationHelper.name(name, requireContext())) return false
        if (!ValidationHelper.email(email, requireContext())) return false
        return ValidationHelper.phone(number)

    }
    companion object {
    private const val CAMERA = 100
    private const val SELECAO_GALERIA = 200
}

    private  fun updateView() {
        val userAux2= preferences?.getUserData()
        val userAux= UserItem()
        userAux.id_user = userAux2?.id
        userAux.let {
            if (it != null) {
                UserControl(requireContext(),   object : WSResult {

                    override fun responseUser(user: User, type: String) {
                        super.responseUser(user, type)
                        val preferences = Preferences(
                            requireContext()
                        )
                        user[0].msg?.let { it1 -> dialogshowRight(it1) }


                        preferences.setUserData(user[0])

                        Glide.with(requireContext())
                            .load(WSConstants.avatarImg+ user[0].avatar)
                            .error(R.drawable.nophoto)
                            .into(profile_image2)

                        user[0].nome?.let { it1 ->  name.setText(it1) }
                        user[0].email?.let { it1 ->  email.setText(it1) }
                        user[0].celular?.let { it1 ->  number.setText(it1) }
                        user[0].password?.let { it1 ->  coPassword.setText(it1) }
                        user[0].password?.let { it1 ->  password.setText(it1) }
                    }

                    override fun error(error: String) {
                        super.error(error)
                        dialogshowAtention(error)
                    }


                }).listId(it)
            }
        }

    }


}