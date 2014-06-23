package eu.linkedtv.utils.asr;

public class SpokenUnitKey {
    private final Integer textNumber;
    private final Integer wordOffset;
    private final Integer offset;
    
    public SpokenUnitKey(int textNumber, int wordOffset, int offset) {
        this.textNumber = textNumber;
        this.offset = offset;
        this.wordOffset = wordOffset;
    }
    
    public SpokenUnitKey(int textNumber, int wordOffset) {
        this(textNumber, wordOffset, -1);
    }

    public Integer getWordOffset() {
        return wordOffset;
    }

    public int getTextNumber() {
        return textNumber;
    }

    public int getOffset() {
        return offset;
    }
    
    @Override
    public int hashCode() {
        return toString().hashCode();
    }
    
    @Override
    public String toString() {
        return textNumber.toString() + ":" + wordOffset.toString(); 
    }
    
    public boolean equals(Object obj) {
        if (obj instanceof SpokenUnitKey) {
            SpokenUnitKey spokenUnitKey = (SpokenUnitKey) obj;
            return (textNumber.equals(spokenUnitKey.getTextNumber())) 
                    && (wordOffset.equals(spokenUnitKey.getWordOffset()));
        }
        else            
            return false; 
    }
}
