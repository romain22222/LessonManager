package com.example.lesson_manager.activity

import android.R.attr.data
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.TextView
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
        setContentView(R.layout.activity_folder)
        if(!dir.exists()) {
            dir.mkdirs()
        }
        populateRecyclerFolder(ROOT_DIRECTORY.absolutePath)
    }

    private fun populateRecyclerFolder (path:String) {
        files = Fichier("", "", "folder", path).getChildrenOfFolder()
        list = findViewById<RecyclerView>(R.id.folder_list)
        list.adapter = object : FolderAdapter(files) {
            override fun onItemClick(view: View) {
                val textAndId = view.findViewById<TextView>(R.id.folder_name)
                val clickedFile = this.folders.first {it.name == textAndId.text}
                if (clickedFile.type == Fichier.TYPE_FOLDER) {

                    this.folders.clear()
                    val newList: ArrayList<Fichier> = clickedFile.getChildrenOfFolder()
                    this.folders.addAll(newList)
                    // TODO : 1 changer le titre de l'activity pour coller au dossier
                    //        2 pouvoir revenir en arrière via le bouton retour / un autre bouton ?

                    this.notifyDataSetChanged()
                }
                else if (clickedFile.type == Fichier.TYPE_FILE) {
                    val test = "nothing"
                }
            }

            override fun onLongItemClick(view: View): Boolean {
                TODO("Not yet implemented")
            }
        }
    }
}
