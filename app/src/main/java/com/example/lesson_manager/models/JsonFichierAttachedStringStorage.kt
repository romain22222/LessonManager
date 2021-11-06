package com.example.lesson_manager.models

import com.example.lesson_manager.activity.FolderListActivity
import com.example.lesson_manager.models.Fichier.Companion.folderToFichier
import org.json.JSONObject
import java.io.File

class JsonFichierAttachedStringStorage {
    companion object {
        private var jsonDescStorage =
            JSONObject(initIfNotInitialized(File(FolderListActivity.ROOT_DIRECTORY.parent + "/descStorage.json")))

        private fun initIfNotInitialized(f: File): String {
            if (f.length() == 0L) {
                f.writeText(fillJsonString(folderToFichier(FolderListActivity.ROOT_DIRECTORY)).toString())
            }
            return f.readText()
        }

        private fun fillJsonString(dir: Fichier): JSONObject {
            val json = JSONObject()
            if (dir.type === Fichier.TYPE_FOLDER) {
                val childs = File(dir.path).listFiles()
                for (child in childs) {
                    json.put(
                        folderToFichier(child).name,
                        if (child.isDirectory) fillJsonString(folderToFichier(child)) else ""
                    )
                }
            }
            return json
        }
        fun deleteDesc(path: String) {
            deleteDescRec(
                path.replace(FolderListActivity.ROOT_DIRECTORY.absolutePath+"/", ""),
                jsonDescStorage
            )
            File(FolderListActivity.ROOT_DIRECTORY.parent + "/descStorage.json").writeText(
                jsonDescStorage.toString()
            )
        }

        private fun deleteDescRec(path: String, json: JSONObject): JSONObject {
            if (path.indexOf("/") != -1) {
                json.put(
                    path.substring(0, path.indexOf("/")),
                    deleteDescRec(
                        path.substring(path.indexOf("/") + 1),
                        json.get(path.substring(0, path.indexOf("/"))) as JSONObject
                    )
                )
            } else {
                json.remove(path)
            }
            return json
        }

        fun changeTitle(oldPath: String, newPath: String) {
            val descContent = getDesc(oldPath)
            jsonDescStorage = setDescRec(
                newPath.replace(FolderListActivity.ROOT_DIRECTORY.absolutePath+"/", ""),
                jsonDescStorage,
                descContent
            )
            deleteDesc(oldPath)
            File(FolderListActivity.ROOT_DIRECTORY.parent + "/descStorage.json").writeText(
                jsonDescStorage.toString()
            )
        }

        fun setDesc(imgPath: String, descContent: String) {
            jsonDescStorage = setDescRec(
                imgPath.replace(FolderListActivity.ROOT_DIRECTORY.absolutePath+"/", ""),
                jsonDescStorage,
                descContent
            )
            File(FolderListActivity.ROOT_DIRECTORY.parent + "/descStorage.json").writeText(
                jsonDescStorage.toString()
            )
        }

        private fun setDescRec(path: String, json: JSONObject, descContent: String): JSONObject {
            if (path.indexOf("/") != -1) {
                json.put(
                    path.substring(0, path.indexOf("/")),
                    setDescRec(
                        path.substring(path.indexOf("/") + 1),
                        json.get(path.substring(0, path.indexOf("/"))) as JSONObject,
                        descContent
                    )
                )
            } else {
                json.put(
                    path,
                    descContent
                )
            }
            return json
        }

        fun getDesc(imgPath: String): String {
            return getDescRec(
                imgPath.substring(0, imgPath.lastIndexOf("/")),
                jsonDescStorage
            ).getString(imgPath.substring(imgPath.lastIndexOf("/") + 1))
        }

        private fun getDescRec(path: String, json: JSONObject): JSONObject {
            return if (File(path).absolutePath == FolderListActivity.ROOT_DIRECTORY.absolutePath) {
                json
            } else {
                getDescRec(
                    path.substring(0, path.lastIndexOf("/")),
                    json
                ).getJSONObject(path.substring(path.lastIndexOf("/") + 1))
            }
        }
    }

}