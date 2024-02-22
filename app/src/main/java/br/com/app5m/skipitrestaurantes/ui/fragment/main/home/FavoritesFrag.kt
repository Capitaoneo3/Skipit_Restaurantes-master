package br.com.app5m.skipitrestaurantes.ui.fragment.main.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.app5m.skipitrestaurantes.R
import br.com.app5m.skipitrestaurantes.helper.RecyclerItemClickListener
import br.com.app5m.skipitrestaurantes.model.RestaurantDELETE
import br.com.app5m.skipitrestaurantes.ui.adapter.Fav_restaurant_adapter
import kotlinx.android.synthetic.main.fragment_favorites.*


class FavoritesFrag : Fragment() {
    private lateinit var alertDialog: AlertDialog
    private lateinit var builder: AlertDialog.Builder
    private lateinit var callsAdapter: RecyclerView.Adapter<*>

    private val callsList = java.util.ArrayList<RestaurantDELETE>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorites, container, false)
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
        callsAdapter = Fav_restaurant_adapter(callsList, object : RecyclerItemClickListener {

        /*    override fun onClickListenerRestaurantAdapter(estaurant: RestaurantDELETE) {
                super.onClickListenerRestaurantAdapter(estaurant)
                findNavController().navigate(R.id.action_favoritesFrag_to_restaurantDetailFragment)
            }*/


        }, requireContext())


        val vmProduct = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        restaurantsRv.apply {
            setHasFixedSize(true)
            setItemViewCacheSize(512)
            callsAdapter.setHasStableIds(true)


            val itemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            itemDecoration.setDrawable(
                resources.getDrawable(
                    R.drawable.decor_layout_no_bg_vert,
                    null
                )
            )
            restaurantsRv.addItemDecoration(itemDecoration)




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
        restaurantDELETE.id="0"
        restaurantDELETE.name = "Restaurant Restaurant Restaurant Restaurant Restaurant 1"
        restaurantDELETE.endereco = "endereço endereço endereço endereço endereço"
        restaurantDELETE.image = R.drawable.cheff_logo_res
        restaurantDELETE.km = "2.7"
        restaurantDELETE.stairs = 1.7.toFloat()
        callsList.add(restaurantDELETE)

        var restaurantDELETE2 = RestaurantDELETE()
        restaurantDELETE2.id="0"
        restaurantDELETE2.name = "Restaurant 2"
        restaurantDELETE2.endereco = "Endereço 2"
        restaurantDELETE2.image =  R.drawable.cheff_logo_res
        restaurantDELETE2.km = "3.7"
        restaurantDELETE2.stairs = 3.7.toFloat()
        callsList.add(restaurantDELETE2)

        var restaurantDELETE3 = RestaurantDELETE()
        restaurantDELETE3.id="2"
        restaurantDELETE3.name = "Restaurant 3"
        restaurantDELETE3.endereco = "Endereço 3"
        restaurantDELETE3.image =  R.drawable.cheff_logo_res
        restaurantDELETE3.km = "1.2"
        restaurantDELETE3.stairs = 5.0.toFloat()
        callsList.add(restaurantDELETE3)

        var restaurantDELETE4 = RestaurantDELETE()
        restaurantDELETE4.id="3"
        restaurantDELETE4.name = "Restaurant 4"
        restaurantDELETE4.endereco = "Endereço 4"
        restaurantDELETE4.image =  R.drawable.cheff_logo_res
        restaurantDELETE4.km = "4.2"
        restaurantDELETE4.stairs = 5.0.toFloat()
        callsList.add(restaurantDELETE4)



    }
}