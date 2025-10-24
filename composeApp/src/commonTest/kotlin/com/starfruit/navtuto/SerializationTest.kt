package com.starfruit.navtuto

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.json.Json
import kotlinx.serialization.protobuf.ProtoBuf
import kotlin.test.Test
import kotlin.test.assertEquals

class SerializationTest {
    @OptIn(ExperimentalSerializationApi::class)
    @Test
    fun serializationTest_happy_path() {
        @Serializable
        data class User(val name: String, val age: Int=10)

        val user = User("Alice", 30)

        //serialize
        val bytes: ByteArray = ProtoBuf.encodeToByteArray(User.serializer(), user)
        val json: String = Json.encodeToString(user)
        val bytesCbor: ByteArray = Cbor.encodeToByteArray(User.serializer(), user)

        //deserialize
        val user1: User = ProtoBuf.decodeFromByteArray(User.serializer(), bytes)
        val user2: User = Json.decodeFromString(json)
        val user3: User = Cbor.decodeFromByteArray(User.serializer(), bytesCbor)

        assertEquals(user.name, user1.name)
        assertEquals(user.age, user1.age)
        assertEquals(user.name, user2.name)
        assertEquals(user.age, user2.age)
        assertEquals(user.name, user3.name)
        assertEquals(user.age, user3.age)

        println("protobuf=${bytes.size}, cbor=${bytesCbor.size}, json=${json.count()}")
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Test
    fun serializationTest_protobuf_unhappy_path() {
        @Serializable
        data class User(val name: String, val age: Int)

        val user = User("Alice", 30)
        //serialize
        val bytes: ByteArray = ProtoBuf.encodeToByteArray(User.serializer(), user)

        //the class User is changed with time
        @Serializable
        data class UserV2(val name: String, val sex: Int?)

        //deserialize
        val user1: UserV2 = ProtoBuf.decodeFromByteArray(UserV2.serializer(), bytes)

        //and it is not good!
        assertEquals(30, user1.sex)
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Test
    fun serializationTest_cbor_difficult_path() {
        @Serializable
        data class User(val name: String, val age: Int)

        val user = User("Alice", 30)
        //serialize
        val bytes: ByteArray = Cbor.encodeToByteArray(User.serializer(), user)

        //the class User is changed with time
        @Serializable
        data class UserV2(val name: String, val sex: Int = 2)

        //deserialization: Detect unrecognized field + Detect missing field
        val user1: UserV2 = Cbor { ignoreUnknownKeys=true }.decodeFromByteArray(UserV2.serializer(), bytes)

        //and it is ok!
        assertEquals(2, user1.sex)
    }

}