
# Usecases (data analysis related):
Note: 
- First time running might take 1 minute to generate data( using api). Average time for any further running should be 12 seconds.
- The warning about the logger (SLF4J) can be ignored for this demo. It indicates that no logging framework is bound to SLF4J, defaulting to a no-operation implementation.

----------------------------------------------------------------------------------

# BreakAndEnterModelDemo

## Introduction
The `BreakAndEnterModelDemo` is a designed to analyze and display break and enter crime data within a specified radius around a given location. The purpose of this application is to help users determine the safety of a location based on historical break and enter crime data.

## Features
- Fetch Data: Uses the CrimeAnalysisFacade to fetch and analyze all known break and enter crime data within the specified radius.
- Incident Rate Calculation: Calculates the average annual rate of break and enter incidents in the area.
- Probability Estimation: Employs the Poisson distribution to estimate the probability of break and enter incidents exceeding a user-defined threshold.
- Safety Recommendation: Provides a recommendation based on the calculated probability, helping users decide whether a location is safe to live in.

## Structure
The project consists of the following key files:
- BreakAndEnterModelDemo.java: The main class for running the demo.
- CrimeAnalysisFacade.java: A facade class to simplify crime analysis operations.


## How it Works
1. **Fetch Data**: The application fetches break and enter crime data from a specified source. The fetched data is converted and processed to filter relevant records.
2. **Display Past Year Data**: The application displays all break and enter incidents that occurred in the past year within the specified radius of the given location.
3. **Display All Data**: It also displays all known break and enter incidents within the specified radius regardless of the time of occurrence.
4. **Calculate Probability**: The application calculates the average annual rate of break and enter incidents and uses the Poisson distribution to estimate the probability of incidents exceeding the given threshold.
5. **Provide Recommendation**: Based on the calculated probability, the application provides a recommendation on whether it is safe to live in the specified location.

## Sample Output
The sample output includes:
- A list of break and enter incidents in the past year within the specified radius.
- A list of all known break and enter incidents within the radius.
- The calculated probability of break and enter incidents exceeding the specified threshold.
- A recommendation based on the probability.

----------------------------------------------------------------------------------

# AutoTheftSafeCaseDemo

## Introduction
The `AutoTheftSafeCaseDemo` is designed to analyze and display auto theft crime data within a specified radius around a given location. The purpose of this application is to help users determine the safety of a parking location based on historical auto theft crime data.
In this demo, the location is preset and tested as a safe location. Users can choose to share this location and benefit other users who are currently at a nearby unsafe parking spots,seeking safer spaces to avoid auto thefts.
  
## Features
- Fetch Data: Uses the CrimeAnalysisFacade to fetch and process crime data.
- Fetches all known auto theft crime data within the specified radius. The earliest year of thefts can be chosen by users, from 2014 to 2024.
- Calculates the average annual rate of incidents.
- Uses the Poisson distribution to estimate the probability of auto theft incidents exceeding a specified threshold.
- Provides a recommendation based on the calculated probability.
- Allows the user to add safe parking locations.
- Simulates the addition of 20 random safe locations.

## Structure
The project consists of the following key files:
- AutoTheftSafeCaseDemo.java: The main class for running the demo.
- CrimeAnalysisFacade.java: A facade class to simplify crime analysis operations.
- SafeParkingLocationManager.java: Manages safe parking locations.

## How it Works
1. **Fetch Data**: The application fetches auto theft crime data from a specified source. The fetched data is converted and processed to filter relevant records.
2. **Display Past Year Data**: The application displays all auto theft incidents that occurred in the past year within the specified radius of the given location.
3. **Display All Data**: It also displays all known auto theft incidents within the specified radius regardless of the time of occurrence. The earliest year of thefts can be set by user.
4. **Calculate Probability**: The application calculates the average annual rate of auto theft incidents and uses the Poisson distribution to estimate the probability of incidents exceeding the given threshold.
5. **Provide Recommendation**: Based on the calculated probability, the application provides a recommendation on whether it is safe to park in the specified location.
6. **Add Safe Location**: If the location is deemed safe, the user can add it to the safe parking locations.
7. **Simulate Safe Locations**: The application simulates the addition of 20 random safe locations. This is to create mocking data for the following demo of unsafe case.

## Sample Output
The sample output includes:
- A list of auto theft incidents in the past year within the specified radius.
- A list of all known auto theft incidents within the radius.
- The calculated probability of auto theft incidents exceeding the specified threshold.
- A recommendation based on the probability.


------------------------------------------------------------------------
# AutoTheftUnsafeCaseDemo

## Introduction
The `AutoTheftUnsafeCaseDemo` is designed to analyze and display auto theft crime data within a specified radius around a given location. The application also suggests alternative safe parking spots as the current location is preset to be unsafe based on historical auto theft crime data.

## Features
- Fetch Data: Uses the CrimeAnalysisFacade to fetch and process crime data.
- Fetches all known auto theft crime data within the specified radius. The earliest year of thefts can be chosen by users, from 2014 to 2024.

- Calculates the average annual rate of incidents.
- Uses the Poisson distribution to estimate the probability of auto theft incidents exceeding a specified threshold.
- Provides a recommendation based on the calculated probability.
- Suggests alternative safe parking spots if the current location is unsafe.

## Structure
The project consists of the following key files:

AutoTheftUnsafeCaseDemo.java: The main class for running the demo.
CrimeAnalysisFacade.java: A facade class to simplify crime analysis operations.
SafeParkingLocationManager.java: Manages safe parking locations.

## How it Works
1. **Fetch Data**: The application fetches auto theft crime data from a specified source. The fetched data is converted and processed to filter relevant records.
2. **Display Past Year Data**: The application displays all auto theft incidents that occurred in the past year within the specified radius of the given location.
3. **Display All Data**: It also displays all known auto theft incidents within the specified radius regardless of the time of occurrence. The earliest year of thefts can be set by user.

