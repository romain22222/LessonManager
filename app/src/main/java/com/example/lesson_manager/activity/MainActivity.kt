package com.example.lesson_manager.activity

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.lesson_manager.R
import com.example.lesson_manager.models.Fichier


class MainActivity : AppCompatActivity() {

    private lateinit var folder: Fichier

    lateinit var buttonPic: ImageButton
    lateinit var toSave : Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        folder = intent.getSerializableExtra(FolderListActivity.EXTRA_FOLDER) as Fichier

        val titleView = findViewById<EditText>(R.id.addTitle)
        val descView = findViewById<EditText>(R.id.addDesc)
        buttonPic = findViewById(R.id.imageButton)
        buttonPic.setOnClickListener {
            try {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                val builder = VmPolicy.Builder()
                StrictMode.setVmPolicy(builder.build())
                startActivityForResult(intent, 22222)
            } catch (e: Error) {
                e.printStackTrace()
            }
        }
        val resB = buttonPic.drawable

        val confirmButton = findViewById<Button>(R.id.confirm_button)
        confirmButton.setOnClickListener {
            if (titleView.text.toString() == "" || descView.text.toString() == "" || buttonPic.drawable == resB) {
                Toast.makeText(applicationContext, "Missing informations !", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Fichier(
                    titleView.text.toString(),
                    descView.text.toString(),
                    Fichier.TYPE_FILE,
                    folder.path + "/" + titleView.text.toString() + ".jpeg"
                ).saveFile(toSave)
                super.finish()
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 22222 && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            buttonPic.setImageBitmap(imageBitmap)
            toSave = imageBitmap
        }
    }
}
