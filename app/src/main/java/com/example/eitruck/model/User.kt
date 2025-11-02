package com.example.eitruck.model

class User (
    val nomeCompleto:String,
    val email:String,
    val urlFoto:String,
    val telefone:String
)

class UserVerify (
    val id:Int,
    val email: String,
    val codigo: String
)

class UserPassword (
    val senha:String,
)