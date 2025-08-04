
# 📘 ProtoQuizz

**ProtoQuizz** ist eine modulare Java-Anwendung zur Erstellung und Verwaltung von Quizfragen. Sie bietet eine intuitive grafische Benutzeroberfläche (GUI) und eine einfache Datenlogik zur Speicherung und Verarbeitung von Fragen.

---

## 🎯 Features
- 🧠 **Quizfragen erstellen**: Benutzer können eigene Fragen mit Antwortmöglichkeiten definieren.
- 🖼️ **Modulare GUI**: Panels zur Frageerstellung, Listenanzeige und Navigation.
- 🗃️ **Business-Logik integriert**: Datenverarbeitung und Speicherung in einer Zwei-Tier-Architektur.
- 📦 **Java Modularisierung**: Verwendung von `module-info.java` zur Strukturierung.

---

## 🛠️ Projektstruktur

```plaintext
ProtoQuizz/
├── src/
│   ├── dbbl/
│   │   ├── DataBusiness.java
│   │   └── QuizFrage.java
│   ├── gui/
│   │   ├── Frame.java
│   │   ├── FormingPanel.java
│   │   ├── CreateQuizfragenPanel.java
│   │   └── QuestionListPanel.java
│   └── module-info.java
├── bin/ (vom Tracking ausgeschlossen)
├── .gitignore
├── .classpath / .project / .settings/
└── README.md
