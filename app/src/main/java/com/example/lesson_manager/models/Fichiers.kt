package com.example.lesson_manager.models

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import java.io.*

class Fichier (
    var name:String,
    var description: String,
    var type:String,
    var path:String
            ) : Serializable {
    companion object {
        const val FILE_ID = "id"
        const val FILE_NAME = "name"
        const val FILE_DESC = "description"
        const val FILE_IMG = "image"
        const val FILE_FOLDER = "folder"
        const val TYPE_FOLDER = "folder"
        const val TYPE_FILE = "file"
        var ID = 1
        fun fileToFichier(file: File) : Fichier {
            val path = file.absolutePath
            return Fichier(path.substring(path.lastIndexOf("/")+1),"Recuperer du json la description du fichier", TYPE_FILE,path)
        }

        fun folderToFichier(file: File) : Fichier {
            val path = file.absolutePath
            return Fichier(path.substring(path.lastIndexOf("/")+1),"", TYPE_FOLDER,path)
        }
    }

    val id : Int = ID++

    fun saveFile(img : Bitmap): Uri? {
        if (type == TYPE_FOLDER)
            return null
        try {
            val stream: OutputStream = FileOutputStream(File(path))
            img.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e:IOException) {
            e.printStackTrace()
        }
        return Uri.parse(path)
        TODO("" +
                "Partie à remplir coté insertion document" +
                "A enregistrer :    Titre fichier -> Titre image" +
                "                   Description fichier -> Description image dans le json" +
                "                   Path -> Endroit du stockage de l'image")
    }



    fun getChildrenOfFolder(): ArrayList<Fichier> {
        if (type== TYPE_FILE)
            return arrayListOf()
        val folderToSearch = File(path).listFiles()
        var listReturn : ArrayList<Fichier> = arrayListOf()
        folderToSearch.forEach {
            listReturn.add(if (it.extension == "") folderToFichier(it) else fileToFichier(it))
        }
        listReturn = ArrayList(listReturn.sortedWith(FichierComparator))
        return listReturn
    }
}