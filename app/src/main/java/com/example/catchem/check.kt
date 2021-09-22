package com.example.catchem

fun main() {
    var x:Int=(Math.random()*10).toInt()
    var y=Math.random()*10
    val current_lat:Double=3.2
    if(x%2==0)
        println (current_lat?.plus(y))
    else
        println (current_lat?.minus(y))
}