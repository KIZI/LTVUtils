package eu.linkedtv.utils.asr;

public class SpokenUnit {
    private final String word;
    private final double confidence;
    private final Integer timePoint;
    private final Integer duration; 

    public SpokenUnit(String word, double confidence, Integer timePoint, Integer duration) {
        this.word = word;
        this.confidence = confidence;
        this.timePoint = timePoint;
        this.duration = duration;
    }
    
    public String getTimePointString() {
        return timePoint.toString();
    }
    
    public Integer getTimepoint() {
        return timePoint;
    }

    public String getWord() {
        return word;
    }

    public double getConfidence() {
        return confidence;
    }
    
    public Integer getDuration() {
        return duration;
    }

}
