package com.example.questionlist

class checkQuestion(title: String) {
    private var title: String=title
    private var checked: Boolean =false

    fun getTitle():String{
        return title
    }

    fun setTitle(ttl:String){
        title = ttl
    }

    fun getChecked():Boolean{
        return checked
    }

    fun setChecked(chk:Boolean){
        checked = chk
    }


}