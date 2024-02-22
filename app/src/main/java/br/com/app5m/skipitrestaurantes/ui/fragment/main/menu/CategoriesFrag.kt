package br.com.app5m.skipitrestaurantes.ui.fragment.main.menu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import br.com.app5m.skipitrestaurantes.R
import br.com.app5m.skipitrestaurantes.helper.RecyclerItemClickListener
import br.com.app5m.skipitrestaurantes.model.RestaurantDELETE
import br.com.app5m.skipitrestaurantes.ui.adapter.CategoriesAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_categories.*


class CategoriesFrag : Fragment() {
    private lateinit var alertDialog: AlertDialog
    private lateinit var builder: AlertDialog.Builder
    private lateinit var callsAdapter: RecyclerView.Adapter<*>

    private val callsList = java.util.ArrayList<RestaurantDELETE>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_categories, container, false)
    }

    override fun onResume() {
        super.onResume()
        requireActivity().main_toolbar.visibility = View.VISIBLE

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        builder = AlertDialog.Builder(requireContext())
        alertDialog = builder.create()
        clicks()

        createLast_calls()
        configureLast_callsAdapter()
    }
    fun configureLast_callsAdapter() {
        callsAdapter = CategoriesAdapter(callsList, object : RecyclerItemClickListener {

          /*  override fun onClickListenerRestaurantAdapter(estaurant: RestaurantDELETE) {
                super.onClickListenerRestaurantAdapter(estaurant)

                findNavController().navigate(R.id.action_categoriesFrag_to_restaurantFragment)

            }*/


        }, requireContext())


        val vmProduct = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        categoriesRv.apply {
            setHasFixedSize(true)
            setItemViewCacheSize(512)
            callsAdapter.setHasStableIds(true)





            layoutManager = vmProduct
            adapter = callsAdapter


        }

    }

    fun clicks() {
        /*open_call_bt.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_newCallFrag)
        }*/
    }

    fun createLast_calls() {
/*        MyUseFulKotlin().openLoading(requireContext(), alertDialog)
        MyUseFulKotlin().closeLoading(alertDialog)*/




        callsList.clear()
        var restaurantDELETE = RestaurantDELETE()
        restaurantDELETE.name = "Categoria Categoria Categoria Categoria Categoria 1"
        restaurantDELETE.image = R.drawable.sushi
        callsList.add(restaurantDELETE)

        var restaurantDELETE2 = RestaurantDELETE()
        restaurantDELETE2.name = "Categoria 2"
        restaurantDELETE2.image =  R.drawable.sushi
        callsList.add(restaurantDELETE2)

        var restaurantDELETE3 = RestaurantDELETE()
        restaurantDELETE3.name = "Categoria 3"
        restaurantDELETE3.image = R.drawable.sushi
        callsList.add(restaurantDELETE3)

        var restaurantDELETE4 = RestaurantDELETE()
        restaurantDELETE4.name = "Categoria 4"
        restaurantDELETE4.image =  R.drawable.sushi
        callsList.add(restaurantDELETE4)

        var restaurantDELETE5 = RestaurantDELETE()
        restaurantDELETE5.name = "Categoria 5"
        restaurantDELETE5.image =  R.drawable.sushi
        callsList.add(restaurantDELETE5)

        var restaurantDELETE6 = RestaurantDELETE()
        restaurantDELETE6.name = "Categoria 6"
        restaurantDELETE6.image =   R.drawable.sushi
        callsList.add(restaurantDELETE6)

        var restaurantDELETE7 = RestaurantDELETE()
        restaurantDELETE7.name = "Categoria 7"
        restaurantDELETE7.image =  R.drawable.sushi
        callsList.add(restaurantDELETE7)

        var restaurantDELETE8 = RestaurantDELETE()
        restaurantDELETE8.name = "Categoria 8"
        restaurantDELETE8.image =   R.drawable.sushi
        callsList.add(restaurantDELETE8)


    }
}