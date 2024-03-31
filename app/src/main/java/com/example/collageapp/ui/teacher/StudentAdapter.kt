package com.example.collageapp.ui.teacher

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.collageapp.R
import com.example.collageapp.database.Student
import com.example.collageapp.database.StudentWithSpeciality

class StudentAdapter : RecyclerView.Adapter<StudentViewHolder>() {
    private var studentsList: List<StudentWithSpeciality> = emptyList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_student_info, parent, false)
        return StudentViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        holder.bind(studentsList[position])
    }

    override fun getItemCount(): Int = studentsList.size

    fun setData(newStudents: List<StudentWithSpeciality>) {
        studentsList = newStudents
        notifyDataSetChanged()
    }
    fun setAllData(newStudents: List<StudentWithSpeciality>){
        studentsList = newStudents
        notifyDataSetChanged()

    }

    fun getStudentsList(): List<StudentWithSpeciality> {
        return studentsList
    }
}
