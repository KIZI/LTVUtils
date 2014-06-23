package eu.linkedtv.utils.asr;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class AsrTranscript {

    private static final String AUDIO_SEGMENT_TAG = "ns1:AudioSegment";
    private final String TRANSCRIPTION_TAG;
    private final String SPOKEN_UNIT_VECTOR_TAG;
    private final String CONFIDENCE_VECTOR_TAG;
    private final String START_TIME_MATRIX_TAG;

    private static final String SEPARATOR = " ";

    private final Document asrTranscriptDom;

    private final List<SpokenUnit> texts;
    //private final Map<SpokenUnitKey, SpokenUnit> spokenUnits;

    public AsrTranscript(InputStream asrTranscript) throws AsrException {
        try {
            asrTranscriptDom = parseTranscriptFile(asrTranscript);
            texts = new LinkedList<SpokenUnit>();
            //spokenUnits = new HashMap<SpokenUnitKey, SpokenUnit>();

            String ifinderNs = "";
            NamedNodeMap attributes = asrTranscriptDom.getDocumentElement().getAttributes();
            for (int i = 0; i < attributes.getLength(); i++) {
                Node item = attributes.item(i);
                if (item.getNodeValue().equals("http://www.iais.fraunhofer.de/ifinder")) {
                    ifinderNs = item.getNodeName().replaceFirst("xmlns:", "") + ":";
                }
            }
            TRANSCRIPTION_TAG = ifinderNs + "Transcription";
            SPOKEN_UNIT_VECTOR_TAG = ifinderNs + "SpokenUnitVector";
            CONFIDENCE_VECTOR_TAG = ifinderNs + "ConfidenceVector";
            START_TIME_MATRIX_TAG = ifinderNs + "StartTimeDurationMatrix";
            initTexts();
        } catch (IOException e) {
            throw new AsrException("Problems reading transcript file", e);
        } catch (ParserConfigurationException e) {
            throw new AsrException("Unable to instantiate the parser", e);
        } catch (SAXException e) {
            throw new AsrException("Parsing exception", e);
        }
    }

    private Document parseTranscriptFile(InputStream asrTranscript) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();

        return db.parse(asrTranscript);
    }

    private void initTexts() {
        NodeList transcriptions = asrTranscriptDom.getElementsByTagName(TRANSCRIPTION_TAG);

        int nodesCount = transcriptions.getLength();
        for (int i = 0; i < nodesCount; i++) {
            String lingusticUnit = ((Element) transcriptions.item(i)).getAttribute("linguisticUnit");
            if ((lingusticUnit != null) && (lingusticUnit.equals("word"))) {
                NodeList textNodes = ((Element) transcriptions.item(i)).getElementsByTagName(SPOKEN_UNIT_VECTOR_TAG);
                NodeList confidences = ((Element) transcriptions.item(i)).getElementsByTagName(CONFIDENCE_VECTOR_TAG);
                NodeList startTimeMatrices = ((Element) transcriptions.item(i)).getElementsByTagName(START_TIME_MATRIX_TAG);

                if (textNodes.getLength() > 0) {
                    //                if ((confidences.getLength() < 1) || (timePoints.getLength() < 1))
                    //                    throw new AsrException("Insufficient information about audio segment number " + i);

                    Node textNode = textNodes.item(0);
                    //texts.add(textNode.getTextContent());
                    String confidenceVector = "";
                    String startTimeMatrix = "";

                    if (confidences.getLength() > 0) {
                        confidenceVector = confidences.item(0).getTextContent();
                    }
                    if (startTimeMatrices.getLength() > 0) {
                        startTimeMatrix = startTimeMatrices.item(0).getTextContent();
                    }

                    addSpokenUnits(textNode.getTextContent(), confidenceVector, startTimeMatrix, i);
                }
            }
        }
    }

    private void addSpokenUnits(String textVector, String confidenceVector, String startTimeMatrix, int textNumber) {
        int offset = 0;
        String[] words = textVector.split(SEPARATOR);
        String[] confidences = confidenceVector.split(SEPARATOR);
        String[] startTimes = startTimeMatrix.split(SEPARATOR);

        if (words.length != confidences.length) {
            confidences = new String[words.length];
            for (int i = 0; i < words.length; i++) {
                confidences[i] = "0";
            }
        }

        for (int i = 0; i < words.length; i++) {
            if (!words[i].equals("<unk>")) {

//            spokenUnits.put(
//                    new SpokenUnitKey(textNumber, i, offset), 
//                    new SpokenUnit(words[i], new Double(confidences[i]), new Integer(startTimes[i * 2]), new Integer(startTimes[i * 2 + 1])));
                texts.add(new SpokenUnit(words[i], new Double(confidences[i]), new Integer(startTimes[i * 2]), new Integer(startTimes[i * 2 + 1])));
                offset += words[i].length() + SEPARATOR.length();
            }
        }
    }

//    public List<SpokenUnitKey> findWordOccurrences(String word) {
//        List<SpokenUnitKey> wordOccurrences = new LinkedList<SpokenUnitKey>();
//        for (Entry<SpokenUnitKey, SpokenUnit> spokenUnitEntry : spokenUnits.entrySet()) {
//            if (spokenUnitEntry.getValue().getWord().equals(word))
//                wordOccurrences.add(spokenUnitEntry.getKey());
//        }
//        
//        return wordOccurrences;
//    }
//    public SpokenUnit getSpokenUnit(SpokenUnitKey spokenUnitKey) {
//        return spokenUnits.get(spokenUnitKey);
//    }
//    public List<SpokenUnit> findSpokenUnits(String word) {
//        List<SpokenUnit> foundSpokenUnits = new LinkedList<SpokenUnit>();
//        for (Entry<SpokenUnitKey, SpokenUnit> spokenUnitEntry : spokenUnits.entrySet()) {
//            if (spokenUnitEntry.getValue().getWord().equals(word))
//                foundSpokenUnits.add(spokenUnitEntry.getValue());
//        }
//        
//        return foundSpokenUnits;
//    }    
    public List<SpokenUnit> getTexts() {
        return texts;
    }
}
