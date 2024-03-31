package com.example.collageapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.collageapp.database.CollegeDatabase
import com.example.collageapp.database.DatabaseDao
import com.example.collageapp.database.Speciality
import com.example.collageapp.databinding.ActivityAddSpecialityBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddSpecialityActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddSpecialityBinding
    private lateinit var databaseDao: DatabaseDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddSpecialityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        databaseDao = CollegeDatabase.getDatabase(this).databaseDao()
        binding.addSpecialityToDbBt.setOnClickListener {
            addSpecialityToDatabase()
        }

    }

    private fun addSpecialityToDatabase() {
        val specialityName = binding.specialityEd.text.toString()
        if (specialityName.isEmpty()) {
            Toast.makeText(
                this@AddSpecialityActivity,
                "Вы не указали специальность",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        lifecycleScope.launch(Dispatchers.IO) {
            // Добавление новой группы в базу данных
            val speciality = Speciality(specialityName)
            databaseDao.insertSpeciality(speciality)

            // Возвращение к предыдущей Activity после добавления группы
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    this@AddSpecialityActivity,
                    "Вы добавили специальность",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }
}