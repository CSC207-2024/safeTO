import pandas as pd
import matplotlib.pyplot as plt

# Load the JSON file into a DataFrame
file_path = '../resources/aggregates/by_year_category_neighbourhood.json'

# Load JSON data directly into a DataFrame
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


# Function to generate plots for a specific year and neighbourhood
def generate_annual_comparison(neighbourhood, year, data):
    # Filter data for the specified neighbourhood
    filtered_data = data[data['NEIGHBOURHOOD_140'] == neighbourhood]

    # Get the data for the specified year and the previous year
    current_year_data = filtered_data[filtered_data['OCC_YEAR'] == year]
    previous_year_data = filtered_data[filtered_data['OCC_YEAR'] == (year - 1)]

    # Function to plot and save data
    def plot_and_save(data, year, file_suffix):
        plt.figure(figsize=(10, 6))
        if not data.empty:
            category_counts = data.groupby('MCI_CATEGORY')['INCIDENTS'].sum()
            category_counts.plot(kind='bar', color='skyblue')
            plt.xlabel('Crime Category')
            plt.ylabel('Number of Incidents')
            plt.title(f'{year} Incidents in {neighbourhood}')
            plt.xticks(rotation=45)
            plt.tight_layout()
            plt.savefig(f'annual_report_{year}_{file_suffix}.png')
            plt.close()
        else:
            print(f"No data available for {year}")

    # Plot for the specified year
    plot_and_save(current_year_data, year, 'current_year')

    # Plot for the previous year
    plot_and_save(previous_year_data, year - 1, 'previous_year')


# Example usage
neighbourhood = 'Clairlea-Birchmount (120)'  # Retrieve this from user profile
year = 2023  # Retrieve this from user input
generate_annual_comparison(neighbourhood, year, data)
