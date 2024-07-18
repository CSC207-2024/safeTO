import React, { useState, useRef, useEffect } from 'react';
import { MapContainer, TileLayer, useMapEvents, GeoJSON } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';
import Geosuggest from 'react-geosuggest';
import 'react-geosuggest/module/geosuggest.css';
import { Tooltip } from 'react-tippy';
import 'react-tippy/dist/tippy.css';
import './App.css';
import Map from './Map';

const TorontoCoordinates = [43.651070, -79.347015];

const HoverCoordinates = ({ setCoordinates }) => {
    useMapEvents({
        mousemove: (event) => {
            const { lat, lng } = event.latlng;
            setCoordinates({ lat, lng });
        },
    });
    return null;
};

const Home = () => {
    const mapRef = useRef();
    const [isEditing, setIsEditing] = useState(false);
    const [userInfo, setUserInfo] = useState({
        firstName: '',
        lastName: '',
        email: '',
        phoneNumber: ''
    });
    const [coordinates, setCoordinates] = useState({ lat: null, lng: null });

    const onSuggestSelect = (suggest) => {
        if (suggest && suggest.location) {
            const { lat, lng } = suggest.location;
            mapRef.current.setView([lat, lng], 13);
        }
    };

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setUserInfo((prevInfo) => ({ ...prevInfo, [name]: value }));
    };

    const toggleEdit = () => {
        setIsEditing((prevEdit) => !prevEdit);
    };

    useEffect(() => {
        if (mapRef.current && mapRef.current.setView) {
            mapRef.current.setView(TorontoCoordinates, 13);
        }
    }, []);

    return (
        <div style={{ height: '100vh', position: 'relative' }}>
            {/* add map layer */}
            <Map />
            <div className="overlay-text">
                <h2>Welcome to Community Safety App</h2>
                <p>Get real-time alerts on ongoing crime incidents and view crime data on an interactive map.</p>
            </div>
            <div style={{ position: 'absolute', top: 100, left: 20, zIndex: 1000 }}>
                <Geosuggest placeholder="Search for a location" onSuggestSelect={onSuggestSelect} />
            </div>
            <div style={{ position: 'absolute', top: 10, right: 10, zIndex: 1000 }}>
                <Tooltip title="User Profile" position="bottom" trigger="mouseenter">
                    <img
                        src="https://img.icons8.com/ios-filled/50/000000/user.png"
                        alt="User Icon"
                        style={{ borderRadius: '50%', cursor: 'pointer' }}
                    />
                    <div className="user-profile">
                        <div>
                            <label htmlFor="firstName">First Name:</label>
                            <input
                                type="text"
                                id="firstName"
                                name="firstName"
                                value={userInfo.firstName}
                                disabled={!isEditing}
                                onChange={handleInputChange}
                            />
                        </div>
                        <div>
                            <label htmlFor="lastName">Last Name:</label>
                            <input
                                type="text"
                                id="lastName"
                                name="lastName"
                                value={userInfo.lastName}
                                disabled={!isEditing}
                                onChange={handleInputChange}
                            />
                        </div>
                        <div>
                            <label htmlFor="email">Email:</label>
                            <input
                                type="email"
                                id="email"
                                name="email"
                                value={userInfo.email}
                                disabled={!isEditing}
                                onChange={handleInputChange}
                            />
                        </div>
                        <div>
                            <label htmlFor="phoneNumber">Phone Number:</label>
                            <input
                                type="tel"
                                id="phoneNumber"
                                name="phoneNumber"
                                value={userInfo.phoneNumber}
                                disabled={!isEditing}
                                onChange={handleInputChange}
                            />
                        </div>
                        <button onClick={toggleEdit}>
                            {isEditing ? 'Save' : 'Edit'}
                        </button>
                    </div>
                </Tooltip>
            </div>
            {coordinates.lat && coordinates.lng && (
                <div style={{ position: 'absolute', bottom: 20, left: 20, zIndex: 1000, backgroundColor: 'white', padding: '10px', borderRadius: '5px' }}>
                    Hovered Coordinates: Latitude: {coordinates.lat}, Longitude: {coordinates.lng}
                </div>
            )}
        </div>
    );
};

export default Home;
