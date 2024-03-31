package com.example.collageapp.ui.student

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
import com.example.collageapp.databinding.ActivityStudentBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StudentActivity : AppCompatActivity() {
    private lateinit var binding:ActivityStudentBinding
    private lateinit var databaseDao: DatabaseDao
    private lateinit var adapter: TeachersAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        databaseDao = CollegeDatabase.getDatabase(this).databaseDao()
        adapter = TeachersAdapter()
        binding.teacherInfoRecycler.layoutManager = LinearLayoutManager(this)
        binding.teacherInfoRecycler.adapter = adapter
        lifecycleScope.launch(Dispatchers.IO){
            val teacherList = databaseDao.getAllTeachers()
            withContext(Dispatchers.Main){
                adapter.setAllData(teacherList)
            }
        }
        binding.searchTeacherBt.setOnClickListener{
            if(binding.searchTeacherByNameEd.text.isEmpty()){
                Toast.makeText(this@StudentActivity, "Вы не ввели имя", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            lifecycleScope.launch(Dispatchers.IO){
                val teacherName = binding.searchTeacherByNameEd.text.toString()
                val teacherList = databaseDao.searchTeachersByName(teacherName)
                Log.d("error",teacherName)
                Log.d("error",teacherList.toString())
                withContext(Dispatchers.Main){
                    adapter.setData(teacherList)
                }
                if(teacherList.isNullOrEmpty()){
                    runOnUiThread{
                        Toast.makeText(this@StudentActivity,"Такого учителя нет",Toast.LENGTH_SHORT).show()
                    }
                }
            }
            binding.cancelButton.visibility = View.VISIBLE
//            val intent = Intent(this@StudentActivity, TeacherInfoActivity::class.java)
//            intent.putExtra("teacherName",binding.searchTeacherByNameEd.text.toString())
//            startActivity(intent)
        }
        binding.cancelButton.setOnClickListener{
            lifecycleScope.launch(Dispatchers.IO){
                val teacherList = databaseDao.getAllTeachers()
                withContext(Dispatchers.Main){
                    adapter.setAllData(teacherList)
                }
            }
            binding.cancelButton.visibility = View.INVISIBLE
        }
    }
}