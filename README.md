# safeTO - A Comprehensive Community Safety App for Torontonians

## Deployment

### Backend

Certainly, here is the reworded version:

---

We use Gradle for dependency management. Please make sure that Gradle is installed locally on your machine.

The source code is located in the [backend](backend) directory.

To build the project, run the build script using `./gradlew build` for Unix-based systems or `gradlew.bat build` for Windows. This process will generate a `.war` file (currently named `ROOT.war`; note that this name may change in the future as we work toward a more elegant deployment solution). The `.war` file, which includes the compiled JAR, dependencies, and configurations, can be found in the `backend/app/build/libs` directory.

We recommend using [Tomcat](https://tomcat.apache.org/) as the servlet framework. Download and extract the appropriate Tomcat release for your platform, then place the `.war` file in the `webapps` folder of the extracted Tomcat directory.

To run the application at the root level of the server, you may want to remove the default `ROOT` folder within the `webapps` directory, which contains Tomcat's welcome page. The build script is configured to generate the `.war` file with the name `ROOT.war`, ensuring it deploys at the root level.

Next, navigate to the `bin` directory and run `startup.sh` for Unix-based systems or `startup.bat` for Windows.

The backend application should now be accessible at `http://localhost:8080`. To expose the application to the public web, you can use common reverse-proxy tools like Nginx or Cloudflare Tunnel.

Since the backend API is intended to be exposed directly on the web, it's advisable to familiarize yourself with common Tomcat misconfigurations and avoid them in your production environment.

### Frontend

## Contact

* Joe Fang [`@MinecraftFuns`](https://github.com/MinecraftFuns) <joe.fang@mail.utoronto.ca>
* Minghe Ma [`@mhnwa`](https://github.com/mhnwa) <minghe.ma@mail.utoronto.ca>
* Bilin Nong [`@Bilin22`](https://github.com/Bilin22)
 <bilin.nong@mail.utoronto.ca>
* Yiyun Zhang [`@Yiyun95788`](https://github.com/Yiyun95788) <yvonnezy.zhang@mail.utoronto.ca>
* Liangyu Zhu [`@larryzhuly`](https://github.com/larryzhuly) <liangyu.zhu@mail.utoronto.ca>
