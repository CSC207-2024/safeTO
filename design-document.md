# Design Document

## Submission

**Team Name:** `Stack Underflow`  
**Submission Date:** August 20, 2024

---

## Overview

Following the completion of Phase 1, our team enhanced the project by focusing on key improvements. We upgraded the user interface design, adopted more reliable data analysis methods, and switched from file-based data storage to a robust database system for better data persistence.

Adhering to our original plan, we developed the website in an organized manner. Through team discussions and iterative revisions, we refined our approach, leading to better solutions and improved project outcomes aligned with Clean Architecture, SOLID Principles, and Design Patterns. This process allowed us to tackle challenges effectively and optimize both functionality and user experience.

---

## Group Members and Contributions

### Joe Fang [`@MinecraftFuns`](https://github.com/MinecraftFuns)

- **Email:** <joe.fang@mail.utoronto.ca>
- **Contributions Since Phase 1:**
  - Create backend API endpoints <sup>[[1]](https://bit.ly/4dtKVAt)</sup> <sup>[[2]](https://bit.ly/4dOdsQS)</sup> for geolocation features
  - Implement a [Key-Value database API](https://github.com/CSC207-2024/safeTO/tree/main/database) using Cloudflare Workers and Cloudflare KV
  - Connect the analysis project to the main backend through [command-line calls](https://bit.ly/3yJPCa8) to the separate JAR file using `ProcessBuilder`
  - Configure the backend server to process HTTP requests
  - Automate the [deployment process](https://bit.ly/3yICguN) for the backend server
- **Significant Pull Request:**
  - [PR #49: feat(database): implement most functionalities](https://github.com/CSC207-2024/safeTO/pull/49)
    - This pull request, together with [PR #48](https://github.com/CSC207-2024/safeTO/pull/48) and [PR #46](https://github.com/CSC207-2024/safeTO/pull/46), completes a [Key-Value database API](https://github.com/CSC207-2024/safeTO/tree/main/database) on the Cloudflare Workers platform using Cloudflare KV. The API lets users organize data into collections and perform create, read, update, and delete operations, while keeping collections separate.
  - [PR #41: feat(backend/SearchResource): Geocoding API](https://github.com/CSC207-2024/safeTO/pull/41)
    - This pull request implements a RESTful API for geocoding (converting a natural language address into a Location object with coordinates, postal codes, and other geolocation attributes) using the Nominatim API provided by OpenStreetMap.

### Minghe Ma [`@mhnwa`](https://github.com/mhnwa)

- **Email:** <minghe.ma@mail.utoronto.ca>
- **Contributions Since Phase 1:**
  - Create interactive panels:
    - Design and implement interactive panels in the user interface for collecting input from users;
    - Ensure the panels are intuitive and user-friendly, providing clear options for user interaction;
  - Connect frontend with backend:
    - Send the validated parameters to the server using HTTP GET method;
    - Display JSON results in the interactive panels;
- **Significant Pull Request:**
  - [PR #69: add panel to show location-based search crime data](https://github.com/CSC207-2024/safeTO/pull/69)
    - When a user searches for an address and double-clicks on the marker, a popup window will appear, allowing the user to select two or three parameters. Based on the chosen parameters, the popup will display relevant crime data.
  - [PR #70: add neighbourhood-based search modal window](https://github.com/CSC207-2024/safeTO/pull/70)
    - When a user clicks on a neighborhood, a popup window will present a bar plot depicting crime data from the past five years. This visualization will include a ranking of crime types, providing insights into the prevalence of different crimes within the selected area.

### Bilin Nong [`@Bilin22`](https://github.com/Bilin22)

- **Email:** <bilin.nong@mail.utoronto.ca>
- **Contributions Since Phase 1:**
  - implement data access and persistence classes that are responsible for:
    - sending http request to fetch data from Toronto Police Service API, and saving responses to local cache.
    - converting data into various formats including `json` and `Table`.
    - analyzing crime data (aggregating, filtering).
    - exporting analysis results in `json` format to [aggregates directory](https://bit.ly/3yD6srg).
  - implement email alert (initial version), and email builder class for building email body text.
  - implement class and interface that are responsible for storing user emails in `safeTO` database.
- **Significant Pull Request:**
  - [PR #65: feat(email): add class to store user email to safeTO db](https://github.com/CSC207-2024/safeTO/pull/65)
    - add email storage class that save user email along with userid.
  - [PR #42: fix: add Email Builder class](https://github.com/CSC207-2024/safeTO/pull/42)
    - add email builder class for building email body text from parameters including `JsonObject`, `String`, and `HashMap`.

### Yiyun Zhang [`@Yiyun95788`](https://github.com/Yiyun95788)

- **Email:** <yvonnezy.zhang@mail.utoronto.ca>
- **Contributions Since Phase 1:**
  - User Login Feature
    - Designed and implemented the backend user login functionality.
    - Updated the `UserService.java` class to generate unique passwords and store user information in a MySQL database
    - Linked user profiles from the frontend to backend storage.
    - Integrated with the Resend API to automatically send a generated password to new users via email.
  - Bar Plot Visualization
    - Performed data extraction and analysis from crime data JSON files using Pandas.
    - Utilized Matplotlib to generate bar plots for neighborhood crime statistics, and successfully integrated these visualizations into the frontend via modal components in `Map.js`.
- **Significant Pull Request:**
  - [PR #126: feat(user login): Implement backend user login & Registration feature and MySQL integration](https://github.com/CSC207-2024/safeTO/commit/c64a01106ce10f14546e93f9f8985cb8b84e6cbc)
  - [PR #38: feat(bar plots): Display crime data bar plots in frontend](https://github.com/CSC207-2024/safeTO/commit/38d40c0cff1ba61415d86f94131579bb11cc989d)
  - [PR #134: feat(api): Add frontend API methods to enhance interaction with backend](https://github.com/CSC207-2024/safeTO/commit/08aa8a0e1e332de043355e1392fa5ed2d947b7d5)

### Liangyu Zhu [`@larryzhuly`](https://github.com/larryzhuly)

- **Email:** <liangyu.zhu@mail.utoronto.ca>
- **Contributions Since Phase 1:**
  1. Encapsulation and Simplification using the Facade Pattern
      - Implementation of CrimeAnalysisFacade
      - Simplification of Demo Classes (e.g., refactored the demo classes AutoTheftSafeCaseDemo to use the CrimeAnalysisFacade)
  1. Switching Output to JSON Format
  1. Adherence to Clean Architecture Principles
      - Use of Interfaces for Dependency Inversion
      - Example: AutoTheftCalculator refactored to implement the CrimeCalculatorInterface

- **Significant Pull Request:**
  - [PR #100: Some key Clean Architecture update](https://github.com/CSC207-2024/safeTO/pull/100)
    - This pull request represents a significant contribution to the team's project as it involved a comprehensive refactor of the codebase to improve maintainability, usability, and integration with the front end. The PR applies the CrimeAnalysisFacade  to each demo, making the demos more representative. The refactor also aligned the code with clean architecture principles by using interfaces to decouple core logic from specific implementations, enhancing testability and scalability.

---

## Additional Features

### Database

[**safeTO-DB**](https://bit.ly/4dNvToM) is a key-value database developed with [**Cloudflare Workers KV**](https://developers.cloudflare.com/kv/). It allows users to organize data into collections and perform basic operations like creating, reading, updating, and deleting items while keeping collections separate from each other.

safeTO-DB can be accessed at <https://csc207-db.joefang.org/>, but you must provide a valid authentication token for it to function. If you need a token, please email us at <contact@csc207.joefang.org>.

#### Key Features

- **Authentication**: All requests to the database require an access token for security. You must include this token in the HTTP header as follows:

  ```yaml
  authorization: Bearer <your_api_key>
  ```

- **Version History**: The database tracks changes made to each key by keeping a version history. When you update a key, a new version is created, while older versions remain accessible with a `_hidden` property that determines their visibility.

#### Core Endpoints

- **GET** `/get/{collection}/{key}`: This endpoint retrieves the most recent visible value for a specific key. It returns a success message (HTTP 200) if found, or an error (HTTP 404) if not.

- **PUT** `/put/{collection}/{key}`: This endpoint adds or updates a value associated with a key in a specific collection. If successful, it returns (HTTP 201); errors return (HTTP 400 or 401) depending on the issue.

- **DELETE** `/delete/{collection}/{key}`: This endpoint marks a key as hidden, making it effectively "invisible." A successful delete returns (HTTP 204).

- **GET** `/history/{collection}/{key}`: This endpoint allows you to see the version history for a specific key. It returns (HTTP 200) with the history details when successful.

- **GET** `/list/{collection}`: This endpoint lists all keys in a collection and supports pagination. A successful request gives (HTTP 200) along with the list of keys.

#### Logging and Security

The database automatically logs each operation for tracking and auditing purposes, noting details like the user, action taken, and the affected keys. Certain key names are restricted from being modified to enhance security.

---

## Summary of Test Coverage

### Frontend

We performed thorough End-to-End Testing of our frontend functionality by systematically evaluating each feature. For the regex functionality, we tested various invalid inputs to ensure robust error handling. For restricted regions, we checked at least 30 locations outside the City of Toronto to verify proper boundary enforcement. For neighborhoods or locations with no available data, we ensured that a clear and informative message is displayed. These tests helped us validate the accuracy and reliability of our frontend components.

### Backend

We performed comprehensive unit tests on our backend codebase with 57 test cases. The test cases cover the main features, including the RESTful API, geolocation-related code, account management, entities, and builder patterns. Below is a screenshot of the test results.

<img src="https://ragnarok.joefang.org/static/x4plqa1569t9pb2d91hvk2gld77cv9d5d.jpg" alt="Test Coverage" height=300px></img>

### Database

We use a local simulated environment that mimics the Cloudflare serverless platform to conduct various tests on the JavaScript code implementing the Key-Value database API. The test code can be found [here](https://bit.ly/46W6uqX).

We mock the actual database with a JavaScript object that supports operations like `get`, `put`, and `list`, allowing us to test our code effectively. Our test cases cover the main operations supported by the database API, including authentication, data retrieval and writing, as well as log retrieval. Additionally, we address edge cases, such as providing an invalid token or using unsupported HTTP methods, to ensure our API handles these scenarios correctly by returning appropriate errors in a RESTful manner.

### Analysis

We performed unit tests on the Analysis module, covering various key components such as crime ranking, break-and-enter analysis, auto theft analysis, and geolocation utilities. Each component was thoroughly tested with a range of scenarios, including edge cases, to ensure accuracy and reliability. All the tests were passed successfully, demonstrating robust performance across all functionalities.

For the **Break-and-Enter Analysis** and **Auto Theft Analysis** feature, we ensured that the system correctly identifies incidents within the specified radius and timeframe(only for auto theft cases). The annual average calculations and Poisson probability estimations are accurate in the tests as well. The management of safe parking locations was also tested, ensuring that safe spots are added, updated, and retrieved accurately.

For the **Crime Ranking** feature, we verified the correctness of neighbourhood rankings based on both total crime data and specific crime types. This included testing the ranking algorithms and ensuring that they produced accurate and consistent results across different datasets.

Finally, our **Geolocation Utilities** were tested to ensure that distance calculations between locations are precise and efficient, which is critical for the accurate determination of nearby incidents and safe spots.
