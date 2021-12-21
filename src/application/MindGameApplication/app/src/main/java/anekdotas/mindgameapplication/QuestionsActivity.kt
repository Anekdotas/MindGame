package anekdotas.mindgameapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import anekdotas.mindgameapplication.databinding.ActivityQuestionsBinding
import anekdotas.mindgameapplication.helpers.HostTalk
import anekdotas.mindgameapplication.helpers.RandomGen
import anekdotas.mindgameapplication.helpers.Time
import anekdotas.mindgameapplication.java.ChatAdapter
import anekdotas.mindgameapplication.java.Message
import anekdotas.mindgameapplication.network.QuestionModel
import anekdotas.mindgameapplication.objects.*
import java.util.*
import kotlin.collections.ArrayList

class QuestionsActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityQuestionsBinding
    private var messageList =  ArrayList<Message>()
    // Variables to allow user to navigate through the questions
    private var myPosition = 1 //current question position
    private var myQuestionsList: MutableList<QuestionModel>? = null //list of questions
    private var mySelectedPosition = 0 //selected position between answers in a specific question
    private var myCorrectAnswers = 0 // number of questions answered correctly
    private var myUserName: String? = null
    val T = Timer()

    var isMessageAudioSet = false
    var messageAudio: MediaPlayer? = null
    var setAudioMessagesID: Int? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        messageList.add(Message(HostObject.host.hostName, HostTalk.giveRandomGreeting(), R.drawable.lasgov))
        myUserName = intent.getStringExtra(UserObjectConst.USERNAME)
        myQuestionsList = QuestionsObject.questionList.toMutableList()
        setQuestion()

        Time.resetTime()
        T.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    UserObjectConst.sessionTimeSeconds++
                }
            }
        }, 1000, 1000)

        binding.tvOptionA.setOnClickListener(this)
        binding.tvOptionB.setOnClickListener(this)
        binding.tvOptionC.setOnClickListener(this)
        binding.tvOptionD.setOnClickListener(this)
        binding.btnSubmit.setOnClickListener(this)


        //FIX THIS MESS
        // - - - - - AUDIO PLAY - - - - -
        var isAudioPaused = true
        binding.ListView.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, i, _ ->

    //            Log.d("|+| ", i.toString())
    //            Log.d("|+| ", "Author: " + messageList.get(i).author)
    //            Log.d("|+|", "audio ID: " + messageList.get(i).audio)

    //          - - - - - RELEASE PLAYER IF DIFFERENT MESSAGE IS SELECTED - - - - -
                if ((isMessageAudioSet && (setAudioMessagesID != i || setAudioMessagesID == null)) && messageList[i].audio != null) {
    //                isMessageAudioSet = false
                    messageAudio?.release()
                }

    //          - - - - - PLAY OR PAUSE AUDIO BY CLICKING ON THE SAME MESSAGE - - - - -
                if (isMessageAudioSet && setAudioMessagesID == i && messageList[i].audio != null)
                    isAudioPaused = when (isAudioPaused) {
                        true -> {
                            messageAudio?.start()
                            false
                        }
                        false -> {
                            messageAudio?.pause()
                            true
                        }
                    }

    //          - - - - - SET NEW AUDIO IF DIFFERENT MESSAGE IS SELECTED - - - - -
                if (messageList.get(i).audio != null && setAudioMessagesID != i) {
                    messageAudio = MediaPlayer.create(this, messageList.get(i).audio.toUri())
                    messageAudio?.start()
                    isAudioPaused = false
                    isMessageAudioSet = true
                    setAudioMessagesID = i
                    Log.d(
                        "|+|",
                        "there is audio"
                    )
                } else {
                    Log.d(
                        "|+|",
                        "Status: This message is without audio OR this audio is already being played"
                    )
                }

            }
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

        if(myPosition!=1){
            messageList.add(Message(HostObject.host.hostName, HostTalk.giveMoveOn(), R.drawable.lasgov))
        }
        if(RandomGen.chance(25) && myPosition!=1){
            messageList.add(Message(HostObject.host.hostName, HostTalk.saySomething(), R.drawable.lasgov))
        }

        when {
            question.media==null -> {
                messageList.add(Message(HostObject.host.hostName, question.question, R.drawable.lasgov))
            }
            question.media.endsWith(".jpg") -> {
                messageList.add(Message(HostObject.host.hostName, question.question, R.drawable.lasgov, question.media))
            }
            question.media.endsWith(".mp3") -> {
                messageList.add(Message(HostObject.host.hostName, question.question+"\n CLICK TO PLAY \uD83D\uDD0A", R.drawable.lasgov, "", question.media))
            }
            else -> {
                Log.e("Media", "Wrong media type!")
            }
        }


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
            option.setTextColor(Color.parseColor("#F2F3F5")) // changes colour of the option
            option.typeface = Typeface.DEFAULT // changes color back to default
            option.background = ContextCompat.getDrawable(
                this,
                R.drawable.button_design_other
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
                    myPosition++ // SKIPS ANSWERING
                    when {
                        myPosition <= myQuestionsList!!.size -> {
                            setQuestion() // STARTS NEW QUESTION
                        }
                        else -> {
                            Time.formatTime(UserObjectConst.sessionTimeSeconds)
                            val intent = Intent(this, ResultsActivity::class.java)
                            intent.putExtra(UserObjectConst.USERNAME, myUserName)
                            intent.putExtra(UserObjectConst.CORRECT_ANSWERS, myCorrectAnswers)
                            intent.putExtra(
                                UserObjectConst.TOTAL_QUESTIONS,
                                myQuestionsList!!.size
                            )
                            T.cancel()
                            startActivity(intent)
                            finish()// ENDS THE QUIZ
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
                        answerView(mySelectedPosition, R.drawable.custom_wrong_btn)
                        messageList.add(Message(HostObject.host.hostName, HostTalk.giveRandomBad(), R.drawable.lasgov))
                        UserStatsObject.sessionStreak=0
                    } // CHECKS IF ANSWER WAS INCORRECT

                    else {
                        myCorrectAnswers++
                        messageList.add(Message(HostObject.host.hostName, HostTalk.giveRandomGood(), R.drawable.lasgov))
                        UserStatsObject.sessionStreak++
                    } //IF THE ANSWER WAS CORRECT

                    answerView(question.answer, R.drawable.custom_correct_btn) //COLORS THE CORRECT

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

        tv.setTextColor(Color.parseColor("#FFFFFF")) // changes colour of the option
        tv.setTypeface(tv.typeface, Typeface.BOLD)
        tv.background = ContextCompat.getDrawable(
            this,
            R.drawable.custom_button
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

    override fun onBackPressed() {
        val intent = Intent(this, MainMenuActivity::class.java)
        T.cancel()
        startActivity(intent)
        finish()
    }

}
