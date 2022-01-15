package anekdotas.mindgameapplication.java

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import anekdotas.mindgameapplication.R

class StatisticsAdapter (
    private val myContext: Context, var myResource: Int, objects: ArrayList<StatisticsRecord>
) :
    ArrayAdapter<StatisticsRecord?>(myContext, myResource, objects as List<StatisticsRecord?>) {
    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val statisticsName = getItem(position)!!.statisticsName
        val statisticsValue = getItem(position)!!.statisticsValue

        val inflater = LayoutInflater.from(myContext)
        val convertView = inflater.inflate(myResource, parent, false)
        val tvStatisticsName = convertView.findViewById<View>(R.id.tv_statisticsName) as TextView
        val tvStatisticsValue = convertView.findViewById<View>(R.id.tv_statisticsValue) as TextView


        tvStatisticsName.text = statisticsName
        tvStatisticsValue.text = statisticsValue

        return convertView
    }
}