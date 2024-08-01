import React, { useState } from 'react';
import { getLocations } from './LocationService';

const LocationSearch = ({ onSuggestSelect }) => {
    const [searchQuery, setSearchQuery] = useState("");
    const [suggestions, setSuggestions] = useState([]);
    const locations = getLocations();

    const handleSearchChange = (e) => {
        const query = e.target.value;
        setSearchQuery(query);
        if (query.length > 1) {
            const filteredSuggestions = locations.filter(location =>
                location.name.toLowerCase().includes(query.toLowerCase())
            );
            setSuggestions(filteredSuggestions);
        } else {
            setSuggestions([]);
        }
    };

    const handleSelect = (location) => {
        onSuggestSelect(location);
        setSuggestions([]);
        setSearchQuery(location.name);
    };

    return (
        <div className='container'>
            <input
                type="text"
                placeholder="Search for a location"
                value={searchQuery}
                onChange={handleSearchChange}
                className='search-bar'

            />
            {suggestions.length > 0 && (
                <ul style={{ listStyleType: 'none', padding: 0, margin: 0, backgroundColor: 'white' }}>
                    {suggestions.map((suggestion, index) => (
                        <li
                            key={index}
                            onClick={() => handleSelect(suggestion)}
                            style={{ cursor: 'pointer', padding: '5px' }}
                        >
                            {suggestion.name}
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
};

export default LocationSearch;
