package com.example.sd_bssd_hw31

import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class Note (var name: String, var decs:String, var date:String){

    init {
        if (date == null){
            //set to today's date
            date = Date().toString()
        }
    }

    fun toJSON(): JSONObject{
        //make a new json object
        val jsonObject = JSONObject().apply{
            //put each piece of data into the object.
            put("name", name)
            put("date", date)
            put("desc", decs)
        }

        return jsonObject
    }

    override fun toString():String {
        return "$name, $date, $decs"
    }
}