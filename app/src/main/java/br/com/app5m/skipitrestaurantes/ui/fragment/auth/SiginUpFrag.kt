package br.com.app5m.skipitrestaurantes.ui.fragment.auth

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import br.com.app5m.skipitrestaurantes.controller.UserControl
import br.com.app5m.skipitrestaurantes.controller.webservice.WSResult
import br.com.app5m.skipitrestaurantes.R
import br.com.app5m.skipitrestaurantes.controller.CompileByCepControl
import br.com.app5m.skipitrestaurantes.helper.DialogClickListener
import br.com.app5m.skipitrestaurantes.helper.MyLocation
import br.com.app5m.skipitrestaurantes.helper.Preferences
import br.com.app5m.skipitrestaurantes.helper.ValidationHelper
import br.com.app5m.skipitrestaurantes.model.user.User
import br.com.app5m.skipitrestaurantes.model.user.UserItem
import br.com.app5m.skipitrestaurantes.ui.activity.HomeActivity
import br.com.app5m.skipitrestaurantes.ui.dialog.AtentionMessageDialog
import br.com.app5m.skipitrestaurantes.ui.dialog.RightMessageDialog
import com.github.rtoshiro.util.format.SimpleMaskFormatter
import com.github.rtoshiro.util.format.text.MaskTextWatcher
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.fragment_sigin_up.*
import java.io.IOException
import java.util.*


class SiginUpFrag : Fragment() {
    var geocoder: Geocoder? = null
    var addresses: List<Address> = emptyList()
    var addressModel = br.com.app5m.skipitrestaurantes.model.Address()


    private var isValidZip: Boolean = false

    private lateinit var locationResult: MyLocation.LocationResult

    private var preferences: Preferences? = null

    private lateinit var fusedLocationClient: FusedLocationProviderClient


    private lateinit var builder: AlertDialog.Builder

