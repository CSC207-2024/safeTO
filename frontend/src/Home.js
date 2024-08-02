import React, { useState, useRef, useEffect } from 'react';
import 'leaflet/dist/leaflet.css';
import './App.css';
import Profile from './Profile';
import Map from './Map';
import LocationSearch from './LocationSearch';

// Initial coordinates for Toronto
const TorontoCoordinates = [43.651070, -79.347015];

const Home = () => {
    // Reference to the map component
    const mapRef = useRef();

    // State for managing editing mode in Profile component
    const [isEditing, setIsEditing] = useState(false);

    // State for managing user information
    const [userInfo, setUserInfo] = useState({
        firstName: '',
        lastName: '',
        email: '',
        phoneNumber: '',
        address: ''
    });

    // State for managing coordinates from map hover
    const [coordinates, setCoordinates] = useState({ lat: null, lng: null });

    // Function to handle location selection from LocationSearch component
    const onSuggestSelect = (location) => {
        if (location) {
            const { lat, lng } = location;
            if (mapRef.current && mapRef.current.flyTo) {
                mapRef.current.flyTo([lat, lng], 13); // Fly to the selected location with zoom level 13
            }
        }
    };

    // Function to handle changes in user information inputs
    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setUserInfo((prevInfo) => ({ ...prevInfo, [name]: value }));
    };

    // Function to toggle the editing state in the Profile component
    const toggleEdit = () => {
        setIsEditing((prevEdit) => !prevEdit);
    };

    // Effect to set the initial view of the map to Toronto coordinates
    useEffect(() => {
        if (mapRef.current && mapRef.current.setView) {
            mapRef.current.setView(TorontoCoordinates, 13); // Set initial view to Toronto with zoom level 13
        }
    }, []);

    
    const [showMessage, setShowMessage] = useState(false);
    
    const handleClick = () => {
        setShowMessage(!showMessage);
    };

    return (
        <div style={{ height: '100vh', position: 'relative' }}>
            {/* Map component with a reference and a function to set coordinates on hover */}
            <Map ref={mapRef} setCoordinates={setCoordinates} />
            <div className="glassmorphism-header">
                <h2>Welcome to <i className={"safe-text-color"}>safe</i><i className={"to-text-color"}>TO</i> <button class="btn" onClick={handleClick}>{showMessage ? 'ℹ Hide Message' : 'ℹ'} </button></h2>
                {showMessage && (
                    <p >
                    A Community Safety Website: Get real-time alerts on ongoing crime incidents and view crime data on an interactive map.
                    </p>
                )}
                
                {/* LocationSearch component for searching locations */}
                <LocationSearch onSuggestSelect={onSuggestSelect} />
            </div>
            
            
            {/* Profile component for displaying and editing user information */}
            <div style={{ position: 'absolute', top: 20, right: 20, zIndex: 1000 }}>
                <Profile
                    userInfo={userInfo}
                    isEditing={isEditing}
                    handleInputChange={handleInputChange}
                    toggleEdit={toggleEdit}
                />
            </div>

            {/* Display hovered coordinates if available */}
            {coordinates.lat && coordinates.lng && (
                <div style={{ position: 'absolute', bottom: 20, left: 20, zIndex: 1000, backgroundColor: 'white', padding: '10px', borderRadius: '5px' }}>
                    Hovered Coordinates: ({coordinates.lat}, {coordinates.lng})
                </div>
            )}
        </div>
    );
};

export default Home;
