package com.example.collageapp

import android.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.collageapp.database.CollegeDatabase
import com.example.collageapp.database.DatabaseDao
import com.example.collageapp.databinding.ActivityAddStudentBinding
import androidx.lifecycle.lifecycleScope
import com.example.collageapp.database.Student
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.launch
import java.util.Calendar


class AddStudentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStudentBinding
    private lateinit var databaseDao: DatabaseDao
    private lateinit var error:String
    private lateinit var calendar: Calendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        databaseDao = CollegeDatabase.getDatabase(this).databaseDao()

        val groupAdapter = ArrayAdapter(
            this,
            R.layout.simple_spinner_item,
            emptyArray<String>()
        ).apply {
            setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        }
        binding.groupSpinner.adapter = groupAdapter

        binding.addStudentButton.setOnClickListener {
            addStudentToDatabase()
        }
        loadGroups()
    }

    private fun loadGroups() {
        lifecycleScope.launch(Dispatchers.IO) {
            val groupsFromDb = databaseDao.getAllSpecialities()
            val groupNames = groupsFromDb.map { it.speciality }

            val groupNamesList = ArrayList<String>(groupNames)

            withContext(Dispatchers.Main) {
                val newAdapter = ArrayAdapter(
                    this@AddStudentActivity,
                    R.layout.simple_spinner_item,
                    groupNamesList
                ).apply {
                    setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
                }
                binding.groupSpinner.adapter = newAdapter
            }
            val calendar = Calendar.getInstance()
            calendar.set(1995, Calendar.JANUARY, 1)
            val minDate = calendar.timeInMillis
            calendar.set(2010, Calendar.DECEMBER, 31)
            val maxDate = calendar.timeInMillis

            runOnUiThread {
                binding.studentBirthdayD.minDate = minDate
                binding.studentBirthdayD.maxDate = maxDate
            }
        }
    }
    private suspend fun isStudentUnique(student: Student): Boolean {
        val studentsList = databaseDao.searchStudentsByName(student.name)
        var hasBudgetPlace = false

        for (existingStudent in studentsList) {
            if(existingStudent.speciality==student.speciality) {
                error = "Студент уже учится на этой специальности"
                return false
            }
            if(existingStudent.isBudget)
                hasBudgetPlace=true;
        }
        if(student.isBudget && hasBudgetPlace){
            error = "У студента уже имеется бюджетное место"
            return false
        }

        // Нет совпадений, студент уникален
        return true
    }
    private fun addStudentToDatabase() {
        val studentName = binding.studentNameEd.text.toString()
        val birthday = "${binding.studentBirthdayD.year}-${binding.studentBirthdayD.month + 1}-${binding.studentBirthdayD.dayOfMonth}"
        if(binding.groupSpinner.selectedItemPosition==-1){
            Toast.makeText(this@AddStudentActivity,"Вы не выбрали группу",Toast.LENGTH_SHORT).show()
            return;
        }
        val groupName = binding.groupSpinner.selectedItem.toString() // Имя группы из Spinner
        val courseText = binding.courseEd.text.toString()
        val isBudget = binding.isBudgetCheckbox.isChecked
        var photoUrl = binding.photoEd.text.toString()


        if (studentName.isBlank() || groupName.isBlank() || courseText.isBlank() || photoUrl.isBlank()) {

            Toast.makeText(this, "Все поля должны быть заполнены", Toast.LENGTH_SHORT).show()
            return
        }

        val course = courseText.toIntOrNull() ?: 1
        if (course<1||course>=5){
            Toast.makeText(this,"Курс должен быть больше 0 и меньше 5",Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch(Dispatchers.IO) {
            val fullName = binding.studentNameEd.text.toString()
            val newLogin = fullName.toLowerCase().replace(" ", "_")
            val student = Student(
                name = studentName,
                login = newLogin,
                password = generateRandomPassword(6),
                birthday = birthday,
                speciality = groupName,
                course = course,
                isBudget = isBudget,
                photo = photoUrl)
            if(isStudentUnique(student)) {
                databaseDao.insertStudent(student)
                finish()
            }
            else{
                runOnUiThread{
                    Toast.makeText(this@AddStudentActivity,error,Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun generateRandomPassword(length: Int):String{
        val charPool = "abcdefghijklmnopqrstuvwxyz"
        return (1..length).map{charPool.random()}.joinToString("")
    }

}

