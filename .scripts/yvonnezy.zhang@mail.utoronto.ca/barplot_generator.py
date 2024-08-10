# The generated bar plots are moved to frontend folder
import json
import os
import re
import sys

import matplotlib.pyplot as plt
import pandas as pd
from constants import by_year_category_neighbourhood_path, root_dir

output_dir = os.path.join(root_dir, "frontend", "public", "neighbourhood_plots")

# Compile the regex pattern to match non-alphanumeric characters
non_alphanumeric = re.compile(r"[^A-Za-z0-9]")


def normalize_neighborhood(neighborhood: str) -> str:
    # Replace non-alphanumeric characters with an underscore
    return non_alphanumeric.sub("_", neighborhood)


def main():
    # Create a directory for storing the plots
    os.makedirs(output_dir, exist_ok=True)

    # Load the JSON data
    with open(by_year_category_neighbourhood_path, "r", encoding="utf-8") as file:
        data = json.load(file)

    # Convert JSON data to a pandas DataFrame
    df = pd.DataFrame(data["attributes"])

    # Group by neighbourhood and create a plot for each one
    for neighbourhood, group in df.groupby("NEIGHBOURHOOD_140"):
        print("neighborhood:", neighbourhood, file=sys.stderr)
        pivot_df = group.pivot(
            index="OCC_YEAR", columns="MCI_CATEGORY", values="INCIDENTS"
        )

        # Plotting
        pivot_df.plot(kind="bar", stacked=False, figsize=(10, 7))
        plt.title(f"Crime Incidents in {neighbourhood}")
        plt.ylabel("Number of Incidents")
        plt.xlabel("Year")
        plt.xticks(rotation=0)
        plt.tight_layout()

        # Save the plot as a PNG file
        plot_filename = normalize_neighborhood(neighbourhood) + ".png"
        plt.savefig(os.path.join(output_dir, plot_filename))
        plt.close()
        print("output:", plot_filename, file=sys.stderr)

    print("output_directory:", output_dir, file=sys.stderr)


if __name__ == "__main__":
    main()
