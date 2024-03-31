package com.example.collageapp.ui.admin.teachers

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.example.collageapp.R
import com.example.collageapp.database.Teacher
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TeacherViewHolder(
    itemView: View,
    private val onDeleteClickListener: ((Int) -> Unit)? = null,
    private val onChangeClickListener: ((Int) -> Unit)? = null
) : RecyclerView.ViewHolder(itemView) {
    private val teacherNameTextView: TextView = itemView.findViewById(R.id.teacherNameTextView)
    private val teacherSalaryTextView: TextView = itemView.findViewById(R.id.teacherSalaryTextView)
    private val teacherHours: TextView = itemView.findViewById(R.id.hoursPerYearItemTextView)
    private val teacherSpeciality: TextView = itemView.findViewById(R.id.specialityTeahcerTextView)
    private val studentLogin: TextView = itemView.findViewById(R.id.textViewLogin)
    private val studentPassword: TextView = itemView.findViewById(R.id.textViewPassword)
    private val deleteTeacherBt: AppCompatButton = itemView.findViewById(R.id.deleteTeacherBt)
    private val changeTeacherBt: AppCompatButton = itemView.findViewById(R.id.changeTeacherBt)

    fun bind(
        teacher: Teacher,
        position: Int,
        allTeachers: List<Teacher>
    ) {
        teacherNameTextView.text = "Преподаватель: ${teacher.name}"
        var totalHours = 0
        for(item in allTeachers){
            if(teacher.name==item.name)
                totalHours+=teacher.hoursPerYear;
        }
        if(totalHours>1440){
            teacherSalaryTextView.text = "Зар.плата за все специальности: "+((totalHours*300)+((totalHours-1440)*150)).toString()
        }
        else{
            teacherSalaryTextView.text = "Зар.Плата за все специальности:" +(totalHours*300).toString()
        }

        teacherHours.text = "Количество часов, которое он ведет: ${teacher.hoursPerYear}"
        teacherSpeciality.text = "Специальность: ${teacher.speciality}"
        studentLogin.text = "Логин: ${teacher.login}"
        studentPassword.text = "Пароль: ${teacher.password}"

        // Обработчик клика на кнопку удаления
        deleteTeacherBt.setOnClickListener {
            onDeleteClickListener?.invoke(position)
        }

        // Обработчик клика на кнопку изменения
        changeTeacherBt.setOnClickListener {
            onChangeClickListener?.invoke(position)
        }
    }
}
