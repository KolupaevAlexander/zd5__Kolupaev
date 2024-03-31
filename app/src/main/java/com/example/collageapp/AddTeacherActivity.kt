package com.example.collageapp

import android.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.collageapp.database.CollegeDatabase
import com.example.collageapp.database.DatabaseDao
import com.example.collageapp.database.Teacher
import com.example.collageapp.databinding.ActivityAddStudentBinding
import com.example.collageapp.databinding.ActivityAddTeacherBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar

class AddTeacherActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddTeacherBinding
    private lateinit var databaseDao: DatabaseDao
    private lateinit var error: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTeacherBinding.inflate(layoutInflater)
        setContentView(binding.root)
        databaseDao = CollegeDatabase.getDatabase(this).databaseDao()

        val groupAdapter = ArrayAdapter(
            this,
            R.layout.simple_spinner_item,
            emptyArray<String>()
        ).apply {
            setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        }
        binding.specialitiesSpinner.adapter = groupAdapter
        binding.addTeacherBt.setOnClickListener {
            addTeacherToDatabase()
        }
        loadGroups()
    }

    private fun loadGroups() {
        lifecycleScope.launch(Dispatchers.IO) {
            val groupsFromDb = databaseDao.getAllSpecialities()
            val groupNames = groupsFromDb.map { it.speciality }

            // Use ArrayList instead of an array
            val groupNamesList = ArrayList<String>(groupNames)

            // Switch to the main thread for UI updates
            withContext(Dispatchers.Main) {
                val newAdapter = ArrayAdapter(
                    this@AddTeacherActivity,
                    R.layout.simple_spinner_item,
                    groupNamesList
                ).apply {
                    setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
                }
                binding.specialitiesSpinner.adapter = newAdapter
            }
        }
    }

    private fun addTeacherToDatabase() {
        try {
            if (binding.specialitiesSpinner.selectedItemPosition == -1 || binding.teacherNameEd.text.toString()
                    .isEmpty() || binding.hoursPerYearTeacher.text.isEmpty()
            ) {
                Toast.makeText(
                    this@AddTeacherActivity,
                    "Вы не заполнили все поля",
                    Toast.LENGTH_SHORT
                )
                    .show()
                return
            }
            if (binding.hoursPerYearTeacher.text.toString().toInt() < 0 ||
                binding.hoursPerYearTeacher.text.toString().toInt() > 2000) {
                Toast.makeText(
                    this@AddTeacherActivity,
                    "Количество часов должно быть от 0 до 2000",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }
            lifecycleScope.launch(Dispatchers.IO) {
                val fullName = binding.teacherNameEd.text.toString()
                val newLogin = fullName.toLowerCase().replace(" ", "_")
                val teacher = Teacher(
                    name = fullName,
                    login = newLogin,
                    password = generateRandomPassword(6),
                    speciality = binding.specialitiesSpinner.selectedItem.toString(),
                    salary = 0.0,
                    hoursPerYear = binding.hoursPerYearTeacher.text.toString().toInt()
                )
                if (isTeacherUnique(teacher)) {
                    databaseDao.insertTeacher(teacher)
                    finish()
                } else {
                    runOnUiThread {
                        Toast.makeText(this@AddTeacherActivity, error, Toast.LENGTH_SHORT).show()
                    }
                }
            }

        } catch (e: Exception) {
            Toast.makeText(this@AddTeacherActivity, "Вы ввели не число", Toast.LENGTH_SHORT).show()
        }

    }
    private fun generateRandomPassword(length: Int):String{
        val charPool = "abcdefghijklmnopqrstuvwxyz"
        return (1..length).map{charPool.random()}.joinToString("")
    }
    private suspend fun isTeacherUnique(teacher: Teacher): Boolean {
        val teacherList = databaseDao.searchTeachersByName(teacher.name)
        for (item in teacherList) {
            if (item.speciality == teacher.speciality) {
                error = "Преподаватель уже ведет у этой специальности"
                return false
            }
        }
        return true;
    }

}