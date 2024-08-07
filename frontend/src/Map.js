import React, { useImperativeHandle, forwardRef, useEffect, useState, useRef } from 'react';
import { MapContainer, TileLayer, GeoJSON, useMapEvents, Popup, Marker, Tooltip } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';
import 'esri-leaflet/dist/esri-leaflet';
import L from 'leaflet';
import LocationSearch from './LocationSearch';
import { Icon } from 'leaflet';
import icon from 'leaflet/dist/images/marker-icon.png';
// import icon from 'frontend/public/images/marker.png'
import Modal from 'react-modal';

// Configure the modal root element for accessibility
Modal.setAppElement('#root');

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

// Create the icon using Icon class from the leaflet.
const IconMarker = new Icon({
  iconUrl: icon
});

const carTheftFucntion = () => {

}

const breakInFucntion = () => {

}


// Map component with forwardRef to allow parent components to control the map
const Map = forwardRef(({ setCoordinates, markerCoordinates }, ref) => {
  // const { setCoordinates } = props;
  const position = [43.737207, -79.343448]; // Initial position for the map (Toronto)
  const mapRef = useRef(); // Reference to the map instance

  const [tooltip, setTooltip] = useState({ visible: false, content: '', position: [0, 0] });

  // State for storing GeoJSON data
  const [geoJsonData, setGeoJsonData] = useState(null);

  // State to track the currently hovered layer
  const [hoveredLayer, setHoveredLayer] = useState(null);
  

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
    fillOpacity: 0.7,
    fillColor: '#89CFEF' //Baby Blue
    
  };

  const defaultStyle = {
    weight: 2,
    opacity: 0.5,
    color: '#3388ff',
    dashArray: '3',
    fillOpacity: 0.2,
    fillColor: '#3388ff'
  };

  // Function to apply styles and event handlers to each GeoJSON feature
  const onEachArea = (area, layer) => {
    let mouseInside = false; // Flag to check if mouse is inside

    // Bind a popup to the layer
    layer.bindPopup(area.properties.Neighbourhood);


    layer.on({

      //TODO 
      // //on click to show stats image
      // click: (event) => {
      //   const imageUrl = '/demo_stats.png'; // Assuming imageUrl is a property in the GeoJSON data
      //   const popupContent = `
      //     <div>
      //       <h3>${area.properties.Neighbourhood}</h3>
      //       <img src="${imageUrl}" alt="${"Car Theft Rate"}" style="width: auto; height: auto;" />
      //     </div>
      //   `;
      //   event.target.bindPopup(popupContent).openPopup(); // Show the popup with image
      // },


      mouseover: (event) => {
        // console.log('mouseover')
        mouseInside = true; // Set flag to true when mouse enters
        // event.target.setStyle(highlightStyle); // Highlight on hover
      },
      mouseout: (event) => {
        // console.log('mouseout')
        mouseInside = false; // Set flag to false when mouse leaves
        // event.target.setStyle(defaultStyle); // Reset style only if highlight is not active
      },
      mousemove: (event) => {
        // console.log(`mousemove: ${mouseInside}`)
        if (mouseInside) {
          event.target.setStyle(highlightStyle); // Ensure highlight stays while mouse is inside
          event.target.openPopup(); // Open the popup on mouseover

        } else {
          event.target.setStyle(defaultStyle);
        }
      }
    });
  };

  const [modalIsOpen, setModalIsOpen] = useState(false);

  const openModal = () => setModalIsOpen(true);
  const closeModal = () => setModalIsOpen(false);

  const [selectedRadius, setSelectedRadius] = useState('');
  const [selectedThreshold, setSelectedThreshold] = useState('');

  const handleChange = (event) => {
    setSelectedRadius(event.target.value);
    alert(`Selected radius: ${event.target.value}`);
  };

  return (
    <div>
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

      {/* Render Marker if markerCoordinates is set */}
      {markerCoordinates && (
          <Marker position={markerCoordinates} icon={IconMarker} 
            eventHandlers={{
              dblclick: openModal,
            }}>
            <Tooltip>
              Double click to view Stats
            </Tooltip>          
          </Marker>
      )}

    </MapContainer>

    <Modal
      isOpen={modalIsOpen}
      onRequestClose={closeModal}
      className='modal-content'
      overlayClassName="overlay">
        <button onClick={closeModal} className="close-button">x</button>
        <h2>Find more crime data at this place?</h2>

        <div className='select-container'>
          <label for="options" >Set the radius </label>
          <select id="radius" name="options"  value={selectedRadius} onChange={handleChange} >
            <option value="option50">50m</option>
            <option value="option100">100m</option>
            <option value="option200">200m</option>
            <option value="option500">500m</option>
            <option value="option1000">1000m</option>
          </select> that incidents happened nearby; <br></br>

          <label for="options" > To get probability that incident would happen greater than </label>
          <select id="options" name="options" value={selectedRadius} onChange={handleChange} >
            <option value="option1">Once</option>
            <option value="option2">Twice</option>
            <option value="option3">3x</option>
            {/* <option value="option4">4x</option> */}
            <option value="option5">5x</option>
            {/* <option value="option6">6x</option>
            <option value="option7">7x</option>
            <option value="option8">8x</option>
            <option value="option9">9x</option> */}
            <option value="option10">10x</option>
          </select>; <br></br>
          
          <label for="options" > </label>
          (<i>Only for Break-In Analysis</i>) Since year
          <select id="options" name="options" value={selectedRadius} onChange={handleChange} >
          {Array.from({ length: 11 }, (_, index) => (
            <option key={index} value={ (new Date().getFullYear()) - index}>
              {2024 - index}
            </option>
          ))}
          </select>; <br></br>

        </div>

        <div className="modal-buttons">
          <button className='modal-button' onClick={carTheftFucntion} > Car Theft Analysis</button> 
          <button className='modal-button' onClick={breakInFucntion} > Break-In Analysis</button>
        </div>
    </Modal>

    </div>

  );
});

export default Map;
