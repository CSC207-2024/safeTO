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
import axios from 'axios';
// Import the JSON file
import testCarTheftData from './/data/test_car_theft.json'; 
import testBreakInData from './/data/test_break_in.json';

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

  // State variables for analysis results
  const [analysisResults, setAnalysisResults] = useState(null);
  

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


  // Variables to store user selected parameters
  const [selectedRadius, setSelectedRadius] = useState('50m');
  const [selectedThreshold, setSelectedThreshold] = useState('1x');
  const [selectedYear, setSelectedYear] = useState(new Date().getFullYear());

  useEffect(() => {
    console.log('Selected Radius:', selectedRadius);
  }, [selectedRadius]);

  useEffect(() => {
    console.log('Selected Threshold:', selectedThreshold);
  }, [selectedThreshold]);

  useEffect(() => {
    console.log('Selected Year:', selectedYear);

    //TODO: 

    console.log(testCarTheftData.data.result);
    setAnalysisResults(testBreakInData.data.result);
    console.log(analysisResults);

  }, [selectedYear]);
  

  const handleRadiusChange = (e) => {
    // console.log(e.target.value, selectedRadius);
    setSelectedRadius(parseInt(e.target.value));
    // console.log(e.target.value, selectedRadius);
  };

  const handleThresholdChange = (e) => {
    setSelectedThreshold(parseInt(e.target.value));
    // console.log(e.target.value, selectedThreshold);

  };

  const handleYearChange = (e) => {
    setSelectedYear(parseInt(e.target.value));
    // console.log(e.target.value, selectedYear);
  };

  const sendToBackend = async (analysisType) => {
    
    // Define data based on analysisType
    const data = {
      latitude: parseFloat(markerCoordinates.lat),
      longitude: parseFloat(markerCoordinates.lng),
      radius: parseInt(selectedRadius),
      threshold: parseInt(selectedThreshold),
      year: analysisType === 'breakIn' ? parseInt(selectedYear) : undefined, // Include year only for 'breakIn'
      analysisType: analysisType
    };

    // Initialize the URL object
    const baseUrl = analysisType === 'carTheft'
    ? 'https://csc207-api.joefang.org/analysis/auto-theft'
    : 'https://csc207-api.joefang.org/analysis/break-and-enter';

    const url = new URL(baseUrl);

    // Add query parameters to URL
    url.searchParams.append('latitude', parseFloat(markerCoordinates.lat));
    url.searchParams.append('longitude', parseFloat(markerCoordinates.lng));
    url.searchParams.append('radius', parseInt(selectedRadius));
    url.searchParams.append('threshold', parseInt(selectedThreshold));

    if (analysisType === 'breakIn') {
      url.searchParams.append('year', parseInt(selectedYear));
    }

    url.searchParams.append('analysisType', analysisType);
    
    console.log('Check url:', url, url.searchParams);

    try {
      // Perform the GET request with the constructed URL
      const response = await axios.get(url.toString(), {
        headers: {
          'Content-Type': 'application/json'
        }
      });

      // Log the response status for debugging
      console.log('Response Status:', response.status);


      // Check if the response is okay
      if (response.status !== 200) {
        throw new Error(`HTTP error! Status: ${response.status}`);
      }

      // Handle the response data
      const result = response.json();
      console.log('Response Data:', result);
      setAnalysisResults(result);
      // alert(result);

      // Handle success (e.g., update UI or state)
    } catch (error) {
      console.error('Error:', error);
      // Handle error interactively
    }

    // Debugging
    // setAnalysisResults(await axios.get('https://csc207-api.joefang.org/analysis/auto-theft'));
    console.log('Request URL:', url.toString());
  };

  const carTheftFunction = () => {
    alert(analysisResults);
    sendToBackend('carTheft');
    
  };

  const breakInFunction = () => {
    sendToBackend('breakIn');
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
          <label for="radius-select" >Set the radius </label>
          <select id="radius-select" name="radius"  value={selectedRadius} onChange={handleRadiusChange} >
            <option value="50">50m</option>
            <option value="100">100m</option>
            <option value="200">200m</option>
            <option value="500">500m</option>
            <option value="1000">1000m</option>
          </select> that incidents happened nearby; <br></br>

          <label for="threshold-select" > How strict do you want the probability calculation to be: </label>
          <input
            type="range"
            id="threshold-select"
            name="thresold"
            min="1"
            max="10"
            value={selectedThreshold}
            onChange={handleThresholdChange}
          /> 
          <p>Selected value: {selectedThreshold} (<i>Note: 1 for most strict and 10 for the least strict) </i></p>
          
          <label for="year-select" > </label>
          (<i>Only for Break-In Analysis</i>) Since year
          <select id="year-select" name="year" value={selectedYear} onChange={handleYearChange} >
          {Array.from({ length: 11 }, (_, index) => (
            <option key={index} value={ (new Date().getFullYear()) - index}>
              {2024 - index}
            </option>
          ))}
          </select>; <br></br>

        </div>

        <div className="modal-buttons">
          <button className='modal-button' onClick={carTheftFunction} > Car Theft Analysis</button> 
          <button className='modal-button' onClick={breakInFunction} > Break-In Analysis</button>
        </div>

        {/* Analysis results */}
        {analysisResults && (
          <div className='analysis-results'>
            <h3>Analysis Results</h3>
            <p>Crime Probability: {analysisResults.probability.toFixed(2)}</p>
            <p>Message: {analysisResults.probabilityMessage}</p>
            <i className='red-text'>{analysisResults.warning}</i>
            <h4>Past Year Incidents</h4>
            <div className='scrollable-table-container'>
              <table className='scrollable-table'>
                <thead>
                  <tr>
                    <th>Date</th>
                    <th>Distance (m)</th>
                  </tr>
                </thead>
                <tbody>
                  {analysisResults.pastYearIncidents.map((incident, index) => (
                    <tr key={index}>
                      <td>{incident.occurDate}</td>
                      <td>{incident.distance.toFixed(1)}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>

            <h4>All Known Incidents</h4>
            <div className='scrollable-table-container'>
              <table className='scrollable-table'>
                <thead>
                  <tr>
                    <th>Date</th>
                    <th>Distance (m)</th>
                  </tr>
                </thead>
                <tbody>
                  {analysisResults.allKnownIncidents.map((incident, index) => (
                    <tr key={index}>
                      <td>{incident.occurDate}</td>
                      <td>{incident.distance.toFixed(1)}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        )}
    </Modal>

    </div>

  );
});

export default Map;

