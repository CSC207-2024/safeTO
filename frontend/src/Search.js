import React from 'react';
import LocationSearch from './LocationSearch';

const Search = ({ onSuggestSelect }) => {
    return (
        <div style={{ position: 'absolute', top: 130, left: 645, zIndex: 1000 }}>
            <LocationSearch onSuggestSelect={onSuggestSelect} />
        </div>
    );
};

export default Search;
