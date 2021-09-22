package com.example.catchem

import android.location.Location

class Pokemons {
    var name:String?=null
    var type:String?=null
    var power:Double?=null
    var loc_poke: Location?=null
    var img: Int?=null
    constructor(name: String, type:String, power:Double, lat: Double, long: Double, img:Int) {
        this.name=name
        this.img=img
        this.loc_poke=Location(name)
        this.loc_poke!!.latitude=lat
        this.loc_poke!!.longitude=long
        this.power=power
        this.type=type
    }
}