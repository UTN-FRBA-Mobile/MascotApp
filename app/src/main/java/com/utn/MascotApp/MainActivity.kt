package com.utn.MascotApp



import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.utn.MascotApp.UI.EncontreMascotaFragment
import com.utn.MascotApp.databinding.ActivityMainBinding
import com.utn.MascotApp.UI.MainMenuFragment




class MainActivity : AppCompatActivity(), MainMenuFragment.OnFragmentInteractionListener, EncontreMascotaFragment.OnFragmentInteractionListener {

    private lateinit var mainMenuFragment: Fragment
    private lateinit var encontreMascotaFragment: Fragment


    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        if (savedInstanceState == null) {
            mainMenuFragment = MainMenuFragment()

            supportFragmentManager.beginTransaction()
                .replace(R.id.container, mainMenuFragment)
                .commitNow()
        }


   }

        override fun enconMasc() {
            encontreMascotaFragment = EncontreMascotaFragment()

            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

            supportFragmentManager.beginTransaction().remove(mainMenuFragment).add(R.id.container, encontreMascotaFragment).commitNow()
        }


}


