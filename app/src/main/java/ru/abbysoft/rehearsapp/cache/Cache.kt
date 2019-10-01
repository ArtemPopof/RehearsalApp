package ru.abbysoft.rehearsapp.cache

import ru.abbysoft.rehearsapp.model.Place

interface Cache {

    fun getPlace(id : Long) : Place

    fun addPlace(place : Place) : Long
}