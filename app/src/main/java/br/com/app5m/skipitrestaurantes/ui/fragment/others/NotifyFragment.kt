package br.com.app5m.skipitrestaurantes.ui.fragment.others

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
import br.com.app5m.skipitrestaurantes.ui.adapter.Notify_adapter
import kotlinx.android.synthetic.main.fragment_notify.*



class NotifyFragment : Fragment() {
    private lateinit var alertDialog: AlertDialog
    private lateinit var builder: AlertDialog.Builder
    private lateinit var callsAdapter: RecyclerView.Adapter<*>

    private val callsList = java.util.ArrayList<RestaurantDELETE>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notify, container, false)
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
        callsAdapter = Notify_adapter(callsList, object : RecyclerItemClickListener {

         /*   override fun onClickListenerRestaurantAdapter(estaurant: RestaurantDELETE) {
                super.onClickListenerRestaurantAdapter(estaurant)

            }*/


        }, requireContext())


        val vmProduct = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        notifyRv.apply {
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
            notifyRv.addItemDecoration(itemDecoration)




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
        restaurantDELETE.name = "Restaurant Restaurant Restaurant Restaurant Restaurant 1"
        restaurantDELETE.description = "Restaurant Restaurant Restaurant Restaurant Restaurant 1"
        restaurantDELETE.image =  R.drawable.ic_baseline_category_24
        callsList.add(restaurantDELETE)

        var restaurantDELETE2 = RestaurantDELETE()
        restaurantDELETE2.name = "Restaurant Restaurant Restaurant Restaurant Restaurant 2"
        restaurantDELETE2.description = "Restaurant Restaurant Restaurant Restaurant Restaurant 2"
        restaurantDELETE2.image = R.drawable.ic_baseline_email_16
        callsList.add(restaurantDELETE2)

        var restaurantDELETE3 = RestaurantDELETE()
        restaurantDELETE3.name = "Restaurant Restaurant Restaurant Restaurant Restaurant 3"
        restaurantDELETE3.description = "Restaurant Restaurant Restaurant Restaurant Restaurant 3"
        restaurantDELETE3.image =  R.drawable.ic_baseline_category_24
        callsList.add(restaurantDELETE3)




    }
}