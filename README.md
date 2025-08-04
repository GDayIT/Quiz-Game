
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
```

---

> [!WARNING]
> ⚠️ [Achtung / Hinweis] 
> Dieses Projekt befindet sich noch in der Entwicklung. Funktionen, Schnittstellen und Datenstrukturen können sich ändern.  
> Bitte verwende die Anwendung mit Vorsicht und melde unerwartetes Verhalten über ein Issue.



## 🚀 Installation & Ausführung

### 📥 Projekt klonen

```bash
git clone https://github.com/dein-nutzername/ProtoQuizz.git

🧩 In Eclipse importieren
Öffne Eclipse.
Gehe zu File → Import....
Wähle Existing Projects into Workspace.
Wähle den geklonten Ordner ProtoQuizz.
Bestätige mit Finish.
▶️ Starten
Öffne die Datei Frame.java im Paket gui.
Führe die Datei aus (Run As → Java Application).
Die grafische Oberfläche startet und du kannst Quizfragen erstellen und verwalten.
```






## 🤝 Mitwirken

Pull Requests sind willkommen!  

Wenn du größere Änderungen vorschlagen möchtest, eröffne bitte zuerst ein Issue, um darüber zu diskutieren.


Wir freuen uns über Beiträge in folgenden Bereichen:
- Neue Features oder Panels
- Verbesserungen der GUI
- Bugfixes und Refactoring
- Dokumentation und Beispiele

---

## 📬 Kontakt

Bei Fragen, Ideen oder Feedback kannst du dich gerne melden:

**D.Georgiou**  
📧 GitHub-Profil  
📧 optional: email.bla@fake.de




