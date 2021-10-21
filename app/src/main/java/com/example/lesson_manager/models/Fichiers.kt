package com.example.lesson_manager.models

import java.io.File
import java.io.Serializable

class Fichier (
    val id: Int,
    val name:String,
    val description: String,
    val type:String,
    val path:String
            ) : Serializable {
    companion object {
        const val FILE_ID = "id"
        const val FILE_NAME = "name"
        const val FILE_DESC = "description"
        const val FILE_IMG = "image"
        const val FILE_FOLDER = "folder"
    }
}

/*
val rootF = File(path)
val files = rootF.listFiles()
*/
