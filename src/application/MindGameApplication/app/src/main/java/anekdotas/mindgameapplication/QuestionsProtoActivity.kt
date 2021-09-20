package anekdotas.mindgameapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView

class QuestionsProtoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questions_proto)
        val questionsList = QuestionsObjectConst.getQuestions()

        // Helpful in logcat for checking the amount of questions in input
        // Log.i("Questions size", questionsList.size.toString())

        var position = 1
        val question : Question? = questionsList[position-1]

        // UI elements please do not touch unless you know what you doing
        var questionView = findViewById<TextView>(R.id.tv_question)
        var progressbar = findViewById<ProgressBar>(R.id.ll_progress)
        var progressText = findViewById<TextView>(R.id.tv_progress)
        var questionImage = findViewById<ImageView>(R.id.iv_question_image)
        var optionA = findViewById<TextView>(R.id.tv_optionA)
        var optionB = findViewById<TextView>(R.id.tv_optionB)
        var optionC = findViewById<TextView>(R.id.tv_optionC)
        var optionD = findViewById<TextView>(R.id.tv_optionD)

        progressbar.progress = position
        progressText.text = "$position" + "/" + progressbar.max
        questionView.text = question!!.question
        questionImage.setImageResource(question.image)
        optionA.text = question.optionA
        optionB.text = question.optionB
        optionC.text = question.optionC
        optionD.text = question.optionD

    }
}