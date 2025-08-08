// File: Signup.js
import React, { useState } from 'react';
import './Login.css'; // Reuse styling for consistency
import { useNavigate } from 'react-router-dom';
import signupimage from '../assets/generated-image.png'
import axios from 'axios';

function Signup() {
  const navigate = useNavigate();
  const [user, setUser] = useState({
    name: '',
    email: '',
    password: ''
  });

  const handleChange = (e) => {
    setUser({ ...user, [e.target.name]: e.target.value });
  };

  const handleSignup = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post("http://localhost:8080/api/auth/signup", user);
      localStorage.setItem('token', response.data);
      navigate('/');
    } catch (err) {
      alert(err.response?.data || "Signup failed.");
    }
  };

  return (
    <div className="login-page">
      <div className="container d-flex align-items-center justify-content-center min-vh-100">
        <div className="row shadow-lg bg-white rounded overflow-hidden">
          {/* Left Column with Image */}
          <div className="col-md-6 d-none d-md-block bg-light p-0">
            <img 
              src={signupimage} 
              alt="Signup" 
              className="img-fluid h-100" 
              style={{ objectFit: 'cover' }}
            />
          </div>

          {/* Right Column with Form */}
          <div className="col-md-6 p-5">
            <h2 className="mb-4 text-center fw-bold text-primary">Create Account</h2>
            <form onSubmit={handleSignup}>
              <div className="mb-3">
                <label className="form-label">Full Name</label>
                <input 
                  type="text" 
                  name="name" 
                  value={user.name} 
                  onChange={handleChange} 
                  className="form-control form-control-lg" 
                  placeholder="John Doe" 
                  required 
                />
              </div>

              <div className="mb-3">
                <label className="form-label">Email Address</label>
                <input 
                  type="email" 
                  name="email" 
                  value={user.email} 
                  onChange={handleChange} 
                  className="form-control form-control-lg" 
                  placeholder="you@example.com" 
                  required 
                />
              </div>

              <div className="mb-4">
                <label className="form-label">Password</label>
                <input 
                  type="password" 
                  name="password" 
                  value={user.password} 
                  onChange={handleChange} 
                  className="form-control form-control-lg" 
                  placeholder="********" 
                  required 
                />
              </div>

              <button type="submit" className="btn btn-primary w-100 py-2 fs-5">
                Sign Up
              </button>
            </form>

            <p className="mt-3 text-center text-muted">
              Already have an account?{' '}
              <span 
                className="text-decoration-underline text-primary" 
                role="button" 
                onClick={() => navigate('/login')}
              >
                Login here
              </span>
            </p>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Signup;
