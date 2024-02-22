package br.com.app5m.skipitrestaurantes.ui.fragment.main.home.food_menu

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.annotation.Nullable
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.app5m.skipitrestaurantes.R
import br.com.app5m.skipitrestaurantes.helper.RecyclerItemClickListener
import br.com.app5m.skipitrestaurantes.ui.adapter.Dishes_adapter
import br.com.app5m.skipitrestaurantes.model.Dish
import kotlinx.android.synthetic.main.fragment_dynamic.*


class DynamicCategoriesFrag : Fragment() {
    private lateinit var truckAdapter: RecyclerView.Adapter<*>
    private val dishFavList = java.util.ArrayList<Dish>()

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    // adding the layout with inflater
    @Nullable
    override fun onCreateView(
        inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_dynamic, container, false)
        initViews(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureInitialViews()

    }




    // initialise the categories
    private fun initViews(view: View) {
//        val textView = view.findViewById<TextView>(R.id.commonTextView)
//        textView.text = "Category :  " + requireArguments().getInt("position")
    }

    override fun onActivityCreated(@Nullable savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    fun configureInitialViews() {






        createEstabilishment()




        truckAdapter = Dishes_adapter(dishFavList, object :
            RecyclerItemClickListener {
            override fun onClickListenerDishFavAdapter(edish: Dish) {
                super.onClickListenerDishFavAdapter(edish)
                findNavController().navigate(R.id.action_foodMenuEstabelishment_to_dhisDetailFragment)

            }





        },requireContext())

        val vmpresc = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)


        dishesRv.apply {
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
            dishesRv.addItemDecoration(itemDecoration)

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



    // pause function call
    override fun onPause() {
        super.onPause()
    }

    // resume function call
    override fun onResume() {
        super.onResume()
    }

    // stop when we close
    override fun onStop() {
        super.onStop()
    }

    // destroy the view
    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    companion object {
        fun newInstance(): DynamicCategoriesFrag {
            return DynamicCategoriesFrag()
        }
    }
}