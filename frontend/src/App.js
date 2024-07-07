import React from 'react';
import { BrowserRouter as Router, Route, Routes, Link } from 'react-router-dom'; // Import Router from react-router-dom
import Home from './Home';
import Profile from './Profile';
import Map from './Map';

function App() {
    return (
        <Router>
            <div className="App">
                <header className="App-header">
                    <h1>Community Safety App</h1>
                    <nav>
                        <ul>
                            <li><Link to="/">Home</Link></li>
                            <li><Link to="/profile">Profile</Link></li>
                            <li><Link to="/map">Map</Link></li>
                        </ul>
                    </nav>
                </header>
                <main>
                    <Routes>
                        <Route path="/" element={<Home />} />
                        <Route path="/profile" element={<Profile />} />
                        <Route path="/map" element={<Map />} />
                    </Routes>
                </main>
            </div>
        </Router>
    );
}

export default App;
