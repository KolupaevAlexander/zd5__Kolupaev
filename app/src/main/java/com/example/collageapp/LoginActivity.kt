// LoginActivity.kt
package com.example.collageapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.collageapp.database.CollegeDatabase
import com.example.collageapp.database.DatabaseDao
import com.example.collageapp.databinding.ActivityLoginBinding
import com.example.collageapp.ui.admin.AdminActivity
import com.example.collageapp.ui.student.StudentActivity
import com.example.collageapp.ui.teacher.TeacherActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var databaseDao: DatabaseDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        databaseDao = CollegeDatabase.getDatabase(this).databaseDao()

        binding.buttonLogin.setOnClickListener{
            val login = binding.editTextLogin.text.toString()
            val password = binding.editTextPassword.text.toString()
            if(login.isBlank() || password.isBlank()){
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(login == "admin" && password == "admin"){
                startActivity(Intent(this, AdminActivity::class.java))
                return@setOnClickListener
            }



            lifecycleScope.launch(Dispatchers.IO) {

                val student = databaseDao.getStudentByLogin(login)
                val teacher = databaseDao.getTeacherByLogin(login)
                if(student != null){
                    if(student.password == password){
                        val sharedPreferences = getSharedPreferences("college", Context.MODE_PRIVATE)
                        sharedPreferences.edit().apply{
                            putLong("id", student.studentId)
                            putString("login", student.login)
                            putString("password", student.password)
                            apply()
                        }
                        startActivity(Intent(this@LoginActivity, StudentActivity::class.java))
                        runOnUiThread {
                            Toast.makeText(this@LoginActivity, "Авторизация успешна", Toast.LENGTH_SHORT).show()
                        }
                    }
                    else{
                        runOnUiThread {
                            Toast.makeText(this@LoginActivity, "Пароль не совпадает", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                else if(teacher != null){
                    if(teacher.password == password){
                        val sharedPreferences = getSharedPreferences("college", Context.MODE_PRIVATE)
                        sharedPreferences.edit().apply{
                            putLong("id", teacher.teacherId)
                            putString("login", teacher.login)
                            putString("password", teacher.password)
                            apply()
                        }
                        startActivity(Intent(this@LoginActivity, TeacherActivity::class.java))
                        runOnUiThread {
                            Toast.makeText(this@LoginActivity, "Авторизация успешна", Toast.LENGTH_SHORT).show()
                        }
                    }
                    else{
                        runOnUiThread {
                            Toast.makeText(this@LoginActivity, "Пароль не совпадает", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                else{
                    runOnUiThread {
                        Toast.makeText(this@LoginActivity, "Пользователь с такими данными не существуют", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
