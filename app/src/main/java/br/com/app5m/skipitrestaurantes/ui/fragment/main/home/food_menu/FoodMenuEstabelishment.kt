package br.com.app5m.skipitrestaurantes.ui.fragment.main.home.food_menu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import br.com.app5m.skipitrestaurantes.R
import com.google.android.material.tabs.TabLayout

import androidx.viewpager.widget.ViewPager
import br.com.app5m.skipitrestaurantes.ui.fragment.main.home.food_menu.DynamicCategoriesAdapter

import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_food_menu_estabelishment.*
import kotlinx.android.synthetic.main.toolbar_food_menu.*


class FoodMenuEstabelishment : Fragment() {
    private lateinit  var viewPager: ViewPager
    private lateinit var mTabLayout: TabLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_food_menu_estabelishment, container, false)
    }

    override fun onResume() {
        super.onResume()
        requireActivity().main_toolbar.visibility = View.GONE

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchBt2.setOnClickListener {
            findNavController().navigate(R.id.action_foodMenuEstabelishment_to_searchDisheFoodMenuFrag)
        }
        initViews()
        backBtToolbar.setOnClickListener {
            findNavController().navigateUp()
        }

    }
    private fun initViews() {

        // initialise the layout
        viewPager = viewpager
        mTabLayout = tabs

        // setOffscreenPageLimit means number
        // of tabs to be shown in one page
        viewPager?.setOffscreenPageLimit(5)
        viewPager?.addOnPageChangeListener(TabLayoutOnPageChangeListener(mTabLayout))
        mTabLayout?.setOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                // setCurrentItem as the tab position
                viewPager?.setCurrentItem(tab.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
        setDynamicFragmentToTabLayout()
    }

    // show all the tab using DynamicFragmnetAdapter
    private fun setDynamicFragmentToTabLayout() {
        // here we have given 10 as the tab number
        // you can give any number here
        for (i in 0..9) {
            // set the tab name as "Page: " + i
            mTabLayout!!.addTab(mTabLayout!!.newTab().setText("Categoria: $i"))
        }
        val mDynamicFragmentAdapter = DynamicCategoriesAdapter(
            childFragmentManager , mTabLayout!!.tabCount
        )

        // set the adapter
        viewPager!!.adapter = mDynamicFragmentAdapter

        // set the current item as 0 (when app opens for first time)
        viewPager!!.currentItem = 0
    }
    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().main_toolbar.visibility = View.VISIBLE
    }
}
