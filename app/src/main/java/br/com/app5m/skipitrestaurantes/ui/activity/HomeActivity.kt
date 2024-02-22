package br.com.app5m.skipitrestaurantes.ui.activity

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.media.RingtoneManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import br.com.app5m.skipitrestaurantes.R
import br.com.app5m.skipitrestaurantes.controller.FcmControl
import br.com.app5m.skipitrestaurantes.controller.webservice.WSResult
import br.com.app5m.skipitrestaurantes.databinding.ActivityHomeBinding
import br.com.app5m.skipitrestaurantes.helper.Preferences
import br.com.app5m.skipitrestaurantes.model.Fcm
import com.google.android.gms.maps.GoogleMap
import com.google.firebase.FirebaseApp
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult
import com.squareup.picasso.Picasso
import java.lang.Exception


class HomeActivity : AppCompatActivity() {

    private var preferences: Preferences? = null


    var textCartItemCount: TextView? = null
    var mCartItemCount = 10
    private lateinit var logoTarget: com.squareup.picasso.Target
    private lateinit var mMap: GoogleMap

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferences = Preferences(this)

        saveFcm()

        val channel = getString(R.string.default_notification_channel_id)
        val uriSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)


        val notification = NotificationCompat.Builder(this, channel)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentTitle(title)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setSound(uriSound)
            .setAutoCancel(true)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nChannel =
                NotificationChannel(channel, "channel", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(nChannel)
        }
//         notificationManager.notify(0, notification.build())


        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.main_nav_host) //Initialising navController
        appBarConfiguration = AppBarConfiguration.Builder(
            R.id.favoritesFrag,
            R.id.menuFrag, R.id.homeFragment, R.id.mapFrag
        ) //Pass the ids of fragments from nav_graph which you d'ont want to show back button in toolbar
            .build()
        setSupportActionBar(binding.mainToolbar) //Set toolba


        setupActionBarWithNavController(
            navController,
            appBarConfiguration
        ) //Setup toolbar with back button and drawer icon according to appBarConfiguration


        visibilityNavElements(navController) //If you want to hide drawer or bottom navigation configure that in this function


    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.actionbar_menu_item, menu)
//        val menuItemSearch: MenuItem = menu.findItem(R.id.action_search)
        val menuItemNotify: MenuItem = menu.findItem(R.id.action_notify)
