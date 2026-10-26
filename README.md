# Proiect Java: Breakout Ball

**Autor:** Galea David-Daniel
**Tehnologii folosite:** Java (Swing, AWT), SQLite (JDBC)

---

## 2. Fundamente teoretice și descrierea problemei
Problema principală a constat în crearea unui motor grafic 2D capabil să randeze elemente la 60 de cadre pe secundă și să detecteze coliziunile matematice (intersecții de dreptunghiuri) dintre o minge în mișcare, pereți, o paletă controlată de utilizator și o matrice de cărămizi. De asemenea, a fost necesară o soluție pentru salvarea istoricului utilizatorilor și a scorurilor acestora între sesiunile de joc.

## 3. Soluția propusă și instrumente
Pentru rezolvarea problemei am utilizat biblioteca standard Java Swing/AWT pentru randarea grafică, eliminând necesitatea unor framework-uri grafice externe greoaie. Pentru stocarea datelor, am integrat **SQLite** via JDBC, o bază de date ușoară care rulează local, direct dintr-un fișier, ideală pentru o aplicație desktop de tip joc.

## 4. Prezentarea aplicației

### Arhitectura și Structura
Aplicația este modularizată în 4 clase principale:
* **`Main`**: Punctul de intrare în aplicație. Inițializează baza de date, preia inputul de login de la utilizator și configurează fereastra principală (`JFrame`).
* **`GamePlay`**: Motorul principal al jocului. Implementează `ActionListener` pentru bucla de joc (Timer) și `KeyListener` pentru captarea comenzilor de la tastatură. Gestionează fizica bilei și coliziunile.
* **`MapGenerator`**: Clasa responsabilă de parsarea fișierelor text (`nivel1.txt`, `nivel2.txt`, etc.) și generarea matricelor de cărămizi pe ecran.
* **`DatabaseManager`**: Clasa care comunică cu baza de date SQLite, executând interogări SQL pentru crearea tabelelor, autentificarea utilizatorilor și actualizarea scorurilor și a nivelurilor.

### Managementul datelor și Reprezentarea
* **Hărțile** sunt citite dinamic din fișiere externe de tip text. Această decizie decuplează arhitectura nivelurilor de logica codului.
* **Datele utilizatorilor** (username, scor total, nivelul curent) sunt stocate în tabelul `users` din baza de date `breakout.db`.

### Mod de utilizare
1. La deschidere, utilizatorul introduce un nume (sau alege să joace "Anonim").
2. Jocul pornește la nivelul la care a rămas utilizatorul (sau nivelul 1 pentru jucători noi).
3. **Săgeata Stânga / Dreapta**: Mișcarea paletei pentru a respinge bila.
4. Scopul este spargerea tuturor cărămizilor înainte de expirarea cronometrului de 60 de secunde.
5. **Tasta ENTER**: Folosită pentru a salva progresul și a trece la nivelul următor în caz de victorie, sau pentru a reîncerca în caz de Game Over.

## 5. Concluzii
Aplicația atinge cu succes toate obiectivele funcționale propuse: un gameplay fluid și receptiv, generare dinamică a nivelurilor din fișiere externe de configurare, un nivel progresiv de dificultate și persistența completă a datelor utilizatorilor prin SQLite.

## 6. Bibliografie
* Documentația oficială Oracle Java SE (Swing, AWT, JDBC)
* Documentația SQLite JDBC Driver
