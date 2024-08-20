# Design Document

## Submission

**Team Name:** `Stack Underflow`  
**Submission Date:** August 20, 2024

---

## Overview

*Following the completion of Phase 1, our team continued to advance the project by enhancing several key aspects. We implemented a more sophisticated user interface design, adopted more reliable and effective data analysis methods, and transitioned from file-based data storage to a robust database system for improved data persistence. 
Adhering closely to our initial blueprint, we built the website in a coherent and structured manner. Through collaborative discussions and iterative revisions, we refined our approach, which led to better solutions and improved project outcomes, aligning well with Clean Architecture, SOLID Principles and Design Patterns. This ongoing development process allowed us to address challenges effectively and optimize the project's functionality and user experience.*

---

## Group Members and Contributions

### 1. Joe Fang [`@MinecraftFuns`](https://github.com/MinecraftFuns)

- **Email:** <joe.fang@mail.utoronto.ca>
- **Contributions Since Phase 1:**
  - Create various backend API endpoints for geolocation features
  - Implement a Key-Value database API using Cloudflare Workers and Cloudflare KV
  - Configure the backend server and production environment to process HTTP requests
  - Connect the analysis project to the main backend through command-line calls to the separate JAR file using ProcessBuilder
- **Significant Pull Request:**
  - [PR #123: *placeholder*](https://github.com/MinecraftFuns/project/pull/123)
    - *placeholder*

### 2. Minghe Ma [`@mhnwa`](https://github.com/mhnwa)

- **Email:** <minghe.ma@mail.utoronto.ca>
- **Contributions Since Phase 1:**
  - *Create interactive panels:*
    - *Design and implement interactive panels in the user interface for collecting input from users;*
    - *Ensure the panels are intuitive and user-friendly, providing clear options for user interaction;*
  - *Connect frontend with backend:*
    - *Send the validated parameters to the server using HTTP GET method;*
    - *Display JSON results in the interactive panels;*
- **Significant Pull Request:**
  - [PR #69: *add panel to show location-based search crime data*](https://github.com/CSC207-2024/safeTO/pull/69)
    - *When a user searches for an address and double-clicks on the marker, a popup window will appear, allowing the user to select two or three parameters. Based on the chosen parameters, the popup will display relevant crime data.*
  - [PR #70: *add neighbourhood-based search modal window*](https://github.com/CSC207-2024/safeTO/pull/70)
    - *When a user clicks on a neighborhood, a popup window will present a bar plot depicting crime data from the past five years. This visualization will include a ranking of crime types, providing insights into the prevalence of different crimes within the selected area.*

### 3. Bilin Nong [`@Bilin22`](https://github.com/Bilin22)

- **Email:** <bilin.nong@mail.utoronto.ca>
- **Contributions Since Phase 1:**
  - *implement data access and persistence classes that are responsible for:*
    - *sending http request to fetch data from Toronto Police Service API, and saving responses to local cache.*
    - *converting data into various formats including `json` and `Table`.*
    - *analyzing crime data (aggregating, filtering).*
    - *exporting analysis results in `json` format to [aggregates directory](backend/app/src/main/resources/aggregates).*
  - *implement email alert (initial version), and email builder class for building email body text.*
  - *implement class and interface that are responsible for storing user emails in `safeTO` database.*
- **Significant Pull Request:**
  - [PR #65: *feat(email): add class to store user email to safeTO db*](https://github.com/CSC207-2024/safeTO/pull/65)
    - *add email storage class that save user email along with userid.*
  - [PR #42: *fix: add Email Builder class*](https://github.com/CSC207-2024/safeTO/pull/42)
    - *add email builder class for building email body text from parameters including `JsonObject`, `String`, and `HashMap`.*

### 4. Yiyun Zhang [`@Yiyun95788`](https://github.com/Yiyun95788)

- **Email:** <yvonnezy.zhang@mail.utoronto.ca>
- **Contributions Since Phase 1:**
  - *User Login Feature*
    - *Designed and implemented the backend user login functionality.*
    - *Updated the `UserService.java` class to generate unique passwords and store user information in a MySQL database*
    - *Linked user profiles from the frontend to backend storage.*
    - *Integrated with the Resend API to automatically send a generated password to new users via email.*
  - *Bar Plot Visualization*
    - *Performed data extraction and analysis from crime data JSON files using Pandas.*
    - *Utilized Matplotlib to generate bar plots for neighborhood crime statistics, and successfully integrated these visualizations into the frontend via modal components in `Map.js`.*
- **Significant Pull Request:**
  - [PR #126: *feat(user login): Implement backend user login & Registration feature and MySQL integration*](https://github.com/CSC207-2024/safeTO/commit/c64a01106ce10f14546e93f9f8985cb8b84e6cbc)
  - [PR #38: *feat(bar plots): Display crime data bar plots in frontend*](https://github.com/CSC207-2024/safeTO/commit/38d40c0cff1ba61415d86f94131579bb11cc989d)
  - [PR #134: *feat(api): Add frontend API methods to enhance interaction with backend*](https://github.com/CSC207-2024/safeTO/commit/08aa8a0e1e332de043355e1392fa5ed2d947b7d5)

### 5. Liangyu Zhu [`@larryzhuly`](https://github.com/larryzhuly)

- **Email:** <liangyu.zhu@mail.utoronto.ca>
- **Contributions Since Phase 1:**
  1. Encapsulation and Simplification using the Facade Pattern
    - Implementation of CrimeAnalysisFacade
    - Simplification of Demo Classes (e.g., refactored the demo classes AutoTheftSafeCaseDemo to use the CrimeAnalysisFacade)
      
  2. Switching Output to JSON Format
     
  3. Adherence to Clean Architecture Principles
    - Use of Interfaces for Dependency Inversion
    - Example: AutoTheftCalculator refactored to implement the CrimeCalculatorInterface
      
- **Significant Pull Request:**
  - [PR #100: *Some key Clean Architecture update*](https://github.com/CSC207-2024/safeTO/pull/100)
    - This pull request represents a significant contribution to the team's project as it involved a comprehensive refactor of the codebase to improve maintainability, usability, and integration with the front end. The PR applies the CrimeAnalysisFacade  to each demo, making the demos more representative. The refactor also aligned the code with clean architecture principles by using interfaces to decouple core logic from specific implementations, enhancing testability and scalability.


---

## Additional Features

### Database

[**safeTO-DB**](database/README.md) is a key-value database developed with [**Cloudflare Workers KV**](https://developers.cloudflare.com/kv/). It allows users to organize data into collections and perform basic operations like creating, reading, updating, and deleting items while keeping collections separate from each other.

safeTO-DB can be accessed at <https://csc207-db.joefang.org/>, but you must provide a valid authentication token for it to function. If you need a token, please email us at <contact@csc207.joefang.org>.

#### Key Features

- **Authentication**: All requests to the database require an access token for security. You must include this token in the HTTP header as follows:

  ```yaml
  authorization: Bearer <your_api_key>
  ```

- **Version History**: The database tracks changes made to each key by keeping a version history. When you update a key, a new version is created, while older versions remain accessible with a `_hidden` property that determines their visibility.

### Core Endpoints

- **GET** `/get/{collection}/{key}`: This endpoint retrieves the most recent visible value for a specific key. It returns a success message (HTTP 200) if found, or an error (HTTP 404) if not.

- **PUT** `/put/{collection}/{key}`: This endpoint adds or updates a value associated with a key in a specific collection. If successful, it returns (HTTP 201); errors return (HTTP 400 or 401) depending on the issue.

- **DELETE** `/delete/{collection}/{key}`: This endpoint marks a key as hidden, making it effectively "invisible." A successful delete returns (HTTP 204).

- **GET** `/history/{collection}/{key}`: This endpoint allows you to see the version history for a specific key. It returns (HTTP 200) with the history details when successful.

- **GET** `/list/{collection}`: This endpoint lists all keys in a collection and supports pagination. A successful request gives (HTTP 200) along with the list of keys.

### Logging and Security

The database automatically logs each operation for tracking and auditing purposes, noting details like the user, action taken, and the affected keys. Certain key names are restricted from being modified to enhance security.

---

## Summary of Test Coverage

- *Frontend: We conducted thorough an End-to-End Testing of our frontend functionality by systematically evaluating each feature. For the regex functionality, we tested various invalid inputs to ensure robust error handling. In the case of restricted regions, we checked at least 30 locations outside the City of Toronto to verify proper boundary enforcement. For neighborhoods or locations with no available data, we ensured that a clear and informative message is generated. These tests helped us validate the accuracy and reliability of our frontend components.*


- *Backend:*
