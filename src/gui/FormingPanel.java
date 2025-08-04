package gui;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import dbbl.DataBusiness;
import dbbl.QuizFrage;
import gui.CreateQuizfragenPanel;
import gui.QuestionListPanel;

public class FormingPanel extends JPanel implements ActionListener {

    private JTextField titelField;
    private JTextArea frageArea;
    private JTextField[] antwortFields = new JTextField[4];
    private JCheckBox[] correctBoxes = new JCheckBox[4];
    private DefaultListModel<String> listModel = new DefaultListModel<>();
    private JList<String> fragenList;
    private JComboBox<String> themaDropdown;
    private DataBusiness db;
    private CreateQuizfragenPanel createPanel;
    private QuestionListPanel questionListPanel;
    private QuestionListPanel listPanel;
    private JComboBox<String> sharedDropdown;
    
    private String aktuellesThema;
    private List<QuizFrage> aktuelleFragen = new ArrayList<>();

    public FormingPanel(String titel, DataBusiness db) {
        this.db = db;
        init();
    }

    public void ladeFragenNachDropdownAuswahl() {
        String thema = (String) themaDropdown.getSelectedItem();
        if (thema != null && !thema.equals(aktuellesThema)) {
            aktuellesThema = thema;
            ladeFragenFuerThema(thema);

            if (createPanel != null) {
                createPanel.setAktuellesThema(thema); // nur aktualisieren
            }
        }
    }
    
    public void init() {
        setLayout(new BorderLayout());

        // Look & Feel setzen (optional)
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {}

        
        
        
        
        
        // Erst QuestionListPanel initialisieren, um das Dropdown zu bekommen
        listPanel = new QuestionListPanel(this, db);
        sharedDropdown = listPanel.getThemaDropdown();

        // Danach CreateQuizfragenPanel mit dem gleichen Dropdown
        createPanel = new CreateQuizfragenPanel(this, sharedDropdown, db);
        
        listPanel.setCreatePanel(createPanel);

        // Editor Panel: Create zuerst (links), Liste danach (rechts)
        JPanel editorPanel = new JPanel(new GridLayout(1, 2));
        editorPanel.add(createPanel);
        editorPanel.add(listPanel);
        
        
        

//        JPanel editorPanel = new JPanel(new GridLayout(1, 2));
//		createPanel = new CreateQuizfragenPanel(this, sharedDropdown, db);
//        editorPanel.add(listPanel);
//
//        JComboBox<String> sharedDropdown = listPanel.getThemaDropdown(); //------------------------------------TODO
//        listPanel = new QuestionListPanel(this, db);
//        editorPanel.add(listPanel);
//        




        // Tabs hinzufügenull
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Quizthemen", new JPanel());
        tabs.addTab("Quizfragen", editorPanel);
        tabs.addTab("Quiz", new JPanel());
        tabs.addTab("Statistik", new JPanel());

        // Panels zum Hauptlayout hinzufügen
//        add(themaDropdown, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
        setPreferredSize(new Dimension(800, 600));


    }


    public void aktualisiereFragenListeNachSpeichern() {
        ladeFragenFuerThema(aktuellesThema);
//        if (aktuellesThema != null) {
//            listPanel.ladeFragenFuerThema(aktuellesThema);
    }

    private void clearForm() {
        if (titelField != null) titelField.setText("");
        if (frageArea != null) frageArea.setText("");
        for (int i = 0; i < 4; i++) {
            if (antwortFields[i] != null) antwortFields[i].setText("");
            if (correctBoxes[i] != null) correctBoxes[i].setSelected(false);
        }
    }

