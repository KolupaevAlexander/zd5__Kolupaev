package com.example.collageapp.ui.admin.specialities

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.collageapp.AddSpecialityActivity
import com.example.collageapp.EditSpecialityActivity
import com.example.collageapp.R
import com.example.collageapp.database.CollegeDatabase
import com.example.collageapp.database.DatabaseDao
import com.example.collageapp.databinding.FragmentSpecialitiesBinding
import com.example.collageapp.ui.admin.students.StudentsAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SpecialitiesFragment : Fragment() {
    private lateinit var binding: FragmentSpecialitiesBinding
    private lateinit var adapter: SpecialitiesAdapter
    private lateinit var databaseDao: DatabaseDao


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSpecialitiesBinding.inflate(layoutInflater, container, false)
        databaseDao = CollegeDatabase.getDatabase(requireContext()).databaseDao()
        adapter = SpecialitiesAdapter()
        binding.specialitiesRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.specialitiesRecycler.adapter = adapter

        lifecycleScope.launch(Dispatchers.IO) {
            val specialitiesList = databaseDao.getAllSpecialities()
            withContext(Dispatchers.Main) {
                adapter.setData(specialitiesList)
            }
        }
        adapter.setOnDeleteClickListener { position ->
            onDeleteSpeciality(position)
        }
        adapter.setOnChangeClickListener { position->
            onChangeSpeciality(position)
        }
        binding.addSpecialityBt.setOnClickListener {
            startActivity(Intent(requireContext(), AddSpecialityActivity::class.java))
        }
        return binding.root
    }

    private fun onDeleteSpeciality(position: Int) {
        lifecycleScope.launch(Dispatchers.IO) {
            val speciality = adapter.getSpecialitiesList().getOrNull(position)
            speciality?.let {
                databaseDao.deleteSpeciality(speciality)
                val updatedSpecialities = databaseDao.getAllSpecialities()
                withContext(Dispatchers.Main) {
                    adapter.setData(updatedSpecialities)
                }
            }
        }
    }

    private fun onChangeSpeciality(position: Int) {
        val speciality = adapter.getSpecialitiesList().getOrNull(position)
        val intent = Intent(requireContext(), EditSpecialityActivity::class.java)
        intent.putExtra("SPECIALITY",speciality?.speciality) // Передайте нужные данные
        startActivity(intent)
    }

}