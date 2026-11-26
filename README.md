# 💸 GastosKita
### "Track your gastos, take control of your budget."

**A Java-Based Personal Budgeting and Expense-Tracking System**

<p align="center">
  <b>Manage your money. Understand your spending. Build better habits.</b>
</p>

---

## 📑 Table of Contents

- [Project Overview](#-project-overview)
- [Project Objectives](#-project-objectives)
- [System Features](#-system-features)
- [OOP Principles Applied](#-oop-principles-applied)
- [Technology Stack](#-technology-stack)
- [Project Structure](#-project-structure)
- [Database and System Architecture](#-database-and-system-architecture)
- [How to Run the Project](#-how-to-run-the-project)
- [Screenshots and System Preview](#-screenshots-and-system-preview)
- [Known Limitations and Future Improvements](#-known-limitations-and-future-improvements)
- [Target Users](#-target-users)
- [Team Members](#-team-members)
- [Acknowledgment](#-acknowledgment)

---

## 🎯 Project Overview

**GastosKita** is a Java-based budgeting and expense-tracking system designed to help students and individuals manage their personal finances more effectively. It provides a convenient way to record expenses, organize transactions, monitor spending habits, and make better financial decisions.

### The Problem We Solve

Managing money can be challenging, especially for students who rely on a limited daily or monthly allowance. Without proper expense tracking, it becomes difficult to identify unnecessary spending, maintain a budget, and set aside money for savings.

### Why GastosKita?

GastosKita aims to make financial management more accessible and engaging. Instead of manually keeping track of every purchase, users can use the system to monitor their expenses, understand their spending patterns, and receive helpful saving suggestions.

What makes GastosKita different is its combination of practical financial tools and witty, relatable budget reminders that encourage users to become more responsible with their money.

---

## ✨ System Features

| Feature | Description |
|---|---|
| 🧾 **Expense Tracking** | Allows users to record and monitor their daily expenses. |
| 📜 **Expense History** | Provides access to previously recorded transactions to help users track their spending activities. |
| 💡 **Saving Suggestions** | Provides recommendations on where users can reduce unnecessary expenses. |
| 🗂️ **Expense Categories** | Organizes transactions into categories such as Food, Transportation, Shopping, and Entertainment. |
| 📊 **Spending Breakdown** | Presents an overview of expenses to help users identify their biggest spending categories. |
| 😆 **Witty Budget Popup Messages** | Displays funny, relatable, and encouraging messages based on certain budget situations. |

### 💬 A Little Humor Goes a Long Way

GastosKita makes budgeting less stressful through witty reminders that encourage users to think twice before spending.

Examples of budget reminders:

- "Sis, set na budget para di ka mag-overspend!"
- "A budget a day keeps the 'asan ang pera ko?' away."
- "I did my best, but I guess my best wasn't good enough... pati budget ko."
- "Wallet check: mas marami pang hangin kaysa pera."

---

## 🧱 OOP Principles Applied

GastosKita is developed using Java and follows an Object-Oriented Programming approach. The following principles serve as the foundation for organizing the system's data and functionality.

| OOP Principle | Application in GastosKita |
|---|---|
| **Encapsulation** | Organizes and protects data related to users, income, expenses, and budgets through controlled access to class attributes and methods. |
| **Abstraction** | Focuses on essential operations such as recording expenses, managing budgets, and viewing spending information while hiding unnecessary implementation details. |
| **Inheritance** | Allows related classes to share common attributes and behaviors when applicable, reducing repeated code. |
| **Polymorphism** | Allows related objects to implement or override behaviors differently while maintaining a common interface. |

*Note: The specific Java classes and methods demonstrating each principle should be identified from the finalized source code before submission.*

---

## 🛠️ Technology Stack

| Technology | Purpose |
|---|---|
| **Java** | Main programming language used for system development and application logic. |
| **HTML** | Structures the web interface. |
| **CSS** | Handles the visual presentation and styling of the interface. |
| **JavaScript** | Supports frontend interactions and functionality. |
| **Database** | Stores and manages system-related information. |
| **Git & GitHub** | Used for version control, source code management, and team collaboration. |

*The exact database engine, Java libraries, and frameworks should be specified according to the project's actual configuration.*

---

## 📂 Project Structure

The repository is organized into several directories that contain the project's source code, database resources, libraries, compiled files, and web application components.

```text
GastosKita_OOP_Project_-Group1/
│
├── database/       # Database-related files
│
├── lib/            # External libraries and dependencies
│
├── out/            # Compiled output files
│
├── src/            # Java source code
│
├── web/            # Web interface and frontend resources
│
├── t-backup/       # Backup files
│
└── README.md       # Project documentation
```

---

## 🗄️ Database and System Architecture

GastosKita includes a dedicated `database/` directory for its database-related resources and separates its Java source code from its web interface.

The system is intended to support the management of financial information, including income, expenses, categories, and budget-related records.

**System Architecture**

```text
             USER
               |
               v
        WEB INTERFACE
               |
               v
        JAVA APPLICATION
               |
       +-------+-------+
       |       |       |
       v       v       v
    Income  Expenses  Budget
       |       |       |
       +-------+-------+
               |
               v
           DATABASE
               |
               v
       FINANCIAL RECORDS
```

*Conceptual system overview. Update this diagram to reflect the actual application flow and database integration.*

---

## 🚀 How to Run the Project

### Prerequisites

Before running GastosKita, prepare the following:

- Java Development Kit (JDK)
- Java-compatible IDE or development environment
- Git
- Required project libraries
- Configured database environment, if required

### 1. Clone the Repository

```bash
git clone https://github.com/calchaze/GastosKita_OOP_Project_-Group1.git
```

### 2. Open the Project

Open the cloned `GastosKita_OOP_Project_-Group1` folder using your preferred Java IDE.

### 3. Configure Dependencies

Ensure that the required libraries and database configuration are properly set up.

### 4. Run the Application

Locate the main Java entry point inside the `src/` directory and execute it using your IDE.

*The exact main class, database setup commands, and execution procedure should be added after verifying the project's configuration.*

---

## 📸 Screenshots and System Preview

This section showcases the actual interface and functionality of GastosKita.

### Application Interface

**Dashboard**

> Add your dashboard screenshot here.

### Expense Management

**Add Expenses**

> Add your expense-entry screenshot here.

**Expense History**

> Add your transaction-history screenshot here.

### Budget and Financial Insights

**Spending Breakdown**

> Add your spending breakdown screenshot here.

**Saving Suggestions**

> Add your saving-suggestion screenshot here.

**Witty Budget Popup Messages**

> Add your budget reminder screenshot here.

---

## ⚠️ Known Limitations and Future Improvements

### Known Limitations

- Users need to consistently record their expenses to maintain accurate spending information.
- Saving suggestions depend on the financial information available in the system.
- Budget monitoring is limited to the features currently implemented.
- The system is primarily designed for personal budgeting and expense monitoring.

### Future Improvements

- Add more detailed financial reports and monthly summaries.
- Introduce customizable savings goals.
- Improve budget notifications and spending alerts.
- Add additional expense categories and filtering options.
- Enhance the user interface for a more accessible experience.
- Expand financial insights to help users make better budgeting decisions.

---

## 👥 Target Users

| Target User | Purpose |
|---|---|
| **Students** | Helps manage daily allowances, monitor expenses, and develop better saving habits. |
| **Individuals** | Helps organize personal finances, control spending, and maintain budgets. |

---

## 👩‍💻 Team Members

### Group 1 – GastosKita

| Member | Role | Contributions |
|---|---|---|
| Haze | UI/UX Designer | Designed the overall user interface and visual layout of the system. |
| Steve | Backend Developer | Handled the core backend development and implemented the system's primary logic. |
| Camille | [Add Role] | [Add actual contributions.] |

*Update the team list and contributions to reflect the final division of responsibilities.*

---

## 🙏 Acknowledgment

We would like to express our sincere gratitude to our Object-Oriented Programming instructor for the guidance, knowledge, and support provided throughout the development of GastosKita.

We also appreciate the effort and collaboration of every group member who contributed their time, skills, and ideas to make this project possible.

---

## 💖 Thank You!

Thank you for visiting **GastosKita!**

We hope this project encourages students and individuals to become more mindful of their spending, develop responsible financial habits, and understand the importance of managing their money wisely.

**Remember: Every peso counts, so spend wisely!**

Made with ☕ and Java by **Group 1 – GastosKita**.
