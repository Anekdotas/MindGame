package anekdotas.mindgameapplication.java;

public class StatisticsRecord {
    private String statisticsName;
    private String statisticsValue;

    public StatisticsRecord(String statisticsName, String statisticsValue) {
        this.statisticsName = statisticsName;
        this.statisticsValue = statisticsValue;
    }

    public String getStatisticsName() {
        return statisticsName;
    }

    public String getStatisticsValue() {
        return statisticsValue;
    }
}
