# GlobalBeverageCorporationExchange

GlobalBeverageCorporationExchange project is a Java based stock market simulation for trading in drinks companies.
This project provides a part of the core object model for a limited phase 1 that allows users to calculate various stock-related metrics, record trades, and compute the GBCE (Global Beverage Corporation Exchange) All Share Index.
The project is built using Gradle and follows an object-oriented design.

## Project Structure

- .idea -> Intellij settings
- main/java -> All java classes
- main/test -> All unit tests
- build.gradle -> build config

Entry point: [MyMain.java](src/main/java/com/globalbeveragecorp/exchange/app/MyMain.java)

## Features

- **Dividend Yield Calculation**:
    - Supports both common and preferred stocks.
    - Formula:
        - Common: `Last Dividend / Price`
        - Preferred: `(Fixed Dividend * Par Value) / Price`

- **P/E Ratio Calculation**:
    - Formula: `Price / Dividend`

- **Trade Recording**:
    - Allows recording of trades with timestamp, quantity, buy/sell indicator, and price.

- **Volume Weighted Stock Price (VWSP)**:
    - Calculates VWSP based on trades recorded in the last 5 minutes.
    - Formula: `(Σ Traded Price × Quantity) / Σ Quantity`

- **GBCE All Share Index**:
    - Computes the geometric mean of the VWSP for all stocks.
    - Formula: `n√(P1 × P2 × ... × Pn)`

## Getting Started

### Prerequisites

- Java 17 or higher
- Gradle 7 or higher

### Local Setup
**For IntelliJ:** Open GlobalBeverageCorporationExchange/build.gradle as a new project.

### Building the Project
Build the project using Gradle: ```gradlew build```

### Running the Application
Run the project using: ```gradlew run```

### Running the Tests
To execute the unit tests, use: ```gradlew test```

### Project Details
- Language: Java
- Build Tool: Gradle
- Version: 1.0.0
- Group: com.globalbeveragecorp.exchange

### Contact
For any inquiries, please contact [Sagar Mishra](mailto:sagar.mishra.110890@gmail.com).


