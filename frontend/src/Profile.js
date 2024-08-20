import React, { useState } from 'react';
import axios from 'axios';
import { Tooltip } from 'react-tippy';
import 'react-tippy/dist/tippy.css';
import './App.css';
import Modal from 'react-modal';


Modal.setAppElement('#root');

const Profile = ({ userInfo, isEditing, handleInputChange, toggleEdit, setUserInfo }) => {
  const [modalIsOpen, setModalIsOpen] = useState(false);

  const [isLoginMode, setIsLoginMode] = useState(false);
  const [isSignUpMode, setIsSignUpMode] = useState(false);


  const openModal = () => setModalIsOpen(true);
  const closeModal = () => setModalIsOpen(false);

  const [showButtons, setShowButtons] = useState(false);

  const handleLoginClick = () => {
      setIsLoginMode(true);
      setIsSignUpMode(false);
      openModal();
  };

  const handleSignUpClick = () => {
      setIsSignUpMode(true);
      setIsLoginMode(false);
      openModal();
  };




    const apiUrl = 'https://csc207-api.joefang.org/user/userinfo';

  // Function to handle data submission to the backend
    const handleSave = async () => {
        // Format the userInfo object
        const formattedUserInfo = {
            firstName: userInfo.firstName.trim(),
            lastName: userInfo.lastName.trim(),
            email: userInfo.email.trim(),
            phoneNumber: userInfo.phoneNumber.trim(),
            address: userInfo.address.trim(),
            subscribed: userInfo.subscribed,
        };

        try {
            await axios.post(apiUrl, formattedUserInfo);
            console.log('User info sent successfully');
            closeModal();
        } catch (error) {
            console.error('Error sending user info:', error);
        }
    };

    const handleLogin = async () => {
        try {
            const response = await axios.post(`${apiUrl}/login`, { identifier: userInfo.identifier.trim() });
            if (response.data) {
                // Handle successful login, e.g., update userInfo state with retrieved data
                setUserInfo(response.data);
                setIsLoginMode(false); // Exit login mode after successful login
                closeModal();
            } else {
                alert('User not found. Please sign up.');
            }
        } catch (error) {
            console.error('Login failed:', error);
            alert('Login failed. Please try again.');
        }
    };


    const handleSaveAndToggleEdit = () => {
        toggleEdit();
        handleSave();
    };

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
                  <button className="login-button" >Login</button>
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
            {/*TODO*/}

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
                    <button onClick={handleLogin} className="save-button">Log In</button>
                </div>
            ) : isSignUpMode ? (
                // Reuse the existing sign-up form logic here
                // For example:
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
        className = 'modal-content'
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
