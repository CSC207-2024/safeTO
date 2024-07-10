import React, { useState, useRef, useEffect } from 'react';
import { MapContainer, TileLayer } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';
import Geosuggest from 'react-geosuggest';
import 'react-geosuggest/module/geosuggest.css';
import { Tooltip } from 'react-tippy';
import 'react-tippy/dist/tippy.css';
import './App.css';

const TorontoCoordinates = [43.65107, -79.347015];

const Home = () => {
    const mapRef = useRef();
    const [isEditing, setIsEditing] = useState(false);
    const [userInfo, setUserInfo] = useState({
        firstName: '',
        lastName: '',
        email: '',
        phoneNumber: ''
    });

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
            <MapContainer center={TorontoCoordinates} zoom={13} style={{ height: '100%', width: '100%' }} whenCreated={mapInstance => { mapRef.current = mapInstance; }}>
                <TileLayer
                    url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                    attribution='&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
                />
            </MapContainer>
            <div className="overlay-text">
                <h2>Welcome to Community Safety App</h2>
                <p>Get real-time alerts on ongoing crime incidents and view crime data on an interactive map.</p>
            </div>
            <div style={{ position: 'absolute', top: 100, left: 20, zIndex: 1000 }}>
                <Geosuggest placeholder="Search for a location" onSuggestSelect={onSuggestSelect} />
            </div>
            <div style={{ position: 'absolute', top: 20, right: 20, zIndex: 1000 }}>
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
        </div>
    );
};

export default Home;
