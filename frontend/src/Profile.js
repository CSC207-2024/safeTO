import React, { useState } from 'react';
import { Tooltip } from 'react-tippy';
import 'react-tippy/dist/tippy.css';
import './App.css';

const Profile = ({ userInfo, isEditing, handleInputChange, toggleEdit }) => {
  return (
      <Tooltip title="User Profile" position="bottom" trigger="mouseenter">
        <img
            src="https://img.icons8.com/ios-filled/50/000000/user.png"
            alt="User Icon"
            style={{ borderRadius: '50%', cursor: 'pointer' }}
        />
        <div className="user-profile">
          <div>
            <label htmlFor="firstName">First Name:</label>
            <input
                type="text"
                id="firstName"
                name="firstName"
                value={userInfo.firstName}
                disabled={!isEditing}
                onChange={handleInputChange}
            />
          </div>
          <div>
            <label htmlFor="lastName">Last Name:</label>
            <input
                type="text"
                id="lastName"
                name="lastName"
                value={userInfo.lastName}
                disabled={!isEditing}
                onChange={handleInputChange}
            />
          </div>
          <div>
            <label htmlFor="email">Email:</label>
            <input
                type="email"
                id="email"
                name="email"
                value={userInfo.email}
                disabled={!isEditing}
                onChange={handleInputChange}
            />
          </div>
          <div>
            <label htmlFor="phoneNumber">Phone Number:</label>
            <input
                type="tel"
                id="phoneNumber"
                name="phoneNumber"
                value={userInfo.phoneNumber}
                disabled={!isEditing}
                onChange={handleInputChange}
            />
          </div>
          <button onClick={toggleEdit}>
            {isEditing ? 'Save' : 'Edit'}
          </button>
        </div>
      </Tooltip>
  );
};

export default Profile;
