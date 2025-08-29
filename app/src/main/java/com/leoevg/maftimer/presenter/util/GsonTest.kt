package com.leoevg.maftimer.presenter.util

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

// Простой класс для тестирования Gson
data class TestUser(
    @SerializedName("name")
    val name: String,
    
    @SerializedName("age")
    val age: Int,
    
    @SerializedName("is_active")
    val isActive: Boolean
)

object GsonTest {
    
    fun testGson(): String {
        val gson = Gson()
        
        // Создаём тестовый объект
        val user = TestUser(
            name = "Иван",
            age = 25,
            isActive = true
        )
        
        // Сериализуем в JSON
        val json = gson.toJson(user)
        
        // Десериализуем обратно
        val deserializedUser = gson.fromJson(json, TestUser::class.java)
        
        // Проверяем, что данные совпадают
        val isCorrect = user == deserializedUser
        
        return """
            Gson Test Results:
            Original: $user
            JSON: $json
            Deserialized: $deserializedUser
            Test passed: $isCorrect
        """.trimIndent()
    }
    
    fun testJsonParsing(): String {
        val gson = Gson()
        
        val jsonString = """
            {
                "name": "Мария",
                "age": 30,
                "is_active": false
            }
        """.trimIndent()
        
        val user = gson.fromJson(jsonString, TestUser::class.java)
        
        return """
            JSON Parsing Test:
            Input JSON: $jsonString
            Parsed object: $user
        """.trimIndent()
    }
}
