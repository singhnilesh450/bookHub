package com.example.bookhub.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.bookhub.*
import com.example.bookhub.fragments.Aboutapp
import com.example.bookhub.fragments.Dashboard
import com.example.bookhub.fragments.Favourits
import com.example.bookhub.fragments.Profile
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    lateinit var drawerLayout:DrawerLayout
    lateinit var toolbar:androidx.appcompat.widget.Toolbar
    lateinit var frameLayout:FrameLayout
    lateinit var navigationView:NavigationView
    lateinit var coordinatorLayout:CoordinatorLayout

    var previousMenuItem:MenuItem?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout=findViewById(R.id.drawer_layout)
        toolbar=findViewById(R.id.toolbar)
        frameLayout=findViewById(R.id.frame_layout)
        navigationView=findViewById(R.id.nav_view)
        coordinatorLayout=findViewById(R.id.coordinator_layout)
     setuptoolbar()
openDashboard()
        val actionBarDrawerToggle=ActionBarDrawerToggle(
            this@MainActivity,
            drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
       navigationView.setNavigationItemSelectedListener {

           if(previousMenuItem!=null){
               previousMenuItem?.isChecked=false
           }

           it.isCheckable=true
           it.isChecked=true
           previousMenuItem=it

           when(it.itemId){
               R.id.dashboard ->{
                  supportFragmentManager.beginTransaction()
                      .replace(
                          R.id.frame_layout,
                          Dashboard()
                      )
                      .commit()

                   supportActionBar?.title="Dashboard"
                   drawerLayout.closeDrawers()
               }
               R.id.favourites ->{
                   supportFragmentManager.beginTransaction()
                       .replace(
                           R.id.frame_layout,
                           Favourits()
                       )
                       .commit()

                   supportActionBar?.title="Favourites"
                   drawerLayout.closeDrawers()
               }
               R.id.profile ->{
                   supportFragmentManager.beginTransaction()
                       .replace(
                           R.id.frame_layout,
                           Profile()
                       )
                       .commit()

                   supportActionBar?.title="Profile"
                   drawerLayout.closeDrawers()
               }
               R.id.aboutapp ->{
                   supportFragmentManager.beginTransaction()
                       .replace(
                           R.id.frame_layout,
                           Aboutapp()
                       )
                       .commit()

                   supportActionBar?.title="About app"
                   drawerLayout.closeDrawers()
               }
           }

           return@setNavigationItemSelectedListener true
       }
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onBackPressed() {
        val frag=supportFragmentManager.findFragmentById(R.id.frame_layout)
        when(frag) {
            !is Dashboard -> openDashboard()

           else -> super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id=item.itemId
        if(id==android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START)

        }

        return super.onOptionsItemSelected(item)
    }

    fun setuptoolbar(){
        setSupportActionBar(toolbar)
        supportActionBar?.title="Toolbar title"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    fun openDashboard(){
        val fragment = Dashboard()
        val transaction=supportFragmentManager.beginTransaction().replace(
            R.id.frame_layout,
            Dashboard()
        )
        transaction.commit()

        supportActionBar?.title="Dashboard"
        navigationView.setCheckedItem(R.id.dashboard)
    }
}