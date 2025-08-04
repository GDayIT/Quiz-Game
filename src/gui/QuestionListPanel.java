package gui;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import dbbl.DataBusiness;
import dbbl.QuizFrage;

public class QuestionListPanel extends JPanel {

    private JComboBox<String> themaDropdown;
    private DefaultListModel<String> listModel;
    private JList<String> fragenList;
    private CreateQuizfragenPanel createPanel;


    private String aktuellesThema = null;
    private List<QuizFrage> aktuelleFragen = new ArrayList<>();
    private DataBusiness db = new DataBusiness();

    public QuestionListPanel(ActionListener listener, DataBusiness db) {
    	
    	this.db = db;
    	
        setLayout(new BorderLayout());
        List<String> themen = db.getAlleThemen();
        themaDropdown = new JComboBox<>();
        for (String thema : themen) {
            themaDropdown.addItem(thema);
        }
        if (!themen.isEmpty()) {
            aktuellesThema = themen.get(0);
            themaDropdown.setSelectedItem(aktuellesThema);
        }
        
        // listModel und fragenList initialisieren
        listModel = new DefaultListModel<>();
        fragenList = new JList<>(listModel);
        fragenList.setVisibleRowCount(8);

        // CreatePanel initialisieren
//        createPanel = new CreateQuizfragenPanel(listener,  themaDropdown, db);
//        createPanel.setAktuellesThema(aktuellesThema);
//        add(createPanel, BorderLayout.CENTER);

        // Rechte Seitenleiste
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton anzeigenBtn = new JButton("Thema anzeigen");
        styleButton(anzeigenBtn);
        anzeigenBtn.addActionListener(e -> {
            themaWechsel(); // hier Thema laden und Fragen aktualisieren
        });
        styleButton(anzeigenBtn);
        anzeigenBtn.addActionListener(e -> {
            themaWechsel();  // direkt Methode aufrufen
        });
        styleButton(anzeigenBtn);
        JPanel themaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        themaPanel.add(themaDropdown);
        themaPanel.add(anzeigenBtn);
        rightPanel.add(themaPanel);

        JLabel fragenLabel = new JLabel("Fragen zum Thema");
        rightPanel.add(fragenLabel);

        JScrollPane listScroll = new JScrollPane(fragenList);
        listScroll.setPreferredSize(new Dimension(200, 300));
        rightPanel.add(listScroll);

        JButton neueFrageBtn = new JButton("Neue Frage");
        styleButton(neueFrageBtn);
        neueFrageBtn.addActionListener(e -> {
            neueFrage();  // direkt Methode aufrufen
        });
        styleButton(neueFrageBtn);
        neueFrageBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(neueFrageBtn);

        add(rightPanel, BorderLayout.EAST);

        // Event-Handling
//        anzeigenBtn.addActionListener(e -> themaWechsel());
//        neueFrageBtn.addActionListener(e -> neueFrage());
        fragenList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                ladeFrageForm(fragenList.getSelectedIndex());
            }
        });

        // Lade erste Fragen ganz am Ende
        if (aktuellesThema != null) {
            ladeFragenFuerThema(aktuellesThema);
        }
    }
    

    public JComboBox<String> getThemaDropdown() {
        return themaDropdown;
    }

    public void aktualisiereFragenListe(List<QuizFrage> fragen) {
        listModel.clear();
        for (QuizFrage frage : fragen) {
            listModel.addElement(frage.getTitel());
        }
    }

    public void leeren() {
        listModel.clear(); 
    }

    public int getSelectedIndex() {
        return fragenList.getSelectedIndex();
    }

    public void clearSelection() {
        fragenList.clearSelection();
    }
    private void ladeFragenFuerThema(String thema) {
        aktuelleFragen = db.getFragenZuThema(thema);
        listModel.clear();
        for (QuizFrage frage : aktuelleFragen) {
            listModel.addElement(frage.getTitel());
        }
        if (createPanel != null) {
            createPanel.clearForm();
        }
    }

