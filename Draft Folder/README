## This is the draft folder.
## Please put the modules you designed here, handwritten is fine.

---- Data Access Layer ----

<<interface>> InterfaceDataFetcher
fetchData():JSONArray

CrimeDataFetcher implements InterfaceDataFetcher
- URL: String
-----------------------
+ fetchData():JSONArray

CrimeDataConverter
- jsonKeys: String
-----------------------
- jsonToString(JSONArray data): StringBuilder
- appendRow(StringBuilder builder, JSONObject obj): void
+ jsonToTable(JSONArray data): Table

<<interface>> Filterable
filterBy(String columnName, String value): Table
filterByRange(String columnName, int startYear, int endYear): Table

<<interface>> Aggregator
+ aggregate(String colName, String byFirst): Table
+ aggregate(String colName, String... byColumns): Table

CrimeDataProcessor implements Aggregator, Filterable
- df: Table
-----------------------
+ setTable(Table df): void
+ selectColumn(ArrayList<String> colNames): Table
+ filterBy(String columnName, String value): Table
+ filterByRange(String columnName, int startYear, int endYear): Table
- getUniqueValues(String columnName): String[]
- checkValidColumn(String colname): void
+ aggregate(String colName, String byFirst): Table
+ aggregate(String colName, String... byColumns): Table

<<interface>> DataExporter
exportToCSV(Table df, String filePath): void

CrimeDataExporter implements DataExporter
+ exportToCSV(Table df, String filePath): void

CrimeRateCalculator
+ calculateRates(Table df): Table

