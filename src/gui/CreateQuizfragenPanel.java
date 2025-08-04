package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import dbbl.DataBusiness;
import dbbl.QuizFrage;

public class CreateQuizfragenPanel extends JPanel implements ActionListener {

    private JTextField titelField;
    private JTextArea frageArea;
    private JTextField[] antwortFields = new JTextField[4];
    private JCheckBox[] correctBoxes = new JCheckBox[4];
    private JComboBox<String> themaDropdown;
    private DataBusiness db;
//    private JComboBox[] sharedDropdown			//----------------------------------TODO
    private String aktuellesThema = null;
    private List<QuizFrage> aktuelleFragen = new ArrayList<>();
	private ActionListener listener;

    public CreateQuizfragenPanel(ActionListener listener, JComboBox<String> themaDropdown, DataBusiness db) {
    	this.listener = listener;
        this.themaDropdown = themaDropdown;
        this.themaDropdown.addActionListener(listener); 
//        this.themaDropdown = sharedDropdown;			//-------------------------------TODO
        this.db = db;
        
        initPanel();
    }
        
    private void initPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Neue Quizfrage erstellen"));

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titelLabel = new JLabel("Titel");
        titelField = new JTextField(20);
        titelField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));

        JLabel frageLabel = new JLabel("Frage");
        frageArea = new JTextArea(5, 20);
        frageArea.setLineWrap(true);
        frageArea.setWrapStyleWord(true);
        JScrollPane frageScroll = new JScrollPane(frageArea);
        frageScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        leftPanel.add(titelLabel);
        leftPanel.add(titelField);
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(frageLabel);
        leftPanel.add(frageScroll);
        leftPanel.add(Box.createVerticalStrut(10));

        JPanel antwortHeaderPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        antwortHeaderPanel.add(new JLabel("Antwort"), gbc);
        gbc.gridx = 1;
        antwortHeaderPanel.add(Box.createHorizontalStrut(250), gbc);
        gbc.gridx = 2;
        antwortHeaderPanel.add(new JLabel("Richtig"), gbc);
        leftPanel.add(antwortHeaderPanel);

        JPanel antwortPanel = new JPanel(new GridBagLayout());
        for (int i = 0; i < 4; i++) {
            GridBagConstraints c = new GridBagConstraints();
            c.insets = new Insets(2, 0, 2, 5);

            c.gridx = 0;
            c.gridy = i;
            c.anchor = GridBagConstraints.WEST;
            JLabel antwortLabel = new JLabel("Antwort " + (i + 1));
            antwortLabel.setPreferredSize(new Dimension(70, 25));
            antwortPanel.add(antwortLabel, c);

            c.gridx = 1;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 1.0;
            antwortFields[i] = new JTextField();
            antwortFields[i].setPreferredSize(new Dimension(250, 25));
            antwortPanel.add(antwortFields[i], c);

            c.gridx = 2;
            c.fill = GridBagConstraints.NONE;
            c.weightx = 0;
            correctBoxes[i] = new JCheckBox();
            antwortPanel.add(correctBoxes[i], c);
        }
        leftPanel.add(antwortPanel);
        leftPanel.add(Box.createVerticalStrut(10));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton deleteBtn = new JButton("Frage Löschen");
        deleteBtn.addActionListener(this);
        deleteBtn.setActionCommand("Frage Löschen");
        JButton saveBtn = new JButton("Speichern");
        buttonPanel.add(deleteBtn);
        buttonPanel.add(saveBtn);
        leftPanel.add(buttonPanel);

        // Listener: Nutze diesen Panel als Listener
        saveBtn.addActionListener(this);
        deleteBtn.addActionListener(this);

        add(leftPanel, BorderLayout.CENTER);

        // Listener für themaDropdown (nur einmal registrieren)
        
        this.themaDropdown.addActionListener(e -> {
            String neuesThema = (String) themaDropdown.getSelectedItem();
            if (neuesThema != null && !neuesThema.equals(aktuellesThema)) {
                aktuellesThema = neuesThema;
                ladeFragenFuerThema(aktuellesThema);
            }
        });
        
        

        aktuellesThema = (String) themaDropdown.getSelectedItem();
        if (aktuellesThema != null) {
            ladeFragenFuerThema(aktuellesThema);
        }
        if (themaDropdown != null) {
            themaDropdown.addActionListener(e -> {
                // Aktion
            });
        } else {
            System.err.println("WARNUNG: ThemaDropdown ist null.");
        }
    }

    
    
    private boolean speichereAktuelleFrageInDB(boolean zeigeFehler) {//------------------------------TODO
        if (aktuellesThema == null) return false;

        String titel = titelField.getText().trim();
        String frage = frageArea.getText().trim();

        if (titel.isEmpty() && frage.isEmpty()) return true;

        if (titel.isEmpty()) {
            if (zeigeFehler) {
                JOptionPane.showMessageDialog(this, "Der Titel darf nicht leer sein!", "Fehler", JOptionPane.ERROR_MESSAGE);
            }
            return false;
        }

        if (frage.isEmpty()) {
            if (zeigeFehler) {
                JOptionPane.showMessageDialog(this, "Die Frage darf nicht leer sein!", "Fehler", JOptionPane.ERROR_MESSAGE);
            }
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
            if (zeigeFehler) {
                JOptionPane.showMessageDialog(this, "Mindestens eine Antwort muss als richtig markiert sein!", "Fehler", JOptionPane.ERROR_MESSAGE);
            }
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
        }

        db.addFrage(aktuellesThema, neueFrage);

        if (zeigeFehler) {
            JOptionPane.showMessageDialog(this, "Frage wurde gespeichert.", "Info", JOptionPane.INFORMATION_MESSAGE);
        }

        ladeFragenFuerThema(aktuellesThema);
        return true;
    }

    private void ladeFragenFuerThema(String thema) {
        if (db == null || thema == null) {
            aktuelleFragen.clear();
            clearForm();
            return;
        }

        // Fragen aus DB holen
        aktuelleFragen = db.getFragenZuThema(thema);

        if (aktuelleFragen.isEmpty()) {
            clearForm();
        } else {
            // Zeige die erste Frage an (oder andere Logik)
            setFrage(aktuelleFragen.get(0));
        }
    }
    


    // Setter für DataBusiness
    public void setDataBusiness(DataBusiness db) {
        this.db = db;
    }

    // Event-Handling
    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        if ("Speichern".equals(cmd)) {
            if (db == null) {
                JOptionPane.showMessageDialog(this, "DataBusiness ist nicht gesetzt!", "Fehler", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String thema = (String) themaDropdown.getSelectedItem();
            boolean erfolgreich = db.speichereFrage(
                    thema,
                    titelField.getText(),
                    frageArea.getText(),
                    antwortFields,
                    correctBoxes,
                    true,
                    this
            );

            if (erfolgreich) {
                JOptionPane.showMessageDialog(this, "Frage wurde gespeichert.", "Info", JOptionPane.INFORMATION_MESSAGE);
                // Optional: Felder zurücksetzen
                titelField.setText("");
                frageArea.setText("");
                for (int i = 0; i < 4; i++) {
                    antwortFields[i].setText("");
                    correctBoxes[i].setSelected(false);
                }
            }
        } else if ("Frage Löschen".equals(cmd)) {
            String thema = (String) themaDropdown.getSelectedItem();
            String titel = titelField.getText().trim();
            if (thema == null || thema.isEmpty() || titel.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Thema und Titel müssen angegeben sein, um zu löschen.", "Fehler", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Frage suchen und löschen
            List<QuizFrage> fragen = db.getFragenZuThema(thema);
            int index = -1;
            for (int i = 0; i < fragen.size(); i++) {
                if (fragen.get(i).getTitel().equals(titel)) {
                    index = i;
                    break;
                }
            }
            if (index >= 0) {
                db.loescheFrage(thema, index);
                JOptionPane.showMessageDialog(this, "Frage wurde gelöscht.", "Info", JOptionPane.INFORMATION_MESSAGE);
                // Formular leeren
                titelField.setText("");
                frageArea.setText("");
                for (int i = 0; i < 4; i++) {
                    antwortFields[i].setText("");
                    correctBoxes[i].setSelected(false);
                }
                // Aktualisiere Liste der Fragen nach dem Löschen
                ladeFragenFuerThema(thema);
            } else {
                JOptionPane.showMessageDialog(this, "Frage nicht gefunden.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

//    public void setFragenFuerThema(String thema, List<QuizFrage> fragen) {
//        this.aktuellesThema = thema;
//        this.aktuelleFragen = fragen != null ? fragen : new ArrayList<>();
//        clearForm();  // Formular leeren, da noch keine Frage gewählt
//    }
    
    public void setFrage(QuizFrage frage) {
        if (frage == null) {
            clearForm();
            return;
        }

        titelField.setText(frage.getTitel());
        frageArea.setText(frage.getFrageText());

        List<String> antworten = frage.getAntworten();
        List<Boolean> korrekt = frage.getKorrekt();

        for (int i = 0; i < 4; i++) {
            antwortFields[i].setText(i < antworten.size() ? antworten.get(i) : "");
            correctBoxes[i].setSelected(i < korrekt.size() && korrekt.get(i));
        }
    }

    /**
     * Löscht alle Eingaben im Formular und setzt das Panel zurück.
     */
    public void clearForm() {
        titelField.setText("");
        frageArea.setText("");
        for (int i = 0; i < 4; i++) {
            antwortFields[i].setText("");
            correctBoxes[i].setSelected(false);
        }
    }
    
    
    public void setAktuellesThema(String aktuellesThema) {
        if (aktuellesThema == null || aktuellesThema.equals(this.aktuellesThema)) return;
        

        this.aktuellesThema = aktuellesThema;
        ladeFragenFuerThema(aktuellesThema);
    }
}


































//package gui;
//
//import java.awt.BorderLayout;
//import java.awt.GridLayout;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.swing.*;
//
//import dbbl.DataBusiness;
//
///**
// * Panel zum Erstellen von Quizfragen. Arbeitet mit dem übergebenen Thema-Dropdown.
// */
//public class CreateQuizfragenPanel extends JPanel {
//
//    private final JComboBox<String> themaDropdown;
//    private final DataBusiness db;
//    private final FormingPanel parent;
//
//    private JTextField titelField;
//    private JTextArea frageTextArea;
//    private JTextField[] antwortFields = new JTextField[4];
//    private JCheckBox[] korrektCheckboxes = new JCheckBox[4];
//
//    public CreateQuizfragenPanel(ActionListener listener, JComboBox<String> themaDropdown, DataBusiness db) {
//        this.parent = (FormingPanel) listener;
//        this.themaDropdown = themaDropdown;
//        this.db = db;
//
//        initPanel();
//    }
//
//    private void initPanel() {
//        setLayout(new BorderLayout());
//        setBorder(BorderFactory.createTitledBorder("Frage erstellen"));
//
//        JPanel inputPanel = new JPanel(new GridLayout(0, 1, 5, 5));
//
//        // Fragetitel
//        titelField = new JTextField();
//        inputPanel.add(new JLabel("Titel der Frage:"));
//        inputPanel.add(titelField);
//
//        // Fragetext
//        frageTextArea = new JTextArea(3, 20);
//        frageTextArea.setLineWrap(true);
//        frageTextArea.setWrapStyleWord(true);
//        inputPanel.add(new JLabel("Fragetext:"));
//        inputPanel.add(new JScrollPane(frageTextArea));
//
//        // Antworten und Checkboxen
//        for (int i = 0; i < 4; i++) {
//            JPanel antwortPanel = new JPanel(new BorderLayout());
//            antwortFields[i] = new JTextField();
//            korrektCheckboxes[i] = new JCheckBox("Korrekt");
//            antwortPanel.add(new JLabel("Antwort " + (i + 1) + ":"), BorderLayout.WEST);
//            antwortPanel.add(antwortFields[i], BorderLayout.CENTER);
//            antwortPanel.add(korrektCheckboxes[i], BorderLayout.EAST);
//            inputPanel.add(antwortPanel);
//        }
//
//        // Speichern Button
//        JButton speichernBtn = new JButton("Frage speichern");
//        speichernBtn.addActionListener(this::frageSpeichern);
//        inputPanel.add(speichernBtn);
//
//        add(inputPanel, BorderLayout.NORTH);
//    }
//
//    private void frageSpeichern(ActionEvent e) {
//        String thema = (String) themaDropdown.getSelectedItem();
//        if (thema == null || thema.isEmpty()) {
//            JOptionPane.showMessageDialog(this, "Bitte ein Thema auswählen!", "Fehler", JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//
//        String titel = titelField.getText().trim();
//        String frage = frageTextArea.getText().trim();
//
//        if (titel.isEmpty() || frage.isEmpty()) {
//            JOptionPane.showMessageDialog(this, "Titel und Frage dürfen nicht leer sein!", "Fehler", JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//
//        List<String> antworten = new ArrayList<>();
//        List<Boolean> korrekt = new ArrayList<>();
//        boolean hatMindestensEineKorrekte = false;
//
//        for (int i = 0; i < 4; i++) {
//            String text = antwortFields[i].getText().trim();
//            antworten.add(text);
//            boolean istKorrekt = korrektCheckboxes[i].isSelected();
//            korrekt.add(istKorrekt);
//            if (istKorrekt) hatMindestensEineKorrekte = true;
//        }
//
//        if (!hatMindestensEineKorrekte) {
//            JOptionPane.showMessageDialog(this, "Mindestens eine Antwort muss korrekt sein!", "Fehler", JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//
//        DataBusiness.QuizFrage neueFrage = new DataBusiness.QuizFrage(titel, frage, antworten, korrekt);
//        db.addFrage(thema, neueFrage);
//
//        JOptionPane.showMessageDialog(this, "Frage gespeichert für Thema: " + thema, "Erfolg", JOptionPane.INFORMATION_MESSAGE);
//
//        // Formular leeren
//        titelField.setText("");
//        frageTextArea.setText("");
//        for (int i = 0; i < 4; i++) {
//            antwortFields[i].setText("");
//            korrektCheckboxes[i].setSelected(false);
//        }
//
//        // Fragenliste aktualisieren
//        parent.aktualisiereFragenListeNachSpeichern();
//    }
//    
//    public void clearForm() {
//        titelField.setText("");
//        frageTextArea.setText("");
//        for (int i = 0; i < 4; i++) {
//            antwortFields[i].setText("");
//            korrektCheckboxes[i].setSelected(false);
//        }
//    }
//
//    public void setAktuellesThema(String thema) {
//        // Optional speichern, falls du später im Panel etwas damit tun willst
//    }
//
//    public void setDataBusiness(DataBusiness db) {
//        // Falls du DataBusiness dynamisch ändern willst (du hast ihn aber eh im Konstruktor gesetzt)
//        // Optional implementierbar:
//        // this.db = db;
//    }
//
//    public void setFrage(DataBusiness.QuizFrage frage) {
//        if (frage == null) return;
//
//        titelField.setText(frage.getTitel());
//        frageTextArea.setText(frage.getFrageText());
//
//        List<String> antworten = frage.getAntworten();
//        List<Boolean> korrekt = frage.getKorrekt();
//
//        for (int i = 0; i < 4; i++) {
//            antwortFields[i].setText(i < antworten.size() ? antworten.get(i) : "");
//            korrektCheckboxes[i].setSelected(i < korrekt.size() && korrekt.get(i));
//        }
//    }
//}


























//package gui;
//
//import java.awt.BorderLayout;
//import java.awt.GridLayout;
//import java.awt.event.ActionEvent;
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.swing.BorderFactory;
//import javax.swing.JButton;
//import javax.swing.JCheckBox;
//import javax.swing.JComboBox;
//import javax.swing.JLabel;
//import javax.swing.JOptionPane;
//import javax.swing.JPanel;
//import javax.swing.JScrollPane;
//import javax.swing.JTextArea;
//import javax.swing.JTextField;
//
//import dbbl.DataBusiness;
//
///**
// * Panel zum Erstellen von Quizfragen. Arbeitet mit dem übergebenen Thema-Dropdown.
// */
//public class CreateQuizfragenPanel extends JPanel {
//
//    private final JComboBox<String> themaDropdown;
//    private final DataBusiness db;
//    private final FormingPanel parent;
//
//    private JTextField titelField;
//    private JTextArea frageTextArea;
//    private JTextField[] antwortFields = new JTextField[4];
//    private JCheckBox[] korrektCheckboxes = new JCheckBox[4];
//
//    public CreateQuizfragenPanel(FormingPanel parent, JComboBox<String> themaDropdown, DataBusiness db) {
//        this.parent = parent;
//        this.themaDropdown = themaDropdown;
//        this.db = db;
//
//        initPanel();
//    }
//
//    private void initPanel() {
//        setLayout(new BorderLayout());
//        setBorder(BorderFactory.createTitledBorder("Frage erstellen"));
//
//        JPanel inputPanel = new JPanel(new GridLayout(0, 1, 5, 5));
//
//        // Fragetitel
//        titelField = new JTextField();
//        inputPanel.add(new JLabel("Titel der Frage:"));
//        inputPanel.add(titelField);
//
//        // Fragetext
//        frageTextArea = new JTextArea(3, 20);
//        frageTextArea.setLineWrap(true);
//        frageTextArea.setWrapStyleWord(true);
//        inputPanel.add(new JLabel("Fragetext:"));
//        inputPanel.add(new JScrollPane(frageTextArea));
//
//        // Antworten und Checkboxen
//        for (int i = 0; i < 4; i++) {
//            JPanel antwortPanel = new JPanel(new BorderLayout());
//            antwortFields[i] = new JTextField();
//            korrektCheckboxes[i] = new JCheckBox("Korrekt");
//            antwortPanel.add(new JLabel("Antwort " + (i + 1) + ":"), BorderLayout.WEST);
//            antwortPanel.add(antwortFields[i], BorderLayout.CENTER);
//            antwortPanel.add(korrektCheckboxes[i], BorderLayout.EAST);
//            inputPanel.add(antwortPanel);
//        }
//
//        // Speichern Button
//        JButton speichernBtn = new JButton("Frage speichern");
//        speichernBtn.addActionListener(this::frageSpeichern);
//        inputPanel.add(speichernBtn);
//
//        add(inputPanel, BorderLayout.NORTH);
//    }
//
//    private void frageSpeichern(ActionEvent e) {
//        String thema = (String) themaDropdown.getSelectedItem();
//        if (thema == null || thema.isEmpty()) {
//            JOptionPane.showMessageDialog(this, "Bitte ein Thema auswählen!", "Fehler", JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//
//        String titel = titelField.getText().trim();
//        String frage = frageTextArea.getText().trim();
//
//        if (titel.isEmpty() || frage.isEmpty()) {
//            JOptionPane.showMessageDialog(this, "Titel und Frage dürfen nicht leer sein!", "Fehler", JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//
//        List<String> antworten = new ArrayList<>();
//        List<Boolean> korrekt = new ArrayList<>();
//        boolean hatMindestensEineKorrekte = false;
//
//        for (int i = 0; i < 4; i++) {
//            String text = antwortFields[i].getText().trim();
//            antworten.add(text);
//            boolean istKorrekt = korrektCheckboxes[i].isSelected();
//            korrekt.add(istKorrekt);
//            if (istKorrekt) hatMindestensEineKorrekte = true;
//        }
//
//        if (!hatMindestensEineKorrekte) {
//            JOptionPane.showMessageDialog(this, "Mindestens eine Antwort muss korrekt sein!", "Fehler", JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//
//        DataBusiness.QuizFrage neueFrage = new DataBusiness.QuizFrage(titel, frage, antworten, korrekt);
//        db.addFrage(thema, neueFrage);
//
//        JOptionPane.showMessageDialog(this, "Frage gespeichert für Thema: " + thema, "Erfolg", JOptionPane.INFORMATION_MESSAGE);
//
//        // Formular leeren
//        titelField.setText("");
//        frageTextArea.setText("");
//        for (int i = 0; i < 4; i++) {
//            antwortFields[i].setText("");
//            korrektCheckboxes[i].setSelected(false);
//        }
//
//        // Fragenliste aktualisieren
//        parent.aktualisiereFragenListeNachSpeichern();
//    }
//}


















//
//package gui;
//
//import java.awt.BorderLayout;
//import java.awt.GridLayout;
//import java.awt.event.ActionEvent;
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.swing.JButton;
//import javax.swing.JCheckBox;
//import javax.swing.JComboBox;
//import javax.swing.JLabel;
//import javax.swing.JOptionPane;
//import javax.swing.JPanel;
//import javax.swing.JScrollPane;
//import javax.swing.JTextArea;
//import javax.swing.JTextField;
//
//import dbbl.DataBusiness;
//
//public class CreateQuizfragenPanel extends JPanel {
//
//    private JTextField titelField;
//    private JTextArea frageArea;
//    private JTextField[] antwortFields;
//    private JCheckBox[] korrektBoxes;
//    private JButton speichernBtn;
//
//    private String aktuellesThema;
//    private JComboBox<String> themaDropdown;
//    private DataBusiness db;
//
//    public CreateQuizfragenPanel(JComboBox<String> themaDropdown, DataBusiness db) {
//        this.themaDropdown = themaDropdown;
//        this.db = db;
//        init();
//    }
//
//    private void init() {
//        setLayout(new BorderLayout());
//
//        JPanel formPanel = new JPanel(new GridLayout(7, 1));
//        titelField = new JTextField();
//        frageArea = new JTextArea(2, 20);
//        antwortFields = new JTextField[4];
//        korrektBoxes = new JCheckBox[4];
//
//        formPanel.add(new JLabel("Titel:"));
//        formPanel.add(titelField);
//        formPanel.add(new JLabel("Frage:"));
//        formPanel.add(new JScrollPane(frageArea));
//
//        for (int i = 0; i < 4; i++) {
//            antwortFields[i] = new JTextField();
//            korrektBoxes[i] = new JCheckBox("Richtig");
//            JPanel antwortPanel = new JPanel(new BorderLayout());
//            antwortPanel.add(antwortFields[i], BorderLayout.CENTER);
//            antwortPanel.add(korrektBoxes[i], BorderLayout.EAST);
//            formPanel.add(antwortPanel);
//        }
//
//        speichernBtn = new JButton("Frage speichern");
//        speichernBtn.addActionListener(this::frageSpeichern);
//        add(formPanel, BorderLayout.CENTER);
//        add(speichernBtn, BorderLayout.SOUTH);
//    }
//
//    public void setAktuellesThema(String thema) {
//        this.aktuellesThema = thema;
//    }
//
//    public void setFrage(DataBusiness.QuizFrage frage) {
//        titelField.setText(frage.getTitel());
//        frageArea.setText(frage.getFrageText());
//
//        List<String> antworten = frage.getAntworten();
//        List<Boolean> richtig = frage.getKorrekt();
//
//        for (int i = 0; i < 4; i++) {
//            antwortFields[i].setText(i < antworten.size() ? antworten.get(i) : "");
//            korrektBoxes[i].setSelected(i < richtig.size() && richtig.get(i));
//        }
//    }
//
//    public void clearForm() {
//        titelField.setText("");
//        frageArea.setText("");
//        for (int i = 0; i < 4; i++) {
//            antwortFields[i].setText("");
//            korrektBoxes[i].setSelected(false);
//        }
//    }
//
//    private void frageSpeichern(ActionEvent e) {
//        String thema = (String) themaDropdown.getSelectedItem();
//        if (thema == null || thema.isEmpty()) {
//            JOptionPane.showMessageDialog(this, "Kein Thema ausgewählt!", "Fehler", JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//
//        String titel = titelField.getText().trim();
//        String frage = frageArea.getText().trim();
//
//        List<String> antworten = new ArrayList<>();
//        List<Boolean> richtig = new ArrayList<>();
//        boolean mindestensEineRichtig = false;
//
//        for (int i = 0; i < 4; i++) {
//            antworten.add(antwortFields[i].getText().trim());
//            boolean istRichtig = korrektBoxes[i].isSelected();
//            richtig.add(istRichtig);
//            if (istRichtig) mindestensEineRichtig = true;
//        }
//
//        if (titel.isEmpty() || frage.isEmpty() || !mindestensEineRichtig) {
//            JOptionPane.showMessageDialog(this, "Bitte alle Felder ausfüllen und mindestens eine richtige Antwort wählen.", "Fehler", JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//
//        DataBusiness.QuizFrage neueFrage = new DataBusiness.QuizFrage(titel, frage, antworten, richtig);
//        db.addFrage(thema, neueFrage);
//        JOptionPane.showMessageDialog(this, "Frage gespeichert.", "Info", JOptionPane.INFORMATION_MESSAGE);
//        clearForm();
//    }
//}