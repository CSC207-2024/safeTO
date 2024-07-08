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
- +jsonToTable `(data: JSONArray) -> Table`
- +tableToJson `(Table table) -> String`

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

`class` **CrimeDataExporter**

> Variables
- -converter: `CrimeDataConverter`

> Methods

- +writeToJson `(Table table, String outputPath) -> void`


