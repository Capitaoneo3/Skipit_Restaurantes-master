package br.com.app5m.skipitrestaurantes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import br.com.app5m.skipitrestaurantes.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.main_nav_host) //Initialising navController

        appBarConfiguration = AppBarConfiguration.Builder( R.id.favoritesFrag,
            R.id.menuFrag, R.id.homeFragment) //Pass the ids of fragments from nav_graph which you d'ont want to show back button in toolbar
            .build()
        setSupportActionBar(binding.mainToolbar) //Set toolbar

        setupActionBarWithNavController(navController, appBarConfiguration) //Setup toolbar with back button and drawer icon according to appBarConfiguration


        visibilityNavElements(navController) //If you want to hide drawer or bottom navigation configure that in this function


        start()
    }

    private fun visibilityNavElements(navController: NavController) {

        //Listen for the change in fragment (navigation) and hide or show drawer or bottom navigation accordingly if required
        //Modify this according to your need
        //If you want you can implement logic to deselect(styling) the bottom navigation menu item when drawer item selected using listener

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                     R.id.loginFragment ->{
                         main_appbar.visibility = View.GONE
                         supportActionBar?.setDisplayShowTitleEnabled(false)
//                         supportActionBar?.setTitle("Favoritos")
                     }
                R.id.siginUpFrag ->{
                    main_appbar.visibility = View.VISIBLE
                    supportActionBar?.setDisplayShowTitleEnabled(true)
                         supportActionBar?.setTitle("Cadastre-se")
                }
                R.id.recoverPassFrag ->{
                    main_appbar.visibility = View.VISIBLE
                    supportActionBar?.setDisplayShowTitleEnabled(true)
                    supportActionBar?.setTitle("Recuperar Senha")
                }
                else ->{
                    main_appbar.visibility = View.GONE

                }
            }
        }

    }

    private fun start() {
        Handler().postDelayed({
            logoimageSplash2.alpha = 0f
            logoimageSplash2.animate().alpha(1f).duration = 500
//            splashIv.translationY = 50F
        }, 1)



        Handler().postDelayed({
            logoimageSplash.alpha = 1f
            logoimageSplash.animate().alpha(0f).duration = 500
/////////////////////////////////////////

            Handler().postDelayed({
                logoimageSplash.alpha = 0f
                logoimageSplash.animate().alpha(1f).duration = 500
            }, 1)



            Handler().postDelayed({
                logoimageSplash.alpha = 1f
                logoimageSplash.animate().alpha(0f).duration = 500
                Handler().postDelayed({
                    startLL.visibility = View.VISIBLE
                }, 1000)

            }, 3200)

///////////////////////////////////////

        }, 10)

    }


    fun exitApp() { //To exit the application call this function from fragment
        this.finishAffinity()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = navController
        return when(navController.currentDestination?.id) {
            R.id.homeFragment -> {
                finish()
                true
            }
            else -> navController.navigateUp()
        }
        //Setup appBarConfiguration for back arrow
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }

/*    override fun onBackPressed() {

        when { //If drawer layout is open close that on back pressed


            }
            else -> {
                super.onBackPressed() //If drawer is already in closed condition then go back
            }
        }
    }*/
}