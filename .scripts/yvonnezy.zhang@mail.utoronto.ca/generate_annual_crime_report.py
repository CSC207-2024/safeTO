import pandas as pd
import matplotlib.pyplot as plt
import base64
from io import BytesIO
import sys


# Function to generate plots for a specific year and neighbourhood
def generate_annual_comparison(neighbourhood, year, data):
    # Filter data for the specified neighbourhood
    filtered_data = data[data['NEIGHBOURHOOD_140'] == neighbourhood]

    # Get the data for the specified year and the previous year
    current_year_data = filtered_data[filtered_data['OCC_YEAR'] == year]
    previous_year_data = filtered_data[filtered_data['OCC_YEAR'] == (year - 1)]

    # Function to plot and save data
    def plot_and_encode(data, year, file_suffix):
        buf = BytesIO()
        plt.figure(figsize=(10, 6))
        if not data.empty:
            category_counts = data.groupby('MCI_CATEGORY')['INCIDENTS'].sum()
            category_counts.plot(kind='bar', color='skyblue')
            plt.xlabel('Crime Category')
            plt.ylabel('Number of Incidents')
            plt.title(f'{year} Incidents in {neighbourhood}')
            plt.xticks(rotation=45)
            plt.tight_layout()
            plt.savefig(buf, format='png')
            plt.close()
            buf.seek(0)
            encoded_image = base64.b64encode(buf.read()).decode('utf-8')
            buf.close()
            return encoded_image
        else:
            return None

    # Get Base64 strings for both plots
    chart_base64 = plot_and_encode(current_year_data, year, 'current_year')
    chart_base64_prev = plot_and_encode(previous_year_data, year - 1, 'previous_year')

    return chart_base64, chart_base64_prev


def main(neighbourhood, year, file_path):
    # Load the JSON file into a DataFrame
    data = pd.read_json(file_path)

    # Check the loaded data
    print(data.head())

    # Extract the 'attributes' key and convert it into a DataFrame
    if 'attributes' in data:
        # Flatten the nested structure
        data = pd.json_normalize(data['attributes'])
    else:
        raise ValueError("Expected 'attributes' key in the JSON data")

    # Check columns in the DataFrame
    print(data.columns)

    # Generate comparison plots
    generate_annual_comparison(neighbourhood, year, data)


# The script can now be called with the parameters for neighbourhood and year
if __name__ == "__main__":
    neighbourhood = sys.argv[1]  # Neighbourhood name
    year = int(sys.argv[2])  # Year
    file_path = '../resources/aggregates/by_year_category_neighbourhood.json'
    main(neighbourhood, year, file_path)
