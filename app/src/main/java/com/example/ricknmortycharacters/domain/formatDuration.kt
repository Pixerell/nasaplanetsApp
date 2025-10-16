package com.example.ricknmortycharacters.domain

import android.util.Log
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.time.ZoneId

fun formatCreated(created: String): String {
    Log.d("Charactersdetails", created)
    return try {
        val odt = OffsetDateTime.parse(created)
        odt.atZoneSameInstant(ZoneId.systemDefault())
            .format(DateTimeFormatter.ofPattern("MMM d, yyyy"))
    } catch (e: Exception) {
        created
    }
}
