package br.com.app5m.skipitrestaurantes.ui.fragment.main.menu

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
import br.com.app5m.skipitrestaurantes.controller.BuscaEstabControl
import br.com.app5m.skipitrestaurantes.controller.QueueControl
import br.com.app5m.skipitrestaurantes.controller.webservice.WSResult
import br.com.app5m.skipitrestaurantes.helper.Preferences
import br.com.app5m.skipitrestaurantes.helper.RecyclerItemClickListener
import br.com.app5m.skipitrestaurantes.model.RestaurantDELETE
import br.com.app5m.skipitrestaurantes.model.fila.Queue
import br.com.app5m.skipitrestaurantes.model.fila.QueueItem
import br.com.app5m.skipitrestaurantes.model.restaurant.Restaurant
import br.com.app5m.skipitrestaurantes.model.restaurant.RestaurantSubListItem
import br.com.app5m.skipitrestaurantes.ui.adapter.Restaurant_history_adapter
import br.com.app5m.skipitrestaurantes.ui.dialog.AtentionMessageDialog
import br.com.app5m.skipitrestaurantes.ui.dialog.RightMessageDialog
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_history.*


class HistoryFrag : Fragment() {
    private lateinit var alertDialog: AlertDialog
    private lateinit var builder: AlertDialog.Builder
    private lateinit var restAdapter: RecyclerView.Adapter<*>

    private val callsList = java.util.ArrayList<RestaurantDELETE>()
    private val restaurantListB = java.util.ArrayList<QueueItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        builder = AlertDialog.Builder(requireContext())
        alertDialog = builder.create()
        clicks()
        create_historyqueue()
        createLast_calls()
        configureLast_callsAdapter()
    }
    fun configureLast_callsAdapter() {
        restAdapter = Restaurant_history_adapter(restaurantListB, object : RecyclerItemClickListener {

        /*    override fun onClickListenerRestaurantAdapter(estaurant: RestaurantDELETE) {
                super.onClickListenerRestaurantAdapter(estaurant)
//                findNavController().navigate(R.id.action_favoritesFrag_to_restaurantDetailFragment)
            }*/


        }, requireContext())


        val vmProduct = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        historyRestaurantsRv.apply {
            setHasFixedSize(true)
            setItemViewCacheSize(512)
            restAdapter.setHasStableIds(true)


            val itemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            itemDecoration.setDrawable(
                resources.getDrawable(
                    R.drawable.decor_layout_no_bg_vert,
                    null
                )
            )
            historyRestaurantsRv.addItemDecoration(itemDecoration)




            layoutManager = vmProduct
            adapter = restAdapter


        }

    }

    fun clicks() {
        /*open_call_bt.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_newCallFrag)
        }*/
    }

    fun create_historyqueue(){
        restaurantListB.clear()

        var restaurantBody = QueueItem()
        restaurantBody.id_de = Preferences(requireContext()).getUserData()?.id

        QueueControl(requireContext(), object : WSResult {
            override fun responseQueue(queue: Queue, type: String) {
                super.responseQueue(queue, type)


                if (queue[0].rows==0){

                }else{
                    restaurantListB.clear()
                    restaurantListB.addAll(queue)
                    restAdapter.notifyDataSetChanged()
                }

            }

            override fun error(error: String) {
                super.error(error)
                dialogshowAtention(error)
            }
        }).historiconafila(restaurantBody)

    }
    fun dialogshowAtention(message: String) {
        val dialog = AtentionMessageDialog(message)

        fragmentManager?.let { it1 -> dialog.show(it1, "BadMessageDialog") }
    }

    fun dialogshowRight(message: String) {
        val dialog = RightMessageDialog(message)

        fragmentManager?.let { it1 -> dialog.show(it1, "BadMessageDialog") }
    }
    fun createLast_calls() {
/*        MyUseFulKotlin().openLoading(requireContext(), alertDialog)
        MyUseFulKotlin().closeLoading(alertDialog)*/




        callsList.clear()
        var restaurantDELETE = RestaurantDELETE()
        restaurantDELETE.id="0"
        restaurantDELETE.name = "Restaurant Restaurant Restaurant Restaurant Restaurant 1"
        restaurantDELETE.endereco = "endereço endereço endereço endereço endereço"
        restaurantDELETE.image =  R.drawable.cheff_logo_res
        restaurantDELETE.status = "Cancelado"
        callsList.add(restaurantDELETE)

        var restaurantDELETE2 = RestaurantDELETE()
        restaurantDELETE2.id="0"
        restaurantDELETE2.name = "Restaurant 2"
        restaurantDELETE2.endereco = "Endereço 2"
        restaurantDELETE2.image =  R.drawable.cheff_logo_res
        restaurantDELETE.status = "Cancelado"
        callsList.add(restaurantDELETE2)

        var restaurantDELETE3 = RestaurantDELETE()
        restaurantDELETE3.id="2"
        restaurantDELETE3.name = "Restaurant 3"
        restaurantDELETE3.endereco = "Endereço 3"
        restaurantDELETE3.image =  R.drawable.cheff_logo_res
        restaurantDELETE.status = "Concluido"

        callsList.add(restaurantDELETE3)

        var restaurantDELETE4 = RestaurantDELETE()
        restaurantDELETE4.id="3"
        restaurantDELETE4.name = "Restaurant 4"
        restaurantDELETE4.endereco = "Endereço 4"
        restaurantDELETE4.image =  R.drawable.cheff_logo_res
        restaurantDELETE.status = "Concluido"

        callsList.add(restaurantDELETE4)



    }
}