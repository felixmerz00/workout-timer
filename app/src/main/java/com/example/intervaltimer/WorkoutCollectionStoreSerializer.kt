package com.example.intervaltimer

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.serialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

@Suppress("BlockingMethodInNonBlockingContext")
object WorkoutCollectionStoreSerializer : Serializer<WorkoutCollectionStore> {
    override val defaultValue: WorkoutCollectionStore
        get() = WorkoutCollectionStore()

    override suspend fun readFrom(input: InputStream): WorkoutCollectionStore {
        return try {
            Json.decodeFromString(
                deserializer = WorkoutCollectionStore.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (e: SerializationException) {
            throw CorruptionException("Unable to read Settings", e)
            e.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(t: WorkoutCollectionStore, output: OutputStream) {
        output.write(
            Json.encodeToString(
                serializer = WorkoutCollectionStore.serializer(),
                value = t
            ).encodeToByteArray()
        )
    }
}


@OptIn(ExperimentalSerializationApi::class)
//@Serializer(forClass = PersistentList::class)
class MyPersistentListSerializer(
    private val serializer: KSerializer<WorkoutStore>,
) : KSerializer<PersistentList<WorkoutStore>> {

    private class PersistentListDescriptor :
        SerialDescriptor by serialDescriptor<List<WorkoutStore>>() {
        @ExperimentalSerializationApi
        override val serialName: String = "kotlinx.serialization.immutable.persistentList"
    }

    override val descriptor: SerialDescriptor = PersistentListDescriptor()

    override fun serialize(encoder: Encoder, value: PersistentList<WorkoutStore>) {
        return ListSerializer(serializer).serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): PersistentList<WorkoutStore> {
        return ListSerializer(serializer).deserialize(decoder).toPersistentList()
    }

}
