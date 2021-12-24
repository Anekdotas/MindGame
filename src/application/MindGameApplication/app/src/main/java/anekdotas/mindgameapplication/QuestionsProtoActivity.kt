package anekdotas.mindgameapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import anekdotas.mindgameapplication.databinding.ActivityQuestionsProtoBinding
import anekdotas.mindgameapplication.network.QuestionModel
import anekdotas.mindgameapplication.objects.QuestionsObject
import anekdotas.mindgameapplication.objects.UserObjectConst
import coil.load

// TODO: 10/3/2021 interface kinda buggy - user can still change answer, need to fix that

class QuestionsProtoActivity : AppCompatActivity(), View.OnClickListener {


    private lateinit var binding: ActivityQuestionsProtoBinding // UI element binding

    // Variables to allow user to navigate through the questions
    private var myPosition = 1 //current question position
    private var myQuestionsList: MutableList<QuestionModel>? = null //list of questions
    private var mySelectedPosition = 0 //selected position between answers in a specific question
    private var myCorrectAnswers = 0 // number of questions answered correctly
    private var myUserName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("Test2! ", ""+ QuestionsObject.questionList)
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionsProtoBinding.inflate(layoutInflater)
        setContentView(binding.root)


        myUserName = intent.getStringExtra(UserObjectConst.USERNAME)
        myQuestionsList = QuestionsObject.questionList?.toMutableList()
        binding.llProgress.max = myQuestionsList!!.size
        setQuestion()

        binding.tvOptionA.setOnClickListener(this)
        binding.tvOptionB.setOnClickListener(this)
        binding.tvOptionC.setOnClickListener(this)
        binding.tvOptionD.setOnClickListener(this)
        binding.btnSubmit.setOnClickListener(this)

        // Helpful in logcat for checking the amount of questions in input:
        // Log.i("Questions size", questionsList.size.toString())
    }

    @SuppressLint("SetTextI18n")
    private fun setQuestion() {

        val question = myQuestionsList!![myPosition - 1]
        defaultOptionView()

        if (myPosition == myQuestionsList!!.size) {
            binding.btnSubmit.text = "FINISH"
        } else {
            binding.btnSubmit.text = "SUBMIT"
        }

        binding.llProgress.progress = myPosition
        binding.tvProgress.text = "$myPosition" + "/" + myQuestionsList!!.size
        binding.tvQuestion.text = question.question
        //Checks if there is an image otherwise uses default

        binding.tvOptionA.text = question.options[0].text
        binding.tvOptionB.text = question.options[1].text
        binding.tvOptionC.text = question.options[2].text
        binding.tvOptionD.text = question.options[3].text
    }

    private fun defaultOptionView() {

        val options = ArrayList<TextView>()
        options.add(0, binding.tvOptionA)
        options.add(1, binding.tvOptionB)
        options.add(2, binding.tvOptionC)
        options.add(3, binding.tvOptionD)

        for (option in options) {
            option.setTextColor(Color.parseColor("#7A8089")) // changes colour of the option
            option.typeface = Typeface.DEFAULT // changes color back to default
            option.background = ContextCompat.getDrawable(
                this,
                R.drawable.default_option_bg
            )
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.tv_optionA -> {
                selectionView(binding.tvOptionA, 1)
            }
            R.id.tv_optionB -> {
                selectionView(binding.tvOptionB, 2)
            }
            R.id.tv_optionC -> {
                selectionView(binding.tvOptionC, 3)
            }
            R.id.tv_optionD -> {
                selectionView(binding.tvOptionD, 4)
            }
            R.id.btn_submit -> {
                if (mySelectedPosition == 0) {
                    myPosition++

                    when {
                        myPosition <= myQuestionsList!!.size -> {
                            setQuestion()
                        }
                        else -> {
                            val intent = Intent(this, ResultsActivity::class.java)
                            intent.putExtra(UserObjectConst.USERNAME, myUserName)
                            intent.putExtra(UserObjectConst.CORRECT_ANSWERS, myCorrectAnswers)
                            intent.putExtra(
                                UserObjectConst.TOTAL_QUESTIONS,
                                myQuestionsList!!.size
                            )
                            startActivity(intent)
                        }
                    }
                } else {
                    val question = myQuestionsList?.get(myPosition - 1)
                    if (question!!.answer != mySelectedPosition) {
                        answerView(mySelectedPosition, R.drawable.wrong_option_bg)
                    } else {
                        myCorrectAnswers++
                    }
                    answerView(question.answer, R.drawable.correct_option_bg)

                    if (myPosition == myQuestionsList!!.size) {
                        binding.btnSubmit.text = "FINISH"
                    } else {
                        binding.btnSubmit.text = "NEXT QUESTION"
                    }
                    mySelectedPosition = 0
                }
            }
        }
    }

    private fun selectionView(tv: TextView, selectedOptionNum: Int) {

        defaultOptionView() // resets to default
        mySelectedPosition = selectedOptionNum

        tv.setTextColor(Color.parseColor("#000000")) // changes colour of the option
        tv.setTypeface(tv.typeface, Typeface.BOLD)
        tv.background = ContextCompat.getDrawable(
            this,
            R.drawable.selected_option_bg
        )
    }

    private fun answerView(answer: Int, drawableView: Int) {

        when (answer) {
            1 -> {
                binding.tvOptionA.background = ContextCompat.getDrawable(this, drawableView)
            }
            2 -> {
                binding.tvOptionB.background = ContextCompat.getDrawable(this, drawableView)
            }
            3 -> {
                binding.tvOptionC.background = ContextCompat.getDrawable(this, drawableView)
            }
            4 -> {
                binding.tvOptionD.background = ContextCompat.getDrawable(this, drawableView)
            }
        }
    }
}

