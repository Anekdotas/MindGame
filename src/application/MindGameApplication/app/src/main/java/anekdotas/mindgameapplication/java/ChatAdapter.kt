package anekdotas.mindgameapplication.java

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import anekdotas.mindgameapplication.R
import coil.load
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlin.collections.ArrayList

class ChatAdapter     //        this.myContext = myContext;
    (//    private static final Tag
    private val myContext: Context, var myResource: Int, objects: ArrayList<Message>
) :
    ArrayAdapter<Message?>(myContext, myResource, objects as List<Message?>) {
    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        //get message information
        var convertView = convertView
        val author = getItem(position)!!.author
        val messageText = getItem(position)!!.text
        val profilePicSource = getItem(position)!!.profPicSource
        val questionPicture = getItem(position)!!.questionPicture

        //Creating message with the information
//        com.example.bruhtest.Message message = new com.example.bruhtest.Message(author, messageText, profilePicSource);
        val inflater = LayoutInflater.from(myContext)
        convertView = inflater.inflate(myResource, parent, false)
        val tvAuthor = convertView.findViewById<View>(R.id.messageAuthor) as TextView
        val tvMessage = convertView.findViewById<View>(R.id.messageText) as TextView
        val tvImage = convertView.findViewById<View>(R.id.profilePicture) as CircleImageView
        val tvQuestionPicture = convertView.findViewById<View>(R.id.messageImage) as ImageView

        tvAuthor.text = author
        tvMessage.text = messageText
        tvImage.setImageResource(profilePicSource)
        Log.d("debug", "$questionPicture")
        tvQuestionPicture.load(questionPicture)

        return convertView
    }
}