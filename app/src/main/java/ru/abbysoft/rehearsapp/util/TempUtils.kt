package ru.abbysoft.rehearsapp.util

import androidx.core.util.Consumer
import ru.abbysoft.rehearsapp.model.Room
import ru.abbysoft.rehearsapp.rest.ServiceFactory

// all this funcs need to be refactored into own classes

fun getRoomFromCache(roomId: Long, callback: Consumer<Room>) {
    AsyncServiceRequest(
        callback
    ).execute(ServiceFactory.getDatabaseService().getRoom(roomId))
}