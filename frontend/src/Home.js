import React from 'react';
import { MapContainer, TileLayer } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';
import Geosuggest from 'react-geosuggest';
import 'react-geosuggest/module/geosuggest.css';
import { Tooltip } from 'react-tippy';
import 'react-tippy/dist/tippy.css';

const Home = () => {
    return (
        <div style={{ height: '100vh', position: 'relative' }}>
            <MapContainer center={[51.505, -0.09]} zoom={13} style={{ height: '100%', width: '100%' }}>
                <TileLayer
                    url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                    attribution='&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
                />
            </MapContainer>
            <div style={{ position: 'absolute', top: 20, left: 20, zIndex: 1000 }}>
                <h2>Welcome to Community Safety App</h2>
                <p>Get real-time alerts on ongoing crime incidents and view crime data on an interactive map.</p>
            </div>
            <div style={{ position: 'absolute', top: 100, left: 20, zIndex: 1000 }}>
                <Geosuggest placeholder="Search for a location" />
            </div>
            <div style={{ position: 'absolute', top: 20, right: 20, zIndex: 1000 }}>
                <Tooltip title="User Profile" position="bottom" trigger="mouseenter">
                    <img
                        src="https://via.placeholder.com/40" // replace with your user icon
                        alt="User Icon"
                        style={{ borderRadius: '50%', cursor: 'pointer' }}
                    />
                    <div style={{ display: 'none' }} className="user-profile">
                        <p>User Name</p>
                        <p>Email: user@example.com</p>
                        <p>Other Profile Info</p>
                    </div>
                </Tooltip>
            </div>
        </div>
    );
};

export default Home;
