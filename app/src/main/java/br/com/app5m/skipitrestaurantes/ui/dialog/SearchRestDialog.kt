package br.com.app5m.skipitrestaurantes.ui.dialog

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.app5m.skipitrestaurantes.R
import br.com.app5m.skipitrestaurantes.controller.BuscaEstabControl
import br.com.app5m.skipitrestaurantes.controller.webservice.WSResult
import br.com.app5m.skipitrestaurantes.helper.Preferences
import br.com.app5m.skipitrestaurantes.helper.RecyclerItemClickListener
import br.com.app5m.skipitrestaurantes.model.restaurant.Restaurant
import br.com.app5m.skipitrestaurantes.model.restaurant.RestaurantSubListItem
import br.com.app5m.skipitrestaurantes.ui.adapter.Restaurant_adapter
import kotlinx.android.synthetic.main.dialog_search_rest.*
import kotlinx.android.synthetic.main.dialog_search_rest.editTextSearchRest
import kotlinx.android.synthetic.main.dialog_search_rest.searchBt

class SearchRestDialog : DialogFragment() {
    private lateinit var restaurantAdapter: RecyclerView.Adapter<*>
    private val restaurantList = java.util.ArrayList<RestaurantSubListItem>()
    private val restaurantListB = java.util.ArrayList<RestaurantSubListItem>()
    private var searchText :String? = ""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.dialog_search_rest,
            container, false
        )
    }

    private lateinit var builder: AlertDialog.Builder
    private lateinit var alertDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogNoBackground)
        if (!arguments?.isEmpty!!) {
            var restaurantListB =
                ((arguments?.getSerializable("restaurantList") as? java.util.ArrayList<RestaurantSubListItem>)!!)
            restaurantList.addAll(restaurantListB)

             searchText =
                arguments?.getString("searchText")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        builder = AlertDialog.Builder(requireContext())
        alertDialog = builder.create()

        editTextSearchRest.setText(searchText)
        configureRestaurants()

        clicks()
    }

    fun configureRestaurants() {
        restaurantAdapter = Restaurant_adapter(restaurantList, object : RecyclerItemClickListener {

            override fun onClickListenerRestaurantAdapter(restaurant: RestaurantSubListItem) {
                super.onClickListenerRestaurantAdapter(restaurant)

                var bundle = bundleOf(
                    "restaurantArgs" to restaurant,
                )
                findNavController().navigate(
                    R.id.action_searchRestDialog2_to_restaurantDetailFragment,
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


    private fun clicks() {
        searchBt.setOnClickListener {
            var name = editTextSearchRest.text.toString()
            buscaEstabReturn(name)

        }

        editTextSearchRest.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event?.getAction() == KeyEvent.ACTION_DOWN) {
                        var  name = editTextSearchRest.text.toString()
                    buscaEstabReturn(name)

                }
                return false
            }
        })
    }

    fun buscaEstabReturn(name: String): ArrayList<RestaurantSubListItem> {
        restaurantListB.clear()

        var restaurantBody = RestaurantSubListItem()
        restaurantBody.id_de = Preferences(requireContext()).getUserData()?.id
        restaurantBody.nome = name
        BuscaEstabControl(requireContext(), object : WSResult {
            @SuppressLint("NotifyDataSetChanged")
            override fun responseRestaurant(restaurent: Restaurant, type: String) {
                super.responseRestaurant(restaurent, type)
                restaurantList.clear()
                restaurantList.addAll(restaurent.get(0))
                restaurantAdapter.notifyDataSetChanged()

                if (restaurantList.isNullOrEmpty()) {
                    dialogshowAtention("Nenhum Restaurante encontrado.")
                    findNavController().navigateUp()
                }
            }


            override fun error(error: String) {
                super.error(error)
                dialogshowAtention(error)
            }


        }).buscaEstab(restaurantBody)

        return restaurantListB
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