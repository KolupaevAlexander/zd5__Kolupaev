package com.example.collageapp.ui.teacher

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.collageapp.R
import com.example.collageapp.database.StudentWithSpeciality
import com.squareup.picasso.Picasso

class StudentViewHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {
    private val studentNameTextView: TextView = itemView.findViewById(R.id.studentNameTextViewInfo)
    private val studentBirthdayTextView: TextView = itemView.findViewById(R.id.studentBirthdayTextViewInfo)
    private val studentCoursesTextView: TextView = itemView.findViewById(R.id.studentCoursesTextViewInfo)
    private val studentSpeciality:TextView = itemView.findViewById(R.id.specialityNameTextViewInfo)
    private val budgetTextView:TextView = itemView.findViewById(R.id.budgetTextViewInfo)
    private val studentImage: ImageView = itemView.findViewById(R.id.studentImageInfo)

    fun bind(studentWithSpeciality: StudentWithSpeciality) {
        studentNameTextView.text = "Студент: ${studentWithSpeciality.student.name}"
        studentBirthdayTextView.text = "Дата рождения:${studentWithSpeciality.student.birthday}"
        studentCoursesTextView.text = "Курс: ${studentWithSpeciality.student.course}"
        studentSpeciality.text = "Специальность: ${studentWithSpeciality.studentSpeciality}"
        Picasso.get()
            .load(studentWithSpeciality.student.photo)
            .placeholder(R.drawable.placeholder_poster)
            .error(R.drawable.placeholder_poster)
            .into(studentImage)

        if(studentWithSpeciality.student.isBudget){
            budgetTextView.text = "Бюджет: Да"
        }
        else{
            budgetTextView.text = "Бюджет: Нет"
        }


    }
}
