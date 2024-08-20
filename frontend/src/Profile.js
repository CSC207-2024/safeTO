import React, {useState} from 'react';
import axios from 'axios';
import {Tooltip} from 'react-tippy';
import 'react-tippy/dist/tippy.css';
import './App.css';
import Modal from 'react-modal';


Modal.setAppElement('#root');

const Profile = ({userInfo, isEditing, handleInputChange, toggleEdit, setUserInfo}) => {
    const [modalIsOpen, setModalIsOpen] = useState(false);

    const [isLoginMode, setIsLoginMode] = useState(false);
    const [isSignUpMode, setIsSignUpMode] = useState(false);

    const openModal = () => setModalIsOpen(true);
    const closeModal = () => setModalIsOpen(false);

    const [showButtons, setShowButtons] = useState(false);

    const apiUrl = 'https://csc207-api.joefang.org';

    function fetchUserProfile(email) {
        axios.get('https://csc207-api.joefang.org/info', {
            params: { email: email }
        })
            .then(response => {
                const userData = response.data;

                // Update the state with the fetched user data
                setUserInfo(userData);
            })
            .catch(error => {
                console.error('Error fetching user info', error);
            });
    }

    function handleLogin(email, password) {
        axios.post('https://csc207-api.joefang.org/login', null, {
            params: {
                email: email,
                password: password
            }
        })
            .then(response => {
                alert(response.data); // Display a success message
                // Optionally, you might want to fetch and display the user profile after successful login
                fetchUserProfile(email);
            })
            .catch(error => {
                console.error('Invalid email or password', error);
                alert('Invalid email or password');
            });
    }

    // Function to handle data submission to the backend
    function handleSaveAndToggleEdit() {
        const userData = {
            firstName: userInfo.firstName,
            lastName: userInfo.lastName,
            email: userInfo.email,
            phoneNumber: userInfo.phoneNumber,
            address: userInfo.address,
            subscribed: userInfo.subscribed
        };

        axios.post('https://csc207-api.joefang.org/register', userData)
            .then(response => {
                const registeredUser = response.data;

                // Fetch the full user profile after successful registration
                fetchUserProfile(registeredUser.email);
                isEditing(false); // Switch to profile view
            })
            .catch(error => {
                console.error('There was an error registering the user!', error);
            });
    }


    const handleMouseEnter = () => {
        setShowButtons(true);
    };

    const handleMouseLeave = () => {
        setShowButtons(false);
    };

    return (
        <div className='profile-container' onMouseEnter={handleMouseEnter} onMouseLeave={handleMouseLeave}>
            <Tooltip position="bottom" trigger="mouseenter">
                <img
                    src="/images/user.png"
                    alt="User Icon"
                    className="profile-image"
                    onClick={openModal}
                />

                {/*TODO*/}
                {showButtons && (
                    <div className="buttons-container">
                        <button className="login-button">Login</button>
                        <button className="signup-button">Signup</button>
                    </div>
                )}
            </Tooltip>

            <Modal
                isOpen={modalIsOpen}
                onRequestClose={closeModal}
                contentLabel="Profile"
                className='modal-content'
                overlayClassName="overlay"
            >

                <button onClick={closeModal} className="close-button">&times;</button>
                <h2>{isLoginMode ? 'Log In' : isSignUpMode ? 'Sign Up' : 'User Profile'}</h2>

                {isLoginMode ? (
                    <div>
                        <input
                            type="text"
                            name="identifier"
                            value={userInfo.identifier || ''}
                            onChange={handleInputChange}
                            placeholder="Email or User ID"
                            className='profile-input'
                        />
                        <button onClick={() => handleLogin(userInfo.email, userInfo.password)} className="login-button">Log In</button>
                    </div>
                ) : isSignUpMode ? (
                    <div>
                        <input
                            type="text"
                            name="firstName"
                            value={userInfo.firstName}
                            onChange={handleInputChange}
                            placeholder="First Name"
                            className='profile-input'
                        />
                        {/* Other fields */}
                        <button onClick={handleSaveAndToggleEdit} className="save-button">Sign Up</button>
                    </div>
                ) : (
                    // Existing profile display or edit form logic here
                    <div>
                        <p><strong>First Name:</strong> {userInfo.firstName}</p>
                        {/* Other fields */}
                        <button onClick={toggleEdit} className="edit-button">Edit</button>
                    </div>
                )}
            </Modal>


            <Modal
                isOpen={modalIsOpen}
                onRequestClose={closeModal}
                contentLabel="Profile"
                className='modal-content'
                overlayClassName="overlay"
            >
                <button onClick={closeModal} className="close-button">x</button>
                <h2>User Profile</h2>

                {isEditing ? (
                    <div>
                        <input
                            type="text"
                            name="firstName"
                            value={userInfo.firstName}
                            onChange={handleInputChange}
                            placeholder="First Name"
                            className='profile-input'
                        />
                        <input
                            type="text"
                            name="lastName"
                            value={userInfo.lastName}
                            onChange={handleInputChange}
                            placeholder="Last Name"
                            className='profile-input'
                        />
                        <input
                            type="email"
                            name="email"
                            value={userInfo.email}
                            onChange={handleInputChange}
                            placeholder="Email"
                            className='profile-input'
                        />
                        <input
                            type="tel"
                            name="phoneNumber"
                            value={userInfo.phoneNumber}
                            onChange={handleInputChange}
                            placeholder="(xxx) xxx-xxxx"
                            className='profile-input'
                        />
                        <input
                            type="text"
                            name="address"
                            value={userInfo.address}
                            onChange={handleInputChange}
                            placeholder="123 Sample St, Toronto, A1A 1A1"
                            className='profile-input'
                        />
                        <label>
                            <input
                                type="checkbox"
                                name="subscribed"
                                checked={userInfo.subscribed}
                                onChange={handleInputChange}
                            />
                            Subscribe to yearly crime reports
                        </label>
                        <p></p>
                        <button onClick={handleSaveAndToggleEdit} className="save-button">Save</button>
                    </div>
                ) : (
                    <div>
                        <p><strong>First Name:</strong> {userInfo.firstName}</p>
                        <p><strong>Last Name:</strong> {userInfo.lastName}</p>
                        <p><strong>Email:</strong> {userInfo.email}</p>
                        <p><strong>Phone Number:</strong> {userInfo.phoneNumber}</p>
                        <p><strong>Address:</strong> {userInfo.address}</p>
                        <p><strong>Subscribed:</strong> {userInfo.subscribed ? 'Yes' : 'No'}</p>

                        <button onClick={toggleEdit} className="edit-button">Edit</button>
                    </div>
                )}
            </Modal>
        </div>
    );
};

export default Profile;
