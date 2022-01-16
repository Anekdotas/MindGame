package anekdotas.mindgameapplication.objects

import anekdotas.mindgameapplication.network.AnalyticModel
import anekdotas.mindgameapplication.network.ChoiceModel
import anekdotas.mindgameapplication.network.StatModel

object StatObject {
    var stats = StatModel(0, mutableListOf(ChoiceModel()),0, 0)
    var analytics = AnalyticModel(0,0, 0,0.0,0,"None",
    0,0,0,0)
}