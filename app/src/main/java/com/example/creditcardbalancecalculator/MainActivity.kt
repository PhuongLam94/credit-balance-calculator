package com.example.creditcardbalancecalculator

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import java.security.Permission

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        askForPermission(Manifest.permission.READ_SMS)

    }

    private fun askForPermission(vararg permissions:String) {
        var deniedPermissions = ArrayList<String>()
        for (permission in permissions)
            if (ContextCompat.checkSelfPermission(this, permission    )
                != PackageManager.PERMISSION_GRANTED) {
                deniedPermissions.add(permission)
            }
        if (!deniedPermissions.isEmpty()) {
            val array = arrayOfNulls<String>(deniedPermissions.size)
            deniedPermissions.toArray(array)
            requestPermissions(array, 1)
        }
    }
}
