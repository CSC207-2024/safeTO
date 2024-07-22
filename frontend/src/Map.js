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

  const [tooltip, setTooltip] = useState({ visible: false, content: '', position: [0, 0] });

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
    fetch('/data/Toronto_Neighbourhoods.geojson') // fix geojson data path
      .then((response) => response.json())
      .then((data) => setGeoJsonData(data)) // Set GeoJSON data
      .catch((error) => console.error('Error fetching GeoJSON data:', error));
    if (mapRef.current) {
      mapRef.current.setView([43.651070, -79.347015], 12); // Set initial view to Toronto with zoom level 16
    }
  }, []);

  // Styles for GeoJSON features
  const highlightStyle = {
    weight: 2,
    color: '#89CFEF', //Baby Blue
    dashArray: '',
    fillOpacity: 0.6,
    fillColor: '#89CFEF' //Baby Blue
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
    let mouseInside = false; // Flag to check if mouse is inside
    // Bind a popup to the layer
    layer.bindPopup(area.properties.Neighbourhood);

    layer.on({
      mouseover: (event) => {
        mouseInside = true; // Set flag to true when mouse enters
        event.target.setStyle(highlightStyle); // Highlight on hover
        
        // Open the popup on mouseover
        event.target.openPopup();
      },
      mouseout: (event) => {
        mouseInside = false; // Set flag to false when mouse leaves
        event.target.setStyle(defaultStyle); // Reset style on mouseout

        setTooltip({ ...tooltip, visible: false });
      },
      mousemove: (event) => {
        if (mouseInside) {
          event.target.setStyle(highlightStyle); // Ensure highlight stays while mouse is inside
        }
      }
    });
  };

  return (
    <MapContainer center={position} zoom={12} style={{ height: '100vh', width: '100%' }} ref={mapRef}>
      {/* Tile layer for the map */}
      
      {/* Default Layer */}
      <TileLayer
        url="https://server.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer/tile/{z}/{y}/{x}"
        attribution='&copy; <a href="https://www.esri.com/">Esri</a> &copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
        opacity={0.6}

        //Satellite Layer
        // url="https://server.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer/tile/{z}/{y}/{x}"
        // attribution='&copy; <a href="https://www.esri.com/">Esri</a> &copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
        // opacity={0.6}
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
