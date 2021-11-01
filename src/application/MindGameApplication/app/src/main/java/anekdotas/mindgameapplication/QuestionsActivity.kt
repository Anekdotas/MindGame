package anekdotas.mindgameapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import anekdotas.mindgameapplication.databinding.ActivityQuestionsBinding
import anekdotas.mindgameapplication.databinding.ActivityQuestionsProtoBinding
import anekdotas.mindgameapplication.java.ChatAdapter
import anekdotas.mindgameapplication.java.Message
import anekdotas.mindgameapplication.network.QuestionModel
import anekdotas.mindgameapplication.objects.QuestionsObject
import anekdotas.mindgameapplication.objects.UserObjectConst
import coil.load
import kotlin.random.Random

class QuestionsActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityQuestionsBinding
    var messageList =  ArrayList<Message>()
    // Variables to allow user to navigate through the questions
    private var myPosition = 1 //current question position
    private var myQuestionsList: MutableList<QuestionModel>? = null //list of questions
    private var mySelectedPosition = 0 //selected position between answers in a specific question
    private var myCorrectAnswers = 0 // number of questions answered correctly
    private var myUserName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myUserName = intent.getStringExtra(UserObjectConst.USERNAME)
        myQuestionsList = QuestionsObject.questionList?.toMutableList()
        setQuestion()

        binding.tvOptionA.setOnClickListener(this)
        binding.tvOptionB.setOnClickListener(this)
        binding.tvOptionC.setOnClickListener(this)
        binding.tvOptionD.setOnClickListener(this)
        binding.btnSubmit.setOnClickListener(this)

        //======================================

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

        val adapter = ChatAdapter(this, R.layout.message_list_view_element, messageList)
        binding.ListView.adapter = adapter
        messageList.add(Message("Professor Lazgov", question.question, R.drawable.bred))



        binding.tvOptionA.text = question.options[0]
        binding.tvOptionB.text = question.options[1]
        binding.tvOptionC.text = question.options[2]
        binding.tvOptionD.text = question.options[3]
        binding.tvOptionA.isClickable=true
        binding.tvOptionB.isClickable=true
        binding.tvOptionC.isClickable=true
        binding.tvOptionD.isClickable=true
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
                    myPosition++ // SKIPS ANSWERING
                    when {
                        myPosition <= myQuestionsList!!.size -> {
                            setQuestion() // STARTS NEW QUESTION
                        }
                        else -> {
                            val intent = Intent(this, ResultsActivity::class.java)
                            intent.putExtra(UserObjectConst.USERNAME, myUserName)
                            intent.putExtra(UserObjectConst.CORRECT_ANSWERS, myCorrectAnswers)
                            intent.putExtra(
                                UserObjectConst.TOTAL_QUESTIONS,
                                myQuestionsList!!.size
                            )
                            startActivity(intent) // ENDS THE QUIZ
                        }
                    }
                } else {
                    val question = myQuestionsList?.get(myPosition - 1)

                    val adapter = ChatAdapter(this, R.layout.message_list_view_element, messageList)
                    binding.ListView.adapter = adapter
                    when (mySelectedPosition){
                        1 -> messageList.add(Message(UserObjectConst.USERNAME, binding.tvOptionA.text.toString(), R.drawable.chuvas_cropped))
                        2 -> messageList.add(Message(UserObjectConst.USERNAME, binding.tvOptionB.text.toString(), R.drawable.chuvas_cropped))
                        3 -> messageList.add(Message(UserObjectConst.USERNAME, binding.tvOptionC.text.toString(), R.drawable.chuvas_cropped))
                        4 -> messageList.add(Message(UserObjectConst.USERNAME, binding.tvOptionD.text.toString(), R.drawable.chuvas_cropped))
                    } //WRITES USER SELECTED ANSWER

                    binding.tvOptionA.isClickable=false
                    binding.tvOptionB.isClickable=false
                    binding.tvOptionC.isClickable=false
                    binding.tvOptionD.isClickable=false

                    if (question!!.answer != mySelectedPosition) {
                        answerView(mySelectedPosition, R.drawable.wrong_option_bg)
                        messageList.add(Message("Professor Lazgov", "You stoopid?", R.drawable.bred))
                    } // CHECKS IF ANSWER WAS INCORRECT

                    else {
                        myCorrectAnswers++
                        messageList.add(Message("Professor Lazgov", "Well...done?", R.drawable.bred))
                    } //IF THE ANSWER WAS CORRECT

                    answerView(question.answer, R.drawable.correct_option_bg) //COLORS THE CORRECT

                    if (myPosition == myQuestionsList!!.size) {
                        binding.btnSubmit.text = "FINISH"
                    } else {
                        binding.btnSubmit.text = "NEXT QUESTION"
                    } // CHANGES TEXT IF LAST QUESTION
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