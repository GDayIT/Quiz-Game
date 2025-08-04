package dbbl;

import java.util.ArrayList;
import java.util.List;

public class QuizFrage {
    private final String titel;
    private final String frageText;
    private final List<String> antworten;
    private final List<Boolean> korrekt;

    public QuizFrage(String titel, String frageText, List<String> antworten, List<Boolean> korrekt) {
        this.titel = titel;
        this.frageText = frageText;
        this.antworten = new ArrayList<>(antworten);
        this.korrekt = new ArrayList<>(korrekt);
    }

    public String getTitel() {
        return titel;
    }

    public String getFrageText() {
        return frageText;
    }

    public List<String> getAntworten() {
        return new ArrayList<>(antworten);
    }

    public List<Boolean> getKorrekt() {
    return new ArrayList<>(korrekt);
		}
	}
