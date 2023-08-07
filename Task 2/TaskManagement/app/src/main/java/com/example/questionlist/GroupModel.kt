package com.example.questionlist

class GroupModel(title: String, number:Int, questions: MutableList<QuestionModel>){
    private val title = title
    private val number = number
    private val questions = questions

    fun getTitle():String {
        return title
    }

    fun getNumber():Int{
        return number
    }

    fun getQuestion():MutableList<QuestionModel>{
        return questions
    }

}