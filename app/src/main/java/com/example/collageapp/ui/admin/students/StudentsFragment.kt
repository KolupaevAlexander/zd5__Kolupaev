package com.example.collageapp.ui.admin.students

import android.R
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.collageapp.AddStudentActivity
import com.example.collageapp.ChangeStudentActivity
import com.example.collageapp.database.CollegeDatabase
import com.example.collageapp.database.DatabaseDao
import com.example.collageapp.database.StudentWithSpeciality
import com.example.collageapp.databinding.FragmentStudentsBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StudentsFragment : Fragment() {
    private lateinit var binding: FragmentStudentsBinding
    private lateinit var adapter: StudentsAdapter
    private lateinit var databaseDao: DatabaseDao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStudentsBinding.inflate(layoutInflater, container, false)
        databaseDao = CollegeDatabase.getDatabase(requireContext()).databaseDao()

        adapter = StudentsAdapter()
        binding.studentsRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.studentsRecycler.adapter = adapter



        loadRecyclerData()

        adapter.setOnDeleteClickListener { position ->
            onDeleteStudent(position)
        }

        binding.addStudentButton.setOnClickListener {
            startActivity(Intent(requireContext(), AddStudentActivity::class.java))
        }
        adapter.setOnChangeClickListener { position ->
            onChangeStudent(position)
        }
        lifecycleScope.launch(Dispatchers.IO) {
            val groupsFromDb = databaseDao.getAllSpecialities()
            val groupNames = groupsFromDb.map { it.speciality }

            val groupNamesList = ArrayList<String>(groupNames)

            withContext(Dispatchers.Main) {
                val newAdapter = ArrayAdapter(
                    requireContext(),
                    R.layout.simple_spinner_item,
                    groupNamesList
                ).apply {
                    setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
                }
                binding.spinerGroup.adapter = newAdapter
            }
        }
        binding.buttonGroup.setOnClickListener {
            if (binding.buttonGroup.text == "Группировать") {
                lifecycleScope.launch(Dispatchers.IO) {
                    val selectedSpeciality = binding.spinerGroup.selectedItem.toString()
                    val studentsList = databaseDao.getStudentsWithSpeciality()
                    val displayedStudents = studentsList.filter { it.studentSpeciality == selectedSpeciality }
                    withContext(Dispatchers.Main) {
                        adapter.setData(displayedStudents)
                    }
                    binding.buttonGroup.text = "Отменить"
                }
            } else {
                loadRecyclerData()
                binding.buttonGroup.text = "Группировать"
            }
        }
        return binding.root
    }

    private fun onDeleteStudent(position: Int) {
        lifecycleScope.launch(Dispatchers.IO) {
            val student = adapter.getStudentsList().getOrNull(position)?.student
            student?.let {
                databaseDao.deleteStudent(it)
                val updatedStudents = databaseDao.getStudentsWithSpeciality()
                withContext(Dispatchers.Main) {
                    adapter.setData(updatedStudents)
                }
            }
        }
    }

    private fun onChangeStudent(position: Int) {
        val student = adapter.getStudentsList().getOrNull(position)
        val intent = Intent(requireContext(), ChangeStudentActivity::class.java)
        Log.d("error",student.toString())
        if (student != null) {
            intent.putExtra("loginStudent", student.student.login)
            intent.putExtra("passwordStudent", student.student.password)
            intent.putExtra("studentId", student.student.studentId.toString())
            Log.d("error",student.student.studentId.toString())
            startActivity(intent)
        }
    }
    private fun loadRecyclerData(){
        lifecycleScope.launch(Dispatchers.IO) {
            val studentsList = databaseDao.getStudentsWithSpeciality()
            withContext(Dispatchers.Main) {
                adapter.setData(studentsList)
            }
        }
    }

}
