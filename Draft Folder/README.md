# This is the draft folder

> Please put the modules you designed here, handwritten is fine

Format:

`$type` **$class-name** `implements` $interface-0, $interface-1, ...

> Variables

- a: `Integer`
- b: `Character`

> Methods

- func_0: `(param_0: int, param_1: char) -> int` (This is a normal method with two parameters and it returns an int)
- func_1: `() -> int` (This is a method without parameters)
- func_2: `(param_0: int) -> void` (This is a method that returns nothing)

## Data Access Layer

`interface` **InterfaceDataFetcher**

> Variables

N/A

> Methods

- fetchData: `() -> JSONArray`

---

`class` **CrimeDataFetcher** `implements` InterfaceDataFetcher

> Variables

- URL: `String`

> Methods

- fetchData: `() -> JSONArray`

---

`class` **CrimeDataConverter**

> Variables

- jsonKeys: `String`

> Methods

- jsonToString: `(data: JSONArray) -> StringBuilder`
- appendRow: `(builder: StringBuilder, obj: JSONObject) -> void`

- jsonToTable: `(data: JSONArray) -> Table`

---

`interface` **Filterable**

- filterBy(String columnName, String value): Table
- filterByRange(String columnName, int startYear, int endYear): Table

`interface` **Aggregator**

- aggregate(String colName, String byFirst): Table
- aggregate(String colName, String... byColumns): Table

`class` **CrimeDataProcessor** `implements` Aggregator, Filterable

- df: Table

-----------------------

- setTable(Table df): void
- selectColumn(ArrayList<String> colNames): Table
- filterBy(String columnName, String value): Table
- filterByRange(String columnName, int startYear, int endYear): Table

- getUniqueValues(String columnName): String[]
- checkValidColumn(String colname): void

- aggregate(String colName, String byFirst): Table

- aggregate(String colName, String... byColumns): Table

`interface` **DataExporter**

exportToCSV(Table df, String filePath): void

`class` **CrimeDataExporter** `implements` DataExporter

- exportToCSV(Table df, String filePath): void

`class` **CrimeRateCalculator**

- calculateRates(Table df): Table
