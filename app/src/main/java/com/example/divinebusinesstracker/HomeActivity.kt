package com.example.divinebusinesstracker

import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_home.*
import androidx.fragment.app.FragmentManager
import com.example.divinebusinesstracker.fragments.HomeFragment


public class HomeActivity : AppCompatActivity()/*,NavigationView.OnNavigationItemSelectedListener*/{
    private lateinit var navControler : NavController
    private lateinit var fragmentManager: FragmentManager
    private lateinit var constants: Constants
    private val homeFragment:HomeFragment = HomeFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(homeActivity_toolbar)
        constants = Constants(this)
        val actionBar = supportActionBar
        actionBar!!.title = "Welcome"
        fragmentManager = supportFragmentManager
        actionBar.subtitle = "to Divine Business Tracker"
        actionBar.setDisplayShowHomeEnabled(true)
        actionBar.setDisplayUseLogoEnabled(true)
        val toggle = ActionBarDrawerToggle(
                this, drawerlayout, homeActivity_toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close)
        drawerlayout.addDrawerListener(toggle)
        toggle.syncState()
       // drawer_navigation_view.setNavigationItemSelectedListener(this)
        navControler = Navigation.findNavController(this,R.id.fragment)
        drawer_navigation_view.setupWithNavController(navControler)
    }
    private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {

        if (drawerlayout.isDrawerOpen(GravityCompat.START)) {
            drawerlayout.closeDrawer(GravityCompat.START)
        }
        else if (Constants.isHomevisible()){
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed()
                return
            }
            this.doubleBackToExitPressedOnce = true
            Toast.makeText(this, "Please click BACK again to go back to HOME or exit", Toast.LENGTH_SHORT).show()
            Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
            //super.onBackPressed()
        }else{
            super.onBackPressed()
        }

    }



}
