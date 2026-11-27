<p align="center">
  <img src="web/header.png" width="100%" alt="GastosKita - Make Every Peso Count">
</p>

<h1 align="center">GastosKita</h1>

<p align="center">
  <strong>Make Every Peso Count.</strong>
  <br>
  A Student Budgeting and Expense Tracking System
</p>

<p align="center">
  Helping students build better spending habits, manage their budgets, and stay on top of their expenses.
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-Backend-orange?style=flat-square&logo=openjdk&logoColor=white">
  <img src="https://img.shields.io/badge/HTML5-Frontend-E34F26?style=flat-square&logo=html5&logoColor=white">
  <img src="https://img.shields.io/badge/CSS3-Styling-1572B6?style=flat-square&logo=css3&logoColor=white">
  <img src="https://img.shields.io/badge/JavaScript-Interactive-F7DF1E?style=flat-square&logo=javascript&logoColor=black">
  <img src="https://img.shields.io/badge/MySQL-Database-4479A1?style=flat-square&logo=mysql&logoColor=white">
</p>

<p align="center">
  <a href="#-about-gastoskita">About</a> •
  <a href="#-key-features">Features</a> •
  <a href="#-oop-concepts">OOP Concepts</a> •
  <a href="#-technologies-used">Technologies</a> •
  <a href="#-application-showcase">Showcase</a> •
  <a href="#-getting-started">Getting Started</a> •
  <a href="#-meet-the-team">Team</a>
</p>

---

## 📌 About GastosKita

**GastosKita** is a student-focused budgeting and expense tracking application designed to help users become more aware of their spending habits and manage their money more effectively.

The system allows students to record expenses, organize transactions, set budgets, and monitor their spending through an organized and user-friendly interface.

With practical budgeting tools and friendly reminders, GastosKita encourages students to make smarter financial decisions and develop responsible money management habits.

## ✨ Key Features

<details>
<summary><strong>💸 Expense Tracking</strong></summary>

Record daily expenses and organize them into different categories:

- Food
- Transportation
- Shopping
- Entertainment
- Others

</details>

<details>
<summary><strong>📜 Expense History</strong></summary>

View previously recorded expenses and keep track of past transactions.

</details>

<details>
<summary><strong>🎯 Budget Management</strong></summary>

Set a budget and monitor your spending to stay within your planned financial limit.

</details>

<details>
<summary><strong>📅 Expense Calendar</strong></summary>

View expenses according to their recorded dates through a calendar interface.

</details>

<details>
<summary><strong>📊 Spending Breakdown</strong></summary>

Understand where your money goes through a visual breakdown of expenses by category.

</details>

<details>
<summary><strong>💡 Saving Suggestions</strong></summary>

Get helpful suggestions that encourage better spending habits and smarter budgeting decisions.

</details>

<details>
<summary><strong>🔔 Witty Budget Notifications</strong></summary>

Receive friendly and humorous notifications that remind users to stay mindful of their spending.

</details>

<details>
<summary><strong>🌗 Light and Dark Mode</strong></summary>

Switch between light and dark themes for a more comfortable viewing experience.

</details>

## 🧩 OOP Concepts

GastosKita applies Object-Oriented Programming principles throughout its Java backend to organize expense management, promote code reusability, and support category-specific operations.

| **OOP Principle** | **How It Is Applied in GastosKita** |
|---|---|
| **Abstraction** | `Expense` is declared as an `abstract` base class containing shared expense properties (`id`, `amount`, `description`, and `date`) and abstract methods (`getCategoryName()` and `getSavingTip()`) that define the required behaviors for all expense categories. |
| **Encapsulation** | The `Expense` class restricts direct access to its fields using `private` and `final` modifiers where appropriate, providing controlled access through getters and `setId()`. Its constructor also validates expense information before assigning values. |
| **Inheritance** | Specialized expense classes—`FoodExpense`, `TransportationExpense`, `ShoppingExpense`, `EntertainmentExpense`, and `OthersExpense`—extend `Expense`, inheriting its shared properties, methods, and constructor validation through `super()`. |
| **Polymorphism** | The five expense subclasses override `getCategoryName()` and `getSavingTip()` to provide category-specific names and randomized saving suggestions while remaining compatible with the common `Expense` type. |
| **Factory Design Pattern** | `ExpenseFactory` centralizes expense object creation through its static `create()` method, using a `switch` statement to instantiate the appropriate subclass based on the selected category and return it as an `Expense` object. |
| **Exception Handling** | The custom checked exception `InvalidInputException`, which extends Java's `Exception` class, handles invalid expense amounts, empty descriptions, missing dates, and null or unrecognized expense categories by providing specific error messages. |

