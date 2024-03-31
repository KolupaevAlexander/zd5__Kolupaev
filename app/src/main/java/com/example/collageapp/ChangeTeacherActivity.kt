package com.example.collageapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.collageapp.database.CollegeDatabase
import com.example.collageapp.database.DatabaseDao
import com.example.collageapp.database.Teacher
import com.example.collageapp.databinding.ActivityChangeStudentBinding
import com.example.collageapp.databinding.ActivityChangeTeacherBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import kotlin.properties.Delegates

class ChangeTeacherActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangeTeacherBinding
    private lateinit var databaseDao: DatabaseDao
    private lateinit var error: String
    private var teacherId by Delegates.notNull<Long>()
    private var teacherLogin by Delegates.notNull<String>()
    private var teacherPassword by Delegates.notNull<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangeTeacherBinding.inflate(layoutInflater)
        setContentView(binding.root)
        databaseDao = CollegeDatabase.getDatabase(this).databaseDao()
        teacherId = intent.getLongExtra("teacherId",-1)
        teacherLogin = intent.getStringExtra("loginTeacher").toString()
        teacherPassword = intent.getStringExtra("passwordTeacher").toString()
        Log.d("error",teacherId.toString())
        val groupAdapter = ArrayAdapter(
            this, android.R.layout.simple_spinner_item, emptyArray<String>()
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        binding.changeSpecialitiesSpinner.adapter = groupAdapter
        loadGroups()
        binding.changeTeacherBt.setOnClickListener {
            try {
                if (binding.changeTeacherNameEd.text.isEmpty() || binding.changeHoursPerYearTeacher.text.isEmpty() || binding.changeSpecialitiesSpinner.selectedItemPosition == -1) {
                    Toast.makeText(
                        this@ChangeTeacherActivity,
                        "Все поля должны быть заполнены",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
                if (binding.changeHoursPerYearTeacher.text.toString().toInt() < 0 ||
                    binding.changeHoursPerYearTeacher.text.toString().toInt() > 2000) {
                    Toast.makeText(
                        this@ChangeTeacherActivity,
                        "Количество часов должно быть от 0 до 2000",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
                lifecycleScope.launch(Dispatchers.IO) {

                    val teacher = Teacher(
                        name = binding.changeTeacherNameEd.text.toString(),
                        login = teacherLogin,
                        password = teacherPassword,
                        speciality = binding.changeSpecialitiesSpinner.selectedItem.toString(),
                        salary = 0.0,
                        hoursPerYear = binding.changeHoursPerYearTeacher.text.toString().toInt()
                    )
                    if (isTeacherUnique(teacher)) {
                        databaseDao.updateTeacher(teacherId, teacherLogin, teacherPassword, teacher.name, teacher.salary, teacher.hoursPerYear, teacher.speciality)
                        finish()
                    } else {
                        runOnUiThread {
                            Toast.makeText(this@ChangeTeacherActivity, error, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(this, "Вы ввели не число", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadGroups() {
        lifecycleScope.launch(Dispatchers.IO) {
            val groupsFromDb = databaseDao.getAllSpecialities()
            val groupNames = groupsFromDb.map { it.speciality }

            val groupNamesList = ArrayList<String>(groupNames)
            withContext(Dispatchers.Main) {
                val newAdapter = ArrayAdapter(
                    this@ChangeTeacherActivity, android.R.layout.simple_spinner_item, groupNamesList
                ).apply {
                    setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                }
                binding.changeSpecialitiesSpinner.adapter = newAdapter
            }
        }
    }

    private suspend fun isTeacherUnique(teacher: Teacher): Boolean {
        val teacherList = databaseDao.searchTeachersByName(teacher.name)
        for (item in teacherList) {
            if(item.teacherId==teacherId)
                continue
            if (item.speciality == teacher.speciality) {
                error = "Преподаватель уже ведет у этой специальности"
                return false
            }
        }
        return true;
    }
}