//        val actionView2: View = menuItemSearch.getActionView()
        val actionView: View = menuItemNotify.getActionView()
        textCartItemCount = actionView.findViewById<View>(R.id.cart_badge) as TextView
        setupBadge()
        actionView.setOnClickListener { onOptionsItemSelected(menuItemNotify) }


        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.homeFragment) {
                menuItemNotify.setVisible(true)
                supportActionBar?.setDisplayHomeAsUpEnabled(false)
                var mActionBarView: View? =
                    layoutInflater.inflate(R.layout.custom_action_item_search_layout, null)
                supportActionBar?.customView = mActionBarView
//                menuItemSearch.setVisible(true)
            } else {
                supportActionBar?.customView?.visibility = View.GONE

                menuItemNotify.setVisible(false)
//                menuItemSearch.setVisible(false)
            }
            /*    if (destination.id == R.id.formsAndVideosFragment || destination.id == R.id.myCallsFrag ) {
                    main_appbar.visibility = View.GONE
                } else {
                    main_appbar.visibility = View.VISIBLE
                }


                if (destination.id == R.id.detailCallFrag) {
                    supportActionBar?.setTitle("Detalhes do chamado")
                }
                if (destination.id == R.id.newCallFrag) {
                    supportActionBar?.setTitle("Abrir Chamado")
                }
                if (destination.id == R.id.sendingAudioFragment) {
                    supportActionBar?.setTitle("Gravar Áudio")
                }
                if (destination.id == R.id.profileFrag) {
                    supportActionBar?.setTitle("Meu Perfil")
                }
                if (destination.id == R.id.vizualizeArchive) {
                    supportActionBar?.setTitle("Detalhe do Arquivo")
                }*/

        }


        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_notify -> {

                navController.navigate(R.id.action_homeFragment_to_notifyFragment)
                // Do something
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupBadge() {
        if (textCartItemCount != null) {
            if (mCartItemCount == 0) {
                if (textCartItemCount!!.visibility != View.GONE) {
                    textCartItemCount!!.visibility = View.GONE
                }
            } else {
                textCartItemCount!!.text = Math.min(mCartItemCount, 99).toString()
                if (textCartItemCount!!.visibility != View.VISIBLE) {
                    textCartItemCount!!.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun visibilityNavElements(navController: NavController) {

        //Listen for the change in fragment (navigation) and hide or show drawer or bottom navigation accordingly if required
        //Modify this according to your need
        //If you want you can implement logic to deselect(styling) the bottom navigation menu item when drawer item selected using listener

        supportActionBar?.show()
        supportActionBar?.setDisplayShowCustomEnabled(true)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.hoursOfWeekQueueDialog -> {
                    supportActionBar?.setDisplayShowTitleEnabled(false)
                    showActionBarLogoFade(this, false)
                    binding.mainToolbar.visibility = View.GONE


                    showBothNavigation()
                }
                R.id.homeFragment -> {
                    supportActionBar?.setDisplayShowTitleEnabled(false)
                    showActionBarLogoFade(this, false)
                    binding.mainToolbar.visibility = View.GONE


                    showBothNavigation()
                }
                R.id.favoritesFrag -> {

                    showBothNavigation()


                    supportActionBar?.setDisplayShowTitleEnabled(true)
                    showActionBarLogo(this, false)
                    supportActionBar?.setTitle("Favoritos")


                }
                R.id.mapFrag -> {
                    supportActionBar?.setDisplayShowTitleEnabled(true)
                    showActionBarLogo(this, false)
                    supportActionBar?.setTitle("Mapa dos Restaurantes")
                    showBothNavigation()

                }
                R.id.menuFrag -> {
                    supportActionBar?.setDisplayShowTitleEnabled(false)
                    showActionBarLogo(this, true)

                    showBothNavigation()
                }
                R.id.historyFrag -> {
                    supportActionBar?.setDisplayShowTitleEnabled(true)
                    showActionBarLogo(this, false)
                    supportActionBar?.setTitle("Histórico de Filas")
                    hideBottomNavigation()

                }
                R.id.categoriesFrag -> {
                    supportActionBar?.setDisplayShowTitleEnabled(true)
                    showActionBarLogo(this, false)
                    supportActionBar?.setTitle("Categorias")
                    hideBottomNavigation()

                }
                R.id.adressDetailFragment -> {
                    supportActionBar?.setDisplayShowTitleEnabled(true)
                    showActionBarLogo(this, false)
                    supportActionBar?.setTitle("Detalhe do Endereço")
                    hideBottomNavigation()

                }

                R.id.selectAddressIndexFrag -> {
                    supportActionBar?.setDisplayShowTitleEnabled(true)
                    showActionBarLogo(this, false)
                    supportActionBar?.setTitle("Meus Endereços")
                    hideBottomNavigation()

                }

                R.id.profileFrag -> {
                    supportActionBar?.setDisplayShowTitleEnabled(true)
                    showActionBarLogo(this, false)
                    supportActionBar?.setTitle("Perfil")
                    hideBottomNavigation()

                }


                R.id.restaurantDetailFragment -> {
                    supportActionBar?.setDisplayShowTitleEnabled(false)
                    showActionBarLogo(this, true)
                    hideBottomNavigation()

                }

                R.id.restaurantFragment -> {
                    supportActionBar?.setDisplayShowTitleEnabled(true)
                    showActionBarLogo(this, false)
                    supportActionBar?.setTitle("Restaurantes")
                    hideBottomNavigation()

                }

                R.id.notifyFragment -> {
                    hideBottomNavigation()

                    supportActionBar?.setDisplayShowTitleEnabled(true)
                    showActionBarLogo(this, false)
                    supportActionBar?.setTitle("Notificações")
                }
                R.id.foodMenuEstabelishment -> {
                    hideBottomNavigation()
                    binding.mainToolbar.visibility = View.GONE

                    supportActionBar?.setDisplayShowTitleEnabled(false)
                    showActionBarLogo(this, false)
                    supportActionBar?.setTitle("Cardápio")
                }
                R.id.searchDisheFoodMenuFrag -> {
                    hideBottomNavigation()
                    binding.mainToolbar.visibility = View.GONE

                    supportActionBar?.setDisplayShowTitleEnabled(false)
                    showActionBarLogo(this, false)
                    supportActionBar?.setTitle("")
                }
                R.id.dhisDetailFragment -> {
                    hideBottomNavigation()
                    binding.mainToolbar.visibility = View.VISIBLE

                    supportActionBar?.setDisplayShowTitleEnabled(true)
                    showActionBarLogo(this, false)
                    supportActionBar?.setTitle("Detalhe do Prato")
                }
                R.id.searchRestDialog2 -> {
                    hideBottomNavigation()
                    binding.mainToolbar.visibility = View.GONE

                    supportActionBar?.setDisplayShowTitleEnabled(true)
                    showActionBarLogo(this, false)
                    supportActionBar?.setTitle("Detalhe do Prato")
                }
                else -> {
                    binding.mainToolbar.visibility = View.VISIBLE


                    showActionBarLogoFade(this, false)

                    supportActionBar?.setDisplayShowTitleEnabled(true)

                    hideBottomNavigation()
                }
            }
        }

    }

    fun showActionBarLogoFade(activity: Activity, show: Boolean) {
        if (show) {
            // Calculate Action bar height
            var actionBarHeight = 200
            val tv = TypedValue()
            if (activity.theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
                actionBarHeight = TypedValue.complexToDimensionPixelSize(
                    tv.data,
                    activity.resources.displayMetrics
                )
            }

            // Using action bar background drawable
            logoTarget = object : com.squareup.picasso.Target {
                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                    val layers = arrayOfNulls<Drawable>(2)
                    layers[0] =
                        ColorDrawable(resources.getColor(R.color.colorPrimary)) // Background color of Action bar
                    val bd = BitmapDrawable(activity.resources, bitmap)
                    bd.gravity = Gravity.CENTER
                    val drawLogo: Drawable = bd
                    layers[1] = drawLogo // Bitmap logo of Action bar (loaded from Picasso)
                    val layerDrawable = LayerDrawable(layers)
                    layers[1]!!.alpha = 0
                    (activity as AppCompatActivity).supportActionBar!!.setBackgroundDrawable(
                        layerDrawable
                    )
                    val animator = ObjectAnimator.ofPropertyValuesHolder(
                        layers[1],
                        PropertyValuesHolder.ofInt("alpha", 255)
                    )
                    animator.target = layers[1]
                    animator.duration = 2000
                    animator.start()
                }

                override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                    Toast.makeText(this@HomeActivity, e?.localizedMessage, Toast.LENGTH_SHORT)
                        .show()

                }

                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {

                }
            }
            Picasso.get()
                .load(R.drawable.logo_skipit)
                .resize(0, actionBarHeight).into(logoTarget)
        } else {
            (activity as AppCompatActivity).supportActionBar!!.setBackgroundDrawable(
                ColorDrawable(
                    resources.getColor(R.color.colorPrimary)
                )
            )
        }
    }

    fun showActionBarLogo(activity: Activity, show: Boolean) {
        if (show) {
            // Calculate Action bar height
            var actionBarHeight = 200
            val tv = TypedValue()
            if (activity.theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
                actionBarHeight = TypedValue.complexToDimensionPixelSize(
                    tv.data,
                    activity.resources.displayMetrics
                )
            }

            // Using action bar background drawable
            logoTarget = object : com.squareup.picasso.Target {
                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                    val layers = arrayOfNulls<Drawable>(2)
                    layers[0] =
                        ColorDrawable(resources.getColor(R.color.colorPrimary)) // Background color of Action bar
                    val bd = BitmapDrawable(activity.resources, bitmap)
                    bd.gravity = Gravity.CENTER
                    val drawLogo: Drawable = bd
                    layers[1] = drawLogo // Bitmap logo of Action bar (loaded from Picasso)
                    val layerDrawable = LayerDrawable(layers)
                    layers[1]!!.alpha = 0
                    (activity as AppCompatActivity).supportActionBar!!.setBackgroundDrawable(
                        layerDrawable
                    )
                    val animator = ObjectAnimator.ofPropertyValuesHolder(
                        layers[1],
                        PropertyValuesHolder.ofInt("alpha", 255)
                    )
                    animator.target = layers[1]
                    animator.duration = 0
                    animator.start()
                }

                override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                    Toast.makeText(this@HomeActivity, e?.localizedMessage, Toast.LENGTH_SHORT)
                        .show()

                }

                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {

                }
            }
            Picasso.get()
                .load(R.drawable.logo_skipit)
                .resize(0, actionBarHeight).into(logoTarget)
        } else {
            (activity as AppCompatActivity).supportActionBar!!.setBackgroundDrawable(
                ColorDrawable(
                    resources.getColor(R.color.colorPrimary)
                )
            )
        }
    }

    private fun saveFcm() {
        FirebaseApp.initializeApp(this)

        if (preferences?.getLogin() == true) {
            FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener { instanceIdResult: InstanceIdResult ->

                if (instanceIdResult.token == "") {

                    Log.d("instanceIdResult", "token vazio")
                }

                if (Preferences(this).getInstanceTokenFcm() == instanceIdResult.token) {
                    var fcmpose = Preferences(this).getInstanceTokenFcm()
                    Log.d("instanceIdResult", "não salvou")
                    Log.d("instanceIdResult", fcmpose)

                } else {


                    val fcm = Fcm()

                    fcm.id_user = Preferences(this).getUserData()?.id.toString()
                    fcm.type = "1"
                    fcm.registration_id = instanceIdResult.token

                    Preferences(this).saveInstanceTokenFcm("tokenFcm", instanceIdResult.token)

                    val fcmControl = FcmControl(this, object : WSResult {
                        override fun resultFcm(response: Fcm?, type: String?) {
                            Log.d("instanceIdResult", "salve1")
                        }


                    })

                    fcmControl.saveFcm(fcm)

                }
            }

        }
    }


    private fun hideBottomNavigation() { //Hide bottom navigation
        binding.mainBottomNavigationView.visibility = View.GONE

    }

    private fun showBothNavigation() {
        binding.mainBottomNavigationView.visibility = View.VISIBLE

        setupNavControl() //To configure navController with drawer and bottom navigation
    }

    private fun setupNavControl() {
        binding.mainBottomNavigationView.setupWithNavController(navController) //Setup Bottom navigation with navController
    }

    fun exitApp() { //To exit the application call this function from fragment
        this.finishAffinity()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = navController
        return when (navController.currentDestination?.id) {
            R.id.homeFragment -> {
                finish()
                true
            }
            else -> navController.navigateUp()
        }
        //Setup appBarConfiguration for back arrow
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }


    override fun onBackPressed() {
        /*  if (Jzvd.backPress()) {
              return
          }*/
        super.onBackPressed()
    }

    override fun onPause() {
        super.onPause()
//        Jzvd.releaseAllVideos()
    }
}

