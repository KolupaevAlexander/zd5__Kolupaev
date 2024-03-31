package com.example.collageapp.ui.admin.teachers

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.collageapp.AddTeacherActivity
import com.example.collageapp.ChangeStudentActivity
import com.example.collageapp.ChangeTeacherActivity
import com.example.collageapp.database.CollegeDatabase
import com.example.collageapp.database.DatabaseDao
import com.example.collageapp.databinding.FragmentTeachersBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TeachersFragment : Fragment() {
    private lateinit var binding: FragmentTeachersBinding
    private lateinit var adapter: TeacherAdapter
    private lateinit var databaseDao: DatabaseDao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTeachersBinding.inflate(layoutInflater, container, false)
        databaseDao = CollegeDatabase.getDatabase(requireContext()).databaseDao()
        adapter = TeacherAdapter()
        binding.teachersRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.teachersRecycler.adapter = adapter
        lifecycleScope.launch(Dispatchers.IO) {
            val studentsList = databaseDao.getAllTeachers()
            withContext(Dispatchers.Main) {
                adapter.setData(studentsList)
            }
        }
        binding.addTeacherButton.setOnClickListener {
            startActivity(Intent(requireContext(), AddTeacherActivity::class.java))
        }
        adapter.setOnDeleteClickListener { position ->
            onDeleteTeacher(position)
        }
        adapter.setOnChangeClickListener { position ->
            onChangeTeacher(position)
        }
        return binding.root
    }

    private fun onDeleteTeacher(position: Int) {
        lifecycleScope.launch(Dispatchers.IO) {
            val teacher = adapter.getTeachersList().getOrNull(position)
            teacher?.let {
                databaseDao.deleteTeacher(it)
                val updatedTeachers = databaseDao.getAllTeachers()
                withContext(Dispatchers.Main) {
                    adapter.setData(updatedTeachers)
                }
            }
        }
    }

    private fun onChangeTeacher(position: Int) {
        val teacher = adapter.getTeachersList().getOrNull(position)
        val intent = Intent(requireContext(),ChangeTeacherActivity::class.java)

        Log.d("error",teacher.toString())
        if (teacher != null) {
            intent.putExtra("loginTeacher", teacher.login)
            intent.putExtra("passwordTeacher", teacher.password)
            intent.putExtra("teacherId",teacher.teacherId)
            Log.d("error",teacher.teacherId.toString())
            startActivity(intent)
        }
    }

}