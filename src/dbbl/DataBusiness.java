package dbbl;

import java.awt.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import dbbl.QuizFrage;

public class DataBusiness {

//    private final Map<String, List<QuizFrage>> fragenProThema = new HashMap<>();
//    private final java.util.Map<String, List<QuizFrage>> themenFragen = new java.util.HashMap<>();
    private final Map<String, List<QuizFrage>> fragenZuThemen = new HashMap<>();
    
    public DataBusiness() {
        addBeispielDaten();//themaDropdown
        

    }
    
    

    private void addBeispielDaten() {
    	
    	
    	
    	// Beispiel-Themen und Fragen anlegen

        List<QuizFrage> matheFragen = List.of(
            new QuizFrage("Addition", "Was ist 2 + 2?",
                List.of("3", "4", "5", "6"),
                List.of(false, true, false, false)),
            new QuizFrage("Wurzel", "Was ist die Wurzel aus 9?",
                List.of("2", "3", "6", "9"),
                List.of(false, true, false, false))
        );
        fragenZuThemen.put("Mathe", new ArrayList<>(matheFragen));

        List<QuizFrage> deutschFragen = List.of(
            new QuizFrage("Wortarten", "Was ist ein Nomen?",
                List.of("Tunwort", "Dingwort", "Wie-Wort", "Zahlwort"),
                List.of(false, true, false, false))
        );
        fragenZuThemen.put("Deutsch", new ArrayList<>(deutschFragen));

        List<QuizFrage> geoFragen = List.of(
            new QuizFrage("Hauptstadt", "Was ist die Hauptstadt von Deutschland?",
                List.of("Berlin", "München", "Hamburg", "Köln"),
                List.of(true, false, false, false)),
            new QuizFrage("Kontinent", "Auf welchem Kontinent liegt Brasilien?",
                List.of("Afrika", "Südamerika", "Asien", "Europa"),
                List.of(false, true, false, false))
        );
        fragenZuThemen.put("Geographie", new ArrayList<>(geoFragen));

        List<QuizFrage> allgemeinFragen = List.of(
            new QuizFrage("Jahreszeiten", "Welche Jahreszeit folgt auf den Sommer?",
                List.of("Frühling", "Winter", "Herbst", "Sommer"),
                List.of(false, false, true, false)),
            new QuizFrage("Farben", "Welche Farbe ergibt Blau + Gelb?",
                List.of("Grün", "Lila", "Orange", "Braun"),
                List.of(true, false, false, false))
        );
        fragenZuThemen.put("Allgemeinwissen", new ArrayList<>(allgemeinFragen));

        // Weitere Beispiel-Themen
        fragenZuThemen.put("Physik", new ArrayList<>(List.of(
            new QuizFrage("Kraft", "Welche Einheit hat die Kraft?",
                List.of("Joule", "Newton", "Pascal", "Watt"),
                List.of(false, true, false, false))
        )));
        fragenZuThemen.put("Literatur", new ArrayList<>(List.of(
            new QuizFrage("Faust", "Wer schrieb 'Faust'?",
                List.of("Goethe", "Schiller", "Lessing", "Kafka"),
                List.of(true, false, false, false))
        )));
        fragenZuThemen.put("Chemie", new ArrayList<>(List.of(
            new QuizFrage("Wasser", "Was ist die Summenformel von Wasser?",
                List.of("CO₂", "O₂", "H₂O", "CH₄"),
                List.of(false, false, true, false))
        )));
        fragenZuThemen.put("Technik", new ArrayList<>(List.of(
            new QuizFrage("Kondensator", "Welches Bauteil speichert elektrische Ladung?",
                List.of("Transistor", "Kondensator", "Spule", "Widerstand"),
                List.of(false, true, false, false))
        )));
        fragenZuThemen.put("Sport", new ArrayList<>(List.of(
            new QuizFrage("Fußballteam", "Wie viele Spieler hat ein Fußballteam auf dem Feld?",
                List.of("9", "10", "11", "12"),
                List.of(false, false, true, false))
        )));
        fragenZuThemen.put("Informatik", new ArrayList<>(List.of(
            new QuizFrage("CPU", "Was ist die Kurzform von CPU?",
                List.of("Central Power Unit", "Central Processing Unit", "Control Panel Unit", "Central Programming Unit"),
                List.of(false, true, false, false))
        )));
        fragenZuThemen.put("Geschichte", new ArrayList<>(List.of(
            new QuizFrage("WW2 Beginn", "Wann begann der Zweite Weltkrieg?",
                List.of("1933", "1939", "1945", "1914"),
                List.of(false, true, false, false))
        )));
   
    }

    
 // Gibt alle vorhandenen Themen zurück.
    public List<String> getAlleThemen() {
        return new ArrayList<>(fragenZuThemen.keySet());
    }

    // Gibt alle Fragen für ein bestimmtes Thema zurück.
    public List<QuizFrage> getFragenZuThema(String thema) {
        return fragenZuThemen.getOrDefault(thema, new ArrayList<>());
    }