4. **Calculate Probability**: The application calculates the average annual rate of auto theft incidents and uses the Poisson distribution to estimate the probability of incidents exceeding the given threshold.
5. **Provide Recommendation**: Based on the calculated probability, the application provides a recommendation on whether it is safe to park in the specified location.
6. **Suggest Safe Spots**: If the location is deemed unsafe, the application suggests up to 3 nearby safe parking spots based on previously added safe locations.

## Sample Output
The sample output includes:
- A list of auto theft incidents in the past year within the specified radius.
- A list of all known auto theft incidents within the radius.
- The calculated probability of auto theft incidents exceeding the specified threshold.
- A recommendation based on the probability.
- Suggested safe parking spots if the current location is unsafe.

----------------------------------------------------------------------------------

# NeighborhoodCrimeRankingDemo

## Introduction
The `NeighborhoodCrimeRankingDemo` is designed to rank neighborhoods based on crime data. It allows users to determine the relative safety of a neighborhood by either the total number of crimes or a specific type of crime.

## Features
- Fetches and processes crime data.
- Ranks neighborhoods by the total number of crimes.
- Ranks neighborhoods by a specific type of crime.
- Provides the ranking of a specified neighborhood by total crimes or a specific crime type.

## Structure
The project includes two demos for neighborhood crime ranking:

1.NeighborhoodCrimeRankingDemo1.java:
Allows ranking of neighborhoods based on a specific crime type.
Uses a flexible approach where the user can specify the neighborhood and crime type through command-line arguments.

2.NeighborhoodCrimeRankingDemo2.java:
Focuses on ranking neighborhoods by total crimes.
Uses the CrimeAnalysisFacade to simplify the process of fetching and analyzing crime data.

## How it Works
NeighborhoodCrimeRankingDemo1.java:
- Fetch Data: Uses the CrimeAnalysisFacade to fetch and analyze crime data.
- Rank Neighborhood: Ranks the specified neighborhood by total crimes.
- Rank Neighborhood: Ranks the specified neighborhood based on the provided crime type using CrimeDataRanker.
- If no arguments are provided, the demo uses default values:Neighborhood: "Maple Leaf" Specific Crime: "Assault"
- Output Results: Outputs the ranking and safety level of the neighborhood in JSON format.

NeighborhoodCrimeRankingDemo2.java:
- Fetch Data: Uses the CrimeAnalysisFacade to fetch and analyze crime data.
- Rank Neighborhood: Ranks the specified neighborhood by total crimes.
- If no argument is provided, the demo uses the default neighborhood: "Willowdale East".
- Output Results: Outputs the ranking and safety level of the neighborhood in JSON format.
## Sample Output
The sample output includes:
- The ranking of a neighborhood by total crimes.
- The ranking of a neighborhood by a specific type of crime.
- The safety level of the neighborhood.


----------------------------------------------------------------------------------

## Some UML:

## Data Access Layer

`interface` **InterfaceDataFetcher**

> Methods

- +fetchData `() -> JsonArray`

---

`class` **CrimeDataFetcher** `implements` InterfaceDataFetcher

> Variables

- -BASE_API_URL `String`

> Methods

- +fetchData `() -> JsonArray`

---

`class` **CrimeDataConverter**

> Variables

- -jsonKeys: `String`

> Methods

- -jsonToString `(data: JsonArray) -> StringBuilder`
- -appendRow `(builder: StringBuilder, obj: JSONObject) -> void`
- -changeKeys `(String jsonKeys, String oldKey, String newKey) -> void`
- +jsonToTable `(data: JsonArra) -> Table`
- +tableToJson `(Table table) -> String`
- +changeJsonKeys `(String jsonKeys) -> String`


---

`interface` **Filterable**

> Methods

- +filterBy `(String columnName, String value) -> Table`
- +filterByRange `(String columnName, int startYear, int endYear) -> Table`

---

`interface` **Aggregator**

> Methods

- +aggregate `(String colName, String byFirst) -> Table`
- +aggregate `(String colName, String... byColumns) -> Table`

---

`class` **CrimeDataProcessor** `implements` **Aggregator**, **Filterable**

> Variables

- -df: `Table`

> Methods

- +setTable `(Table df): void`
- +selectColumn `(ArrayList<String> colNames) -> Table`
- +filterBy `(String columnName, String value) -> Table`
- +filterByRange `(String columnName, int startYear, int endYear) -> Table`
- +aggregate `(String colName, String byFirst) -> Table`
- +aggregate `(String colName, String... byColumns) -> Table`
- -getUniqueValues `(String columnName) -> String[]`
- -checkValidColumn `(String colname) -> void`

---
`interface` **Exportable**

> Methods
- +writeToJson `(Table table, String outputPath) -> void`
- +writeToJson `(String jsonString, String outputPath) -> void`
- +exportToSVG `(JFreeChart chart, String outputPath) -> void`


---

`class` **CrimeDataExporter** `implements` **Exportable**

> Variables
- -converter: `CrimeDataConverter`

> Methods

- +writeToJson `(Table table, String outputPath) -> void`
- +writeToJson `(String jsonString, String outputPath) -> void`
- +exportToSVG `(JFreeChart chart, String outputPath) -> void`

---

`class` **CrimeDataPlotter**

> Methods

- +barPlot `(Table table, String xCol, String yCol, String title, String xLabel, String yLabel) -> JFreeChart`
- +linePlot `linePlot(Table table, String xCol, String yCol, String type, String xLab, String yLab, String title) -> JFreeChart`



Here is a well-formatted README for the `BreakAndEnterModelDemo` project in Markdown format that you can use in IntelliJ:

---
