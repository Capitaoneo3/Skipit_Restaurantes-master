package br.com.app5m.skipitrestaurantes.ui.fragment.main.home.food_menu

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter


class DynamicCategoriesAdapter internal constructor(
    fm: FragmentManager?,
    private val mNumOfTabs: Int
) :
    FragmentStatePagerAdapter(fm!!) {
    // get the current item with position number
    override fun getItem(position: Int): Fragment {
        val b = Bundle()
        b.putInt("position", position)
        val frag: Fragment = DynamicCategoriesFrag.newInstance()
        frag.arguments = b
        return frag
    }

    // get total number of tabs
    override fun getCount(): Int {
        return mNumOfTabs
    }
}