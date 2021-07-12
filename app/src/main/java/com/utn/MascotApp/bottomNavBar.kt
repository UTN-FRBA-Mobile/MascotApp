package com.utn.MascotApp

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import kotlinx.android.synthetic.main.fragment_main_menu.*

class BottomNavBar {
    fun setBottomNavBar(bottom_navigation: BottomNavigationView, actual_position: String, navController: NavController, action_to_mainMenu: Int? = null, action_to_publicar: Int? = null, action_to_MiPerfil: Int? = null){
        /* :actual_position: es un string que indica desde donde estamos llamando al bottomNavBar
         * Puede ser cualquiera de estos 3 valores:
         *  MainMenu
         *  Publicar
         *  MiPerfil
         * */

        /*
            Con respecto a las actions, la idea es mandarle las navegaciones,ejemplo : R.id.action_mainMenuFragment_to_miPerfilFragment
            Pero si no hay navegación, dejar el parámetro en null. Por ejemplo, si estoy en mainMenu, no hay navegación a mainMenu
         */

        setButtonBooleans(actual_position)
        bottom_navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.mainMenuItem -> {
                    if (actual_position != "MainMenu"){
                        if (action_to_mainMenu != null) {
                            navController.navigate(action_to_mainMenu)
                        }
                    }
                }
                R.id.publicarItem -> {
                    if (actual_position != "Publicar"){
                        if (action_to_publicar != null) { navController.navigate(action_to_publicar) }
                    }
                }
                R.id.miPerfilItem -> {
                    if (actual_position != "MiPerfil") {
                        if (action_to_MiPerfil != null) { navController.navigate(action_to_MiPerfil) }
                    }
                }
            }
            true
        }
    }

    companion object {
        var mainMenu_button_clicked = false
        var publicar_button_clicked = false
        var miPerfil_button_clicked = false

    }

    private fun setButtonBooleans(actual_position: String){
        if (actual_position == "MainMenu"){
            mainMenu_button_clicked = true
            publicar_button_clicked = false
            miPerfil_button_clicked = false
        }
        if (actual_position == "Publicar"){
            mainMenu_button_clicked = false
            publicar_button_clicked = true
            miPerfil_button_clicked = false
        }
        if (actual_position == "MiPerfil"){
            mainMenu_button_clicked = false
            publicar_button_clicked = false
            miPerfil_button_clicked = true
        }
    }


}