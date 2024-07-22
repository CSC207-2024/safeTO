import React, { useImperativeHandle, forwardRef, useEffect, useState, useRef } from 'react';
import { MapContainer, TileLayer, GeoJSON, useMapEvents } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';
import 'esri-leaflet/dist/esri-leaflet';

// Component to track mouse movements and update coordinates
const HoverCoordinates = ({ setCoordinates }) => {
  useMapEvents({
    mousemove: (event) => {
      const { lat, lng } = event.latlng;
      setCoordinates({ lat, lng }); // Update coordinates state
    },
  });
  return null;
};

// Map component with forwardRef to allow parent components to control the map
const Map = forwardRef((props, ref) => {
  const { setCoordinates } = props;
  const position = [43.737207, -79.343448]; // Initial position for the map (Toronto)
  const mapRef = useRef(); // Reference to the map instance

  // State for storing GeoJSON data
  const [geoJsonData, setGeoJsonData] = useState(null);

  // Expose map methods to parent components
  useImperativeHandle(ref, () => ({
    flyTo: (coords, zoom) => {
      if (mapRef.current) {
        mapRef.current.flyTo(coords, zoom);
      }
    },
    setView: (coords, zoom) => {
      if (mapRef.current) {
        mapRef.current.setView(coords, zoom);
      }
    }
  }));

  // Fetch GeoJSON data and set initial view
  useEffect(() => {
    fetch('../../frontend/public/Toronto_Neighbourhoods.geojson')
        .then((response) => response.json())
        .then((data) => setGeoJsonData(data)) // Set GeoJSON data
        .catch((error) => console.error('Error fetching GeoJSON data:', error));
    if (mapRef.current) {
      mapRef.current.setView([43.651070, -79.347015], 16); // Set initial view to Toronto with zoom level 16
    }
  }, []);

  // Styles for GeoJSON features
  const highlightStyle = {
    weight: 2,
    color: '#00FF00',
    dashArray: '',
    fillOpacity: 0.7,
    fillColor: '#00FF00'
  };

  const defaultStyle = {
    weight: 2,
    opacity: 0.5,
    color: '#3388ff',
    dashArray: '3',
    fillOpacity: 0.1,
    fillColor: '#3388ff'
  };

  // Function to apply styles and event handlers to each GeoJSON feature
  const onEachArea = (area, layer) => {
    layer.on({
      mouseover: (event) => {
        event.target.setStyle(highlightStyle); // Highlight on hover
      },
      mouseout: (event) => {
        event.target.setStyle(defaultStyle); // Reset style on mouseout
      }
    });
  };

  return (
      <MapContainer center={position} zoom={15} style={{ height: '100vh', width: '100%' }} ref={mapRef}>
        {/* Tile layer for the map */}
        <TileLayer
            url="https://server.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer/tile/{z}/{y}/{x}"
            attribution='&copy; <a href="https://www.esri.com/">Esri</a> &copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
            opacity={0.6}
        />
        {/* Render GeoJSON data if available */}
        {geoJsonData && (
            <GeoJSON data={geoJsonData} style={defaultStyle} onEachFeature={onEachArea} />
        )}
        {/* Component to track mouse movements */}
        <HoverCoordinates setCoordinates={setCoordinates} />
      </MapContainer>
  );
});

export default Map;
