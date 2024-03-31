package com.example.collageapp.ui.teacher

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.collageapp.database.CollegeDatabase
import com.example.collageapp.database.DatabaseDao
import com.example.collageapp.databinding.ActivityTeacherBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TeacherActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTeacherBinding
    private lateinit var databaseDao: DatabaseDao
    private lateinit var adapter: StudentAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeacherBinding.inflate(layoutInflater)
        setContentView(binding.root)
        databaseDao = CollegeDatabase.getDatabase(this).databaseDao()
        adapter = StudentAdapter()
        binding.studentInfoRecycler.layoutManager = LinearLayoutManager(this)
        binding.studentInfoRecycler.adapter = adapter
        lifecycleScope.launch(Dispatchers.IO){
            val studentsList = databaseDao.getStudentsWithSpeciality()
            withContext(Dispatchers.Main){
                adapter.setAllData(studentsList)
            }
        }


        binding.searchStudentBt.setOnClickListener {
            if (binding.searchStudentByNameEd.text.isEmpty()) {
                Toast.makeText(this@TeacherActivity, "Вы не ввели имя", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            lifecycleScope.launch(Dispatchers.IO){
                val studentName = binding.searchStudentByNameEd.text.toString()
                val studentsList = databaseDao.getStudentsWithSpecialityByName(studentName)
                Log.d("error",studentName)
                Log.d("error",studentsList.toString())
                withContext(Dispatchers.Main){
                    adapter.setData(studentsList)
                }
                if(studentsList.isNullOrEmpty()){
                    runOnUiThread{
                        Toast.makeText(this@TeacherActivity,"Такого студента нет",Toast.LENGTH_SHORT).show()
                    }
                }
            }
            binding.cancelButton.visibility = View.VISIBLE
//            val intent = Intent(this@TeacherActivity, StudentInfoActivity::class.java)
//            intent.putExtra("studentName", binding.searchStudentByNameEd.text.toString())
//            startActivity(intent)

        }
        binding.cancelButton.setOnClickListener{
            lifecycleScope.launch(Dispatchers.IO){
                val studentsList = databaseDao.getStudentsWithSpeciality()
                withContext(Dispatchers.Main){
                    adapter.setAllData(studentsList)
                }
            }
            binding.cancelButton.visibility = View.INVISIBLE
        }
    }
}
