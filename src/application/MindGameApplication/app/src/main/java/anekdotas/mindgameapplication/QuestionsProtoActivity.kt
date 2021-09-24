package anekdotas.mindgameapplication

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat

class QuestionsProtoActivity : AppCompatActivity(), View.OnClickListener {
    // Variables to allow user to navigate through the questions
    private var myPosition = 1
    private var myQuestionsList : ArrayList<Question>? = null
    private var mySelectedPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questions_proto)
        var optionA = findViewById<TextView>(R.id.tv_optionA)
        var optionB = findViewById<TextView>(R.id.tv_optionB)
        var optionC = findViewById<TextView>(R.id.tv_optionC)
        var optionD = findViewById<TextView>(R.id.tv_optionD)

        myQuestionsList = QuestionsObjectConst.getQuestions()
        setQuestion()

        optionA.setOnClickListener(this)
        optionB.setOnClickListener(this)
        optionC.setOnClickListener(this)
        optionD.setOnClickListener(this)

        // Helpful in logcat for checking the amount of questions in input
        // Log.i("Questions size", questionsList.size.toString())
        }


    private fun setQuestion(){
        myPosition = 1
        val question = myQuestionsList!![myPosition-1]
        defaultOptionView()
        val progressbar=findViewById<ProgressBar>(R.id.ll_progress)
        val progressText=findViewById<TextView>(R.id.tv_progress)
        val questionView=findViewById<TextView>(R.id.tv_question)
        val questionImage=findViewById<ImageView>(R.id.iv_question_image)
        var optionA = findViewById<TextView>(R.id.tv_optionA)
        var optionB = findViewById<TextView>(R.id.tv_optionB)
        var optionC = findViewById<TextView>(R.id.tv_optionC)
        var optionD = findViewById<TextView>(R.id.tv_optionD)

        progressbar.progress = myPosition
        progressText.text = "$myPosition" + "/" + progressbar.max
        questionView.text = question!!.question
        questionImage.setImageResource(question.image)
        optionA.text = question.optionA
        optionB.text = question.optionB
        optionC.text = question.optionC
        optionD.text = question.optionD
    }

    private fun defaultOptionView(){
        var optionA = findViewById<TextView>(R.id.tv_optionA)
        var optionB = findViewById<TextView>(R.id.tv_optionB)
        var optionC = findViewById<TextView>(R.id.tv_optionC)
        var optionD = findViewById<TextView>(R.id.tv_optionD)
        val options = ArrayList<TextView>()
        options.add(0, optionA)
        options.add(1, optionB)
        options.add(2, optionC)
        options.add(3, optionD)


        for(option in options) {
            option.setTextColor(Color.parseColor("#7A8089")) // changes colour of the option
            option.typeface = Typeface.DEFAULT // changes color back to default
            option.background = ContextCompat.getDrawable(
                this,
                R.drawable.default_option_bg)
        }
    }

    override fun onClick(v: View?) {
        var optionA = findViewById<TextView>(R.id.tv_optionA)
        var optionB = findViewById<TextView>(R.id.tv_optionB)
        var optionC = findViewById<TextView>(R.id.tv_optionC)
        var optionD = findViewById<TextView>(R.id.tv_optionD)
        when(v?.id){
            R.id.tv_optionA-> {
                selectionView(optionA, 1)
            }
            R.id.tv_optionB-> {
                selectionView(optionB, 2)
            }
            R.id.tv_optionC-> {
                selectionView(optionC, 3)
            }
            R.id.tv_optionD-> {
                selectionView(optionD, 4)
            }
        }
    }

    private fun selectionView(tv: TextView, selectedOptionNum: Int){
        defaultOptionView() // resets to default
        mySelectedPosition = selectedOptionNum

            tv.setTextColor(Color.parseColor("#7A8089")) // changes colour of the option
            tv.setTypeface(tv.typeface, Typeface.BOLD) // changes color back to default
            tv.background = ContextCompat.getDrawable(
                this,
                R.drawable.selected_option_bg)
    }
}