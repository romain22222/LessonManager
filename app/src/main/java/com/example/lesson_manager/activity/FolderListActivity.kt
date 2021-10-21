package com.example.lesson_manager.activity

import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.lesson_manager.R
import com.example.lesson_manager.adapter.FolderAdapter
import com.example.lesson_manager.models.Fichier
import java.io.File


class FolderListActivity : AppCompatActivity() {
    companion object {
        val EXTRA_FOLDER = "EXTRA_FOLDER"
        val ROOT_DIRECTORY = File(Environment.getDataDirectory(),"/data/com.example.lesson_manager/userData")
    }

    var files : ArrayList<Fichier> = arrayListOf()
    lateinit var list: RecyclerView
    var dir = ROOT_DIRECTORY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        files.add(
            Fichier(1,"TestNom", "Ceci est une description", "folder", ROOT_DIRECTORY.absolutePath+"/test"))
        setContentView(R.layout.activity_folder)
        files.add(
            Fichier(2,"TestNom", "Ceci est une description", "folder", ROOT_DIRECTORY.absolutePath+"/test"))
        setContentView(R.layout.activity_folder)
        files.add(
            Fichier(3,"TestNom", "Ceci est une description", "file", ROOT_DIRECTORY.absolutePath+"/test"))
        setContentView(R.layout.activity_folder)
        if(!dir.exists()) {
            dir.mkdirs()
        }
        populateRecyclerFolder(ROOT_DIRECTORY.absolutePath)
    }

    private fun populateRecyclerFolder (path:String) {

        list = findViewById<RecyclerView>(R.id.folder_list)

        list.adapter = object : FolderAdapter(files) {
            override fun onItemClick(view: View) {
                TODO("Not yet implemented")
            }

            override fun onLongItemClick(view: View): Boolean {
                TODO("Not yet implemented")
            }
        }
    }
}
