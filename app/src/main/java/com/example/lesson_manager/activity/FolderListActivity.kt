package com.example.lesson_manager.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.lesson_manager.R
import com.example.lesson_manager.adapter.FolderAdapter
import com.example.lesson_manager.models.Fichier
import com.example.lesson_manager.models.Fichier.Companion.folderToFichier
import com.example.lesson_manager.models.JsonFichierAttachedStringStorage
import org.json.JSONObject
import java.io.File
import java.util.*
import kotlin.collections.ArrayList


class FolderListActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_FOLDER = "EXTRA_FOLDER"
        const val EXTRA_FILE = "EXTRA_FILE"
        val ROOT_DIRECTORY =
            File(Environment.getDataDirectory(), "/data/com.example.lesson_manager/userData")
        var isReady = false


    }

    var files: ArrayList<Fichier> = arrayListOf()
    private lateinit var list: RecyclerView
    var dir = ROOT_DIRECTORY

    private fun initIfNeeded(context: Context) {
        if (folderToFichier(ROOT_DIRECTORY).getChildrenOfFolder().isEmpty()) {
            val queue = Volley.newRequestQueue(context)
            val request = JsonObjectRequest(
                Request.Method.GET,
                "http://os-vps418.infomaniak.ch:1186/i507_1_1/structureType.json",
                null,
                { response ->
                    resolveRequest(response)
                },
                {
                    resolveRequest(null)
                }
            )
            queue.add(request)
            queue.start()
        } else {
            populateRecyclerFolder()
            isReady = true
        }
    }

    private fun resolveRequest(response: JSONObject?) {
        if (response !== null) {
            for (key in response.keys()) {
                File(ROOT_DIRECTORY.absolutePath + "/" + key).mkdirs()
            }
        }
        JsonFichierAttachedStringStorage()
        isReady = true
        populateRecyclerFolder()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_folder)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        initIfNeeded(applicationContext)
    }

    override fun onResume() {
        super.onResume()
        if (isReady) {
            populateRecyclerFolder()
        }
    }

    private fun populateRecyclerFolder() {
        val toolBarLeft = findViewById<ImageView>(R.id.logo_return_button)
        val toolBarRight = findViewById<TextView>(R.id.topBarText)
        val path: String =
            if (dir.absolutePath.length >= ROOT_DIRECTORY.absolutePath.length) dir.absolutePath else ROOT_DIRECTORY.absolutePath
        if (path == ROOT_DIRECTORY.absolutePath) {
            toolBarLeft.isClickable = false
            toolBarLeft.setImageResource(R.drawable.logo)
            toolBarRight.text = getText(R.string.baseHead)
        } else {
            toolBarLeft.isClickable = true
            toolBarLeft.setImageResource(R.drawable.return_button)
            toolBarRight.text = path.substring(path.lastIndexOf("/") + 1)
            toolBarLeft.setOnClickListener {
                dir = File(dir.parent as String)
                populateRecyclerFolder()
            }
        }
        files = Fichier("", "", "folder", path).getChildrenOfFolder()
        list = findViewById(R.id.folder_list)
        list.adapter = object : FolderAdapter(files) {
            override fun onItemClick(view: View) {
                val textAndId = view.findViewById<TextView>(R.id.folder_name)
                val clickedFile = folders.first { it.name == textAndId.text }
                if (clickedFile.type == Fichier.TYPE_FOLDER) {
                    dir = File(clickedFile.path)
                    folders.clear()
                    populateRecyclerFolder()
                    notifyDataSetChanged()
                } else if (clickedFile.type == Fichier.TYPE_FILE) {
                    val intent = Intent(applicationContext, VisualisationFileActivity::class.java).apply {
                        putExtra(
                            EXTRA_FILE,
                            clickedFile
                        )
                    }
                    startActivity(intent)
                }
            }

            override fun onLongItemClick(view: View): Boolean {
                val textAndId = view.findViewById<TextView>(R.id.folder_name)
                val clickedFile = folders.first { it.name == textAndId.text }
                showPopupDeleteItem(clickedFile)
                return true
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

    fun showPopupDeleteItem(file : Fichier) {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        builder.setTitle(String.format("Delete %s : %s ?",file.type,file.name))
        val dialogLayout = inflater.inflate(R.layout.dialog_delete_item, null)
        builder.setView(dialogLayout)
        builder.setNegativeButton("Cancel") { _, _ ->
            Toast.makeText(applicationContext, "Action cancelled", Toast.LENGTH_SHORT).show()
        }
        builder.setPositiveButton("Delete") { _, _ ->
            JsonFichierAttachedStringStorage.deleteDesc(file.path)
            File(file.path).deleteRecursively()
            Toast.makeText(
                applicationContext,
                String.format("%s %s deleted !",
                    file.type.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
                    file.name
                ),
                Toast.LENGTH_LONG
            ).show()
            populateRecyclerFolder()
        }
        builder.show()
    }

    fun showPopupNewFolder(v: View) {
        PopupMenu(this, v).apply {
            setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.new_folder -> {
                        withEditText()
                        true
                    }
                    R.id.new_file -> {
                        val intent = Intent(applicationContext, MainActivity::class.java).apply {
                            putExtra(
                                EXTRA_FOLDER,
                                Fichier("", "", Fichier.TYPE_FOLDER, dir.absolutePath)
                            )
                        }
                        startActivity(intent)
                        true
                    }
                    else -> false
                }
            }
            inflate(R.menu.menu_new_fichier)
            show()
        }
    }

    private fun withEditText() {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        builder.setTitle("New Folder")
        val dialogLayout = inflater.inflate(R.layout.dialog_new_folder, null)
        val editText = dialogLayout.findViewById<EditText>(R.id.string_new_folder)
        builder.setView(dialogLayout)
        builder.setNegativeButton("Cancel") { _, _ ->
            Toast.makeText(applicationContext, "Action cancelled", Toast.LENGTH_SHORT).show()
        }
        builder.setPositiveButton("Create") { _, _ ->
            if (!File(dir.absolutePath + "/" + editText.text.toString()).exists()) {
                File(dir.absolutePath + "/" + editText.text.toString()).mkdirs()
                JsonFichierAttachedStringStorage.createFolder(dir.absolutePath + "/" + editText.text.toString())
                Toast.makeText(
                    applicationContext,
                    "The folder " + editText.text.toString() + " has been created !",
                    Toast.LENGTH_LONG
                ).show()
                populateRecyclerFolder()
            }
        }
        builder.show()
    }
}
