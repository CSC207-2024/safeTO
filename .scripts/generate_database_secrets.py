import base64
import os

import toml

current_dir = os.path.dirname(os.path.abspath(__file__))
output_path = os.path.join(current_dir, "database_secrets.toml")


# Reference: <https://bit.ly/3SD0Eol>
class InlineDict(dict, toml.decoder.InlineTableDict):
    pass


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
        InlineDict(
            {
                "user": user,
                "token": base64.urlsafe_b64encode(os.urandom(36)).decode(),
                "comment": "Purpose: Local Test",
            }
        )
    )
for user in deployments:
    authorized_users.append(
        InlineDict(
            {
                "user": user,
                "token": base64.urlsafe_b64encode(os.urandom(36)).decode(),
                "comment": "Purpose: Deployment",
            }
        )
    )

with open(output_path, "w", encoding="utf-8") as fout:
    toml.dump(
        {"AUTHORIZED_USERS": authorized_users},
        fout,
        encoder=toml.TomlPreserveInlineDictEncoder(),
    )
