package anekdotas.mindgameapplication

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class QuestionsProtoActivity : AppCompatActivity(), View.OnClickListener {

    // Variables to allow user to navigate through the questions
    private var myPosition = 1 //current question position
    private var myQuestionsList : ArrayList<Question>? = null //list of questions
    private var mySelectedPosition = 0 //selected position between answers in a specific question

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questions_proto)
        val optionA = findViewById<TextView>(R.id.tv_optionA)
        val optionB = findViewById<TextView>(R.id.tv_optionB)
        val optionC = findViewById<TextView>(R.id.tv_optionC)
        val optionD = findViewById<TextView>(R.id.tv_optionD)
        val submitButton = findViewById<Button>(R.id.btn_submit)
        val progressBar = findViewById<ProgressBar>(R.id.ll_progress)

        myQuestionsList = QuestionsObjectConst.getQuestions()
        progressBar.max=myQuestionsList!!.size
        setQuestion()

        optionA.setOnClickListener(this)
        optionB.setOnClickListener(this)
        optionC.setOnClickListener(this)
        optionD.setOnClickListener(this)
        submitButton.setOnClickListener(this)

        // Helpful in logcat for checking the amount of questions in input
        // Log.i("Questions size", questionsList.size.toString())
        }

    @SuppressLint("SetTextI18n")
    private fun setQuestion(){

        val question = myQuestionsList!![myPosition-1]
        defaultOptionView()

        val submitButton = findViewById<Button>(R.id.btn_submit)

        if(myPosition == myQuestionsList!!.size){
            submitButton.text="FINISH"
        }
        else{
            submitButton.text="SUBMIT"
        }

        val progressbar=findViewById<ProgressBar>(R.id.ll_progress)
        val progressText=findViewById<TextView>(R.id.tv_progress)
        val questionView=findViewById<TextView>(R.id.tv_question)
        val questionImage=findViewById<ImageView>(R.id.iv_question_image)
        val optionA = findViewById<TextView>(R.id.tv_optionA)
        val optionB = findViewById<TextView>(R.id.tv_optionB)
        val optionC = findViewById<TextView>(R.id.tv_optionC)
        val optionD = findViewById<TextView>(R.id.tv_optionD)

        progressbar.progress = myPosition
        progressText.text = "$myPosition" + "/" + myQuestionsList!!.size
        questionView.text = question!!.question
        questionImage.setImageResource(question.image)
        optionA.text = question.optionA
        optionB.text = question.optionB
        optionC.text = question.optionC
        optionD.text = question.optionD
    }

    private fun defaultOptionView(){
        val optionA = findViewById<TextView>(R.id.tv_optionA)
        val optionB = findViewById<TextView>(R.id.tv_optionB)
        val optionC = findViewById<TextView>(R.id.tv_optionC)
        val optionD = findViewById<TextView>(R.id.tv_optionD)
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

    @SuppressLint("SetTextI18n")
    override fun onClick(v: View?) {
        val optionA = findViewById<TextView>(R.id.tv_optionA)
        val optionB = findViewById<TextView>(R.id.tv_optionB)
        val optionC = findViewById<TextView>(R.id.tv_optionC)
        val optionD = findViewById<TextView>(R.id.tv_optionD)
        val submitButton = findViewById<Button>(R.id.btn_submit)
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
            R.id.btn_submit-> {
                if(mySelectedPosition == 0){
                    myPosition++

                    when {
                        myPosition <= myQuestionsList!!.size ->{
                            setQuestion()
                        }else -> {
                            Toast.makeText(this, "Quiz has been complete, congrats boy", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                else{
                    val question = myQuestionsList?.get(myPosition-1)
                    if(question!!.answer != mySelectedPosition){
                        answerView(mySelectedPosition, R.drawable.wrong_option_bg)
                    }
                    answerView(question.answer, R.drawable.correct_option_bg)

                    if(myPosition == myQuestionsList!!.size){
                        submitButton.text = "FINISH"
                    }
                    else{
                        submitButton.text = "NEXT QUESTION"
                    }
                    mySelectedPosition=0
                }
            }
        }
    }

    private fun selectionView(tv: TextView, selectedOptionNum: Int){
        defaultOptionView() // resets to default
        mySelectedPosition = selectedOptionNum

            tv.setTextColor(Color.parseColor("#000000")) // changes colour of the option
            tv.setTypeface(tv.typeface, Typeface.BOLD)
            tv.background = ContextCompat.getDrawable(
                this,
                R.drawable.selected_option_bg)
    }

    private fun answerView(answer: Int, drawableView : Int) {
        val optionA = findViewById<TextView>(R.id.tv_optionA)
        val optionB = findViewById<TextView>(R.id.tv_optionB)
        val optionC = findViewById<TextView>(R.id.tv_optionC)
        val optionD = findViewById<TextView>(R.id.tv_optionD)

        when(answer){
            1 -> {
                optionA.background = ContextCompat.getDrawable(this, drawableView)
            }
            2 -> {
                optionB.background = ContextCompat.getDrawable(this, drawableView)
            }
            3 -> {
                optionC.background = ContextCompat.getDrawable(this, drawableView)
            }
            4 -> {
                optionD.background = ContextCompat.getDrawable(this, drawableView)
            }
        }
    }
}