## 🛠️ Technologies Used

| Technology | Purpose |
|---|---|
| Java | Backend development and OOP implementation |
| HTML5 | Web page structure |
| CSS3 | Interface styling and layout |
| JavaScript | Frontend interactions |
| MySQL | Database management |
| JDBC | Java-to-MySQL connectivity |
| MySQL Connector/J | MySQL JDBC driver |
| XAMPP | Local MySQL database environment |
| phpMyAdmin | Database administration |
| Visual Studio Code | Development environment |
| Git and GitHub | Version control and collaboration |

## 🖥️ Application Showcase

Explore the six main pages of **GastosKita**, each designed to help students track expenses, manage budgets, and develop better financial habits.

<table>
  <tr>
    <td align="center" width="50%">
      <strong>🏠 Dashboard</strong><br><br>
      <img src="screenshots/dashboard.png" width="100%" alt="GastosKita Dashboard"><br>
      <sub>View your monthly budget, total expenses, remaining balance, and budget progress at a glance.</sub>
    </td>
    <td align="center" width="50%">
      <strong>🎯 Set Budget</strong><br><br>
      <img src="screenshots/set-budget.png" width="100%" alt="GastosKita Set Budget"><br>
      <sub>Set your monthly spending limit and monitor your current budget information.</sub>
    </td>
  </tr>
  <tr>
    <td align="center" width="50%">
      <strong>💸 Add Expense</strong><br><br>
      <img src="screenshots/add-expense.png" width="100%" alt="GastosKita Add Expense"><br>
      <sub>Record purchases by entering the amount, category, description, and date.</sub>
    </td>
    <td align="center" width="50%">
      <strong>📜 Expense History</strong><br><br>
      <img src="screenshots/history.png" width="100%" alt="GastosKita Expense History"><br>
      <sub>Review, search, filter, and manage previously recorded transactions.</sub>
    </td>
  </tr>
  <tr>
    <td align="center" width="50%">
      <strong>📅 Calendar</strong><br><br>
      <img src="screenshots/calendar.png" width="100%" alt="GastosKita Calendar"><br>
      <sub>View daily spending and recorded transactions through a calendar interface.</sub>
    </td>
    <td align="center" width="50%">
      <strong>📊 Spending Breakdown</strong><br><br>
      <img src="screenshots/breakdown.png" width="100%" alt="GastosKita Spending Breakdown"><br>
      <sub>Understand spending distribution by category and receive saving suggestions.</sub>
    </td>
  </tr>
</table>

## 📁 Project Structure

```text
GastosKita_Group1/
│
├── database/
│   └── schema.sql
│
├── lib/
│   └── mysql-connector-j-26.7.0.jar
│
├── out/
│   ├── Budget$Status.class
│   ├── Budget.class
│   ├── DBConnection.class
│   ├── EntertainmentExpense.class
│   ├── Expense.class
│   ├── ExpenseCategory.class
│   ├── ExpenseDAO.class
│   ├── ExpenseFactory.class
│   ├── FoodExpense.class
│   ├── GastosKitaServer.class
│   ├── InvalidInputException.class
│   ├── PopupMessages$1.class
│   ├── PopupMessages.class
│   ├── ShoppingExpense.class
│   └── TransportationExpense.class
│
├── src/
│   ├── Budget.java
│   ├── DBConnection.java
│   ├── EntertainmentExpense.java
│   ├── Expense.java
│   ├── ExpenseCategory.java
│   ├── ExpenseDAO.java
│   ├── ExpenseFactory.java
│   ├── FoodExpense.java
│   ├── GastosKitaServer.java
│   ├── InvalidInputException.java
│   ├── OthersExpense.java
│   ├── PopupMessages.java
│   ├── ShoppingExpense.java
│   └── TransportationExpense.java
│
├── web/
│   ├── app.js
│   ├── header.png
│   ├── index.html
│   ├── logo-dark.png
│   ├── logo-icon.png
│   ├── logo.png
│   └── style.css
│
├── screenshots/
│   ├── breakdown.png
│   ├── history.png
│   ├── calendar.png
│   ├── add-expense.png
│   └── set-budget.png
│
└── README.md
```

