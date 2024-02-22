package br.com.app5m.skipitrestaurantes.ui.fragment.main.menu

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.app5m.skipitrestaurantes.R
import br.com.app5m.skipitrestaurantes.controller.AddresseControl
import br.com.app5m.skipitrestaurantes.controller.CompileByCepControl
import br.com.app5m.skipitrestaurantes.controller.webservice.WSResult
import br.com.app5m.skipitrestaurantes.helper.Message
import br.com.app5m.skipitrestaurantes.helper.Preferences
import br.com.app5m.skipitrestaurantes.helper.RecyclerItemClickListener
import br.com.app5m.skipitrestaurantes.model.user.UserItem
import br.com.app5m.skipitrestaurantes.ui.activity.PermissionLocalizationAct
import br.com.app5m.skipitrestaurantes.ui.adapter.Addresses_adapter
import br.com.app5m.skipitrestaurantes.ui.dialog.AtentionMessageDialog
import br.com.app5m.skipitrestaurantes.ui.dialog.MenuAddressDialog
import br.com.app5m.skipitrestaurantes.ui.dialog.RightMessageDialog
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.fragment_select_address_index.*
import kotlinx.android.synthetic.main.toolbar_addressess_index.*
import java.io.IOException
import java.util.*


class SelectAddressIndexFrag : Fragment() {
    private var  isValidZip:Boolean = false
    private var  postalCode:String = ""
    private val  locationUser:br.com.app5m.skipitrestaurantes.model.Address = br.com.app5m.skipitrestaurantes.model.Address()

    private lateinit var fusedLocationClient: FusedLocationProviderClient


    private lateinit var truckAdapter: RecyclerView.Adapter<*>
    private val addressList = java.util.ArrayList<br.com.app5m.skipitrestaurantes.model.Address>()

    private var preferences: Preferences? = null

    var geocoder: Geocoder? = null
    var addresses: List<Address> = emptyList()

    var gps_enabled = false
    var network_enabled = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_select_address_index, container, false)
    }

    override fun onResume() {
        super.onResume()
        var lm = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (ex: Exception) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        } catch (ex: Exception) {
        }
        getLastKnownLocation()

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferences = Preferences(requireContext())

        configureInitialViews()

    }
    fun getLastKnownLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
//            startActivity(Intent(requireContext(), PermissionLocalizationAct::class.java))

            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        } else {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
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
                            val addressFull = addresses.get(0)
                                .getAddressLine(0)
                            isValidZip=true

//                        Log.d("lastL", "getLastKnownLocation: $addresses")

                            // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                            val city = addresses.get(0).locality
                            val state = addresses.get(0).adminArea
                            val country = addresses.get(0).countryName
                            postalCode = addresses.get(0).postalCode.replace("-", "")
                            val knownName =
                                addresses.get(0).featureName


                            locationUser.latitude = latitude.toString()
                            locationUser.longitude = longitude.toString()
                            locationUser.country = country

                            fullAddressText.text = addressFull

                            CompileByCepControl.compile(postalCode, object :
                                CompileByCepControl.CepResult {
                                override fun resultCep(cep: br.com.app5m.skipitrestaurantes.model.Address) {
                                    isValidZip=true

                                    if (cep.cep == null) {
                                        isValidZip=false

//                        cep_et.error = "Por favor, digite um CEP válido."
                                        return
                                    }
                                    locationUser.city = cep.place
                                    locationUser.address = cep.logradouro
                                    locationUser.state = cep.state
                                    locationUser.cep = cep.cep?.replace("-", "")
                                    locationUser.neighborhood = cep.neighborhood
                                    locationUser.complement = cep.complement

//                                 Log.d("locationUser", cep.logradouro.toString())


                                    val currentLIsDefaultAddress = Preferences(context).getUserLocation()
//                                    Log.d("172line", currentLIsDefaultAddress?.address.toString()+" "+locationUser.address)
                                    if (currentLIsDefaultAddress?.address == locationUser.address ){
                                        rightImg.visibility = View.VISIBLE
                                    }else{
                                        rightImg.visibility = View.GONE


                                    }
                                }



                            })



                        } catch (e: IOException) {
                            isValidZip=false
//                        cep_et.error = "Não foi possível achar o CEP."

                            e.printStackTrace()
                        }
                    }

                }








        }





    }

    fun configureInitialViews() {
        getLastKnownLocation()

        addAddress.setOnClickListener {
            var bundleAddress = bundleOf("type" to "add"
            )
            findNavController().navigate(R.id.action_selectAddressIndexFrag_to_addressDetailFragment,bundleAddress)
        }

        actualLocale.setOnClickListener {

            if (!gps_enabled && !network_enabled) {
                Message.msg(requireActivity(),getString(R.string.gps_network_not_enabled) ,
                    getString(R.string.open_location_settings),    object : Message.Answer {
                        override fun setOnClickListener() {
                            requireContext().startActivity(
                                Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                            )
                        }
                    })
                return@setOnClickListener
            }

            Message.msg(requireActivity(),"Endereço Padrão",
                "Usar a localização atual para definir o endereço atual?",    object : Message.Answer {
                    override fun setOnClickListener() {

                        if (ActivityCompat.checkSelfPermission(
                                requireContext(),
                                Manifest.permission.ACCESS_FINE_LOCATION
                            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                                requireContext(),
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {


                            startActivity(Intent(requireContext(), PermissionLocalizationAct::class.java))

                        }else{
                            if (locationUser.cep.isNullOrEmpty()){
                                dialogshowAtention("Houve um problema ao definir este endereço como padrão.")
                            }else{
                                preferences?.clearUserLocation()
                                locationUser.id="0"
                                preferences?.setUserLocation(locationUser)
                                dialogshowRight("Endereço padrão atualizado com sucesso!")
                                findNavController().navigate(br.com.app5m.skipitrestaurantes.R.id.action_selectAddressIndexFrag_self)
                            }
                        }
                    }
                })


//               startActivity(Intent(requireContext(), PermissionLocalizationAct::class.java))

        }






if (preferences?.getLogin() == true){

    createAddress()
}




        truckAdapter = Addresses_adapter(addressList, object :
            RecyclerItemClickListener {
            override fun onClickListenerAddressesAdapter2(
                title: String,
                fullText: String,
                address1: br.com.app5m.skipitrestaurantes.model.Address
            ) {
                super.onClickListenerAddressesAdapter2(title, fullText, address1)
                val menuAddressDialog = MenuAddressDialog(title,fullText,address1,addressList)
                menuAddressDialog.show((context as AppCompatActivity).supportFragmentManager,"sdfdf")
            }


        },requireContext())

        val vmpresc = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)


        addressesRv.apply {
            setHasFixedSize(true)

            setItemViewCacheSize(512)
            truckAdapter.setHasStableIds(true)


            layoutManager = vmpresc
            adapter = truckAdapter

        }

    }

    fun createAddress() {
        var user = preferences?.getUserData()
        var userAux=UserItem()
        userAux.id_user = user?.id
        if (user != null) {
            AddresseControl(requireContext(),   object : WSResult {

                override fun responseAddress(adL: List<br.com.app5m.skipitrestaurantes.model.Address>, type: String) {
                    super.responseAddress(adL, type)

                    if (!adL.isNullOrEmpty() && adL[0].rows!="0" ){


                        addressList.clear()
                        addressList.addAll(adL)
                        truckAdapter.notifyDataSetChanged()
                    }else{


                    }
                }


                override fun error(error: String) {
                    super.error(error)
                    dialogshowAtention(error)
                }


            }).listenderecoid(userAux)
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
}