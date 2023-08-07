package com.example.questionlist

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.questionlist.ui.theme.QuestionListTheme

//import kotlinx.coroutines.flow.internal.NoOpContinuation.context
//import kotlin.coroutines.jvm.internal.CompletedContinuation.context

class MainActivity : ComponentActivity() {

    lateinit var context: Context
    // list of groups
    val groups: MutableList<GroupModel> = mutableListOf(
        GroupModel(
            title = "Task 1",
            number = 1,
            questions = mutableListOf(
                QuestionModel(
                    title = "Subtask 1 title",
                    comment = "Comment 1",
                    checkBoxes = mutableListOf(
                        checkQuestion("Not Finished"),
                        checkQuestion("In Progress"),
                        checkQuestion("Done")
                    ),
                    number = 1
                ),
                QuestionModel(
                    title = "Subtask 2 title",
                    comment = "Comment 2",
                    checkBoxes = mutableListOf(
                        checkQuestion("Not Finished"),
                        checkQuestion("In Progress"),
                        checkQuestion("Done")
                    ),
                    number = 2
                )
            )
        ),
        GroupModel(
            title = "Task 2",
            number = 2,
            questions = mutableListOf(
                QuestionModel(
                    title = "Subtask 1 title",
                    comment = "Comment 3",
                    checkBoxes = mutableListOf(
                        checkQuestion("Not Finished"),
                        checkQuestion("In Progress"),
                        checkQuestion("Done")
                    ),
                    number = 3
                ),
                QuestionModel(
                    title = "Subtask 2 title",
                    comment = "Comment 4",
                    checkBoxes = mutableListOf(
                        checkQuestion("Not Finished"),
                        checkQuestion("In Progress"),
                        checkQuestion("Done")
                    ),
                    number = 4
                )
            )
        )
    )

    var llGroups :LinearLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_main)
        context = this
        llGroups = findViewById(R.id.llGroups)



        // Iterate over the groups list
        for (group in groups) {
            // Bind the group to its corresponding view
            val groupView = bindGroup(group)

            //create a child view (questions) that's part of the parent view (group)
            var childView = LinearLayout(context)
            childView.visibility = View.GONE
            childView.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, //width
                LinearLayout.LayoutParams.MATCH_PARENT, //height
            )
            childView.orientation = LinearLayout.VERTICAL

            groupView.setOnClickListener{
                if (childView.visibility == View.GONE){
                    childView.visibility = View.VISIBLE
                }
                else{
                    childView.visibility = View.GONE
                }
            }



            // Add the group view to the linear layout
            llGroups?.addView(groupView)


            // Iterate over the questions in the group
            for (question in group.getQuestion()) {
                // Bind the question to its corresponding view
                val questionView = bindQuestion(question)
                // Add the question view to the linear layout
                childView.addView(questionView)
            }

            llGroups?.addView(childView)
        }
    }

    fun bindGroup(grp: GroupModel):View{
        // Assuming you have an activity or a view context available
        val inflater = LayoutInflater.from(context)

        // Inflate the XML layout
        val view = inflater.inflate(R.layout.group_view, null)

        //set the title
        var txtTitle = view.findViewById(R.id.txtGroupTitle) as TextView
        txtTitle.text = grp.getTitle()

        //set an id for the view
        view.id = grp.getNumber()

        //set the number of questions
        var txtNbQuestions = view.findViewById(R.id.txtNbQuestions) as TextView
        txtNbQuestions.text = grp.getQuestion().size.toString()

        return view
    }

    fun bindQuestion(qst: QuestionModel):View{
        // Assuming you have an activity or a view context available
        val inflater = LayoutInflater.from(context)

        // Inflate the XML layout
        val view = inflater.inflate(R.layout.question_view, null)

        //set the question title
        var txtQuestionTitle = view.findViewById(R.id.txtQuestionTitle) as TextView
        txtQuestionTitle.text = qst.getTitle()
//        view.id = "question" + qst.getNumber().toString()

        //set the view id
        view.id = qst.getNumber() + 200

        //set the text and checked status of each checkbox
        var ch1 = view.findViewById(R.id.ch1) as CheckBox
        ch1.text = qst.getCheckBoxes()[0].getTitle()
        ch1.isChecked = qst.getCheckBoxes()[0].getChecked()
        ch1.setOnCheckedChangeListener { _, isChecked ->
            // Update the status of the object in getCheckBoxes()[0] based on the checked state
            qst.getCheckBoxes()[0].setChecked(isChecked)
        }

        var ch2 = view.findViewById(R.id.ch2) as CheckBox
        ch2.text = qst.getCheckBoxes()[1].getTitle()
        ch2.isChecked = qst.getCheckBoxes()[1].getChecked()
        ch2.setOnCheckedChangeListener { _, isChecked ->
            // Update the status of the object in getCheckBoxes()[1] based on the checked state
            qst.getCheckBoxes()[1].setChecked(isChecked)
        }


        var ch3 = view.findViewById(R.id.ch3) as CheckBox
        ch3.text = qst.getCheckBoxes()[2].getTitle()
        ch3.isChecked = qst.getCheckBoxes()[2].getChecked()
        ch3.setOnCheckedChangeListener { _, isChecked ->
            // Update the status of the object in getCheckBoxes()[2] based on the checked state
            qst.getCheckBoxes()[2].setChecked(isChecked)
        }

        //set the comment
        var et1 = view.findViewById(R.id.et1) as EditText
        qst.setComment(et1.text.toString())

        et1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not needed for this case, but required to implement
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Update the comment property of the QuestionModel
                qst.setComment(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                // Not needed for this case, but required to implement
            }
        })





        return view

    }
}




