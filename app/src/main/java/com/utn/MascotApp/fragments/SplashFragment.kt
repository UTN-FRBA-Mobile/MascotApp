package com.utn.MascotApp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.utn.MascotApp.R
import kotlinx.android.synthetic.main.fragment_splash.*


class SplashFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val notificationType =this.arguments?.getString("notificationType")

        if (notificationType == "Publication") titleTextView.text = "Su publicación se generó con éxito"
        if (notificationType == "Delete") titleTextView.text = "Su publicación se eliminó con éxito"

        val motionLayout = view.findViewById<MotionLayout>(R.id.motionLayout)
        motionLayout.addTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {

            }

            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
            }

            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                findNavController().navigate(R.id.action_splashFragment_to_mainMenuFragment)
            }

            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
            }

        })
    }



}