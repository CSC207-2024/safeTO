## Data Access Layer

`interface` **InterfaceDataFetcher**

> Methods

- +fetchData `() -> JSONArray`

---

`class` **CrimeDataFetcher** `implements` InterfaceDataFetcher

> Variables

- -BASE_API_URL `String`

> Methods

- +fetchData `() -> JSONArray`

---

`class` **CrimeDataConverter**

> Variables

- -jsonKeys: `String`

> Methods

- -jsonToString `(data: JSONArray) -> StringBuilder`
- -appendRow `(builder: StringBuilder, obj: JSONObject) -> void`
- -changeKeys `(String jsonKeys, String oldKey, String newKey) -> void`
- +jsonToTable `(data: JSONArray) -> Table`
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

