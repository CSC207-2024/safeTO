# Design Document

## Submission

**Team Name:** `Stack Underflow`  
**Submission Date:** [Insert Date]

---

## Overview

The design document serves as a progress report, summarizing contributions and developments made by each team member since Phase 1.

---

## Group Members and Contributions

### 1. Joe Fang [`@MinecraftFuns`](https://github.com/MinecraftFuns)

- **Email:** <joe.fang@mail.utoronto.ca>
- **Contributions Since Phase 1:**
  - Developed user authentication features.
  - Improved the UI design of the application.
- **Significant Pull Request:**
  - [PR #123: Implement User Login](https://github.com/MinecraftFuns/project/pull/123)
    - This PR demonstrates a significant contribution by adding secure login functionality, enhancing user access control.

### 2. Minghe Ma [`@mhnwa`](https://github.com/mhnwa)

- **Email:** <minghe.ma@mail.utoronto.ca>
- **Contributions Since Phase 1:**
  - Focused on developing backend API endpoints.
  - Optimized database queries for performance improvements.
- **Significant Pull Request:**
  - [PR #124: Create API for User Profiles](https://github.com/mhnwa/project/pull/124)
    - This contribution is crucial as it establishes foundational API integration for user profile management.

### 3. Bilin Nong [`@Bilin22`](https://github.com/Bilin22)

- **Email:** <bilin.nong@mail.utoronto.ca>
- **Contributions Since Phase 1:**
  - Worked on implementing the testing suite for the application.
  - Conducted code reviews and improved code quality.
- **Significant Pull Request:**
  - [PR #125: Add Unit Tests for User Module](https://github.com/Bilin22/project/pull/125)
    - This PR shows a strong contribution by increasing test coverage and ensuring the user module functions correctly.

### 4. Yiyun Zhang [`@Yiyun95788`](https://github.com/Yiyun95788)

- **Email:** <yvonnezy.zhang@mail.utoronto.ca>
- **Contributions Since Phase 1:**
  - Created documentation for user features.
  - Enhanced the application’s accessibility options.
- **Significant Pull Request:**
  - [PR #126: Improve Accessibility Features](https://github.com/Yiyun95788/project/pull/126)
    - This contribution is significant as it ensures the application is usable for a wider audience, including people with disabilities.

### 5. Liangyu Zhu [`@larryzhuly`](https://github.com/larryzhuly)

- **Email:** <liangyu.zhu@mail.utoronto.ca>
- **Contributions Since Phase 1:**
  - Developed additional features for data export.
  - Assisted in frontend development activities.
- **Significant Pull Request:**
  - [PR #127: Implement Data Export Functionality](https://github.com/larryzhuly/project/pull/127)
    - This PR is a key contribution as it enables users to export their data conveniently, enhancing user experience.

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

- Comprehensive tests have been added across all modules, achieving an overall coverage of 85%.
- Significant emphasis placed on edge cases within user authentication and data handling modules.

---

## Submission Instructions

This document is to be submitted on Quercus. Our TA will also review the code in our GitHub repository and the accompanying git logs.

---

### End of Document

---

Feel free to add or modify sections as necessary to fit your team’s needs!
