package br.com.app5m.skipitrestaurantes.ui.fragment.main.home.food_menu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.app5m.skipitrestaurantes.R
import br.com.app5m.skipitrestaurantes.helper.RecyclerItemClickListener
import br.com.app5m.skipitrestaurantes.model.Dish
import br.com.app5m.skipitrestaurantes.ui.adapter.Dishes_adapter
import br.com.app5m.skipitrestaurantes.ui.dialog.FilterDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_search_dishe_food_menu.*
import kotlinx.android.synthetic.main.toolbar_search_dishes_foodmenu.*


class SearchDisheFoodMenuFrag : Fragment() {
    private lateinit var truckAdapter: RecyclerView.Adapter<*>
    private val dishFavList = java.util.ArrayList<Dish>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_dishe_food_menu, container, false)
    }
    override fun onResume() {
        super.onResume()
        requireActivity().main_toolbar.visibility = View.GONE

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureInitialViews()
        filterHeader.setOnClickListener {
         /*   val dialog = FilterDialog()

            dialog.setTargetFragment(this, 1)
            fragmentManager?.let { it1 -> dialog.show(it1, "AdresseDialog") }*/
        }
        backBtToolbar.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    fun configureInitialViews() {

        createEstabilishment()

        truckAdapter = Dishes_adapter(dishFavList, object :
            RecyclerItemClickListener {
            override fun onClickListenerDishFavAdapter(edish: Dish) {
                super.onClickListenerDishFavAdapter(edish)
                findNavController().navigate(R.id.action_searchDisheFoodMenuFrag_to_dhisDetailFragment)

            }





        },requireContext())

        val vmpresc = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)


        disheRv.apply {
            setHasFixedSize(true)

            setItemViewCacheSize(512)
            truckAdapter.setHasStableIds(true)


            val itemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            itemDecoration.setDrawable(
                resources.getDrawable(
                    R.drawable.decor_layout_no_bg_vert,
                    null
                )
            )
            disheRv.addItemDecoration(itemDecoration)

            layoutManager = vmpresc
            adapter = truckAdapter

        }

    }

    fun createEstabilishment() {
        var dish = Dish(R.drawable.dishes1, "Prato 1",5.0f)
        dishFavList.add(dish)
        dish = Dish(R.drawable.dishes2, "Prato 2",2.3f)
        dishFavList.add(dish)
        dish = Dish(R.drawable.dishes3, "Prato 3",7.0f)
        dishFavList.add(dish)
        dish = Dish(R.drawable.dishes4, "Prato 4",10.0f)
        dishFavList.add(dish)

    }
    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().main_toolbar.visibility = View.VISIBLE

    }
}