//    private void ladeFragenFuerThema(String thema) {
//        aktuelleFragen = db.getFragenZuThema(thema);
//        listModel.clear();
//        for (DataBusiness.QuizFrage frage : aktuelleFragen) {
//            listModel.addElement(frage.getTitel());
//        }
//        createPanel.clearForm();
//    }
    
    public void updateFragenListe(List<QuizFrage> fragen) {
        listModel.clear();
        for (QuizFrage frage : fragen) {
            listModel.addElement(frage.getTitel());
        }
    }

    public void ladeFrageForm(int index) {
        if (index < 0 || index >= aktuelleFragen.size()) {
            createPanel.clearForm();
            return;
        }
        QuizFrage frage = aktuelleFragen.get(index);
        createPanel.setFrage(frage);
    }

    private void neueFrage() {
        createPanel.clearForm();
        fragenList.clearSelection();
    }

 
//    private void themaWechsel() {
//        String neuesThema = (String) themaDropdown.getSelectedItem();
//        if (neuesThema == null || neuesThema.equals(aktuellesThema)) return;
//
//        aktuellesThema = neuesThema;
//        ladeFragenFuerThema(aktuellesThema);
//        createPanel.setAktuellesThema(aktuellesThema);
//    }
    
    
    public void setCreatePanel(CreateQuizfragenPanel createPanel) {
        this.createPanel = createPanel;
    }
    
