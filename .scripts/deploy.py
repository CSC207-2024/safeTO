import os
import shutil
import subprocess
from datetime import datetime

current_dir = os.path.dirname(os.path.abspath(__file__))
root_dir = os.path.dirname(current_dir)
backend_dir = os.path.join(root_dir, "backend")
analysis_dir = os.path.join(root_dir, "analysis")

is_my_laptop = os.getenv("COMPUTERNAME") == "ARKG15"

war_path = os.path.join(backend_dir, "app", "build", "libs", "ROOT.war")
jar_path = os.path.join(analysis_dir, "app", "build", "libs", "analysis.jar")
SAFETO_ANALYSIS_PATH = os.getenv(
    "SAFETO_ANALYSIS_PATH", "/home/vixen-kite-celery/safeto-analysis/analysis.jar"
)
tomcat_path = os.getenv(
    "SAFETO_TOMCAT_PATH",
    (
        "{}\\Desktop\\apache-tomcat-10.1.26".format(os.environ["USERPROFILE"])
        if is_my_laptop
        else "/home/vixen-kite-celery/apache-tomcat-10.1.26"  # GCP server
    ),
)
SAFETO_BINARY_PATH = os.path.join(tomcat_path, "webapps", "ROOT.war")


def main():
    os.chdir(current_dir)

    # Check out the main branch
    subprocess.run(args=("git", "checkout", "main"))

    # Sync with the codebase on GitHub
    subprocess.run(args=("git", "pull"))

    # Gradle build - analysis
    os.chdir(analysis_dir)
    subprocess.run(
        args=(
            "gradlew.bat" if is_my_laptop else "./gradlew",
            "build",
            "--exclude-task",
            "test",
        )
    )
    os.chdir(current_dir)

    # Copy build result to the deploy folder
    shutil.copyfile(jar_path, SAFETO_ANALYSIS_PATH)

    # Gradle build - backend
    os.chdir(backend_dir)
    subprocess.run(
        args=(
            "gradlew.bat" if is_my_laptop else "./gradlew",
            "build",
            "--exclude-task",
            "test",
        )
    )
    os.chdir(current_dir)

    # Copy build result to the deploy folder
    shutil.copyfile(war_path, SAFETO_BINARY_PATH)


if __name__ == "__main__":
    status = None
    message = None
    try:
        main()
        status = "ok"
        message = "Updated to the latest version on GitHub; commit hash = {}".format(
            subprocess.check_output(["git", "rev-parse", "HEAD"]).decode().strip()
        )
    except Exception as err:
        status = "error"
        message = str(err)
    finally:
        print(f"status: {status}")
        print(f"message: {message}")
        with open(
            os.path.join(current_dir, "deploy.log"), "a", encoding="utf-8"
        ) as fout:
            fout.write(f"{datetime.now().isoformat()} [{status}]: {message}\n")
