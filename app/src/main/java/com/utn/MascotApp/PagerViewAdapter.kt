package com.utn.MascotApp

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.utn.MascotApp.fragments.MapsFragment
import com.utn.MascotApp.fragments.MascotaVistasFragment


class PagerViewAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        // Show 2 total pages.
        return 2
    }

    override fun getItem(position: Int): Fragment {
        // check which pages should be destroyed and which should be kept
        return when(position){
            0 -> {
                MapsFragment()
            }
            1 -> {
                MascotaVistasFragment()
            }
            else -> MapsFragment()
        }
    }

}