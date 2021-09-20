package anekdotas.mindgameapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class QuestionsProtoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questions_proto)
        val questionsList = QuestionsObjectConst.getQuestions()
        Log.i("Questions size", questionsList.size.toString())
    }
}