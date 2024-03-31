package com.example.collageapp.ui.admin.specialities

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.example.collageapp.R
import com.example.collageapp.database.Speciality

class SpecialitiesViewHolder(
    itemView: View,
    private val onDeleteClickListener:((Int)->Unit)?=null,
    private val onChangeClickListener:((Int)->Unit)?=null
):RecyclerView.ViewHolder(itemView) {
    private val specialityName:TextView = itemView.findViewById(R.id.specialityTextView)
    private val deleteButton: AppCompatButton = itemView.findViewById(R.id.deleteSpecialityBt)
    private val changeSpeciality:AppCompatButton = itemView.findViewById(R.id.changeSpecialityBt)
    fun bind(speciality: Speciality,position:Int){
        specialityName.text = "Специальность: ${speciality.speciality}"
        deleteButton.setOnClickListener{
            onDeleteClickListener?.invoke(position)
        }
        changeSpeciality.setOnClickListener{
            onChangeClickListener?.invoke(position)
        }
    }
}