## 🚀 Getting Started

Follow these instructions to set up and run GastosKita on your local machine.

### Prerequisites

Make sure you have installed:

- [Java Development Kit (JDK)](https://www.oracle.com/java/technologies/downloads/)
- [XAMPP](https://www.apachefriends.org/)
- [Visual Studio Code](https://code.visualstudio.com/)
- [Git](https://git-scm.com/)

### 1. Clone the Repository

Open your terminal and run:

```bash
git clone https://github.com/calchaze/GastosKita_OOP_Project_-Group1.git
```

Navigate to the project directory:

```bash
cd GastosKita_OOP_Project_-Group1
```

### 2. Set Up the Database

1. Open the XAMPP Control Panel.
2. Start **MySQL**.
3. Open phpMyAdmin at `http://localhost/phpmyadmin`.
4. Create the database using the database name configured in `DBConnection.java`.
5. Select the database you created.
6. Open the **Import** tab.
7. Choose the `database/schema.sql` file.
8. Click **Import** or **Go** to execute the SQL file.

### 3. Configure the Database Connection

Open:

```text
src/DBConnection.java
```

Check the database connection settings and make sure these match your local MySQL configuration:

- Database name
- MySQL host
- MySQL port
- Username
- Password

### 4. Compile the Java Files

From the project root directory, open the terminal and run:

```powershell
javac -cp "lib/mysql-connector-j-26.7.0.jar" -d out src/*.java
```

### 5. Run GastosKita

Run the Java server:

```powershell
java -cp "out;lib/mysql-connector-j-26.7.0.jar" GastosKitaServer
```

Once the server starts, follow the local address and port configured in `GastosKitaServer.java` to open the application in your browser.

## 🔮 Future Improvements

GastosKita may be further improved through:

- More detailed spending analytics.
- Additional budgeting tools.
- Improved saving recommendations.
- Enhanced user experience and accessibility.
- Additional features that support responsible financial habits.

## 👥 Meet the Team

GastosKita was developed through the collaboration of the following team members.

| Team Member | Role |
|---|---|
| **Albuñan, Haze Darel L.** | UI/UX Designer & Frontend Developer |
| **Epa, Camille P.** | Project Lead & Docs Architect |
| **Manalo, Steve Nash E.** | Lead Backend & OOP Engineer |

## 🙏 Acknowledgment

This project would not have been possible without the guidance and support of our Object-Oriented Programming instructor at **Batangas State University – The National Engineering University (Alangilan Campus)**, College of Informatics and Computing Sciences (CICS). The knowledge and insights shared throughout the course helped us transform our understanding of Java and Object-Oriented Programming into a functional application. We also extend our appreciation to our classmates and fellow students who shared their suggestions and feedback, helping us make GastosKita more relevant to the everyday financial needs of students.

---

## 🏗️ System Architecture

GastosKita follows a modular architecture that separates the web interface, HTTP server, database operations, and expense management components. This structure helps organize the application's responsibilities and makes the system easier to maintain.

The architecture illustrates how users interact with the web interface, how HTTP requests are handled and routed, and how the backend communicates with the MySQL database to manage expenses, budgets, and spending information.

<p align="center">
  <img src="screenshots/system-architecture.png" width="100%" alt="GastosKita System Architecture">
</p>


## Thank You!

Thank you for exploring **GastosKita**! More than an expense tracker, this project reflects our goal of making budgeting easier and encouraging students to become more mindful of their financial decisions. We hope it inspires better spending habits, one small choice at a time.

We code the system, you make every peso count — proudly built by Team 1 – GastosKita
