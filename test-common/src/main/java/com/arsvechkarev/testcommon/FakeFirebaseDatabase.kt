package com.arsvechkarev.testcommon

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.JsonPrimitive
import firebase.database.FirebaseDatabase

class FakeFirebaseDatabase(json: String) : FirebaseDatabase {
  
  val rootJsonObject: JsonElement = JsonParser.parseString(json)
  
  override suspend fun getList(path: String): MutableList<String> {
    val children = path.split("/")
        .filter { it.isNotBlank() }
    var obj: Any = rootJsonObject.asJsonObject.get(children[0])
    for (i in 1 until children.size) {
      val name = children[i]
      val tempObj = (obj as? JsonObject)?.get(name)
      if (tempObj is JsonObject) {
        obj = tempObj
      } else if (tempObj is JsonArray) {
        obj = tempObj
      }
    }
    if (obj is JsonArray) {
      val list = ArrayList<String>()
      obj.forEach { element -> list.add((element as JsonPrimitive).asString) }
      return list
    }
    return ArrayList()
  }
  
  override suspend fun setValues(map: Map<String, Any>) {
    for ((path, value) in map) {
      val children = path.split("/")
          .filter { it.isNotBlank() }
          .toMutableList()
      val dstJsonName = children.last()
      var obj: Any = rootJsonObject.asJsonObject
      for (i in 0 until children.size - 1) {
        val name = children[i]
        obj = (obj as JsonObject).get(name)
      }
      if (obj is JsonObject) {
        val list = JsonArray()
        if (value is List<*>) {
          value.forEach {
            list.add(it as String)
          }
          obj.add(dstJsonName, list)
          println()
        } else if (value is String) {
          obj.addProperty(dstJsonName, "")
        }
      }
    }
  }
}