package com.example.lesson_manager.activity

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.lesson_manager.R
import com.example.lesson_manager.models.Fichier
import java.io.File


class VisualisationFileActivity : AppCompatActivity() {
    private lateinit var file : Fichier
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visualisation_file)

        file = intent.getSerializableExtra(FolderListActivity.EXTRA_FILE) as Fichier

        val titleView = findViewById<TextView>(R.id.topBarText)
        titleView.text = file.name
        val descView = findViewById<TextView>(R.id.description)
        descView.text = file.description
        val imageView = findViewById<ImageView>(R.id.img)
        imageView.setImageDrawable(Drawable.createFromPath(file.path))
        val buttonEditTitle = findViewById<ImageButton>(R.id.editTitle)
        buttonEditTitle.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val inflater = layoutInflater
            builder.setTitle("Edit title")
            val dialogLayout = inflater.inflate(R.layout.dialog_edit_title, null)
            val editText = dialogLayout.findViewById<EditText>(R.id.string_edit_title)
            editText.setText(file.name.slice(IntRange(0,file.name.length-6)))
            builder.setView(dialogLayout)
            builder.setNegativeButton("Cancel") { _, _ ->
                Toast.makeText(applicationContext, "Action cancelled", Toast.LENGTH_SHORT).show()
            }
            builder.setPositiveButton("Edit") { _, _ ->
                when {
                    editText.text.toString() == file.name.slice(IntRange(0,file.name.length-6)) -> {}
                    editText.text.contains(regex = Regex("^[\\w,\\s-]+$")) -> {
                        file.changeTitle(editText.text.toString())
                        Toast.makeText(applicationContext, "Filename edited", Toast.LENGTH_SHORT).show()
                        titleView.text = file.name
                    }
                    editText.text.toString() == "" -> {
                        Toast.makeText(applicationContext, "You can't edit the filename for an empty string", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        Toast.makeText(applicationContext, "Illegal characters detected", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            builder.show()
        }
        val buttonEditDescription = findViewById<ImageButton>(R.id.editDesc)
        buttonEditDescription.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val inflater = layoutInflater
            builder.setTitle("Edit description")
            val dialogLayout = inflater.inflate(R.layout.dialog_edit_description, null)
            val editText = dialogLayout.findViewById<EditText>(R.id.string_edit_description)
            editText.setText(file.description)
            builder.setView(dialogLayout)
            builder.setNegativeButton("Cancel") { _, _ ->
                Toast.makeText(applicationContext, "Action cancelled", Toast.LENGTH_SHORT).show()
            }
            builder.setPositiveButton("Edit") { _, _ ->
                file.changeDesc(editText.text.toString())
                Toast.makeText(applicationContext, "Description edited", Toast.LENGTH_SHORT).show()
                descView.text = file.description
            }
            builder.show()
        }
        val toolBarLeft = findViewById<ImageView>(R.id.logo_return_button)
        toolBarLeft.isClickable = true
        toolBarLeft.setOnClickListener {
            super.finish()
        }
    }
}
