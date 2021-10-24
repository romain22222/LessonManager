package com.example.lesson_manager.activity

import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.ImageView
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
        populateRecyclerFolder()
    }

    private fun populateRecyclerFolder () {
        val toolBarLeft = findViewById<ImageView>(R.id.logo_return_button)
        val toolBarRight = findViewById<TextView>(R.id.topBarText)
        val path: String = if (dir.absolutePath.length >= ROOT_DIRECTORY.absolutePath.length) dir.absolutePath else ROOT_DIRECTORY.absolutePath
        if (path == ROOT_DIRECTORY.absolutePath) {
            toolBarLeft.isClickable = false
            toolBarLeft.setImageResource(R.drawable.logo)
            toolBarRight.text = getText(R.string.baseHead)
        } else {
            toolBarLeft.isClickable = true
            toolBarLeft.setImageResource(R.drawable.return_button)
            toolBarRight.text = path.substring(path.lastIndexOf("/")+1)
            toolBarLeft.setOnClickListener{
                dir = File(dir.parent as String)
                populateRecyclerFolder()
            }
        }
        files = Fichier("", "", "folder", path).getChildrenOfFolder()
        list = findViewById<RecyclerView>(R.id.folder_list)
        list.adapter = object : FolderAdapter(files) {
            override fun onItemClick(view: View) {
                val textAndId = view.findViewById<TextView>(R.id.folder_name)
                val clickedFile = folders.first {it.name == textAndId.text}
                if (clickedFile.type == Fichier.TYPE_FOLDER) {
                    dir = File(clickedFile.path)
                    folders.clear()
                    populateRecyclerFolder()
                    notifyDataSetChanged()
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
    override fun onBackPressed() {
        if (dir.absolutePath != ROOT_DIRECTORY.absolutePath) {
            dir = File(dir.parent as String)
            populateRecyclerFolder()
        } else {
            super.onBackPressed()
        }
    }
    // TODO : Faire une autre activity qui contient le menu : https://developer.android.com/guide/topics/ui/menus.html#PopupMenu
//    fun showPopup(v:View) {
//        PopupMenu(this,v).apply {
//            setOnMenuItemClickListener(this@FolderListActivity)
//            inflate(R.menu.menu_new_fichier)
//            show()
//    }
//    override fun onMenuItemClick(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            R.id.new_folder -> {
//                Toast.makeText(applicationContext,"NOUVEAU DOSSIER",Toast.LENGTH_SHORT).show()
//                true
//            }
//            R.id.new_file -> {
//                Toast.makeText(applicationContext,"NOUVEAU FICHIER",Toast.LENGTH_SHORT).show()
//                true
//            }
//            else -> false
//        }
//    }
}
