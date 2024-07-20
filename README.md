# safeTO - A Comprehensive Community Safety App for Torontonians

## Deployment

### Backend

We use Gradle for dependency management. Please ensure that Gradle is installed locally on your machine.

You can find the code in the [backend](backend) directory.

To build the project, run the build script (`./gradlew build` for Unix-based systems or `gradlew.bat build` for Windows). This will generate a `.war` file, which includes the compiled JAR, dependencies, and configurations, located in the `backend/app/build/libs` directory.

We recommend using [Tomcat](https://tomcat.apache.org/) as the servlet framework. Download and extract the appropriate Tomcat release for your platform, then place the `.war` file in the `webapps` folder of the extracted Tomcat directory. Navigate to the `bin` directory and execute `startup.sh` for Unix-based systems or `startup.bat` for Windows.

The backend application should now be accessible at `http://localhost:8080`. The specific path will depend on the name of the `.war` file.

### Frontend

## Contact

* Joe Fang [`@MinecraftFuns`](https://github.com/MinecraftFuns) <joe.fang@mail.utoronto.ca>
* Minghe Ma [`@mhnwa`](https://github.com/mhnwa) <minghe.ma@mail.utoronto.ca>
* Bilin Nong [`@Bilin22`](https://github.com/Bilin22)
 <bilin.nong@mail.utoronto.ca>
* Yiyun Zhang [`@Yiyun95788`](https://github.com/Yiyun95788) <yvonnezy.zhang@mail.utoronto.ca>
* Liangyu Zhu [`@larryzhuly`](https://github.com/larryzhuly) <liangyu.zhu@mail.utoronto.ca>
