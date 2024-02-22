package br.com.app5m.skipitrestaurantes.ui.fragment.main.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.app5m.skipitrestaurantes.R
import br.com.app5m.skipitrestaurantes.controller.AddresseControl
import br.com.app5m.skipitrestaurantes.controller.BuscaEstabControl
import br.com.app5m.skipitrestaurantes.controller.CategoryControl
import br.com.app5m.skipitrestaurantes.controller.CompileByCepControl
import br.com.app5m.skipitrestaurantes.controller.webservice.WSResult
import br.com.app5m.skipitrestaurantes.databinding.FragmentHomeBinding
import br.com.app5m.skipitrestaurantes.helper.Message
import br.com.app5m.skipitrestaurantes.helper.Preferences
import br.com.app5m.skipitrestaurantes.helper.RecyclerItemClickListener
import br.com.app5m.skipitrestaurantes.model.Address
import br.com.app5m.skipitrestaurantes.model.Category.Category
import br.com.app5m.skipitrestaurantes.model.Category.CategoryItem
import br.com.app5m.skipitrestaurantes.model.restaurant.Restaurant
import br.com.app5m.skipitrestaurantes.model.restaurant.RestaurantSubListItem
import br.com.app5m.skipitrestaurantes.model.user.UserItem
import br.com.app5m.skipitrestaurantes.ui.activity.LoginActivity
import br.com.app5m.skipitrestaurantes.ui.adapter.CategoriesHori
import br.com.app5m.skipitrestaurantes.ui.adapter.Restaurant_adapter
import br.com.app5m.skipitrestaurantes.ui.dialog.AtentionMessageDialog
import br.com.app5m.skipitrestaurantes.ui.dialog.RightMessageDialog
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_home.*
import java.io.IOException
import java.util.*


class HomeFragment : Fragment() {
    private val locationUser: br.com.app5m.skipitrestaurantes.model.Address =
        br.com.app5m.skipitrestaurantes.model.Address()

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    var geocoder: Geocoder? = null
    var addresses: List<android.location.Address> = emptyList()
    private var preferences: Preferences? = null
    private var isValidZip: Boolean = false
    private var postalCode: String = ""
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private lateinit var alertDialog: AlertDialog
    private lateinit var builder: AlertDialog.Builder

    private var _binding: FragmentHomeBinding? = null
    private lateinit var restaurantAdapter: RecyclerView.Adapter<*>
    private lateinit var categoriesAdapter: RecyclerView.Adapter<*>

