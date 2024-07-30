import json
import os

current_dir = os.path.dirname(os.path.abspath(__file__))
root_dir = os.path.normpath(os.path.join(current_dir, ".."))

input_path = os.path.join(
    root_dir,
    "backend",
    "app",
    "src",
    "main",
    "resources",
    "aggregates",
    "total_by_neighbourhood.json",
)
output_path = os.path.join(
    root_dir,
    "backend",
    "app",
    "src",
    "main",
    "resources",
    "datasets",
    "neighbourhood_158.json",
)

if __name__ == "__main__":
    with open(input_path, "r", encoding="utf-8") as fin, open(
        output_path, "w", encoding="utf-8"
    ) as fout:
        data = json.load(fin)
        neighbourhoods = set()
        for item in data["attributes"]:
            neighbourhoods.add(item["NEIGHBOURHOOD_158"])

        # Fix: The unknown neighbourhood was marked "NSA" in the original dataset, which isn't a valid neighbourhood name;
        # see <https://bit.ly/46s4giH>
        if "NSA" in neighbourhoods:
            neighbourhoods.remove("NSA")

        assert len(neighbourhoods) == 158
        neighbourhoods_list = list(neighbourhoods)
        neighbourhoods_list.sort()
        json.dump(neighbourhoods_list, fout, indent=2)
