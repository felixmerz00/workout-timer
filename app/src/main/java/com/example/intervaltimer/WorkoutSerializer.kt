@file:Suppress("BlockingMethodInNonBlockingContext")

package com.example.intervaltimer

import androidx.datastore.core.Serializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import java.io.InputStream
import java.io.OutputStream

object WorkoutSerializer : Serializer<WorkoutStore> {
    override val defaultValue: WorkoutStore
        get() = WorkoutStore()

    override suspend fun readFrom(input: InputStream): WorkoutStore {
        return try {
            Json.decodeFromString(
                deserializer = WorkoutStore.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (e: SerializationException) {
            e.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(t: WorkoutStore, output: OutputStream) {
        output.write(
            Json.encodeToString(
                serializer = WorkoutStore.serializer(),
                value = t
            ).encodeToByteArray()
        )
    }
}