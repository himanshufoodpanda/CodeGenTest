package com.example.gsontest

import com.google.gson.Gson

interface Codable:Encodable {
    fun encode():String
}

interface Encodable {

}



interface Encoder {

  fun encode(codable: Encodable):String
  fun encode(codable: String,key:String):String

  interface Container{
      fun encode(codable: String,key:String)
      fun getContainerMap():Map<String,String>
  }

    fun getContainerWithKeys(): Container
}



