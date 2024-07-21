import React, {useImperativeHandle, forwardRef, useEffect, useState, useRef} from 'react';
import { MapContainer, TileLayer, GeoJSON} from 'react-leaflet';
// import L from 'leaflet';
import 'leaflet/dist/leaflet.css';
import 'esri-leaflet/dist/esri-leaflet';
// import geoJsonData from '../../frontend/public/Toronto_Neighbourhoods.geojson'; // Import GeoJSON file
// import axios from 'axios';


const Map = forwardRef((props, ref) => {
  // Central View Coordinates of Toronto
  const position = [43.737207, -79.343448];

  const mapRef = useRef();

  const [geoJsonData, setGeoJsonData] = useState(null);

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

  useEffect(() => {
    // Fetch the GeoJSON data for Toronto
    fetch('../../frontend/public/Toronto_Neighbourhoods.geojson')
      .then((response) => response.json())
      .then((data) => setGeoJsonData(data))
      .catch((error) => console.error('Error fetching GeoJSON data:', error));
    if (mapRef.current) {
      mapRef.current.setView([43.651070, -79.347015], 13); // Central coordinate of Toronto
    }
  }, []);

  const highlightStyle = {
    weight: 2,
    color: '#00FF00', // Outline color when hovered
    dashArray: '',
    fillOpacity: 0.7,
    fillColor: '#00FF00' // Fill color when hovered
  };

  const defaultStyle = {
    weight: 2,
    opacity: 0.5,
    color: '#3388ff', // Outline color
    dashArray: '3',
    fillOpacity: 0.1,
    fillColor: '#3388ff' // Fill color
  };


  //   <MapContainer center={position} zoom={12} style={{ height: '100vh', width: '100%' }}>
    
  //     {/* <Marker position={position}>
  //       <Popup>
  //         Toronto Center view
  //       </Popup>
  //     </Marker> */}

      

  // );

  const onEachArea = (area, layer) => {
    // const name = area.properties.name;
    layer.on({
      mouseover: (event) => {
        event.target.setStyle(highlightStyle);
      },
      mouseout: (event) => {
        event.target.setStyle(defaultStyle);
      }
    });
  };

  return (
    <MapContainer center={position} zoom={12} style={{ height: '100vh', width: '100%' }}>
      {/* <TileLayer
        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
        attribution='&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
      /> */}
      <TileLayer
        url="https://server.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer/tile/{z}/{y}/{x}"
        attribution='&copy; <a href="https://www.esri.com/">Esri</a> &copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
        opacity={0.6}

      />

      {geoJsonData && (
        <GeoJSON data={geoJsonData} style={highlightStyle} onEachFeature={onEachArea} />
      )}
    </MapContainer>
  );
});


export default Map;
