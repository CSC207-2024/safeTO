import React, { useState } from 'react';
import { getLocations } from './LocationService';
import axios from 'axios';

const LocationSearch = ({ onSuggestSelect }) => {
    const [query, setQuery] = useState('');
    const [results, setResults] = useState([]);
    const [error, setError] = useState(null);

    const handleSearch = async () => {
        setResults([]);
        setError(null);

        try {
        const response = await axios.get('https://api.canadapost.ca/pcd/addresses', {
            headers: {
            'Authorization': `Bearer YOUR_API_KEY`, // Replace with your actual API key
            'Accept': 'application/vnd.cpc.addresses-v1+xml' // Adjust the Accept header if needed
            },
            params: {
            'postal-code': query,
            'address': query
            }
        });

        // Parse the response data based on the API's response format
        const data = response.data; // Adjust based on response structure
        setResults(data.results || []); // Adjust based on response structure
        } catch (err) {
        setError('Failed to fetch addresses');
        console.error('Error fetching addresses:', err);
        }
    };

    return (
        <div>
        <input
            type="text"
            value={query}
            onChange={(e) => setQuery(e.target.value)}
            placeholder="Enter Address or Postal Code"
            className='search-bar'

        />
        <button onClick={handleSearch} className='search-button'>Search</button>
        {error && <p>{error}</p>}
        {results.length > 0 && (
            <ul>
            {results.map((result, index) => (
                <li key={index}>
                {result.address} {/* Adjust based on response structure */}
                </li>
            ))}
            </ul>
        )}
        </div>
    );
};

export default LocationSearch;
