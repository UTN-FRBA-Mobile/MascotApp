package com.utn.MascotApp

import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import com.utn.MascotApp.databinding.ActivityMainBinding
import android.view.inputmethod.InputMethodManager
import com.google.android.libraries.places.api.Places


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_MascotApp)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, "AIzaSyCFCmb9MGL22ulEXiHHo6hs3XANIUNrnEI")
        }
        setContentView(view)
        supportActionBar!!.hide()
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }

}