    // Fügt eine neue Frage zu einem Thema hinzu.
    public void addFrage(String thema, QuizFrage frage) {
        fragenZuThemen.computeIfAbsent(thema, k -> new ArrayList<>()).add(frage);
    }

    // Löscht eine Frage aus einem Thema per Index.
    public void loescheFrage(String thema, int index) {
        List<QuizFrage> fragen = fragenZuThemen.get(thema);
        if (fragen != null && index >= 0 && index < fragen.size()) {
            fragen.remove(index);
        }
    }
    

    
	/**
	* Speichert die aktuell im Formular eingegebene Frage in der Datenbank.
	* Validiert Eingaben und zeigt optional Fehlermeldungen an.
	* 
	* @param zeigeFehler true, wenn Fehlermeldungen angezeigt werden sollen
	* @return true, wenn die Frage erfolgreich gespeichert wurde oder das Formular leer ist
	* @param thema          Thema der Frage
    * @param titel          Titel der Frage
    * @param frageText      Text der Frage
    * @param antwortFields  Array von JTextFields für Antworten
    * @param korrektBoxes   Array von JCheckBoxes für korrekte Antworten
    * @param zeigeFehler    true, wenn Fehlermeldungen angezeigt werden sollen
    * @param parentComponent Eltern-Komponente für Dialoge
    * @return true, wenn die Frage erfolgreich gespeichert wurde oder das Formular leer ist
	*/
    public boolean speichereFrage(
        String thema,
        String titel,
        String frageText,
        JTextField[] antwortFields,
        JCheckBox[] korrektBoxes,
        boolean zeigeFehler,
        Component parentComponent
    ) {
        if (thema == null) return false;

        titel = titel.trim();
        frageText = frageText.trim();

        if (titel.isEmpty() && frageText.isEmpty()) return true;

        if (titel.isEmpty()) {
            if (zeigeFehler) {
                JOptionPane.showMessageDialog(parentComponent, "Der Titel darf nicht leer sein!", "Fehler", JOptionPane.ERROR_MESSAGE);
            }
            return false;
        }

        if (frageText.isEmpty()) {
            if (zeigeFehler) {
                JOptionPane.showMessageDialog(parentComponent, "Die Frage darf nicht leer sein!", "Fehler", JOptionPane.ERROR_MESSAGE);
            }
            return false;
        }

        List<String> antworten = new ArrayList<>();
        List<Boolean> korrekt = new ArrayList<>();
        boolean mindestensEineRichtige = false;

        for (int i = 0; i < antwortFields.length; i++) {
            String text = antwortFields[i].getText().trim();
            antworten.add(text);
            boolean istKorrekt = korrektBoxes[i].isSelected();
            korrekt.add(istKorrekt);
            if (istKorrekt) mindestensEineRichtige = true;
        }

        if (!mindestensEineRichtige) {
            if (zeigeFehler) {
                JOptionPane.showMessageDialog(parentComponent, "Mindestens eine Antwort muss als richtig markiert sein!", "Fehler", JOptionPane.ERROR_MESSAGE);
            }
            return false;
        }

        QuizFrage neueFrage = new QuizFrage(titel, frageText, antworten, korrekt);

        List<QuizFrage> fragen = fragenZuThemen.getOrDefault(thema, new ArrayList<>());
        // Frage mit gleichem Titel ersetzen
        fragen.removeIf(f -> f.getTitel().equalsIgnoreCase("QUIZ"));
        fragen.add(neueFrage);
        fragenZuThemen.put(thema, fragen);

        if (zeigeFehler) {
            JOptionPane.showMessageDialog(parentComponent, "Frage wurde gespeichert.", "Info", JOptionPane.INFORMATION_MESSAGE);
        }

        return true;
    }
    

	
	}
	
//    private Map<String, List<QuizFrage>> fragenZuThemen = new HashMap<>();

    

//    /**
//     * Gibt alle vorhandenen Themen zurück.
//     */
//    public List<String> getAlleThemen() {
//        return new ArrayList<>(fragenZuThemen.keySet());
//    }
//
//    /**
//     * Gibt alle Fragen für ein bestimmtes Thema zurück.
//     */
//    public List<QuizFrage> getFragenZuThema(String thema) {
//        return fragenZuThemen.getOrDefault(thema, new ArrayList<>());
//    }
//
//    /**
//     * Fügt eine neue Frage zu einem Thema hinzu.
//     */
//    public void addFrage(String thema, QuizFrage frage) {
//        fragenZuThemen.computeIfAbsent(thema, k -> new ArrayList<>()).add(frage);
//    }
//
//    /**
//     * Löscht eine Frage aus einem Thema per Index.
//     */
//    public void loescheFrage(String thema, int index) {
//        List<QuizFrage> fragen = fragenZuThemen.get(thema);
//        if (fragen != null && index >= 0 && index < fragen.size()) {
//            fragen.remove(index);
//        }
//    }

	
	
















