    private val restaurantList = java.util.ArrayList<RestaurantSubListItem>()
    private val restaurantListB = java.util.ArrayList<RestaurantSubListItem>()
    private val categoriesList = java.util.ArrayList<CategoryItem>()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }

    override fun onResume() {
        super.onResume()
            getLastKnownLocationAndStart()

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createCategories()
        createRestaurants()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swiperefresh.setOnRefreshListener {
            createCategories()
            createRestaurants()
            swiperefresh.isRefreshing = false

        }

    }

    fun configureInitialViews() {
        preferences = Preferences(requireContext())

        if (preferences?.getLogin() == true) {
            saveAddress()
        }


        builder = AlertDialog.Builder(requireContext())
        alertDialog = builder.create()
        clicks()
        configureCategoriessAdapter()
        configureRestaurantsAdapter()


    }

    fun configureRestaurantsAdapter() {
        restaurantAdapter = Restaurant_adapter(restaurantList, object : RecyclerItemClickListener {

            override fun onClickListenerRestaurantAdapter(restaurant: RestaurantSubListItem) {
                super.onClickListenerRestaurantAdapter(restaurant)

                var bundle = bundleOf(
                    "restaurantArgs" to restaurant
                )
                findNavController().navigate(
                    R.id.action_homeFragment_to_restaurantDetailFragment,
                    bundle
                )

            }

        }, requireContext())


        val vmProduct = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        restaurantsRv.apply {
            setHasFixedSize(true)
            setItemViewCacheSize(512)
            restaurantAdapter.setHasStableIds(true)


            val itemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            itemDecoration.setDrawable(
                resources.getDrawable(
                    R.drawable.decor_layout_no_bg_vert,
                    null
                )
            )
            restaurantsRv.addItemDecoration(itemDecoration)




            layoutManager = vmProduct
            adapter = restaurantAdapter


        }

    }

    private fun saveAddress() {
        var user = preferences?.getUserData()
        var userAux= UserItem()
        userAux.id_user = user?.id

        if (user != null) {
            //listendereco
            AddresseControl(requireContext(), object : WSResult {
                fun saveAddressOfPref(){
                    var addressModel = preferences?.getUserLocation()

                    if (addressModel != null) {
                        AddresseControl(requireContext(),   object : WSResult {
                            override fun responseAddress(addressList: List<Address>, type: String) {
                                super.responseAddress(addressList, type)
                                preferences?.clearUserLocation()
                                saveAddress()

                            }

                            override fun error(error: String) {
                                super.error(error)
                                dialogshowAtention(error)
                            }


                        }).saveEndereco(addressModel)
                    }
                }

                override fun responseAddress(addressList: List<Address>, type: String) {
                    super.responseAddress(addressList, type)


                    if (!addressList[0].id.isNullOrEmpty() && preferences?.getUserLocation()?.address.isNullOrEmpty()){
                        preferences?.setUserLocation(addressList[0])

                        adressTextHome.text = addressList[0].address

                    }

                    //cai aqui após o cadastro
                    else if (!preferences?.getUserLocation()?.address.isNullOrEmpty()) {
                        if (preferences?.getUserLocation()?.id.isNullOrEmpty()){

                            saveAddressOfPref()
                        }
                    }
                    else if(addressList[0].rows == "0"){
                        saveAddressOfPref()

                    }

                    if( !preferences?.getUserLocation()?.address.isNullOrEmpty()){

                        adressTextHome.text = preferences?.getUserLocation()?.address
                    }


                }


                override fun error(error: String) {
                    super.error(error)
                    dialogshowAtention(error)
                }


            }).listenderecoid(userAux)
        }
    }

    fun createCategories() {

        CategoryControl(requireContext(),object :WSResult{
            override fun responseCategory(category: Category, type: String) {
                super.responseCategory(category, type)
                categoriesList.clear()

                categoriesList.addAll(category)
                if (::restaurantAdapter.isInitialized) categoriesAdapter.notifyDataSetChanged()
            }
            override fun error(error: String) {
                super.error(error)
                dialogshowAtention(error)
            }
        }).listCategories()



    }


    fun configureCategoriessAdapter() {
        categoriesAdapter = CategoriesHori(categoriesList, object : RecyclerItemClickListener {
            override fun onClickListenerCategoryAdapter(categoryItem: CategoryItem) {
                super.onClickListenerCategoryAdapter(categoryItem)
                var bundle = bundleOf(
                    "id_categoria" to categoryItem.id,"nome_categoria" to categoryItem.nome
                )


                findNavController().navigate(R.id.action_homeFragment_to_restaurantFragment,bundle)
            }



        }, requireContext())


        val vmProduct = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        categoriesRv.apply {
            setHasFixedSize(true)
            setItemViewCacheSize(512)
            categoriesAdapter.setHasStableIds(true)

            val dividerItemDecoration = DividerItemDecoration(
                categoriesRv.context,
                vmProduct.orientation
            )
            dividerItemDecoration.setDrawable(
                resources.getDrawable(
                    R.drawable.decor_layout_no_bg_hori,
                    null
                ))
            categoriesRv.addItemDecoration(dividerItemDecoration)





            layoutManager = vmProduct
            adapter = categoriesAdapter


        }

    }

    @SuppressLint("ClickableViewAccessibility")
    fun clicks() {
        notifyBt.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_notifyFragment)
        }
        adressTextHome.setOnClickListener {
            if (preferences?.getLogin() == true) {
                if (isAdded){
                    Message.msg(
                        requireContext(), "Escolher seu Local?",
                        "", object : Message.Answer {
                            override fun setOnClickListener() {
                                findNavController().navigate(R.id.action_homeFragment_to_selectAddressIndexFrag)


                            }
                        })
                }
            }else{
                goLogin()
            }
            return@setOnClickListener
        }
        searchBt.setOnClickListener {
            var name = editTextSearchRest.text.toString()
            buscaEstabReturn(name)

        }





        editTextSearchRest.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event?.getAction() == KeyEvent.ACTION_DOWN) {
                    var name = editTextSearchRest.text.toString()
                    buscaEstabReturn(name)

                }
                return false
            }
        })





    }
    fun goLogin(){
        Message.msg(
            requireContext(), "Fazer Login?",
            "", object : Message.Answer {
                override fun setOnClickListener() {
                    requireActivity().finishAffinity()
                    activity?.let {
                        val intent = Intent(it, LoginActivity::class.java)
                        it.startActivity(intent)
                        it.finishAffinity()
                    }

                }
            })
    }
    fun getLastKnownLocationAndStart() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            configureInitialViews()

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
                        latitude = location.latitude
                        longitude = location.longitude
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
                            isValidZip = true

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