    private void neueFrage() {
        fragenList.clearSelection();
        clearForm();
        if (titelField != null) titelField.requestFocus();
        JOptionPane.showMessageDialog(this, "Bereit für eine neue Frage.", "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        System.out.println("[DEBUG] ActionCommand: '" + cmd + "'");
        
        if ("comboBoxChanged".equals(cmd)) {
            return;
        }
        

        switch (cmd) {
            case "Frage Löschen":
                int index = fragenList.getSelectedIndex();
                if (index < 0) {
                    JOptionPane.showMessageDialog(this, "Keine Frage zum Löschen ausgewählt.", "Fehler", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                db.loescheFrage(aktuellesThema, index);
                aktuelleFragen.remove(index);
                clearForm();
                JOptionPane.showMessageDialog(this, "Frage gelöscht.", "Info", JOptionPane.INFORMATION_MESSAGE);
                break;

            case "Thema anzeigen":
                ladeFragenFuerThema(aktuellesThema);
                break;

            case "Neue Frage":
                if (speichereAktuelleFrageInDB(true)) {
                    neueFrage();
                }
                break;

            default:
                JOptionPane.showMessageDialog(this, "Aktion nicht möglich!", "Fehler", JOptionPane.ERROR_MESSAGE);
                break;
        }
    }

    private boolean speichereAktuelleFrageInDB(boolean zeigeFehler) {
        if (aktuellesThema == null) return false;

        String titel = titelField.getText().trim();
        String frage = frageArea.getText().trim();

        if (titel.isEmpty() && frage.isEmpty()) return true;

        if (titel.isEmpty()) {
            if (zeigeFehler) JOptionPane.showMessageDialog(this, "Der Titel darf nicht leer sein!", "Fehler", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (frage.isEmpty()) {
            if (zeigeFehler) JOptionPane.showMessageDialog(this, "Die Frage darf nicht leer sein!", "Fehler", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        List<String> antworten = new ArrayList<>();
        List<Boolean> korrekt = new ArrayList<>();
        boolean mindestensEineRichtige = false;

        for (int i = 0; i < 4; i++) {
            String antwortText = antwortFields[i].getText().trim();
            antworten.add(antwortText);
            boolean istKorrekt = correctBoxes[i].isSelected();
            korrekt.add(istKorrekt);
            if (istKorrekt) mindestensEineRichtige = true;
        }

        if (!mindestensEineRichtige) {
            if (zeigeFehler) JOptionPane.showMessageDialog(this, "Mindestens eine Antwort muss als richtig markiert sein!", "Fehler", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        QuizFrage neueFrage = new QuizFrage(titel, frage, antworten, korrekt);
        int vorhandenerIndex = -1;
        for (int i = 0; i < aktuelleFragen.size(); i++) {
            if (aktuelleFragen.get(i).getTitel().equals(titel)) {
                vorhandenerIndex = i;
                break;
            }
        }

        if (vorhandenerIndex >= 0) {
            db.loescheFrage(aktuellesThema, vorhandenerIndex);
            db.addFrage(aktuellesThema, neueFrage);
            if (zeigeFehler) JOptionPane.showMessageDialog(this, "Frage wurde aktualisiert.", "Info", JOptionPane.INFORMATION_MESSAGE);
        } else {
            db.addFrage(aktuellesThema, neueFrage);
            if (zeigeFehler) JOptionPane.showMessageDialog(this, "Neue Frage wurde gespeichert.", "Info", JOptionPane.INFORMATION_MESSAGE);
        }

        ladeFragenFuerThema(aktuellesThema);
        return true;
    }

    private void ladeFragenFuerThema(String thema) {
        aktuelleFragen = db.getFragenZuThema(thema);
        if (listPanel != null) {
            listPanel.updateFragenListe(aktuelleFragen);
        }
        clearForm();
    }
    
    


    private void styleButton(JButton button) {
        button.setFocusPainted(false);
        button.setForeground(Color.DARK_GRAY);
        button.setBackground(new Color(220, 220, 220));
        button.setOpaque(true);
        button.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        button.setFont(new Font("Arial", Font.PLAIN, 12));
        button.setPreferredSize(new Dimension(120, 30));
    }
}


















//package gui;
//
//import java.awt.BorderLayout;
//import java.awt.Color;
//import java.awt.Dimension;
//import java.awt.Font;
//import java.awt.GridLayout;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.swing.BorderFactory;
//import javax.swing.DefaultListModel;
//import javax.swing.JButton;
//import javax.swing.JCheckBox;
//import javax.swing.JComboBox;
//import javax.swing.JList;
//import javax.swing.JOptionPane;
//import javax.swing.JPanel;
//import javax.swing.JTabbedPane;
//import javax.swing.JTextArea;
//import javax.swing.JTextField;
//import javax.swing.UIManager;
//
//import dbbl.DataBusiness;
//import dbbl.DataBusiness.QuizFrage;
//
///**
// * FormingPanel stellt die grafische Oberfläche zur Eingabe, Bearbeitung und Verwaltung
// * von Quizfragen bereit. Es ermöglicht die Auswahl von Themen, das Anzeigen,
// * Erstellen, Speichern und Löschen von Fragen mit mehreren Antwortmöglichkeiten.
// * 
// * Die Klasse implementiert ActionListener für die Bedienung der Buttons und
// * verwaltet interne Zustände wie aktuelles Thema und Fragenliste.
// */
//public class FormingPanel extends JPanel implements ActionListener {
//
//    private JTextField titelField;               // Eingabefeld für den Fragetitel
//    private JTextArea frageArea;                  // Textbereich für die Fragestellung
//    private JTextField[] antwortFields = new JTextField[4]; // Eingabefelder für Antworten
//    private JCheckBox[] correctBoxes = new JCheckBox[4];    // Checkboxen für richtige Antworten
//    private DefaultListModel<String> listModel;  // Modell für die Liste der Fragen-Titel
//    private JList<String> fragenList;             // Anzeige der Fragenliste
//    private JComboBox<String> themaDropdown;
//    private DataBusiness db = new DataBusiness();                   // Schnittstelle zur Datenbank
//    private CreateQuizfragenPanel createPanel;
//
//    // Aktuelles Thema und lokale Kopie der Fragen
//    private String aktuellesThema;
//    private List<QuizFrage> aktuelleFragen = new ArrayList<>();
//
//
//    /**
//     * Konstruktor für das FormingPanel.
//     * Initialisiert die GUI-Komponenten, Look & Feel und Tabs.
//     * @param string 
//     * @param titiel
//     * @parmam DataBusiness
//     * 
//     * @return 
//     */
//	public FormingPanel(String titel, DataBusiness db) {
//	        super();
//	        this.db = db;
////	        createQuizfragenPanel qp = new createQuizfragenPanel(this, themaDropdown, db);
//	        
//	        init();
//	    }
//	
//	    public void init() {
//	    	JPanel editorPanel = new JPanel(new GridLayout(1, 2)); 
//	
//
//	    	
//	        List<String> themen = db.getAlleThemen(); // von DataBusiness
//	        themaDropdown = new JComboBox<>();
//	        for (String thema : themen) {
//	            themaDropdown.addItem(thema);
//	        }
//	        if (!themen.isEmpty()) {
//	            aktuellesThema = themen.get(0); // z.B. erstes Thema als Standard
//	        }
//	        
////	        createPanel = new createQuizfragenPanel(this, themaDropdown, db);
////	        editorPanel.add(createPanel);
//	        QuestionListPanel lp = new QuestionListPanel(this);
//	        editorPanel.add(lp);
//
////			QuizEditorPanel editorPanel = new QuizEditorPanel(db);
////			add(editorPanel);
//
//
//
//        // Versuche, den Nimbus Look and Feel zu setzen (modern und Apple-ähnlich)
//        try {
//            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (Exception e) {
//            // Fallback für die Noesis: Falls Nimbus nicht gesetzt werden kann, keine Exception anzeigen
//        }
//        themaDropdown.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                ladeFragenNachDropdownAuswahl();
//            }
//        });
//    
//
//        // Erstelle ein TabbedPane mit verschiedenen Bereichen
//        JTabbedPane tabs = new JTabbedPane();
//        tabs.addTab("Quizthemen", new JPanel());         // Platzhalter für Themenverwaltung
//        tabs.addTab("Quizfragen", editorPanel);
//        
//        // Panel für Fragenverwaltung
//        tabs.addTab("Quiz", new JPanel());                // Platzhalter für Quizmodus
//        tabs.addTab("Statistik", new JPanel());           // Platzhalter für Statistik
//
//        setLayout(new BorderLayout());
//        add(tabs, BorderLayout.CENTER);
//        setPreferredSize(new Dimension(800, 600));
//        
//        
//    }
//
//
//
//
//
//	/**
//     * Lädt eine Frage anhand ihres Index aus der lokalen Liste in das Formular.
//     * 
//     * @param index Index der auszuwählenden Frage
//     */
////    private void ladeFrageInForm(int index) {
////        if (index < 0 || index >= aktuelleFragen.size()) {
////            clearForm();
////            return;
////        }
////
////        DataBusiness.QuizFrage frage = aktuelleFragen.get(index);
////        titelField.setText(frage.getTitel());
////        frageArea.setText(frage.getFrageText());
////        List<String> antworten = frage.getAntworten();
////        List<Boolean> korrekt = frage.getKorrekt();
////
////        for (int i = 0; i < 4; i++) {
////            antwortFields[i].setText(i < antworten.size() ? antworten.get(i) : "");
////            correctBoxes[i].setSelected(i < korrekt.size() && korrekt.get(i));
////        }
////    }
//
//    /**
//     * Löscht alle Eingaben im Formular und setzt das Panel zurück.
//     */
//    private void clearForm() {
//        titelField.setText("");
//        frageArea.setText("");
//        for (int i = 0; i < 4; i++) {
//            antwortFields[i].setText("");
//            correctBoxes[i].setSelected(false);
//        }
//    }
//
//    /**
//     * Bereitet das Formular für die Eingabe einer neuen Frage vor,
//     * löscht die Auswahl in der Liste und setzt Fokus auf den Titel.
//     * Informiert den Benutzer via Popup.
//     */
//    private void neueFrage() {
//        fragenList.clearSelection();
//        clearForm();
//        titelField.requestFocus();
//        JOptionPane.showMessageDialog(this, "Bereit für eine neue Frage.", "Info", JOptionPane.INFORMATION_MESSAGE);
//    }
//
//    /**
//     * Reagiert auf Button- und andere ActionEvents im Panel.
//     * Steuert Speichern, Löschen, Thema anzeigen und neue Frage.
//     * 
//     * @param e ActionEvent, das ausgelöst wurde
//     */
//    @Override
//    public void actionPerformed(ActionEvent e) {
//        String cmd = e.getActionCommand();
//
//        switch (cmd) {
// 
//
//            case "Frage Löschen":
//                int index = fragenList.getSelectedIndex();
//                if (index < 0) {
//                    JOptionPane.showMessageDialog(this, "Keine Frage zum Löschen ausgewählt.", "Fehler", JOptionPane.ERROR_MESSAGE);
//                    return;
//                }
//                db.loescheFrage(aktuellesThema, index);
//                aktuelleFragen.remove(index);
////                ladeFragenFuerThema(aktuellesThema);
//                clearForm();
//                JOptionPane.showMessageDialog(this, "Frage gelöscht.", "Info", JOptionPane.INFORMATION_MESSAGE);
//                break;
//
//            case "Thema anzeigen":
//                ladeFragenFuerThema(aktuellesThema);
//                break;
//
//            case "Neue Frage":
//                if (speichereAktuelleFrageInDB(true)) {
//                    neueFrage();
//                }
//                break;
//
//            default:
//                JOptionPane.showMessageDialog(this, "Aktion nicht möglich!", "Fehler", JOptionPane.ERROR_MESSAGE);
//                break;
//        }
//    }
//
//
//
//	/**
//	* Speichert die aktuell im Formular eingegebene Frage in der Datenbank.
//	* Validiert Eingaben und zeigt optional Fehlermeldungen an.
//	* 
//	* @param zeigeFehler true, wenn Fehlermeldungen angezeigt werden sollen
//	* @return true, wenn die Frage erfolgreich gespeichert wurde oder das Formular leer ist
//	*/
//	private boolean speichereAktuelleFrageInDB(boolean zeigeFehler) {
//	  if (aktuellesThema == null) return false;
//	
//	  String titel = titelField.getText().trim();
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
//	      if (((QuizFrage) aktuelleFragen.get(i)).getTitel().equals(titel)) {
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
//    
//    
//    
//	public void ladeFragenNachDropdownAuswahl() {
//	    String thema = (String) themaDropdown.getSelectedItem();
//	    if (thema != null && !thema.equals(aktuellesThema)) {
//	        aktuellesThema = thema;
//	        ladeFragenFuerThema(thema);
//
//	        if (createPanel != null) {
//	            remove(createPanel);
//	        }
//	        createPanel = new CreateQuizfragenPanel(this, themaDropdown, db);
//	        add(createPanel, BorderLayout.SOUTH);
//	        revalidate();
//	        repaint();
//	    }
//	}
//	
//	public void aktualisiereFragenListeNachSpeichern() {
//	    ladeFragenFuerThema(aktuellesThema);
//	}
//    
//
//    /**
//     * Stilisiert Buttons mit einem einheitlichen, modernen Design.
//     * Entfernt Fokusrand, setzt Farbe, Schriftart und Rahmen.
//     * 
//     * @param button JButton, der gestylt werden soll
//     */
//    private void styleButton(JButton button) {
//        button.setFocusPainted(false);
//        button.setForeground(Color.DARK_GRAY);
//        button.setBackground(new Color(220, 220, 220)); // hellgrau
//        button.setOpaque(true);
//        button.setBorder(BorderFactory.createLineBorder(Color.GRAY));
//        button.setFont(new Font("Arial", Font.PLAIN, 12));
//        button.setPreferredSize(new Dimension(120, 30));
//    }
//}