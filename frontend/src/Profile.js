import React, { useState } from 'react';
import { Tooltip } from 'react-tippy';
import 'react-tippy/dist/tippy.css';
import './App.css';
import Modal from 'react-modal';


Modal.setAppElement('#root');

const Profile = ({ userInfo, isEditing, handleInputChange, toggleEdit }) => {
  const [modalIsOpen, setModalIsOpen] = useState(false);

  const openModal = () => setModalIsOpen(true);
  const closeModal = () => setModalIsOpen(false);

  return (
    <div>
      <Tooltip title="Profile" position="bottom" trigger="mouseenter">
        <img
          src="/images/user.png"
          alt="User Icon"
          className="profile-image"
          onClick={openModal}
        />
      </Tooltip>
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
                <button onClick={toggleEdit} className="save-button">Save</button>
            </div>
        ) : (
            <div>
                <p><strong>First Name:</strong> {userInfo.firstName}</p>
                <p><strong>Last Name:</strong> {userInfo.lastName}</p>
                <p><strong>Email:</strong> {userInfo.email}</p>
                <p><strong>Phone Number:</strong> {userInfo.phoneNumber}</p>
                <p><strong>Address:</strong> {userInfo.address}</p>
                <button onClick={toggleEdit} className="edit-button">Edit</button>
            </div>
        )}
      </Modal>
    </div>
  );
};

export default Profile;
