package com.example.lesson_manager.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lesson_manager.R
import com.example.lesson_manager.models.Fichier

abstract class FolderAdapter(val folders : MutableList<Fichier>) :
    RecyclerView.Adapter<FolderAdapter.FolderHolder>(){
    class FolderHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.folder_name)
        val typeImage = itemView.findViewById<ImageView>(R.id.folder_logo)
    }

    abstract fun onItemClick(view: View)
    abstract fun onLongItemClick(view: View): Boolean
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_folder,parent, false)
        view.setOnClickListener{view -> onItemClick(view)}
        view.setOnLongClickListener{view -> onLongItemClick(view)}
        return FolderHolder(view)
    }

    override fun onBindViewHolder(holder: FolderHolder, position: Int) {
        holder.name.text = folders[position].name
        holder.name.tag = folders[position].id
        if (folders[position].type == "folder") {
            holder.typeImage.setImageResource(R.drawable.folder)
        } else if (folders[position].type == "file") {
            holder.typeImage.setImageResource(R.drawable.file)
        }
    }

    override fun getItemCount(): Int {
        return folders.size
    }

    fun updateData() {}
}