//
//
//private boolean speichereAktuelleFrageInDB(boolean zeigeFehler) {
//	  if (aktuellesThema == null) return false;
//	
//
//	QuizFrage frage = new QuizFrage(titel, frageText, antworten, korrekt);
//	dataBusiness.addFrage(aktuellesThema, frage);
//
//	  String frage = frageArea.getText().trim();
//	
//	  // Formular komplett leer => nichts speichern, kein Fehler
//	  if (titel.isEmpty() && frage.isEmpty()) {
//	      return true;
//	  }
//	
//	  // Validierung Titel
//	  if (titel.isEmpty()) {
//	      if (zeigeFehler) {
//	          JOptionPane.showMessageDialog(this, "Der Titel darf nicht leer sein!", "Fehler", JOptionPane.ERROR_MESSAGE);
//	      }
//	      return false;
//	  }
//	
//	  // Validierung Frage
//	  if (frage.isEmpty()) {
//	      if (zeigeFehler) {
//	          JOptionPane.showMessageDialog(this, "Die Frage darf nicht leer sein!", "Fehler", JOptionPane.ERROR_MESSAGE);
//	      }
//	      return false;
//	  }
//	
//	  // Antworten und Korrekt-Flags sammeln
//	  List<String> antworten = new ArrayList<>();
//	  List<Boolean> korrekt = new ArrayList<>();
//	  boolean mindestensEineRichtige = false;
//	
//	  for (int i = 0; i < 4; i++) {
//	      String antwortText = antwortFields[i].getText().trim();
//	      antworten.add(antwortText);
//	      boolean istKorrekt = correctBoxes[i].isSelected();
//	      korrekt.add(istKorrekt);
//	      if (istKorrekt) mindestensEineRichtige = true;
//	  }
//	
//	  // Validierung: mindestens eine Antwort muss als richtig markiert sein
//	  if (!mindestensEineRichtige) {
//	      if (zeigeFehler) {
//	          JOptionPane.showMessageDialog(this, "Mindestens eine Antwort muss als richtig markiert sein!", "Fehler", JOptionPane.ERROR_MESSAGE);
//	      }
//	      return false;
//	  }
//	
//	  // Neue Frage-Instanz erstellen
//	  DataBusiness.QuizFrage neueFrage = new DataBusiness.QuizFrage(titel, frage, antworten, korrekt);
//	
//	  // Prüfen, ob Frage mit diesem Titel bereits existiert
//	  int vorhandenerIndex = -1;
//	  for (int i = 0; i < aktuelleFragen.size(); i++) {
//	      if (aktuelleFragen.get(i).getTitel().equals(titel)) {
//	          vorhandenerIndex = i;
//	          break;
//	      }
//	  }
//	
//	  // Wenn vorhanden, alte Frage löschen und neue hinzufügen (Update)
//	  if (vorhandenerIndex >= 0) {
//	      db.loescheFrage(aktuellesThema, vorhandenerIndex);
//	      db.addFrage(aktuellesThema, neueFrage);
//	      if (zeigeFehler) {
//	          JOptionPane.showMessageDialog(this, "Frage wurde aktualisiert.", "Info", JOptionPane.INFORMATION_MESSAGE);
//	      }
//	  } else {
//	      // Neue Frage hinzufügen
//	      db.addFrage(aktuellesThema, neueFrage);
//	      if (zeigeFehler) {
//	          JOptionPane.showMessageDialog(this, "Neue Frage wurde gespeichert.", "Info", JOptionPane.INFORMATION_MESSAGE);
//	      }
//	  }
//	
//	  // Fragenliste neu laden, um Änderungen sichtbar zu machen
//	  ladeFragenFuerThema(aktuellesThema);
//	  return true;
//	  
//}
//
//
//	public void speichereFrage(String thema, QuizFrageDTO dto) {
//	    QuizFrage frage = new QuizFrage(dto.titel, dto.frageText, dto.antworten, dto.korrekt);
//	    addFrage(thema, frage);
//}
//
//	
//
//	public class QuizFrageDTO {
//	    public String titel;
//	    public String frageText;
//	    public List<String> antworten;
//	    public List<Boolean> korrekt;
//	
//	    public QuizFrageDTO(String titel, String frageText, List<String> antworten, List<Boolean> korrekt) {
//	        this.titel = titel;
//	        this.frageText = frageText;
//	        this.antworten = antworten;
//	        this.korrekt = korrekt;
//  }
//}
//
//	
//	/**
//	* Lädt alle Fragen für das gegebene Thema aus der Datenbank
//	* und aktualisiert die Liste der Fragen im GUI.
//	* 
//	* @param thema Thema, für das die Fragen geladen werden sollen
//	*/
//	private void ladeFragenFuerThema(String thema) {
//	  aktuelleFragen = db.getFragenZuThema(thema);
//	  listModel.clear();
//	  for (DataBusiness.QuizFrage frage : aktuelleFragen) {
//	      listModel.addElement(frage.getTitel());
//	  }
//	  clearForm();
//	}