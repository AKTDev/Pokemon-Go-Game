package com.udemycource.pokemongo

import android.location.Location

class Pokemon
{
    var name : String? = null
    var des : String? = null
    var img : Int? = null
//    var lat : Double? = null
//    var long : Double? = null
    var location : Location ? = null
    var isCatched : Boolean? = false
    var power : Double? = null

    constructor(name : String,des:String,img : Int,lat:Double,long:Double,power:Double)
    {
        this.name = name
        this.des = des
        this.img = img
//        this.lat = lat
//        this.long = long
        this.location = Location(name)
        this.location!!.latitude = lat
        this.location!!.longitude = long
        this.isCatched = false
        this.power = power
    }
}