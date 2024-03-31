package com.example.collageapp.ui.admin.teachers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.example.collageapp.R
import com.example.collageapp.database.Teacher

class TeacherAdapter:RecyclerView.Adapter<TeacherViewHolder>() {

    private var teachersList: List<Teacher> = emptyList()
    private var onDeleteClickListener: ((Int) -> Unit)? = null
    private var onChangeClickListener:((Int) ->Unit)?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeacherViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_teacher, parent, false)
        return TeacherViewHolder(itemView,onDeleteClickListener,onChangeClickListener)
    }

    override fun onBindViewHolder(holder: TeacherViewHolder, position: Int) {
        holder.bind(teachersList[position] ,position,teachersList)
    }

    override fun getItemCount(): Int = teachersList.size

    fun setData(newTeachers: List<Teacher>) {
        teachersList = newTeachers
        notifyDataSetChanged()
    }
    fun setOnDeleteClickListener(listener: (Int) -> Unit) {
        onDeleteClickListener = listener
    }
    fun setOnChangeClickListener(listener: (Int) -> Unit) {
        onChangeClickListener = listener
    }
    fun getTeachersList(): List<Teacher> {
        return teachersList
    }
}
