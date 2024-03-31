package com.example.collageapp

import android.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.collageapp.database.CollegeDatabase
import com.example.collageapp.database.DatabaseDao
import com.example.collageapp.database.Student
import com.example.collageapp.databinding.ActivityChangeStudentBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.properties.Delegates

class ChangeStudentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangeStudentBinding
    private lateinit var databaseDao: DatabaseDao
    private lateinit var calendar: Calendar
    private lateinit var error: String
    private var studentId by Delegates.notNull<Long>()
    private var studentLogin by Delegates.notNull<String>()
    private var studentPassword by Delegates.notNull<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangeStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        databaseDao = CollegeDatabase.getDatabase(this).databaseDao()
        studentId = intent.getStringExtra("studentId")?.toLongOrNull()!!
        studentLogin = intent.getStringExtra("loginStudent").toString()
        studentPassword = intent.getStringExtra("passwordStudent").toString()
        val groupAdapter = ArrayAdapter(
            this, R.layout.simple_spinner_item, emptyArray<String>()
        ).apply {
            setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        }
        binding.changeSpecialitySpinner.adapter = groupAdapter
        loadGroups()
        binding.changeStudentButton.setOnClickListener {
            if (binding.changeCourseEd.text.isEmpty() || binding.changeStudentNameEd.text.isEmpty() || binding.ChangephotoEd.text.isEmpty() || binding.changeSpecialitySpinner.selectedItemPosition == -1) {
                Toast.makeText(this, "Вы не заполнили все поля", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val student = Student(
                        name = binding.changeStudentNameEd.text.toString(),
                        login = studentLogin,
                        password = studentPassword,
                        birthday = formatDate(
                            binding.changeStudentBirthdayD.year,
                            binding.changeStudentBirthdayD.month,
                            binding.changeStudentBirthdayD.dayOfMonth
                        ),
                        speciality = binding.changeSpecialitySpinner.selectedItem.toString(),
                        course = binding.changeCourseEd.text.toString().toInt(),
                        isBudget = binding.changeIsBudgetCheckbox.isChecked,
                        photo = binding.ChangephotoEd.text.toString(),
                        studentId = studentId
                    )
                    if(!isStudentUnique(student)){
                        runOnUiThread {
                            Toast.makeText(this@ChangeStudentActivity, error, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                    else {
                        databaseDao.updateStudent(
                            studentId = student.studentId,
                            name = student.name,
                            login = studentLogin,
                            password = studentPassword,
                            birthday = student.birthday,
                            speciality = student.speciality,
                            course = student.course,
                            isBudget = student.isBudget,
                            photo = student.photo!!
                        )

                        withContext(Dispatchers.Main) {
                            finish()
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@ChangeStudentActivity,
                            "Неверный формат числа",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

        }
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
                    this@ChangeStudentActivity, R.layout.simple_spinner_item, groupNamesList
                ).apply {
                    setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
                }
                binding.changeSpecialitySpinner.adapter = newAdapter
            }
            calendar = Calendar.getInstance()
            binding.changeStudentBirthdayD.maxDate = calendar.timeInMillis
            binding.changeStudentBirthdayD.init(
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(
                    Calendar.DAY_OF_MONTH
                ), null
            )
        }
    }

    private suspend fun isStudentUnique(student: Student): Boolean {
        val studentsList = databaseDao.searchStudentsByName(student.name)
        var hasBudgetPlace = false

        for (existingStudent in studentsList) {
            if(existingStudent.studentId == studentId)
                continue
            if (existingStudent.speciality == student.speciality) {
                error = "Студент уже учится на этой специальности"
                return false
            }
            if (existingStudent.isBudget) hasBudgetPlace = true;
        }
        if (student.isBudget && hasBudgetPlace) {
            error = "У студента уже имеется бюджетное место"
            return false
        }

        // Нет совпадений, студент уникален
        return true
    }

    private fun formatDate(year: Int, month: Int, day: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }
}