//    private void themaWechsel() {
//    	List<String> themen = db.getAlleThemen();
//        if (themen == null || themen.equals(aktuellesThema)) return;
//
//        aktuellesThema = themen;
//        ladeFragenFuerThema(aktuellesThema);
//        createPanel.setAktuellesThema(aktuellesThema);
//    }

    private void themaWechsel() {
    	
        // Themen-Dropdown initialisieren
        List<String> themen = db.getAlleThemen();
        themaDropdown = new JComboBox<>();
        for (String thema : themen) {
            themaDropdown.addItem(thema);
        }

        if (!themen.isEmpty()) {
            aktuellesThema = themen.get(0); // Setze erstes Thema
        }
    
        String neuesThema = (String) themaDropdown.getSelectedItem();
        if (neuesThema == null || neuesThema.equals(aktuellesThema)) return;

        aktuellesThema = neuesThema;
        ladeFragenFuerThema(aktuellesThema);
        createPanel.setAktuellesThema(aktuellesThema);
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
//import java.awt.Component;
//import java.awt.Dimension;
//import java.awt.FlowLayout;
//import java.awt.Font;
//import java.awt.event.ActionListener;
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.swing.BorderFactory;
//import javax.swing.Box;
//import javax.swing.BoxLayout;
//import javax.swing.DefaultListModel;
//import javax.swing.JButton;
//import javax.swing.JCheckBox;
//import javax.swing.JComboBox;
//import javax.swing.JLabel;
//import javax.swing.JList;
//import javax.swing.JOptionPane;
//import javax.swing.JPanel;
//import javax.swing.JScrollPane;
//import javax.swing.JTextArea;
//import javax.swing.JTextField;
//
//import dbbl.DataBusiness;
//import dbbl.DataBusiness.QuizFrage;
//
//public class QuestionListPanel extends JPanel {
//
//    private JComboBox<String> themaDropdown;
//    private DefaultListModel<String> listModel;
//    private JList<String> fragenList;
//    private CreateQuizfragenPanel createPanel;
//
//    private JTextField titelField;
//    private JTextArea frageArea;
//    private JTextField[] antwortFields = new JTextField[4];
//    private JCheckBox[] correctBoxes = new JCheckBox[4];
//
//    private String aktuellesThema = null;
//    private List<DataBusiness.QuizFrage> aktuelleFragen = new ArrayList<>();
//    private DataBusiness db = new DataBusiness();
//
//    public QuestionListPanel(ActionListener listener) {
//        setLayout(new BorderLayout());
//
//        titelField = new JTextField();
//        frageArea = new JTextArea();
//        for (int i = 0; i < 4; i++) {
//            antwortFields[i] = new JTextField();
//            correctBoxes[i] = new JCheckBox();
//        }
//
//        themaDropdown = new JComboBox<>(new String[]{"Thema 1", "Thema 2", "Titel des Themas 15"});
//        createPanel = new CreateQuizfragenPanel(listener, themaDropdown, db);
//        add(createPanel, BorderLayout.CENTER);
//        createPanel.setDataBusiness(db);
//
//        JPanel rightPanel = new JPanel();
//        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
//        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
//
//        JButton anzeigenBtn = new JButton("Thema anzeigen");
//        styleButton(anzeigenBtn);
//        JPanel themaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        themaPanel.add(themaDropdown);
//        themaPanel.add(anzeigenBtn);
//        rightPanel.add(themaPanel);
//
//        JLabel fragenLabel = new JLabel("Fragen zum Thema");
//        rightPanel.add(fragenLabel);
//
//        listModel = new DefaultListModel<>();
//        fragenList = new JList<>(listModel);
//        fragenList.setVisibleRowCount(8);
//        JScrollPane listScroll = new JScrollPane(fragenList);
//        listScroll.setPreferredSize(new Dimension(200, 300));
//        rightPanel.add(listScroll);
//
//        JButton neueFrageBtn = new JButton("Neue Frage");
//        styleButton(neueFrageBtn);
//        neueFrageBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
//        rightPanel.add(Box.createVerticalStrut(10));
//        rightPanel.add(neueFrageBtn);
//
//        add(rightPanel, BorderLayout.EAST);
//
//        anzeigenBtn.addActionListener(e -> themaWechsel());
//        neueFrageBtn.addActionListener(e -> neueFrage());
//        fragenList.addListSelectionListener(e -> {
//            if (!e.getValueIsAdjusting()) {
//                ladeFrageForm(fragenList.getSelectedIndex());
//            }
//        });
//    }
//
//    public void aktualisiereFragenListe(List<DataBusiness.QuizFrage> fragen) {
//        listModel.clear();
//        for (DataBusiness.QuizFrage frage : fragen) {
//            listModel.addElement(frage.getTitel());
//        }
//    }
//
//    public void leeren() {
//        listModel.clear();
//    }
//
//    public int getSelectedIndex() {
//        return fragenList.getSelectedIndex();
//    }
//
//    public void clearSelection() {
//        fragenList.clearSelection();
//    }
//
//    private boolean speichereAktuelleFrageInDB(boolean zeigeFehler) {
//        if (aktuellesThema == null) return false;
//
//        String titel = titelField.getText().trim();
//        String frage = frageArea.getText().trim();
//
//        if (titel.isEmpty() && frage.isEmpty()) return true;
//
//        if (titel.isEmpty()) {
//            if (zeigeFehler) {
//                JOptionPane.showMessageDialog(this, "Der Titel darf nicht leer sein!", "Fehler", JOptionPane.ERROR_MESSAGE);
//            }
//            return false;
//        }
//
//        if (frage.isEmpty()) {
//            if (zeigeFehler) {
//                JOptionPane.showMessageDialog(this, "Die Frage darf nicht leer sein!", "Fehler", JOptionPane.ERROR_MESSAGE);
//            }
//            return false;
//        }
//
//        List<String> antworten = new ArrayList<>();
//        List<Boolean> korrekt = new ArrayList<>();
//        boolean mindestensEineRichtige = false;
//
//        for (int i = 0; i < 4; i++) {
//            String antwortText = antwortFields[i].getText().trim();
//            antworten.add(antwortText);
//            boolean istKorrekt = correctBoxes[i].isSelected();
//            korrekt.add(istKorrekt);
//            if (istKorrekt) mindestensEineRichtige = true;
//        }
//
//        if (!mindestensEineRichtige) {
//            if (zeigeFehler) {
//                JOptionPane.showMessageDialog(this, "Mindestens eine Antwort muss als richtig markiert sein!", "Fehler", JOptionPane.ERROR_MESSAGE);
//            }
//            return false;
//        }
//
//        DataBusiness.QuizFrage neueFrage = new DataBusiness.QuizFrage(titel, frage, antworten, korrekt);
//
//        int vorhandenerIndex = -1;
//        for (int i = 0; i < aktuelleFragen.size(); i++) {
//            if (aktuelleFragen.get(i).getTitel().equals(titel)) {
//                vorhandenerIndex = i;
//                break;
//            }
//        }
//
//        if (vorhandenerIndex >= 0) {
//            db.loescheFrage(aktuellesThema, vorhandenerIndex);
//        }
//
//        db.addFrage(aktuellesThema, neueFrage);
//
//        if (zeigeFehler) {
//            JOptionPane.showMessageDialog(this, "Frage wurde gespeichert.", "Info", JOptionPane.INFORMATION_MESSAGE);
//        }
//
//        ladeFragenFuerThema(aktuellesThema);
//        createPanel.clearForm();
//        return true;
//    }
//
//    private void ladeFragenFuerThema(String thema) {
//        aktuelleFragen = db.getFragenZuThema(thema);
//        listModel.clear();
//        for (DataBusiness.QuizFrage frage : aktuelleFragen) {
//            listModel.addElement(frage.getTitel());
//        }
//        clearForm();
//    }
//
//    private void clearForm() {
//        titelField.setText("");
//        frageArea.setText("");
//        for (int i = 0; i < 4; i++) {
//            antwortFields[i].setText("");
//            correctBoxes[i].setSelected(false);
//        }
//    }
//
//    public void ladeFrageForm(int index) {
//        if (index < 0 || index >= aktuelleFragen.size()) {
//            createPanel.clearForm();
//            return;
//        }
//
//        DataBusiness.QuizFrage frage = aktuelleFragen.get(index);
//        createPanel.setFrage(frage);
//    }
//
//    public void setFrage(QuizFrage frage) {
//        titelField.setText(frage.getTitel());
//        frageArea.setText(frage.getFrageText());
//
//        List<String> antworten = frage.getAntworten();
//        List<Boolean> korrekt = frage.getKorrekt();
//
//        for (int i = 0; i < 4; i++) {
//            antwortFields[i].setText(i < antworten.size() ? antworten.get(i) : "");
//            correctBoxes[i].setSelected(i < korrekt.size() && korrekt.get(i));
//        }
//    }
//
//    private void neueFrage() {
//        clearForm();
//        fragenList.clearSelection();
//    }
//
//    private void themaWechsel() {
//        if (!speichereAktuelleFrageInDB(false)) return;
//
//        String neuesThema = (String) themaDropdown.getSelectedItem();
//        if (neuesThema == null || neuesThema.equals(aktuellesThema)) return;
//
//        aktuellesThema = neuesThema;
//
//        ladeFragenFuerThema(aktuellesThema);
//        createPanel.setAktuellesThema(aktuellesThema);
//        createPanel.clearForm();
//    }
//
//    private void styleButton(JButton button) {
//        button.setFocusPainted(false);
//        button.setForeground(Color.DARK_GRAY);
//        button.setBackground(new Color(220, 220, 220));
//        button.setOpaque(true);
//        button.setBorder(BorderFactory.createLineBorder(Color.GRAY));
//        button.setFont(new Font("Arial", Font.PLAIN, 12));
//        button.setPreferredSize(new Dimension(120, 30));
//    }
//}











































//package gui;
//
//import java.awt.BorderLayout;
//import java.awt.Color;
//import java.awt.Component;
//import java.awt.Dimension;
//import java.awt.FlowLayout;
//import java.awt.Font;
//import java.awt.event.ActionListener;
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.swing.BorderFactory;
//import javax.swing.Box;
//import javax.swing.BoxLayout;
//import javax.swing.DefaultListModel;
//import javax.swing.JButton;
//import javax.swing.JCheckBox;
//import javax.swing.JComboBox;
//import javax.swing.JLabel;
//import javax.swing.JList;
//import javax.swing.JOptionPane;
//import javax.swing.JPanel;
//import javax.swing.JScrollPane;
//import javax.swing.JTextArea;
//import javax.swing.JTextField;
//
//import dbbl.DataBusiness;
//import dbbl.DataBusiness.QuizFrage;
//
//
//public class QuestionListPanel extends JPanel {
//
//    private JComboBox<String> themaDropdown;
//    private DefaultListModel<String> listModel;
//    private JList<String> fragenList; 
//    private CreateQuizfragenPanel createPanel;
//
//    // Diese Felder wurden vorher nicht initialisiert - jetzt im Konstruktor:
//    private JTextField titelField;
//    private JTextArea frageArea;
//    private JTextField[] antwortFields = new JTextField[4];
//    private JCheckBox[] correctBoxes = new JCheckBox[4];
//
//    private String aktuellesThema = null;
//    private List<DataBusiness.QuizFrage> aktuelleFragen = new ArrayList<>();
//    private DataBusiness db = new DataBusiness();
//
//    public QuestionListPanel(ActionListener listener) {
//        setLayout(new BorderLayout());
//
//
//        // Initialisierung der Felder, die vorher fehlten:
//        titelField = new JTextField();
//        frageArea = new JTextArea();
//        for (int i = 0; i < 4; i++) {
//            antwortFields[i] = new JTextField();
//            correctBoxes[i] = new JCheckBox();
//        }
//
//        themaDropdown = new JComboBox<>(new String[]{"Thema 1", "Thema 2", "Titel des Themas 15"});
//        createPanel = new CreateQuizfragenPanel(listener, themaDropdown, db);
//        add(createPanel, BorderLayout.CENTER);
//        createPanel.setDataBusiness(db);
//
//
//        // Rechte Seite - Themenauswahl und Fragenliste
//        JPanel rightPanel = new JPanel();
//        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
//        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
//
//
//        JButton anzeigenBtn = new JButton("Thema anzeigen");
//        styleButton(anzeigenBtn);
//        JPanel themaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        themaPanel.add(themaDropdown);
//        themaPanel.add(anzeigenBtn);
//        rightPanel.add(themaPanel);
//        
//        JLabel fragenLabel = new JLabel("Fragen zum Thema");
//        rightPanel.add(fragenLabel);
//
//        listModel = new DefaultListModel<>();
//        fragenList = new JList<>(listModel);
//        fragenList.setVisibleRowCount(8);
//        JScrollPane listScroll = new JScrollPane(fragenList);
//        listScroll.setPreferredSize(new Dimension(200, 300));
//        rightPanel.add(listScroll);
//
//        JButton neueFrageBtn = new JButton("Neue Frage");
//        styleButton(neueFrageBtn);
//        neueFrageBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
//        rightPanel.add(Box.createVerticalStrut(10));
//        rightPanel.add(neueFrageBtn);
//
//        add(rightPanel, BorderLayout.EAST);
//
//        anzeigenBtn.addActionListener(e -> themaWechsel());
//        neueFrageBtn.addActionListener(e -> neueFrage());
//        fragenList.addListSelectionListener(e -> {
//            if (!e.getValueIsAdjusting()) {
//                ladeFrageForm(fragenList.getSelectedIndex());
//            }
//        });
//    }
//    
//    /**
//     * Wird vom FormingPanel aufgerufen, um die Liste zu aktualisieren.
//     */
//    public void aktualisiereFragenListe(List<DataBusiness.QuizFrage> fragen) {
//        listModel.clear();
//        for (DataBusiness.QuizFrage frage : fragen) {
//            listModel.addElement(frage.getTitel());
//        }
//    }
//
//    public void leeren() {
//        listModel.clear();
//    }
//
//    public int getSelectedIndex() {
//        return fragenList.getSelectedIndex();
//    }
//
//    public void clearSelection() {
//        fragenList.clearSelection();
//    }
//
//    private boolean speichereAktuelleFrageInDB(boolean zeigeFehler) {
//        if (aktuellesThema == null) return false;
//
//        String titel = titelField.getText().trim();
//        String frage = frageArea.getText().trim();
//
//        if (titel.isEmpty() && frage.isEmpty()) return true;
//
//        if (titel.isEmpty()) {
//            if (zeigeFehler) {
//                JOptionPane.showMessageDialog(this, "Der Titel darf nicht leer sein!", "Fehler", JOptionPane.ERROR_MESSAGE);
//            }
//            return false;
//        }
//
//        if (frage.isEmpty()) {
//            if (zeigeFehler) {
//                JOptionPane.showMessageDialog(this, "Die Frage darf nicht leer sein!", "Fehler", JOptionPane.ERROR_MESSAGE);
//            }
//            return false;
//        }
//
//        List<String> antworten = new ArrayList<>();
//        List<Boolean> korrekt = new ArrayList<>();
//        boolean mindestensEineRichtige = false;
//
//        for (int i = 0; i < 4; i++) {
//            String antwortText = antwortFields[i].getText().trim();
//            antworten.add(antwortText);
//            boolean istKorrekt = correctBoxes[i].isSelected();
//            korrekt.add(istKorrekt);
//            if (istKorrekt) mindestensEineRichtige = true;
//        }
//
//        if (!mindestensEineRichtige) {
//            if (zeigeFehler) {
//                JOptionPane.showMessageDialog(this, "Mindestens eine Antwort muss als richtig markiert sein!", "Fehler", JOptionPane.ERROR_MESSAGE);
//            }
//            return false;
//        }
//
//        DataBusiness.QuizFrage neueFrage = new DataBusiness.QuizFrage(titel, frage, antworten, korrekt);
//
//        int vorhandenerIndex = -1;
//        for (int i = 0; i < aktuelleFragen.size(); i++) {
//            if (aktuelleFragen.get(i).getTitel().equals(titel)) {
//                vorhandenerIndex = i;
//                break;
//            }
//        }
//
//        if (vorhandenerIndex >= 0) {
//            db.loescheFrage(aktuellesThema, vorhandenerIndex);
//        }
//
//        db.addFrage(aktuellesThema, neueFrage);
//
//        if (zeigeFehler) {
//            JOptionPane.showMessageDialog(this, "Frage wurde gespeichert.", "Info", JOptionPane.INFORMATION_MESSAGE);
//        }
//
//        ladeFragenFuerThema(aktuellesThema);
//        return true;
//    }

    
    
  
    
    
    
    
    
    
	//_____________________________________________________________________________________________________________nicht  
//    private void ladeFrageForm(int index) {
//        if (index < 0 || index >= aktuelleFragen.size()) {
//            clearForm();
//            return;
//        }
//
//        DataBusiness.QuizFrage frage = aktuelleFragen.get(index);
//        titelField.setText(frage.getTitel());
//        frageArea.setText(frage.getFrageText());
//        List<String> antworten = frage.getAntworten();
//        List<Boolean> korrekt = frage.getKorrekt();
//
//        for (int i = 0; i < 4; i++) {
//            antwortFields[i].setText(i < antworten.size() ? antworten.get(i) : "");
//            correctBoxes[i].setSelected(i < korrekt.size() && korrekt.get(i));
//        }
//    }
    //_____________________________________________________________________________________________________________nicht
// 
//
//    private void ladeFragenFuerThema(String thema) {
//        aktuelleFragen = db.getFragenZuThema(thema);
//        listModel.clear();
//        for (DataBusiness.QuizFrage frage : aktuelleFragen) {
//            listModel.addElement(frage.getTitel());
//        }
//        clearForm();
//    }
//
//    private void clearForm() {
//        titelField.setText("");
//        frageArea.setText("");
//        for (int i = 0; i < 4; i++) {
//            antwortFields[i].setText("");
//            correctBoxes[i].setSelected(false);
//        }
//    }
//
//    public void ladeFrageForm(int index) {
//        if (index < 0 || index >= aktuelleFragen.size()) {
//            createPanel.clearForm();
//            return;
//        }
//
//        DataBusiness.QuizFrage frage = aktuelleFragen.get(index);
//        createPanel.setFrage(frage);  // <-- Übergib die Frage an das andere Panel
//    }
//    
//    public void setFrage(QuizFrage frage) {
//        titelField.setText(frage.getTitel());
//        frageArea.setText(frage.getFrageText());
//
//        List<String> antworten = frage.getAntworten();
//        List<Boolean> korrekt = frage.getKorrekt();
//
//        for (int i = 0; i < 4; i++) {
//            antwortFields[i].setText(i < antworten.size() ? antworten.get(i) : "");
//            correctBoxes[i].setSelected(i < korrekt.size() && korrekt.get(i));
//        }
//    }
//    
//
//    
//    
//    private void neueFrage() {
//        clearForm();
//        fragenList.clearSelection();
//    }
//
//    private void themaWechsel() {
//        if (!speichereAktuelleFrageInDB(false)) return;
//        String neuesThema = (String) themaDropdown.getSelectedItem();
//        if (neuesThema == null) return;
//        aktuellesThema = neuesThema;
//        ladeFragenFuerThema(aktuellesThema);
//        neueFrage();
//    }
//
//    private void styleButton(JButton button) {
//        button.setFocusPainted(false);
//        button.setForeground(Color.DARK_GRAY);
//        button.setBackground(new Color(220, 220, 220));
//        button.setOpaque(true);
//        button.setBorder(BorderFactory.createLineBorder(Color.GRAY));
//        button.setFont(new Font("Arial", Font.PLAIN, 12));
//        button.setPreferredSize(new Dimension(120, 30));
//    }
//}


















