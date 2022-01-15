package anekdotas.mindgameapplication.network

data class AnalyticModel (
    var totalTimeSpent: Int = 0,
    val correctAnswers : Int = 0,
    val correctAnswersPercentage : Double = 0.0,
    val longestStreak: Int =0,
    val longestStreakTopicId: Int = 0,
    var averageGameTime: Int = 0,
    val topicsCreated: Int = 0,
    val topicsRated: Int = 0,
    val topicsPlayed: Int = 0
)
