package anekdotas.mindgameapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

// TODO: 10/2/2021  (add a background for the results screen, something that looks proper, right now it uses the
//  menu bg that does no fit well with design)

// TODO: 10/2/2021 (add an original asset for trophy if possible) ; not high priority but would be nice

class ResultsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)
    }
}