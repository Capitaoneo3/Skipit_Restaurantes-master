package br.com.app5m.skipitrestaurantes.ui.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import br.com.app5m.skipitrestaurantes.R
import br.com.app5m.skipitrestaurantes.helper.Preferences
import kotlinx.android.synthetic.main.activity_permission_localization.*

class PermissionLocalizationAct : AppCompatActivity() {

    private val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission_localization)
        negative_bt.setOnClickListener {
            Preferences(this).setLocationAux("1")
            finish()
        }
        allow_bt.setOnClickListener {
            Preferences(this).setLocationAux("1")

            this.requestPermissions(permissions, 1)
        }

    }

    override fun onRestart() {
        super.onRestart()
 /*       val mIntent = Intent(this, MainAct::class.java)
        startActivity(mIntent)
        finishAffinity()*/
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        for (permission in permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                //denied
                Toast.makeText(this,
                    "Por favor, aceite todas as permissões necessárias!", Toast.LENGTH_SHORT).show()
            } else {
                if (ActivityCompat.checkSelfPermission(this, permission)
                    == PackageManager.PERMISSION_GRANTED
                ) {
                    //allowed
                    finish()
                } else {
                    //never ask again
                    Toast.makeText(this,
                        "Por favor, aceite todas as permissões necessárias!", Toast.LENGTH_SHORT).show()
                    startInstalledAppDetailsActivity(this)
                }
            }
        }
    }

    private fun startInstalledAppDetailsActivity(activity: Activity?) {
        if (activity == null) {
            return
        }
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri = Uri.fromParts("package", activity.packageName, null)
        intent.data = uri
        activity.startActivity(intent)
    }
}