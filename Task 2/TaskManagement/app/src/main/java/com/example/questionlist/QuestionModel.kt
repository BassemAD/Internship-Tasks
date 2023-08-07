package com.example.questionlist

class QuestionModel(title: String, comment: String, checkBoxes: MutableList<checkQuestion>, number:Int){
    private val title = title
    private var comment = comment
    private var checkBoxes = checkBoxes
    private val number:Int = number

    fun getNumber():Int{
        return number
    }

    fun getTitle():String{
        return title
    }

    fun setComment(cmt:String){
        comment = cmt
    }

    fun getComment():String{
        return comment
    }

    fun getCheckBoxes():MutableList<checkQuestion>{
        return checkBoxes
    }

    fun setCheckBoxes(chkQst: MutableList<checkQuestion>){
        checkBoxes = chkQst
    }

}