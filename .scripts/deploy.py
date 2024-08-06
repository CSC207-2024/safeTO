import os
import shutil
import subprocess
from datetime import datetime

current_dir = os.path.dirname(os.path.abspath(__file__))
root_dir = os.path.dirname(current_dir)
backend_dir = os.path.join(root_dir, "backend")
war_path = os.path.join(backend_dir, "app", "build", "libs", "ROOT.war")
tomcat_path = os.getenv("SAFETO_BINARY_PATH") or (
    "%USERPROFILE%/Desktop/apache-tomcat-10.1.26"
    if os.environ.get("COMPUTERNAME") == "ARKG15"  # my laptop
    else "/home/vixen-kite-celery/apache-tomcat-10.1.26"  # GCP server
)


def main():
    # Sync with the codebase on GitHub
    subprocess.run("git", "pull")

    # Check out the main branch
    subprocess.run("git", "checkout", "main")

    # Gradle build
    os.chdir(backend_dir)
    subprocess.run("./gradlew", "build")

    # Copy build result to deploy folder
    shutil.copyfile(war_path, os.path.join(tomcat_path, "webapps"))


if __name__ == "__main__":
    status = None
    message = None
    try:
        main()
        status = "ok"
        message = "updated to the latest version on GitHub; commit hash = {}".format(
            subprocess.check_output(args=("git", "rev-parse", "HEAD")).decode().strip()
        )
    except Exception as err:
        status = "error"
        message = str(err)
    finally:
        with open(
            os.path.join(current_dir, "deploy.log"), "a", encoding="utf-8"
        ) as fout:
            fout.write(f"{datetime.now().isoformat()} [{status}]: {message}")
