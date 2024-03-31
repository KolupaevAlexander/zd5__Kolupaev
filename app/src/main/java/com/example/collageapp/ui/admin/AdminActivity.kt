package com.example.collageapp.ui.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.collageapp.R
import com.example.collageapp.ui.admin.specialities.SpecialitiesFragment
import com.example.collageapp.ui.admin.students.StudentsFragment
import com.example.collageapp.ui.admin.teachers.TeachersFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class AdminActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        bottomNavigationView = findViewById(R.id.bottomNavigation) // Update this line
        bottomNavigationView.inflateMenu(R.menu.admin_bottom_navigation_menu)

        // Загрузка StudentsFragment при запуске
        loadFragment(StudentsFragment())

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_students -> loadFragment(StudentsFragment())
                R.id.navigation_teachers -> loadFragment(TeachersFragment())
                R.id.navigation_specialities->loadFragment(SpecialitiesFragment())
            }
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment).commit()
    }
}
