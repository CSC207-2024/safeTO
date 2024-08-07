import base64
import os
import subprocess

import ujson

current_dir = os.path.dirname(os.path.abspath(__file__))
root_dir = os.path.dirname(current_dir)
database_dir = os.path.join(root_dir, "database")
output_path = os.path.join(current_dir, "database_secrets.json")

users = (
    "joe.fang@mail.utoronto.ca",
    "minghe.ma@mail.utoronto.ca",
    "bilin.nong@mail.utoronto.ca",
    "yvonnezy.zhang@mail.utoronto.ca",
    "liangyu.zhu@mail.utoronto.ca",
)
deployments = ("gcp@joefang.org",)

authorized_users = []
for user in users:
    authorized_users.append(
        {
            "user": user,
            "token": "test-" + base64.urlsafe_b64encode(os.urandom(36)).decode(),
            "comment": "Purpose: Local Test",
        }
    )
for user in deployments:
    authorized_users.append(
        {
            "user": user,
            "token": "deploy-" + base64.urlsafe_b64encode(os.urandom(36)).decode(),
            "comment": "Purpose: Deployment",
        }
    )


def deploy(data: list[dict[str, str]]):
    result = ujson.dumps(data)
    print("len(result) =", len(result))
    os.chdir(database_dir)
    subprocess.run(
        args=("wsl", "npx", "wrangler", "secret", "put", "AUTHORIZED_USERS"),
        input=result.encode(),
    )
    os.chdir(current_dir)


if __name__ == "__main":
    if os.path.exists(output_path):
        raise SystemExit

    deploy(authorized_users)
    with open(output_path, "w", encoding="utf-8") as fout:
        ujson.dump(authorized_users, fout, indent=2)
