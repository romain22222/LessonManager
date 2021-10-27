package com.example.lesson_manager.models

class FichierComparator {
    companion object : Comparator<Fichier> {
        override fun compare(p0: Fichier, p1: Fichier): Int = when (p0.type) {
            p1.type -> p0.name.compareTo(p1.name)
            Fichier.TYPE_FOLDER -> -1
            else -> 1
        }
    }
}