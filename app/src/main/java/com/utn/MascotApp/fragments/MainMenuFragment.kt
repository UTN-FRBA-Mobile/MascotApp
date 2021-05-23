package com.utn.MascotApp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.utn.MascotApp.PagerViewAdapter
import com.utn.MascotApp.R
import com.utn.MascotApp.databinding.FragmentMainMenuBinding
import kotlinx.android.synthetic.main.fragment_main_menu.*


class MainMenuFragment : Fragment() {
    private var _binding: FragmentMainMenuBinding? = null
    private val binding get() = _binding!!

    private lateinit var listadoBtn: ImageButton
    private lateinit var mapaBtn: ImageButton

    private lateinit var mViewPager: ViewPager
    private lateinit var mPagerViewAdapter: PagerViewAdapter


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        bottom_navigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.publicarItem -> {
                    findNavController().navigate(R.id.action_mainMenuFragment_to_publicarFragment2)
                }
                R.id.misPublicacionesItem -> {
                    findNavController().navigate(R.id.action_mainMenuFragment_to_misPublicacionesFragment)
                }
            }
            true
        }
        listadoBtn = binding.listadoBtn
        mapaBtn = binding.mapaBtn
        mViewPager = binding.viewPager

        mapaBtn.setOnClickListener {
            mViewPager.currentItem = 0
        }

        listadoBtn.setOnClickListener {
            mViewPager.currentItem = 1
        }

        mPagerViewAdapter = PagerViewAdapter(childFragmentManager)
        mViewPager.adapter = mPagerViewAdapter
        mViewPager.offscreenPageLimit = 2


        mViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                changeTabs(position)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })

        mViewPager.currentItem = 0
        mapaBtn.setImageResource(R.drawable.map_white)
    }

    private fun changeTabs(position: Int) {
        if (position == 0) {
            mapaBtn.setImageResource(R.drawable.map_white)
            listadoBtn.setImageResource(R.drawable.list)
        }

        if (position == 1) {
            listadoBtn.setImageResource(R.drawable.list_white)
            mapaBtn.setImageResource(R.drawable.map)
        }
    }

}