import os

current_dir = os.path.dirname(os.path.abspath(__file__))
root_dir = os.path.dirname(os.path.dirname(current_dir))

by_year_category_neighbourhood_path = os.path.join(
    root_dir,
    "backend",
    "app",
    "src",
    "main",
    "resources",
    "aggregates",
    "by_year_category_neighbourhood.json",
)