    private lateinit var alertDialog: AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sigin_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateData.setOnClickListener {

            activity?.let {
                val intent = Intent(it, HomeActivity::class.java)
                it.startActivity(intent)
                it.finishAffinity()
            }
        }
    }
    override fun onResume() {
        super.onResume()

        preferences = Preferences(requireContext())



        getLastKnownLocation()




        configureInitialViews()
    }



    fun getLastKnownLocation() {
        preferences?.clearUserLocation()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            addressFields.visibility = View.VISIBLE


            return
        } else {
            addressFields.visibility = View.GONE

//            cepCardV.visibility = View.GONE
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    addressFields.visibility = View.GONE


                    var latitude = location.latitude
                    var longitude = location.longitude
                    // use your location object
                    // get latitude , longitude and other info from this

                    geocoder = Geocoder(requireContext(), Locale.getDefault())
                    try {
                        addresses = geocoder!!.getFromLocation(
                            latitude,
                            longitude,
                            1
                        ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                        val address = addresses.get(0)
                            .getAddressLine(0)
                        isValidZip = true

//                        Log.d("lastL", "getLastKnownLocation: $addresses")

                        // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        val city = addresses.get(0).locality
                        val state = addresses.get(0).adminArea
                        val country = addresses.get(0).countryName
                        val postalCode = addresses.get(0).postalCode
                        val knownName =
                            addresses.get(0).featureName // Only if available else return NULL

                        cep_et.setText(postalCode.toString())

                        addressModel.latitude = latitude.toString()
                        addressModel.longitude = longitude.toString()
//                        locationUser.address = address
//                        locationUser.country = country

                        preferences?.setUserLocation(addressModel)
                    } catch (e: IOException) {
                        isValidZip = false
                        cep_et.error = "Não foi possível achar o CEP."

                        e.printStackTrace()
                    }
                }

            }

    }

    fun configureInitialViews() {
        builder = AlertDialog.Builder(requireContext())
        alertDialog = builder.create()


        var smf2 = SimpleMaskFormatter("(NN) NNNNN-NNNN")
        var mtw2 = MaskTextWatcher(number, smf2)
        number.addTextChangedListener(mtw2)

        clicks()
        /*     val action = SiginUpFragDirections.actionSiginUpFragToPhoneValidation1Frag(user)
             findNavController().navigate(action)*/
        cep_et.addTextChangedListener(object : TextWatcher {
            val handler = Handler()
            var runnable: Runnable = Runnable { }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                handler.removeCallbacks(runnable)


            }

            override fun afterTextChanged(s: Editable) {
                runnable = Runnable {
                    val geocoder = Geocoder(requireContext())

                    val zip = cep_et.text.toString()
                    try {
                        val addresses = geocoder.getFromLocationName(zip, 1)
                        if (addresses != null && !addresses.isEmpty()) {
                            isValidZip = true

                            val address = addresses[0]
                            // Use the address as needed
                            val message = String.format(
                                "Latitude: %f, Longitude: %f",
                                address.latitude, address.longitude
                            )
                            val addressName = addresses.get(0)
                                .getAddressLine(0)
                            val country = addresses.get(0).countryName

                            //                            addressModel.address = addressName
//                            addressModel.country = country

                        } else {
                            isValidZip = false
                            cep_et.error = "Não foi possível achar o CEP."

                            // Display appropriate message when Geocoder services are not available

                        }
                    } catch (e: IOException) {
                        // handle exception
                    }




                    CompileByCepControl.compile(cep_et.rawText, object :
                        CompileByCepControl.CepResult {
                        override fun resultCep(cep: br.com.app5m.skipitrestaurantes.model.Address) {
                            isValidZip = true

                            if (cep.cep == null) {
                                isValidZip = false

                                cep_et.error = "Por favor, digite um CEP válido."
                                return
                            }
                            addressModel.city = cep.place
                            addressModel.address = cep.logradouro
                            addressModel.state = cep.state
                            //                            addressModel.country = cep.place
                            addressModel.cep = cep.cep
                            addressModel.neighborhood = cep.neighborhood
                            addressModel.complement = cep.complement
                            addressModel.latitude = ""
                            addressModel.longitude = ""

                            city_et.setText(cep.place)
                            state_et.setText(cep.state)
                            nbh_et.setText(cep.neighborhood)
                            address_et.setText(cep.logradouro)
                            complemento_et.setText(cep.complement)

                            preferences?.setUserLocation(addressModel)


                        }
                    })
                    // Do some work with s.toString()
                }
                handler.postDelayed(runnable, 500)

            }

        })
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

        } else {
            getLastKnownLocation()
        }

    }

    private fun clicks() {




     updateData.setOnClickListener {


            val userL = preferences?.getUserLocation()

            Log.d("TAG", userL.toString())
            if (!validation()) return@setOnClickListener

            val user = UserItem()

            user.nome = name.text.toString()
            user.email = email.text.toString()
            user.password = password.text.toString()
            user.celular = number.text.toString()

            UserControl(requireContext(), object : WSResult {
                override fun responseUser(user: User, type: String) {
                    super.responseUser(user, type)
                    val preferences = Preferences(
                        requireContext()
                    )
                    if (user[0].usuario?.status == "01") {
                        user[0].usuario?.msg?.let { it1 ->

                            val dialog = RightMessageDialog(it1, object : DialogClickListener {
                                override fun onClickListener() {
                                    super.onClickListener()
                                    activity?.let {
                                        val intent = Intent(it, HomeActivity::class.java)
                                        it.startActivity(intent)
                                        it.finishAffinity()
                                    }
                                }
                            })
                            fragmentManager?.let { it1 -> dialog.show(it1, "BadMessageDialog") }
                        }

                        preferences.setUserData(user[0].usuario)
                        addressModel.id_user = user[0].usuario?.id.toString()
                        preferences.setUserLocation(addressModel)
                        preferences.setLogin(true)
                    } else {
                        user[0].msg?.let { it1 -> dialogshowAtention(it1) }

                        preferences.setLogin(false)
                        preferences.clearUserLocation()
                    }
                }



                override fun error(error: String) {
                    super.error(error)
                    dialogshowAtention(error)
                }


            }).cadastrar(user)

        }


    }


    private fun validation(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            if (!ValidationHelper.validateTexField(cep_et, requireContext())) return false
            if (!ValidationHelper.validateTexField(city_et, requireContext())) return false
            if (!ValidationHelper.validateTexField(state_et, requireContext())) return false
            if (!ValidationHelper.validateTexField(nbh_et, requireContext())) return false
            if (!ValidationHelper.validateTexField(number_et, requireContext())) return false
            if (!ValidationHelper.validateTexField(address_et, requireContext())) return false


        }


        if (!ValidationHelper.name(name, requireContext())) return false
        if (!ValidationHelper.email(email, requireContext())) return false
        if (!isValidZip) return false
        if (!ValidationHelper.phone(number)) return false
        if (!ValidationHelper.password(password, requireContext())) return false
        return ValidationHelper.coPassword(
            password,
            coPassword,
            requireContext()
        )
    }

    fun dialogshowAtention(message: String) {
        val dialog = AtentionMessageDialog(message)
        fragmentManager?.let { it1 -> dialog.show(it1, "BadMessageDialog") }
    }

    fun dialogshowRight(message: String) {
        val dialog = RightMessageDialog(message)
        fragmentManager?.let { it1 -> dialog.show(it1, "BadMessageDialog") }


    }

}