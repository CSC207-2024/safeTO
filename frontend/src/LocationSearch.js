import React, {useState, useRef, useEffect, Marker} from 'react';
import axios from 'axios';
import debounce from 'lodash.debounce';
import {Popup} from 'react-leaflet';

const LocationSearch = ({onSuggestSelect}) => {
    const [query, setQuery] = useState('');
    const [results, setResults] = useState([]);
    const [error, setError] = useState(null);
    const [selectedLocation, setSelectedLocation] = useState(null);
    const mapRef = useRef(null);

    const handleSearch = async () => {
        setResults([]);
        setError(null);

        try {
            const response = await axios.get('https://maps.googleapis.com/maps/api/geocode/json', {
                params: {
                    address: query,
                    key: 'AIzaSyC7Aecp14Bec_eTqrAAGlBQsqS80BmedkA',
                    components: 'country:CA|locality:Toronto', // Restrict to Toronto, Canada
                }
            });

            const data = response.data;
            //alert(response.data.status);
            if (data.status === 'OK') {
                // Filter results within Toronto
                const torontoResults = data.results.filter((result) => {
                    return result.address_components.some((component) => {
                        return component.types.includes('locality') && component.long_name === 'Toronto';
                    });
                });

                if (torontoResults.length > 0) {
                    setResults(torontoResults);

                } else {
                    alert('The address is outside the City of Toronto.');
                }
            } else {
                setError('No results found.');
            }
        } catch (err) {
            setError('Failed to fetch addresses.');
            console.error('Error fetching addresses:', err);
        }
    };

    //prevent the handleSearch function from being called too frequently
    const debouncedHandleSearch = useRef(debounce(handleSearch, 500)).current;

    const handleResultClick = (result) => {
        const location = result.geometry.location;
        setSelectedLocation([location.lat, location.lng]);
        onSuggestSelect(result.geometry.location, result.formatted_address);

    };


    return (
        <div className="search-container">
            <input
                type="text"
                value={query}
                onChange={(e) => {
                    setQuery(e.target.value);
                    debouncedHandleSearch();
                }}
                placeholder="Search within City of Toronto"
                className='search-bar'
            />

            <button onClick={handleSearch} className='search-button'>Search</button>


            {error && <p>{error}</p>}
            {results.length > 0 && (
                <div className="results-container">
                    <ul className="results-list">
                        {results.map((result, index) => (
                            <li key={index}>
                                <button onClick={() => handleResultClick(result)}>
                                    {result.formatted_address}
                                </button>
                            </li>
                        ))}
                    </ul>
                </div>
            )}

        </div>
    );
};

export default LocationSearch;
