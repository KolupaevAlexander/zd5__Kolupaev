package com.example.collageapp.ui.admin.specialities

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.collageapp.R
import com.example.collageapp.database.Speciality

class SpecialitiesAdapter : RecyclerView.Adapter<SpecialitiesViewHolder>() {
    private var specialitiesList:List<Speciality> = emptyList()
    private var onDeleteClickListener: ((Int) -> Unit)? = null
    private var onChangeClickListener:((Int)->Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecialitiesViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_speciality,parent,false)
        return SpecialitiesViewHolder(itemView,onDeleteClickListener,onChangeClickListener)
    }
    override fun onBindViewHolder(holder: SpecialitiesViewHolder, position: Int) {
        holder.bind(specialitiesList[position], position)
    }


    override fun getItemCount(): Int =specialitiesList.size
    fun setData(newSpecialities: List<Speciality>) {
        specialitiesList = newSpecialities
        notifyDataSetChanged()
    }
    fun setOnDeleteClickListener(listener: (Int) -> Unit) {
        onDeleteClickListener = listener
    }
    fun setOnChangeClickListener(listener: (Int) -> Unit){
        onChangeClickListener = listener
    }
    fun getSpecialitiesList():List<Speciality>{
        return specialitiesList
    }


}