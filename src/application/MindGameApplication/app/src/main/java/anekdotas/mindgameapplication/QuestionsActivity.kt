package anekdotas.mindgameapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import anekdotas.mindgameapplication.databinding.ActivityQuestionsBinding
import anekdotas.mindgameapplication.helpers.HostTalk
import anekdotas.mindgameapplication.helpers.RandomGen
import anekdotas.mindgameapplication.helpers.Time
import anekdotas.mindgameapplication.java.ChatAdapter
import anekdotas.mindgameapplication.java.Message
import anekdotas.mindgameapplication.network.ChoiceModel
import anekdotas.mindgameapplication.network.QuestionModel
import anekdotas.mindgameapplication.objects.*
import java.util.*
import kotlin.collections.ArrayList

class QuestionsActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityQuestionsBinding
    private var messageList =  ArrayList<Message>()
    // Variables to allow user to navigate through the questions
    private var myPosition = 1      //current question position
    private var myQuestionsList: MutableList<QuestionModel>? = null     //list of questions
    private var mySelectedPosition = 0  //selected position between answers in a specific question
    private var myCorrectAnswers = 0    // number of questions answered correctly
    private var myUserName: String? = null
    val T = Timer()

    private var isMessageAudioSet = false
    private var messageAudio: MediaPlayer? = null
    private var setAudioMessagesID: Int? = null
    private var isAudioPaused = true
    private var x = 0
    private var select = false


    override fun onCreate(savedInstanceState: Bundle?) {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        messageList.add(Message(HostObject.host.hostName, HostTalk.giveRandomGreeting(), R.drawable.lasgov))
        myUserName = intent.getStringExtra(UserObjectConst.USERNAME)
        myQuestionsList = QuestionsObject.questionList.toMutableList()
        setQuestion()
        timerfun()

        binding.tvOptionA.setOnClickListener(this)
        binding.tvOptionB.setOnClickListener(this)
        binding.tvOptionC.setOnClickListener(this)
        binding.tvOptionD.setOnClickListener(this)
        binding.btnSubmit.setOnClickListener(this)


        // - - - - - AUDIO PLAY - - - - -
        binding.ListView.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, i, _ ->
//          - - - - - RELEASE PLAYER IF DIFFERENT MESSAGE IS SELECTED - - - - -
                if (isDifferentMessageSelected(i)) {
                    messageAudio?.release()
                }

//          - - - - - PLAY OR PAUSE AUDIO BY CLICKING ON THE SAME MESSAGE - - - - -
                playOrPauseAudio(i)

//          - - - - - SET NEW AUDIO IF DIFFERENT MESSAGE IS SELECTED - - - - -
                setNewAudio(i)
            }
    }

    private fun setNewAudio(i: Int) {
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

    private fun playOrPauseAudio(i: Int) {
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
    }

    private fun isDifferentMessageSelected(i: Int): Boolean {
        return (isMessageAudioSet && (setAudioMessagesID != i || setAudioMessagesID == null)) && messageList[i].audio != null
    }


    @SuppressLint("SetTextI18n")
    private fun setQuestion() {

        val question = myQuestionsList!![myPosition - 1]
        defaultOptionView()
        checkLast()
        setHostResponse(question)


        binding.tvOptionA.text = question.options[0].text
        binding.tvOptionB.text = question.options[1].text
        binding.tvOptionC.text = question.options[2].text
        binding.tvOptionD.text = question.options[3].text
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

                    if(!select){
                        StatObject.stats!!.choices.add(ChoiceModel(QuestionsObject.questionList[x].id, 0))
                    }

                    myPosition++ // COULD SKIP ANSWERING
                    when {
                        myPosition <= myQuestionsList!!.size -> {
                            setQuestion() // STARTS NEW QUESTION
                            select = false
                            x++
                        }
                        else -> {
                            //add time spent to stat object
                            StatObject.stats!!.secondsSpent=UserObjectConst.sessionTimeSeconds.toInt()
                            Time.formatTime(UserObjectConst.sessionTimeSeconds)
                            val intent = Intent(this, ResultsActivity::class.java)
                            intent.putExtra(UserObjectConst.USERNAME, myUserName)
                            intent.putExtra(UserObjectConst.CORRECT_ANSWERS, myCorrectAnswers)
                            intent.putExtra(
                                UserObjectConst.TOTAL_QUESTIONS,
                                myQuestionsList!!.size
                            )
                            T.cancel()


                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            startActivity(intent)
                            finish()// ENDS THE QUIZ
                        }
                    }
                } else {

                    println("printed!")
                    StatObject.stats!!.choices.add(ChoiceModel(QuestionsObject.questionList[x].id, QuestionsObject.questionList[x].options[mySelectedPosition-1].id))
                    select = true //didnt skip

                    val question = myQuestionsList?.get(myPosition - 1)

                    val adapter = ChatAdapter(this, R.layout.message_list_view_element, messageList)
                    binding.ListView.adapter = adapter
                    when (mySelectedPosition){
                        1 -> messageList.add(Message(UserObjectConst.USERNAME, binding.tvOptionA.text.toString(), R.drawable.chuvas_cropped))
                        2 -> messageList.add(Message(UserObjectConst.USERNAME, binding.tvOptionB.text.toString(), R.drawable.chuvas_cropped))
                        3 -> messageList.add(Message(UserObjectConst.USERNAME, binding.tvOptionC.text.toString(), R.drawable.chuvas_cropped))
                        4 -> messageList.add(Message(UserObjectConst.USERNAME, binding.tvOptionD.text.toString(), R.drawable.chuvas_cropped))
                    } //WRITES USER SELECTED ANSWER
                    nonClickable()

                    if (question!!.answer != QuestionsObject.questionList[myPosition-1].options[mySelectedPosition-1].id) {
                        answerView(mySelectedPosition, R.drawable.custom_wrong_btn)
                        messageList.add(Message(HostObject.host.hostName, HostTalk.giveRandomBad(), R.drawable.lasgov))
                        UserObjectConst.sessionStreak=0
                    } // CHECKS IF ANSWER WAS INCORRECT

                    else {
                        myCorrectAnswers++
                        messageList.add(Message(HostObject.host.hostName, HostTalk.giveRandomGood(), R.drawable.lasgov))
                        UserObjectConst.sessionStreak++
                        if(UserObjectConst.sessionStreak> StatObject.stats!!.streak) {
                            StatObject.stats!!.streak=UserObjectConst.sessionStreak
                        }

                    } //IF THE ANSWER WAS CORRECT

                    answerView(findCorrectIndex(), R.drawable.custom_correct_btn) //COLORS THE CORRECT

                    checkLast()
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

    private fun checkLast(){
        if (myPosition == myQuestionsList!!.size) {
            binding.btnSubmit.text = getString(R.string.finish)
        } else {
            binding.btnSubmit.text = getString(R.string.submit)
        }
    }
    
    private fun timerfun(){
        Time.resetTime()
        T.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    UserObjectConst.sessionTimeSeconds++
                }
            }
        }, 1000, 1000)
    }

    private fun setHostResponse(question : QuestionModel){
        val adapter = ChatAdapter(this, R.layout.message_list_view_element, messageList)
        binding.ListView.adapter = adapter

        if(RandomGen.chance(30) && myPosition!=1){
            messageList.add(Message(HostObject.host.hostName, HostTalk.saySomething(), R.drawable.lasgov))
        }
        if(myPosition!=1){
            messageList.add(Message(HostObject.host.hostName, HostTalk.giveMoveOn(), R.drawable.lasgov))
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
    }

    private fun nonClickable(){
        binding.tvOptionA.isClickable=false
        binding.tvOptionB.isClickable=false
        binding.tvOptionC.isClickable=false
        binding.tvOptionD.isClickable=false
    }

    private fun findCorrectIndex() : Int{
        var x=0
        when {
            QuestionsObject.questionList[myPosition-1].options[0].id==QuestionsObject.questionList[myPosition-1].answer -> {
                x=1
            }
            QuestionsObject.questionList[myPosition-1].options[1].id==QuestionsObject.questionList[myPosition-1].answer -> {
                x=2
            }
            QuestionsObject.questionList[myPosition-1].options[2].id==QuestionsObject.questionList[myPosition-1].answer -> {
                x=3
            }
            QuestionsObject.questionList[myPosition-1].options[3].id==QuestionsObject.questionList[myPosition-1].answer -> {
                x=4
            }
        }
        return x
    }
}
