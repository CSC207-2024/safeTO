import pandas as pd
import matplotlib.pyplot as plt
from matplotlib.backends.backend_pdf import PdfPages

# Load the JSON file into a DataFrame
file_path = 'path/to/by_year_category_neighbourhood.json'
data = pd.read_json(file_path)

# Ensure the JSON structure is properly loaded
data = pd.json_normalize(data, 'attributes')

# Function to generate plots for a specific neighbourhood
def generate_annual_report(neighbourhood, data):
    # Filter data for the specified neighbourhood
    filtered_data = data[data['NEIGHBOURHOOD_140'] == neighbourhood]
    
    # Get unique crime categories
    crime_categories = filtered_data['MCI_CATEGORY'].unique()
    
    # Create a PDF to save all plots
    with PdfPages('annual_report.pdf') as pdf:
        for category in crime_categories:
            category_data = filtered_data[filtered_data['MCI_CATEGORY'] == category]
            
            # Group by year and sum the counts
            plot_data = category_data.groupby('OCC_YEAR')['INCIDENTS'].sum().reset_index()
            
            # Plot
            plt.figure(figsize=(10, 6))
            plt.bar(plot_data['OCC_YEAR'], plot_data['INCIDENTS'], color='skyblue')
            plt.xlabel('Year')
            plt.ylabel('Number of Incidents')
            plt.title(f'Annual Report: {category} Incidents in {neighbourhood}')
            plt.xticks(rotation=45)
            plt.tight_layout()
            
            # Save the plot to PDF
            pdf.savefig()
            plt.close()

# Example usage
neighbourhood = 'Clairlea-Birchmount (120)'  # Retrieve this from user profile
generate_annual_report(neighbourhood, data)
