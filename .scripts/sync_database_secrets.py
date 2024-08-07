import os
import subprocess

import ujson

current_dir = os.path.dirname(os.path.abspath(__file__))
root_dir = os.path.dirname(current_dir)
database_dir = os.path.join(root_dir, "database")
output_path = os.path.join(current_dir, "database_secrets.json")


def deploy():
    with open(output_path, "r", encoding="utf-8") as fin:
        json = ujson.load(fin)

    content = ujson.dumps(json)
    print("len(content) =", len(content))

    os.chdir(database_dir)
    subprocess.run(
        args=("wsl", "npx", "wrangler", "secret", "put", "AUTHORIZED_USERS"),
        input=content.encode(),
    )
    os.chdir(current_dir)


if __name__ == "__main__":
    deploy()
