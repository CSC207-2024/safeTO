import React, { useState } from 'react';

// LocationSearch component that takes an onSuggestSelect function as a prop
const LocationSearch = ({ onSuggestSelect }) => {
    // State to hold the current search query
    const [query, setQuery] = useState('');
    // State to hold the list of suggestions returned from the API
    const [suggestions, setSuggestions] = useState([]);

    // Function to handle the search operation
    const handleSearch = async () => {
        try {
            // Fetch search results from the API using the current query
            const response = await fetch(`https://csc207-api.joefang.org/search?query=${query}`);
            // Assuming the API returns an array of location objects
            if (response.ok) {
                const {data} = await response.json();
                setSuggestions(data);
            } else {
                console.error('Failed to fetch search results');
            }
        } catch (error) {
            console.error('Error:', error);
        }
    };

    // Function to handle the selection of a suggestion
    const handleSelect = (location) => {
        // Call the onSuggestSelect function passed as a prop with the selected location
        onSuggestSelect(location);
        // Clear the query after a selection is made
        setQuery('');
    };

    return (
        <div>
            {/* Input field for the search query */}
            <input
                type="text"
                value={query}
                onChange={(e) => setQuery(e.target.value)}
                placeholder="Search for a location..."
            />
            {/* Button to trigger the search */}
            <button onClick={handleSearch}>Search</button>
            {/* List of suggestions */}
            <ul>
                {suggestions.map((location, index) => (
                    <li key={index} onClick={() => handleSelect(location)}>
                        {location.name} {/* Assuming each location object has a 'name' property */}
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default LocationSearch;
