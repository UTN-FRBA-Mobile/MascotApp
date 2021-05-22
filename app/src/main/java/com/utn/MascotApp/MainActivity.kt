package com.utn.MascotApp

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.utn.MascotApp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var listadoBtn: ImageButton
    private lateinit var mapaBtn: ImageButton

    private lateinit var binding: ActivityMainBinding
    private lateinit var mViewPager: ViewPager
    private lateinit var mPagerViewAdapter: PagerViewAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_MascotApp)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        supportActionBar!!.hide()

        listadoBtn = binding.listadoBtn
        mapaBtn = binding.mapaBtn
        mViewPager = binding.viewPager

        listadoBtn.setOnClickListener {
            mViewPager.currentItem = 0
        }

        mapaBtn.setOnClickListener {
            mViewPager.currentItem = 1
        }

        mPagerViewAdapter = PagerViewAdapter(supportFragmentManager)
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
        listadoBtn.setImageResource(R.drawable.list_white)
    }

    private fun changeTabs(position: Int) {
        if (position == 0) {
            listadoBtn.setImageResource(R.drawable.list_white)
            mapaBtn.setImageResource(R.drawable.map)
        }

        if (position == 1) {
            listadoBtn.setImageResource(R.drawable.list)
            mapaBtn.setImageResource(R.drawable.map_white)
        }
    }
}
