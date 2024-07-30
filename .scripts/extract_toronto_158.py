import json
import os

current_dir = os.path.dirname(os.path.abspath(__file__))
root_dir = os.path.normpath(os.path.join(current_dir, ".."))

input_path = os.path.join(
    root_dir, "frontend", "aggregates", "by_year_neighbourhood.json"
)
output_path = os.path.join(
    root_dir, "backend", "app", "src", "main", "resources", "neighbourhood_158.json"
)

if __name__ == "__main__":
    pass
