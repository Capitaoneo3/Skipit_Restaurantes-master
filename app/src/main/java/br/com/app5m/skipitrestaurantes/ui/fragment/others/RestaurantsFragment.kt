 package br.com.app5m.skipitrestaurantes.ui.fragment.others

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.app5m.skipitrestaurantes.R
import br.com.app5m.skipitrestaurantes.controller.BuscaEstabControl2
import br.com.app5m.skipitrestaurantes.controller.webservice.WSResult
import br.com.app5m.skipitrestaurantes.helper.RecyclerItemClickListener
import br.com.app5m.skipitrestaurantes.model.restaurant.RestaurantSubList
import br.com.app5m.skipitrestaurantes.model.restaurant.RestaurantSubListItem
import br.com.app5m.skipitrestaurantes.ui.adapter.Restaurant_adapter
import br.com.app5m.skipitrestaurantes.ui.dialog.AtentionMessageDialog
import br.com.app5m.skipitrestaurantes.ui.dialog.RightMessageDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_restaurants.*


 class RestaurantsFragment : Fragment() {
    private lateinit var alertDialog: AlertDialog
    private lateinit var builder: AlertDialog.Builder
    private  var id_categoria: Int? = null
     private val restaurantList = java.util.ArrayList<RestaurantSubListItem>()
     private lateinit var restaurantAdapter: RecyclerView.Adapter<*>




     override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_restaurants, container, false)
    }

     override fun onResume() {
         super.onResume()
         requireActivity().main_toolbar.visibility = View.VISIBLE

     }

     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         if (!arguments?.isEmpty!!) {
              id_categoria =
                 ((arguments?.getInt("id_categoria") as? Int)!!)
             createRestaurants(id_categoria!!)

             var titleTollBar = ((arguments?.getString("nome_categoria") as? String)!!)


             (activity as AppCompatActivity?)!!.supportActionBar?.setTitle(titleTollBar)


         }
     }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        builder = AlertDialog.Builder(requireContext())
        alertDialog = builder.create()
        clicks()
        configureRestaurantsAdapter()
    }
     fun createRestaurants(id_categoria: Int) {


         restaurantList.clear()

         var restaurantBody = RestaurantSubListItem()
         restaurantBody.id_categoria = id_categoria

         BuscaEstabControl2(requireContext(), object : WSResult {
             override fun responseRestaurant(restaurent: RestaurantSubList, type: String) {
                 super.responseRestaurant(restaurent, type)
                 restaurantList.clear()
                 restaurantList.addAll(restaurent)
                 if (::restaurantAdapter.isInitialized) restaurantAdapter.notifyDataSetChanged()
             }


             override fun error(error: String) {
                 super.error(error)
                 dialogshowAtention(error)
             }


         }).buscaPorCategoria(restaurantBody)


     }
     fun configureRestaurantsAdapter() {
         restaurantAdapter = Restaurant_adapter(restaurantList, object : RecyclerItemClickListener {

             override fun onClickListenerRestaurantAdapter(restaurant: RestaurantSubListItem) {
                 super.onClickListenerRestaurantAdapter(restaurant)

                 var bundle = bundleOf(
                     "restaurantArgs" to restaurant,
                 )
                 findNavController().navigate(
                     R.id.action_restaurantFragment_to_restaurantDetailFragment,
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
     fun dialogshowAtention(message: String) {
         val dialog = AtentionMessageDialog(message)

         fragmentManager?.let { it1 -> dialog.show(it1, "BadMessageDialog") }
     }

     fun dialogshowRight(message: String) {
         val dialog = RightMessageDialog(message)

         fragmentManager?.let { it1 -> dialog.show(it1, "BadMessageDialog") }
     }

    fun clicks() {
        /*open_call_bt.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_newCallFrag)
        }*/
    }

}