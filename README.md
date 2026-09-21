<h1 align="center">🧾 Smart Bill Splitter</h1>

<p align="center">
  <strong>An intelligent expense calculation and bill-splitting platform developed in Java, leveraging <code>BigDecimal</code> monetary precision, item-wise sharing, and an interactive AWT graphical interface.</strong>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Language-Java%2017+-orange?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java" />
  <img src="https://img.shields.io/badge/GUI-Abstract_Window_Toolkit_(AWT)-007396?style=for-the-badge&logo=java" alt="AWT" />
  <img src="https://img.shields.io/badge/Build-Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white" alt="Maven" />
  <img src="https://img.shields.io/badge/Tests-JUnit_5-25A162?style=for-the-badge&logo=junit5&logoColor=white" alt="JUnit 5" />
  <a href="LICENSE"><img src="https://img.shields.io/badge/License-MIT-blue.svg?style=for-the-badge" alt="License: MIT" /></a>
</p>

---

## 🎓 Academic Background

This software was engineered as part of the **Java Programming (EGB1201)** curriculum:
- **Author**: **Albar Rahman A**
- **Register Number**: 2403811710621004
- **Department**: Electronics and Communication Engineering
- **Institution**: K. Ramakrishnan College of Technology (Autonomous), Samayapuram, Trichy

---

## 🌟 Key Features

| Feature | Description |
| :--- | :--- |
| 🍕 **Item-Wise Splitting** | Granular expense allocation allowing each dish or expense item to be shared across any arbitrary subset of participants (e.g. `Pizza,450,Alice|Bob,Alice`). |
| 💰 **Financial-Grade Precision** | Eliminates floating-point rounding errors by utilizing Java's `BigDecimal` with `RoundingMode.HALF_UP` across all divisions and multiplications. |
| 🧾 **Proportional Tax & Tip** | Automatically distributes sales tax and gratuity strictly in proportion to each person's individual subtotal consumption. |
| ⚖️ **Net Debt Settlement** | Automatically reconciles the difference between what each participant owes and what they paid up front, yielding a clean ledger of who receives or owes funds. |
| 🖥️ **Lightweight AWT GUI** | Fast, cross-platform graphical desktop interface with zero third-party UI dependencies. |
| 🧪 **Automated Unit Testing** | Includes automated JUnit 5 test suites validating rounding stability, multiple payers, and settlement arithmetic. |

---

## 📊 Mathematical Formulation

The system guarantees fair cost distribution using the following equations:

1. **Individual Item Share**:
   For any item $i$ with price $P_i$ shared equally among a set of sharers $S_i$:
   $$
   \text{Share}_{i} = \frac{P_i}{|S_i|}
   $$

2. **Personal Subtotal**:
   For participant $u$, their subtotal is the sum of shares for items they participated in:
   $$
   \text{Subtotal}_u = \sum_{i \in \text{Items}_u} \text{Share}_{i}
   $$

3. **Proportional Tax & Tip**:
   Given tax rate $T$ and tip rate $G$ as percentages:
   $$
   \text{Tax}_u = \text{Subtotal}_u \times \frac{T}{100}, \quad \text{Tip}_u = \text{Subtotal}_u \times \frac{G}{100}
   $$

4. **Total Liability & Net Balance**:
   $$
   \text{Total}_u = \text{Subtotal}_u + \text{Tax}_u + \text{Tip}_u
   $$
   $$
   \text{Net Balance}_u = \text{Total}_u - \text{Paid}_u
   $$
   *(Negative Net Balance indicates the participant is **owed** a reimbursement; Positive indicates the participant **must pay**).*

---

## 🖥 Application Interface

The interactive AWT interface allows intuitive data entry and produces instant tabular summaries:

```text
+---------------------------------------------------------------------------------+
| Smart Bill Splitter (AWT)                                            [_][X]     |
+---------------------------------------------------------------------------------+
| People Count: [ 3 ]   Items Count: [ 2 ]   Tax %: [ 5.0 ]   Tip %: [ 10.0 ]     |
+---------------------------------------------------------------------------------+
| Enter item data in this format:                                                 |
| ItemName,Price,Sharer1|Sharer2|Sharer3,Payer                                    |
|                                                                                 |
| Pizza,450,Alice|Bob,Alice                                                       |
| Burger,200,Bob|Charlie,Bob                                                      |
+---------------------------------------------------------------------------------+
| ===== BILL SUMMARY =====                                                        |
| Name             Subtotal        Tax        Tip       Paid        Net           |
| Alice              225.00      11.25      22.50     450.00    -191.25           |
| Bob                325.00      16.25      32.50     200.00     173.75           |
| Charlie            100.00       5.00      10.00       0.00     115.00           |
+---------------------------------------------------------------------------------+
|                             [ Calculate ]   [ Clear ]                           |
+---------------------------------------------------------------------------------+
```

---

## 📁 Project Structure

```text
smart-bill-splitter/
├── pom.xml                                      # Maven build descriptor
├── build.bat                                    # Windows compilation script
├── run.bat                                      # Windows application launcher
├── .gitignore                                   # Standard Java & IDE ignore rules
├── LICENSE                                      # MIT Open Source License (Albar Rahman A)
├── CODE_OF_CONDUCT.md                           # Contributor Covenant Code of Conduct
├── CONTRIBUTING.md                              # Open source contribution guidelines
├── README.md                                    # Comprehensive project documentation
├── SECURITY.md                                  # Vulnerability reporting policy
│
└── src/
    ├── main/
    │   └── java/
    │       └── com/
    │           └── smartbillsplitter/
    │               ├── Main.java                # Universal entry point
    │               ├── SmartBillSplitterAWT.java # AWT Graphical User Interface
    │               ├── model/
    │               │   ├── Item.java            # Expense item domain entity
    │               │   └── PersonResult.java    # Calculation result breakdown
    │               └── service/
    │                   └── BillSplitterService.java # High-precision calculation engine
    │
    └── test/
        └── java/
            └── com/
                └── smartbillsplitter/
                    └── BillSplitterServiceTest.java # JUnit 5 test suite
```

---

## 🚀 Getting Started

### Prerequisites
- [Java Development Kit (JDK) 17+](https://adoptium.net/)
- [Apache Maven](https://maven.apache.org/) (optional, if using Maven)

### Building the Project

#### Option A: Using Maven
```bash
# Compile and run test suite
mvn clean test

# Package into executable JAR
mvn package
```

#### Option B: Using Windows Scripts
```cmd
# Compile source files into 'bin/'
build.bat

# Launch the AWT Graphical Interface
run.bat
```

#### Option C: Direct Java Command
```bash
# Compile
javac -encoding UTF-8 -d bin src/main/java/com/smartbillsplitter/model/*.java src/main/java/com/smartbillsplitter/service/*.java src/main/java/com/smartbillsplitter/*.java

# Run
java -cp bin com.smartbillsplitter.SmartBillSplitterAWT
```

---

## 🧪 Running Tests

Execute the automated JUnit test suite via Maven:

```bash
mvn test
```

---

## 🤝 Contributing

Contributions, issues, and feature requests are warmly welcomed! Please check [CONTRIBUTING.md](CONTRIBUTING.md) for full details on our branch workflow and submission process.

---

## 📜 License

This project is licensed under the **MIT License** - see the [LICENSE](LICENSE) file for details.

---

<p align="center">
  Crafted with ❤️ by <a href="https://github.com/albar-rahman"><strong>Albar Rahman A</strong></a>
</p>