package com.example.collageapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.collageapp.database.CollegeDatabase
import com.example.collageapp.database.DatabaseDao
import com.example.collageapp.databinding.ActivityEditSpecialityBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditSpecialityActivity : AppCompatActivity() {
    private lateinit var binding:ActivityEditSpecialityBinding
    private lateinit var databaseDao: DatabaseDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditSpecialityBinding.inflate(layoutInflater)
        val speciality = intent.getStringExtra("SPECIALITY")
        databaseDao = CollegeDatabase.getDatabase(this).databaseDao()
        setContentView(binding.root)
        binding.applyChanges.setOnClickListener{
            if (binding.changeSpecialityEd.text.isEmpty()){
                Toast.makeText(this,"Вы не ввели название",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            lifecycleScope.launch(Dispatchers.IO){
                val studentsList = databaseDao.getAllStudents()
                databaseDao.updateSpecialityName(speciality.toString(),binding.changeSpecialityEd.text.toString())
                for(currStudent in studentsList){
                    if(currStudent.speciality==speciality)
                        currStudent.speciality = binding.changeSpecialityEd.text.toString()
                }
                databaseDao.insertStudents(studentsList)
                runOnUiThread{
                    Toast.makeText(this@EditSpecialityActivity,"Вы изменили специальность!",Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }
}