//                            fullAddressText.text = addressFull

                            CompileByCepControl.compile(postalCode, object :
                                CompileByCepControl.CepResult {

                                override fun resultCep(cep: br.com.app5m.skipitrestaurantes.model.Address) {
//                                    Toast.makeText(requireContext(), postalCode, Toast.LENGTH_SHORT).show()
//não consigo resultado com o cep
                                    isValidZip = true

                                    if (cep.cep == null) {
                                        isValidZip = false

//                        cep_et.error = "Por favor, digite um CEP válido."
                                    }
                                    locationUser.city = cep.place
                                    locationUser.address = cep.logradouro
                                    locationUser.state = cep.state
                                    locationUser.cep = cep.cep?.replace("-", "")
                                    locationUser.neighborhood = cep.neighborhood
                                    locationUser.complement = cep.complement

//                                 Log.d("locationUser", cep.logradouro.toString())

                                        if (!preferences?.getUserLocation()?.address.isNullOrEmpty()) {

                                            adressTextHome.text = preferences?.getUserLocation()?.address
                                        } else {
                                            preferences?.setUserLocation(locationUser)
                                            adressTextHome.text = preferences?.getUserLocation()?.address

//                                            adressTextHome.text = cep.logradouro

                                        }


                                }


                            })

                            configureInitialViews()


                        } catch (e: IOException) {
                            isValidZip = false

//                        cep_et.error = "Não foi possível achar o CEP."

                            e.printStackTrace()
                            configureInitialViews()

                        }
                    } else {
                        configureInitialViews()
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

    fun createRestaurants() {
        buscaEstab("")

    }

    fun buscaEstab(name: String) {


        restaurantList.clear()

        var restaurantBody = RestaurantSubListItem()
        restaurantBody.id_de = Preferences(requireContext()).getUserData()?.id
        restaurantBody.nome = name
        BuscaEstabControl(requireContext(), object : WSResult {
            override fun responseRestaurant(restaurent: Restaurant, type: String) {
                super.responseRestaurant(restaurent, type)
                restaurantList.clear()
                restaurantList.addAll(restaurent.get(0))
                if (::restaurantAdapter.isInitialized) restaurantAdapter.notifyDataSetChanged()
            }


            override fun error(error: String) {
                super.error(error)
                dialogshowAtention(error)
            }


        }).buscaEstab(restaurantBody)
    }

    fun buscaEstabReturn(name: String): ArrayList<RestaurantSubListItem> {
        restaurantListB.clear()

        var restaurantBody = RestaurantSubListItem()
        restaurantBody.id_de = Preferences(requireContext()).getUserData()?.id
        restaurantBody.nome = name
        BuscaEstabControl(requireContext(), object : WSResult {
            override fun responseRestaurant(restaurent: Restaurant, type: String) {
                super.responseRestaurant(restaurent, type)
                restaurantListB.clear()
                restaurantListB.addAll(restaurent.get(0))
                var searchText = editTextSearchRest.text.toString()
                var bundle = bundleOf(
                    "restaurantList" to restaurantListB,"searchText" to searchText
                )
                if (!restaurantListB.isNullOrEmpty()) {
                    editTextSearchRest.setText("")
                    findNavController().navigate(
                        R.id.action_homeFragment_to_searchRestDialog22,
                        bundle
                    )
                } else {
                    dialogshowAtention("Nenhum Restaurante encontrado.")
                }
            }


            override fun error(error: String) {
                super.error(error)
                dialogshowAtention(error)
            }


        }).buscaEstab(restaurantBody)

        return restaurantListB
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().main_toolbar.visibility = View.VISIBLE
        _binding = null
    }
}