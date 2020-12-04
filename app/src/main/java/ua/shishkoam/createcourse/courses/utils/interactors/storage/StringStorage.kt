package ua.mil.armysos.storage

interface StringStorage {
    fun load(path: String) : String?
    fun save(data: String, path: String) : Boolean
}