package com.example.collageapp.ui.student

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.collageapp.R
import com.example.collageapp.database.Teacher

class TeachersViewHolder(itemView:View): RecyclerView.ViewHolder(itemView) {
    private val teacherNameTextView: TextView = itemView.findViewById(R.id.teacherNameTextViewInfo)
    private val teacherHours: TextView = itemView.findViewById(R.id.hoursPerYearItemTextViewInfo)
    private val teacherSpeciality: TextView = itemView.findViewById(R.id.specialityTeahcerTextViewInfo)
    fun bind(
        teacher: Teacher,
    ) {
        teacherNameTextView.text = "Преподаватель: ${teacher.name}"
        teacherHours.text = "Количество часов, которое он ведет: ${teacher.hoursPerYear}"
        teacherSpeciality.text = "Специальность: ${teacher.speciality}"
    }
}