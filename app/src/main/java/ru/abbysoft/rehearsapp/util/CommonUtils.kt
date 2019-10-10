package ru.abbysoft.rehearsapp.util

fun fromStringToImageId(string: String): Long {
    return string.substring(3).toLong()
}

fun longIdToString(long: Long): String {
    return "img$long"
}