# [safeTO](https://csc207.joefang.org/) - A Comprehensive Community Safety Website for Torontonians

> Check out our [Design Document](https://bit.ly/3YUnvzO) on GitHub!

## Deployment

### Backend

We use Gradle for dependency management. Please make sure that Gradle is installed locally on your machine.

The source code is located in the [backend](backend) directory.

To build the project, run the build script using `./gradlew build` for Unix-based systems or `gradlew.bat build` for Windows. This process will generate a `.war` file (currently named `ROOT.war`; note that this name may change in the future as we work toward a more elegant deployment solution). The `.war` file, which includes the compiled JAR, dependencies, and configurations, can be found in the `backend/app/build/libs` directory.

We recommend using [Tomcat](https://tomcat.apache.org/) as the servlet framework. Download and extract the appropriate Tomcat release for your platform, then place the `.war` file in the `webapps` folder of the extracted Tomcat directory.

To run the application at the root level of the server, you may want to remove the default `ROOT` folder within the `webapps` directory, which contains Tomcat's welcome page. The build script is configured to generate the `.war` file with the name `ROOT.war`, ensuring it deploys at the root level.

Next, navigate to the `bin` directory and run `startup.sh` for Unix-based systems or `startup.bat` for Windows.

The backend application should now be accessible at `http://localhost:8080`. To expose the application to the public web, you can use common reverse-proxy tools like Nginx or Cloudflare Tunnel.

Since the backend API is intended to be exposed directly on the web, it's advisable to familiarize yourself with common Tomcat misconfigurations and avoid them in your production environment.

### Frontend

Execute `npm run build` within the `frontend` folder to build the app for production. In CI environments such as Netlify or Cloudflare Pages, it might be advisable to set `CI=false` since our team is currently working on resolving several warnings. For additional information, take a look at this [Stack Overflow post](https://bit.ly/4fdHRty).

### Data Access and Persistence

Our data access and persistence code is located in [access](analysis/app/src/main/java/access) directory.

We send a request to the Toronto Police Service API to fetch [Major Crime Indicators Data](https://data.torontopolice.on.ca/datasets/0a239a5563a344a3bbf8452504ed8d68_0/explore?location=9.598356%2C-39.819624%2C1.56) in JSON format for the years 2019 to 2024.
The fetched data is saved in the `cache` directory for further analysis. (Note: This directory is included in `.gitignore` to prevent large data files from being committed to the repository.)
This approach minimizes repeated API calls and optimizes the performance of our analysis pipeline.

The analysis results are exported in JSON format to the [resources](backend/app/src/main/resources) directory.

### Analysis

Our usecase demo is available at [analysis](analysis/app/src/main/java/analysis) directory.

1. [Break and Enter](analysis/app/src/main/java/analysis/breakAndEnter)

    * Execute the `BreakAndEnterModelDemo.java` file to see the analysis results for Break and Enter incidents in Toronto within a specified radius.
    You may adjust the `latitude`, `longitude`, `radius`, and `threshold` to customize the search parameter.
        * The results are displayed in the console, showing all known incidents within the specified `radius`.
        * For each incident, the following information is provided:
            * occur date: The date when the incident happened.
            * distance from you: The distance from the specified location to the incident location.
    * Additionally, we calculate the probability that Break and Enter incidents will occur more than the specified `threshold` in the future using a Poisson model.
        Based on this analysis, we provide a suggestion on whether the user is safe to live here.

1. [Car Theft](analysis/app/src/main/java/analysis/carTheft)

    * Execute the `AutoTheftSafeCaseDemo.java` or `AutoTheftUnsafeCaseDemo.java` file to see the analysis results for auto theft incidents in Toronto within a specified radius.
        You may adjust the `latitude`, `longitude`, `radius`,`threshold`, and `earliestYear` to customize the search parameter.
    * The results are displayed in the console, showing all known incidents within the specified `radius`.
    * For each incident, the following information is provided:
        * occur date: The date when the incident happened.
        * distance from you: The distance from the specified location to the incident location.
    * Additionally, we calculate the probability that auto theft incidents will occur more than the specified `threshold` in the future using a Poisson model.
        we also provide a suggestion on whether is safe to park here based on our analysis.

1. [Crime Data Ranking](analysis/app/src/main/java/analysis/crimeDataRanking)

    * Execute the `NeighborhoodCrimeRankingDemo.java` file to see the analysis results for crime data ranking in Toronto.
    * You may interact with the console to input the `neighourhood`, and `sepcific crime type` to get the ranking of
    the `neighbourhood` by total number of `specific crime type` incidents.
    * You may also leave blank for `specific crime type` to get the ranking of the `neighbourhood` by total number of all crime incidents.

## Contact

> Team name: `Stack Underflow`

* Joe Fang [`@MinecraftFuns`](https://github.com/MinecraftFuns) <joe.fang@mail.utoronto.ca>
* Minghe Ma [`@mhnwa`](https://github.com/mhnwa) <minghe.ma@mail.utoronto.ca>
* Bilin Nong [`@Bilin22`](https://github.com/Bilin22)
 <bilin.nong@mail.utoronto.ca>
* Yiyun Zhang [`@Yiyun95788`](https://github.com/Yiyun95788) <yvonnezy.zhang@mail.utoronto.ca>
* Liangyu Zhu [`@larryzhuly`](https://github.com/larryzhuly) <liangyu.zhu@mail.utoronto.ca>

## Some Revisions to Blueprint

1. In the `User.java` class, change `names` from `List<String>` to `firstName`, `lastName`;
2. In the `User.java` class, change `userID` from `long` to `String` in order to accommodate alphanumeric usernames;
3. In the `User.java` class, change `notificationPreferences` from `List<String>` to `Map<String, Boolean>`;
4. In the `User.java` class, change `homeLocation` and the elements of `savedLocations` from `Location` to `SimpleLocation`;
5. In the `User.java` class, change `contacts` from `List<long>` to `List<String>`;
6. In the `Location.java` class, apply a regular expression to Canadian postcodes;
