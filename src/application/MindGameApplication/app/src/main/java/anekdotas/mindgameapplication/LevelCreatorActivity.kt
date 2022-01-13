package anekdotas.mindgameapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import anekdotas.mindgameapplication.databinding.ActivityLevelCreatorBinding
import anekdotas.mindgameapplication.network.*
import anekdotas.mindgameapplication.objects.JwtObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LevelCreatorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLevelCreatorBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLevelCreatorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        //ActionBar setup
        binding.actionBar.title.setText(R.string.lvl_creator_title)
        binding.actionBar.actionBarBackArrow.setOnClickListener {
            val intent = Intent(this, LevelCreatorTitlePageActivity::class.java)
            startActivity(intent)
            finish()
        }

        setAnswerNumbers()  //used to have answers in "Answer 1, Answer 2..." format instead of "Answer, Answer, Answer, Answer"


        val myCustomQuestions = ArrayList<QuestionModelForLevelCreator>()
        var currentQuestionNr = 1

        setQuestionNr(currentQuestionNr)

        // - - - - - - - - - TEST  CHAMBER - - - - - - - - - - - - - - - - -
//        fillQuestion()
        // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

        binding.btnSaveQuestion.setOnClickListener {

            displayQuestionLogs()

            if (questionTests()) {   //test if all fields are filled correctly
                saveQuestion(myCustomQuestions)

                Toast.makeText(this, getString(R.string.lvl_creator_question_saved_toast), Toast.LENGTH_SHORT).show()

                clearQuestion()                     // clear question info
                currentQuestionNr += 1
                setQuestionNr(currentQuestionNr)    // update question nr
            }
        }

        binding.btnPlay.setOnClickListener {        //This button is used for uploading created topic
            if (myCustomQuestions.size > 0) {
                //Actions to play created topic
//                    playTopic(myCustomQuestions)

                // Actions to upload topic
                uploadTopic(myCustomQuestions)
            }
            else {
                Toast.makeText(this, getString(R.string.lvl_creator_pls_create_question_first_toast), Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnPreviousQuestion.setOnClickListener {
            if (myCustomQuestions.size > 0 && currentQuestionNr > 1) {
                currentQuestionNr -= 1
                Log.d("Cur.Ques#: ", currentQuestionNr.toString())
                loadQuestion(myCustomQuestions, currentQuestionNr - 1)
                setQuestionNr(currentQuestionNr)
            }
        }

        binding.btnNextQuestion.setOnClickListener {

            if ((currentQuestionNr > 0) and (currentQuestionNr < myCustomQuestions.size)) {
                currentQuestionNr += 1
                Log.d("Cur.Ques#: ", currentQuestionNr.toString())
                loadQuestion(myCustomQuestions, currentQuestionNr - 1)
                setQuestionNr(currentQuestionNr)
            }
                else if (questionTests()) {
                Toast.makeText(this, getString(R.string.lvl_creator_creating_new_question_toast), Toast.LENGTH_SHORT).show()
                clearQuestion()                     // clear question info
                currentQuestionNr += 1
                setQuestionNr(currentQuestionNr)    // update question nr
            }
        }

        binding.btnUpdateQuestion.setOnClickListener {
            if (myCustomQuestions.size > 0 && currentQuestionNr <= myCustomQuestions.size){
                updateQuestion(currentQuestionNr, myCustomQuestions)
            } else {
                Toast.makeText(this, getString(R.string.lvl_creator_you_cannot_modify_toast), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uploadTopic(myCustomQuestions: ArrayList<QuestionModelForLevelCreator>) {
        callNetworkUploadTopic()
        Thread.sleep(100)
        callNetworkUploadQuestions(myCustomQuestions)
        intent = Intent(this, ListCategoriesActivity::class.java)
        startActivity(intent)
        finish()
    }

 /*   private fun playTopic(myCustomQuestions: ArrayList<QuestionModel>) {
                QuestionsObject.questionList = myCustomQuestions
                intent = Intent(this, QuestionsActivity::class.java)
                startActivity(intent)
                finish()
    } */

    private fun updateQuestion(currentQuestionNr: Int, myCustomQuestions: ArrayList<QuestionModelForLevelCreator>) {
        Toast.makeText(this, getString(R.string.lvl_creator_current_question_toast) + currentQuestionNr, Toast.LENGTH_SHORT).show()

        val question = binding.objQuestionElement.inputEtQuestion.text.toString()
        val answer1 = binding.objQuestionElement.Answer1.etAnswer.text.toString()
        val answer2 = binding.objQuestionElement.Answer2.etAnswer.text.toString()
        val answer3 = binding.objQuestionElement.Answer3.etAnswer.text.toString()
        val answer4 = binding.objQuestionElement.Answer4.etAnswer.text.toString()
        val correctAnswerInt = binding.objQuestionElement.spCorrectAnswerID.selectedItemPosition

        val answersList = listOf<AnswerModelForLevelCreator>(AnswerModelForLevelCreator(answer1)
            , AnswerModelForLevelCreator(answer2), AnswerModelForLevelCreator(answer3), AnswerModelForLevelCreator(answer4)
        )

        val updatedQuestion = QuestionModelForLevelCreator(1, question, "", correctAnswerInt, answersList)

        myCustomQuestions[currentQuestionNr - 1] = updatedQuestion
    }

    private fun setAnswerNumbers() {
        binding.objQuestionElement.Answer1.tvAnswer.setText(getString(R.string.lvl_creator_answer_overloaded,1))
        binding.objQuestionElement.Answer2.tvAnswer.setText(getString(R.string.lvl_creator_answer_overloaded,2))
        binding.objQuestionElement.Answer3.tvAnswer.setText(getString(R.string.lvl_creator_answer_overloaded,3))
        binding.objQuestionElement.Answer4.tvAnswer.setText(getString(R.string.lvl_creator_answer_overloaded,4))
    }

    private fun displayQuestionLogs() {
        // - - - - - - LOGS TO DISPLAY FILLED QUESTION - - - - - - - - -
        Log.d("|x|", "\t question: " + binding.objQuestionElement.inputEtQuestion.text.toString())
        Log.d("|x|", "\t answer 1: " + binding.objQuestionElement.Answer1.etAnswer.text.toString())
        Log.d("|x|", "\t answer 2: " + binding.objQuestionElement.Answer2.etAnswer.text.toString())
        Log.d("|x|", "\t answer 3: " + binding.objQuestionElement.Answer3.etAnswer.text.toString())
        Log.d("|x|", "\t answer 4: " + binding.objQuestionElement.Answer4.etAnswer.text.toString())

        val correctAnswerInt = binding.objQuestionElement.spCorrectAnswerID.selectedItemPosition
        Log.d("|x|", "\t answer Int: $correctAnswerInt")
    }

    private fun saveQuestion(myCustomQuestions: ArrayList<QuestionModelForLevelCreator>) {
        val question = binding.objQuestionElement.inputEtQuestion.text.toString()
        val answer1 = binding.objQuestionElement.Answer1.etAnswer.text.toString()
        val answer2 = binding.objQuestionElement.Answer2.etAnswer.text.toString()
        val answer3 = binding.objQuestionElement.Answer3.etAnswer.text.toString()
        val answer4 = binding.objQuestionElement.Answer4.etAnswer.text.toString()
        val correctAnswerInt = binding.objQuestionElement.spCorrectAnswerID.selectedItemPosition

        val answersList = listOf<AnswerModelForLevelCreator>(AnswerModelForLevelCreator(answer1)
            , AnswerModelForLevelCreator(answer2), AnswerModelForLevelCreator(answer3), AnswerModelForLevelCreator(answer4))

        myCustomQuestions.add(QuestionModelForLevelCreator(1, question, "", correctAnswerInt, answersList))
    }

    private fun setQuestionNr(questionNr: Int) {
        binding.objQuestionElement.tvQuestionNr.setText(getString(R.string.lvl_creator_answer_overloaded, questionNr))
    }

    private fun loadQuestion(myCustomQuestions: ArrayList<QuestionModelForLevelCreator>, questionNr: Int) {
        binding.objQuestionElement.inputEtQuestion.setText(myCustomQuestions[questionNr].question)
        binding.objQuestionElement.Answer1.etAnswer.setText(myCustomQuestions[questionNr].options[0].text)
        binding.objQuestionElement.Answer2.etAnswer.setText(myCustomQuestions[questionNr].options[1].text)
        binding.objQuestionElement.Answer3.etAnswer.setText(myCustomQuestions[questionNr].options[2].text)
        binding.objQuestionElement.Answer4.etAnswer.setText(myCustomQuestions[questionNr].options[3].text)
        binding.objQuestionElement.spCorrectAnswerID.setSelection(myCustomQuestions[questionNr].answer)
    }

    private fun questionTests(): Boolean {
        // - - - - - QUESTION TESTS - - - -
        val question = binding.objQuestionElement.inputEtQuestion.text.toString()
        val answer1 = binding.objQuestionElement.Answer1.etAnswer.text.toString()
        val answer2 = binding.objQuestionElement.Answer2.etAnswer.text.toString()
        val answer3 = binding.objQuestionElement.Answer3.etAnswer.text.toString()
        val answer4 = binding.objQuestionElement.Answer4.etAnswer.text.toString()
        val correctAnswerInt = binding.objQuestionElement.spCorrectAnswerID.selectedItemPosition

        var test1 = false   //test for question input
        var test2 = false   //test for 1st answer input
        var test3 = false   //test for 2nd answer input
        var test4 = false   //test for 3rd answer input
        var test5 = false   //test for 4th answer input
        var test6 = false   //test for correct answer ID

        when (question.isNotEmpty()) {
            true -> test1 = true
            false -> Toast.makeText(this, getString(R.string.lvl_creator_question_cannot_be_empty_toast), Toast.LENGTH_SHORT).show()
        }
        when (answer1.isNotEmpty()) {
            true -> test2 = true
            false -> Toast.makeText(this, getString(R.string.lvl_creator_option_cannot_be_empty_toast,1), Toast.LENGTH_SHORT).show()
        }
        when (answer2.isNotEmpty()) {
            true -> test3 = true
            false -> Toast.makeText(this, getString(R.string.lvl_creator_option_cannot_be_empty_toast,2), Toast.LENGTH_SHORT).show()
        }
        when (answer3.isNotEmpty()) {
            true -> test4 = true
            false -> Toast.makeText(this, getString(R.string.lvl_creator_option_cannot_be_empty_toast,3), Toast.LENGTH_SHORT).show()
        }
        when (answer4.isNotEmpty()) {
            true -> test5 = true
            false -> Toast.makeText(this, getString(R.string.lvl_creator_option_cannot_be_empty_toast,4), Toast.LENGTH_SHORT).show()
        }
        when (correctAnswerInt > 0) {
            true -> test6 = true
            false -> Toast.makeText(this, R.string.lvl_creator_number_of_correct_answer_mustbe_toast, Toast.LENGTH_SHORT).show()
        }

        Log.d("boolean: ", (test1 && test2 && test3 && test4 && test5 && test6).toString())
        return test1 && test2 && test3 && test4 && test5 && test6
    }

    private fun clearQuestion() {
        binding.objQuestionElement.inputEtQuestion.setText("")
        binding.objQuestionElement.Answer1.etAnswer.setText("")
        binding.objQuestionElement.Answer2.etAnswer.setText("")
        binding.objQuestionElement.Answer3.etAnswer.setText("")
        binding.objQuestionElement.Answer4.etAnswer.setText("")
        binding.objQuestionElement.spCorrectAnswerID.setSelection(0)
    }

    private fun callNetworkUploadTopic() {
        val topicName = intent.getStringExtra("topicName").toString()
        val topicDesc = intent.getStringExtra("topicDesc").toString()
        val topicDifficulty = intent.getIntExtra("topicDifficulty",0)
        val clientPOST = ApiClient.apiService.pushPostTopic("Bearer " + JwtObject.userJwt.token,TopicPostModel(topicName,topicDesc,topicDifficulty))
        Log.d("callNetworkUploadTopic", "has been called")
        clientPOST.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if(response.isSuccessful){
                    Log.d("POST response code topic upload is", ""+ response.code())
                    Toast.makeText(this@LevelCreatorActivity, getString(R.string.lvl_creator_topic_uploaded_toast), Toast.LENGTH_SHORT).show()
                    startActivity(intent)
                }
                else {
                    Log.d("POST for topic upload did not succeed", "" + response.code() + " | " + response.body() + " | sending: ")
                    Toast.makeText(this@LevelCreatorActivity, getString(R.string.lvl_creator_topic_upload_failed_toast), Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<Void>, response: Throwable) {
                Log.e("Something went wrong! ", ""+response.message)
            }
        })
    }

    private fun callNetworkUploadQuestions(myCustomQuestions: ArrayList<QuestionModelForLevelCreator>) {
        val topicName = intent.getStringExtra("topicName").toString()
        for (item in myCustomQuestions) {
            val clientPOST = ApiClient.apiService.pushPostQuestions("https://193.219.91.103:14656/categories/1/topics/$topicName/questions","Bearer " + JwtObject.userJwt.token, item)
            Log.d("callNetworkUploadTopic", "has been called")
            clientPOST.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if(response.isSuccessful){
                        Log.d("POST response code for question upload is", ""+ response.code())
                        Toast.makeText(this@LevelCreatorActivity, getString(R.string.lvl_creator_questions_uploaded_toast), Toast.LENGTH_SHORT).show()
                        startActivity(intent)
                    }
                    else {
                        Log.d("POST for question upload did not succeed", "" + response.code())
                        Toast.makeText(this@LevelCreatorActivity, getString(R.string.lvl_creator_question_upload_failed_toast), Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<Void>, response: Throwable) {
                    Log.e("Something went wrong! ", ""+response.message)
                }
            })
        }
    }

    // - - - - - - - - FUNCTIONS USED FOR TESTING - - - - - - - - - -
    /* private fun fillQuestion() {
        binding.objQuestionElement.inputEtQuestion.setText("What was the main reason for Jeremy Clarkson getting fired from Top Gear in 2015?")
        binding.objQuestionElement.Answer1.etAnswer.setText("For punching a producer in the face")
        binding.objQuestionElement.Answer2.etAnswer.setText("Due to budget cuts")
        binding.objQuestionElement.Answer3.etAnswer.setText("For making a racist comment")
        binding.objQuestionElement.Answer4.etAnswer.setText("For arguing with a BBC senior manager")
        binding.objQuestionElement.spCorrectAnswerID.setSelection(1)
    }
     */
}
