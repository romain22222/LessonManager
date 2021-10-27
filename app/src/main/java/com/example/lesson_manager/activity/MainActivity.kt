package com.example.lesson_manager.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.lesson_manager.R
import com.example.lesson_manager.models.Fichier

class MainActivity : AppCompatActivity() {

    private val ADD_PICTURE_CODE : Int = 1

    private lateinit var  folder : Fichier


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        folder= intent.getSerializableExtra(FolderListActivity.EXTRA_FOLDER) as Fichier

        val titleView = findViewById<EditText>(R.id.addTitle)
        val descView = findViewById<EditText>(R.id.addDesc)
//        val imageView = findViewById<ImageView>(R.id.image)

//        val llpath : LinearLayout = findViewById(R.id.addImage)
//
//        llpath.setOnClickListener {
//            if (checkPermission(permission.READ_EXTERNAL_STORAGE, ADD_PICTURE_CODE)) {
//                addPicture()
//            }
//        }

        val confirmButton = findViewById<Button>(R.id.confirm_button)
        confirmButton.setOnClickListener {
            if (titleView.text.toString() == "" || descView.text.toString() == "") {
                Toast.makeText(applicationContext,"Missing informations !",Toast.LENGTH_SHORT).show()
            } else {
                Fichier(titleView.text.toString(), descView.text.toString(), Fichier.TYPE_FILE, folder.path+"/"+titleView.text.toString()+".png").saveFile(resources.getDrawable(R.drawable.test_image))
                super.finish()
            }
        }

    }

    private fun addPicture(){
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, ADD_PICTURE_CODE)
    }

    private fun checkPermission(permission: String, requestCode: Int): Boolean {
        var res = true
        if (ContextCompat.checkSelfPermission(this, permission)
            != PackageManager.PERMISSION_GRANTED
        ) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
            }
            res = false
        }
        return res
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == ADD_PICTURE_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                addPicture()
            }
        }
    }


}
