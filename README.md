# Three Musketeers – Turn-Based Strategy Game in Java 🛡️⚔️

A fully playable turn-based strategy game built entirely in Java, based on the classic "Three Musketeers" board game. This project demonstrates advanced object-oriented programming, modular architecture, undo functionality, AI strategies, and automated testing.

---

## 🎯 Features

- ✅ **Fully Playable Game Loop** with Human vs Human and Human vs Computer modes  
- 🔁 **Undo System** implemented using stack data structures  
- 🧠 **AI Agents** using both Random and Greedy strategies with heuristic board evaluation  
- 💾 **Game State Saving** to `.txt` board files for resuming or analyzing gameplay  
- 🧱 **SOLID Principles** applied: SRP, Dependency Inversion, Polymorphism  
- 📐 **MVC-Inspired Architecture** with full separation of concerns  
- 🧪 **JUnit Test Coverage** for game logic, board state, and move validation  
- 📦 Extensible structure for adding new agents or gameplay mechanics  

---

## 🧠 Tech Stack

- **Language:** Java (JDK 16)  
- **IDE:** IntelliJ IDEA  
- **Testing:** JUnit  
- **Design:** OOP, SOLID, MVC, Strategy Pattern  
- **Data Structures:** Stack (for undo), List, Map  

---

## 📂 Project Structure

```

├── boards/
│   ├── Starter.txt
│   ├── GameOver.txt
│   ├── NearEnd.txt
│   ├── BlockedMusketeers.txt
│   ├── \*.txt  ← \[Test boards, saved states, and unit test boards]
│
└── src/
└── assignment1/
├── ThreeMusketeers.java         # Main class (entry point)
├── Board.java                   # Board logic and game state
├── Move.java                    # Move representation
├── Coordinate.java, Cell.java   # Grid and positioning
├── Piece.java, Musketeer.java, Guard.java
├── Agent.java                   # Abstract agent
├── HumanAgent.java              # Player input logic
├── RandomAgent.java             # Basic AI agent
├── GreedyAgent.java             # Heuristic-based AI
├── BoardEvaluator.java, BoardEvaluatorImpl.java
├── Exceptions/
│   └── InvalidMoveException.java
├── Utils.java                   # Helper utilities
└── testing/
└── BoardTest.java           # JUnit test suite

```

---

## 📸 Gameplay Example

```

Initial Board:
A B C D E
1 O O O O X
2 O O O O O
3 O O X O O
4 O O O O O
5 X O O O O

Moves:

* Musketeer captures a guard
* Guard attempts to isolate Musketeers
* Guard wins if Musketeers align in a row/column

````

---

## 🧠 AI Strategy

- **RandomAgent**: Chooses any valid move randomly  
- **GreedyAgent**: Evaluates board score using heuristics and selects the move with the highest evaluation  
- **Evaluation Logic**: Higher score → advantage for Musketeers, lower score → advantage for Guards  

---

## ✅ Testing

Robust unit testing is conducted with **JUnit**, covering:

- Board mechanics (move legality, undo stack, win conditions)  
- AI decision behavior  
- Edge cases and invalid inputs using predefined `.txt` board files  
- Game outcome logic and state transitions  

---

## 🛠️ How to Run

1. **Clone the repository**
```bash
git clone https://github.com/yourusername/three-musketeers-java.git
cd three-musketeers-java
````

2. **Open in IntelliJ IDEA or any Java IDE**

3. **Set the SDK to Java 16**

4. **Run the game**

   * Open `src/assignment1/ThreeMusketeers.java`
   * Run the `main()` method

5. **Select a board file to start**

   * Choose a `.txt` file from the `boards/` folder
   * The first line defines which piece moves first (e.g., `MUSKETEER`)
   * Files can be saved and reused for custom levels or game resumes

6. **In-Game Options**

   * Choose between Human vs Human or Human vs Computer
   * Input moves using the console (e.g., `D5 -> C5`)
   * Use the undo feature, and **save the current board** to a new `.txt` file at any time for analysis or replay

7. **Run Tests**

   * Navigate to `src/assignment1/testing/BoardTest.java`
   * Use IntelliJ or your test runner to execute unit tests

---

## 🚀 Future Improvements

* Add a GUI using JavaFX or Swing
* Smarter AI using Minimax or Monte Carlo Tree Search
* Load/save full match history
* Multiplayer networking support

---

## 👤 Author

**Max Eskandari**

