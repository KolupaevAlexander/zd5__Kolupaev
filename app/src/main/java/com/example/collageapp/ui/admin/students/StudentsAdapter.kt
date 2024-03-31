package com.example.collageapp.ui.admin.students

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.collageapp.R
import com.example.collageapp.database.StudentWithSpeciality

class StudentsAdapter : RecyclerView.Adapter<StudentsViewHolder>() {
    private var studentsList: List<StudentWithSpeciality> = emptyList()
    private var onDeleteClickListener: ((Int) -> Unit)? = null
    private var onChanceClickListener:((Int)->Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentsViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_student, parent, false)
        return StudentsViewHolder(itemView, onDeleteClickListener,onChanceClickListener)
    }

    override fun onBindViewHolder(holder: StudentsViewHolder, position: Int) {
        holder.bind(studentsList[position], position)
    }

    override fun getItemCount(): Int = studentsList.size

    fun setData(newStudents: List<StudentWithSpeciality>) {
        studentsList = newStudents
        notifyDataSetChanged()
    }

    fun getStudentsList(): List<StudentWithSpeciality> {
        return studentsList
    }

    // Set the callback for delete action
    fun setOnDeleteClickListener(listener: (Int) -> Unit) {
        onDeleteClickListener = listener
    }
    fun setOnChangeClickListener(listener: (Int) -> Unit){
        onChanceClickListener = listener
    }
}
