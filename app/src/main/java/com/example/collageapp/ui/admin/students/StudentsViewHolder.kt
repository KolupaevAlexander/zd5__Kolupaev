package com.example.collageapp.ui.admin.students

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.example.collageapp.R
import com.example.collageapp.database.StudentWithSpeciality
import com.squareup.picasso.Picasso

class StudentsViewHolder(
    itemView: View,
    private val onDeleteClickListener: ((Int) -> Unit)? = null,
    private val onChangeClickListener:((Int)->Unit)? = null
) : RecyclerView.ViewHolder(itemView) {
    private val studentNameTextView: TextView = itemView.findViewById(R.id.studentNameTextView)
    private val studentBirthdayTextView: TextView = itemView.findViewById(R.id.studentBirthdayTextView)
    private val studentCoursesTextView: TextView = itemView.findViewById(R.id.studentCoursesTextView)
    private val deleteStudentButton: AppCompatButton = itemView.findViewById(R.id.deleteStudentButton)
    private val studentSpeciality:TextView = itemView.findViewById(R.id.specialityNameTextView)
    private val changeSpecialityButton:AppCompatButton = itemView.findViewById(R.id.editStudentButton)
    private val budgetTextView:TextView = itemView.findViewById(R.id.budgetTextView)
    private val studentImage:ImageView = itemView.findViewById(R.id.studentImage)
    private val studentLogin: TextView = itemView.findViewById(R.id.textViewLogin)
    private val studentPassword: TextView = itemView.findViewById(R.id.textViewPassword)

    fun bind(studentWithSpeciality: StudentWithSpeciality, position: Int) {
        studentNameTextView.text = "Студент: ${studentWithSpeciality.student.name}"
        studentLogin.text = "Логин: ${studentWithSpeciality.student.login}"
        studentPassword.text = "Пароль: ${studentWithSpeciality.student.password}"
        studentBirthdayTextView.text = "(${studentWithSpeciality.student.birthday})"
        studentCoursesTextView.text = "Курс: ${studentWithSpeciality.student.course}"
        studentSpeciality.text = "Специальность: ${studentWithSpeciality.studentSpeciality}"
        if(studentWithSpeciality.student.isBudget){
            budgetTextView.text = "Бюджет: Да"
        }
        else{
            budgetTextView.text = "Бюджет: Нет"
        }

        Picasso.get()
            .load(studentWithSpeciality.student.photo)
            .placeholder(R.drawable.placeholder_poster)
            .error(R.drawable.placeholder_poster)
            .into(studentImage)

//        if(studentWithSpeciality.student.photo.isNullOrEmpty()){
//            studentWithSpeciality.student.photo =
//        }
//        else{
//            Picasso.get()
//                .load(studentWithSpeciality.student.photo)
//                .placeholder(R.drawable.placeholder_poster)
//                .error(R.drawable.placeholder_poster)
//                .into(studentImage)
//        }


        // Handle delete button click
        deleteStudentButton.setOnClickListener {
            onDeleteClickListener?.invoke(position)
        }
        changeSpecialityButton.setOnClickListener{
            onChangeClickListener?.invoke(position)
        }
    }
}
