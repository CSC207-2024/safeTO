# The generated bar plots are moved to frontend folder
import json
import os

import matplotlib.pyplot as plt
import pandas as pd

current_dir = os.path.dirname(os.path.abspath(__file__))
root_dir = os.path.dirname(os.path.dirname(current_dir))

# Load the JSON data
with open("../resources/aggregates/by_year_category_neighbourhood.json", "r") as file:
    data = json.load(file)

# Convert JSON data to a pandas DataFrame
df = pd.DataFrame(data["attributes"])

# Create a directory for storing the plots
output_dir = "neighbourhood_plots"
os.makedirs(output_dir, exist_ok=True)

# Group by neighbourhood and create a plot for each one
for neighbourhood, group in df.groupby("NEIGHBOURHOOD_140"):
    pivot_df = group.pivot(index="OCC_YEAR", columns="MCI_CATEGORY", values="INCIDENTS")

    # Plotting
    pivot_df.plot(kind="bar", stacked=False, figsize=(10, 7))
    plt.title(f"Crime Incidents in {neighbourhood}")
    plt.ylabel("Number of Incidents")
    plt.xlabel("Year")
    plt.xticks(rotation=0)
    plt.tight_layout()

    # Save the plot as a PNG file
    plot_filename = f"{output_dir}/{neighbourhood.replace('/', '_')}.png"
    plt.savefig(plot_filename)
    plt.close()

print(f"Plots saved to {output_dir}/")
