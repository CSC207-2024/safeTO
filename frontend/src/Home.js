import React, { useState, useRef, useEffect } from 'react';
import { MapContainer, TileLayer, useMapEvents } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';
import Geosuggest from 'react-geosuggest';
import 'react-geosuggest/module/geosuggest.css';
import './App.css';
import Profile from './Profile';

const TorontoCoordinates = [43.65107, -79.347015];

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
            <MapContainer center={TorontoCoordinates} zoom={13} style={{ height: '100%', width: '100%' }} whenCreated={mapInstance => { mapRef.current = mapInstance; }}>
                <TileLayer
                    url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                    attribution='&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
                />
                <HoverCoordinates setCoordinates={setCoordinates} />
            </MapContainer>
            <div className="overlay-text">
                <h2>Welcome to Community Safety App</h2>
                <p>Get real-time alerts on ongoing crime incidents and view crime data on an interactive map.</p>
            </div>
            <div style={{ position: 'absolute', top: 100, left: 20, zIndex: 1000 }}>
                <Geosuggest placeholder="Search for a location" onSuggestSelect={onSuggestSelect} />
            </div>
            <div style={{ position: 'absolute', top: 20, right: 20, zIndex: 1000 }}>
                <Profile
                    userInfo={userInfo}
                    isEditing={isEditing}
                    handleInputChange={handleInputChange}
                    toggleEdit={toggleEdit}
                />
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
