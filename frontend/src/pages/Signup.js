// File: Signup.js
import React, { useState } from 'react';
import './Login.css'; // Reuse styling for consistency
import { useNavigate } from 'react-router-dom';
import signupimage from '../assets/generated-image.png';
import axios from 'axios';
import GoogleOauth from '../components/GoogleOath'; // keep if it already performs window.location.href

function Signup() {
  const navigate = useNavigate();

  const [user, setUser] = useState({
    name: '',
    email: '',
    password: ''
  });

  // UI/validation state
  const [error, setError] = useState('');
  const [fieldErrors, setFieldErrors] = useState({ name: '', email: '', password: '' });
  const [loading, setLoading] = useState(false);
  const [showGoogleCta, setShowGoogleCta] = useState(false);

  const handleChange = (e) => {
    const { name, value } = e.target;

    // Update form
    setUser((u) => ({ ...u, [name]: value }));

    // Clear global and field errors on edit
    if (error) setError('');
    if (fieldErrors[name]) {
      setFieldErrors((fe) => ({ ...fe, [name]: '' }));
    }
    if (showGoogleCta) setShowGoogleCta(false);
  };

  // Simple client-side validation
  const validate = () => {
    const next = { name: '', email: '', password: '' };
    let ok = true;

    const nameTrim = user.name.trim();
    const emailTrim = user.email.trim();
    const pass = user.password;

    if (!nameTrim) {
      next.name = 'Full name is required.';
      ok = false;
    } else if (nameTrim.length < 2) {
      next.name = 'Full name must be at least 2 characters.';
      ok = false;
    }

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailTrim) {
      next.email = 'Email is required.';
      ok = false;
    } else if (!emailRegex.test(emailTrim)) {
      next.email = 'Enter a valid email address.';
      ok = false;
    }

    if (!pass) {
      next.password = 'Password is required.';
      ok = false;
    } else if (pass.length < 6) {
      next.password = 'Password must be at least 6 characters.';
      ok = false;
    }

    setFieldErrors(next);
    return ok;
  };

  const handleSignup = async (e) => {
    e.preventDefault();

    setError('');
    setShowGoogleCta(false);

    if (!validate()) return;

    setLoading(true);
    try {
      // Trim only name/email; keep password unchanged
      const payload = {
        name: user.name.trim(),
        email: user.email.trim(),
        password: user.password
      };

      const response = await axios.post('https://localhost:8443/api/auth/signup', payload, {
        withCredentials: true
      });

      // If backend sets cookie and/or returns token, proceed
      console.log('Signup success:', response.status, response.data);
      navigate('/home'); // or wherever you land post-signup
    } catch (err) {
      if (err.response) {
        const { status, data } = err.response;
        const serverCode = data?.code;
        const serverMsg =
          data?.message ||
          data?.error ||
          data?.detail ||
          (typeof data === 'string' ? data : null);

        if (status === 409 && serverCode === 'ACCOUNT_EXISTS_GOOGLE') {
          setError(serverMsg || 'This email is registered with Google Sign-In. Please continue with Google.');
          setShowGoogleCta(true);
        } else if (status === 409 && serverCode === 'EMAIL_ALREADY_IN_USE') {
          setError(serverMsg || 'An account with this email already exists.');
        } else if (status === 422) {
          setError(serverMsg || 'Validation failed. Please review your input.');
        } else if (status === 400) {
          setError(serverMsg || 'Please check your details and try again.');
        } else if (status === 401) {
          setError(serverMsg || 'Not authorized to perform this action.');
        } else {
          setError(serverMsg || 'Something went wrong. Please try again.');
        }
      } else if (err.request) {
        setError('Network error. Check connection or certificate trust.');
      } else {
        setError('Unexpected error. Please try again.');
      }
    } finally {
      setLoading(false);
    }
  };

  // Optional: if your GoogleOath component previously used Axios,
  // ensure it navigates with window.location.href to:
  // https://localhost:8443/oauth2/authorization/google
  const ContinueWithGoogle = () => (
    <div className="mb-3">
      <button
        type="button"
        className="btn btn-outline-danger w-100 rounded-3"
        onClick={() => {
          window.location.href = 'https://localhost:8443/oauth2/authorization/google';
        }}
      >
        Continue with Google
      </button>
    </div>
  );

  return (
    <div className="login-page">
      <div className="container d-flex align-items-center justify-content-center min-vh-100 card">
        <div className="row shadow-lg rounded overflow-hidden">
          {/* Left Column with Image */}
          <div className="col-md-6 d-none d-md-block p-0">
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

            {error && <div className="alert alert-danger">{error}</div>}

            {showGoogleCta && <ContinueWithGoogle />}

            <form onSubmit={handleSignup} noValidate>
              <div className="mb-3">
                <label className="form-label">Full Name</label>
                <input
                  type="text"
                  name="name"
                  value={user.name}
                  onChange={handleChange}
                  className={`form-control form-control-lg ${fieldErrors.name ? 'is-invalid' : ''}`}
                  placeholder="John Doe"
                  autoComplete="name"
                  required
                />
                {fieldErrors.name && <div className="invalid-feedback">{fieldErrors.name}</div>}
              </div>

              <div className="mb-3">
                <label className="form-label">Email Address</label>
                <input
                  type="email"
                  name="email"
                  value={user.email}
                  onChange={handleChange}
                  className={`form-control form-control-lg ${fieldErrors.email ? 'is-invalid' : ''}`}
                  placeholder="you@example.com"
                  autoComplete="email"
                  required
                />
                {fieldErrors.email && <div className="invalid-feedback">{fieldErrors.email}</div>}
              </div>

              <div className="mb-4">
                <label className="form-label">Password</label>
                <input
                  type="password"
                  name="password"
                  value={user.password}
                  onChange={handleChange}
                  className={`form-control form-control-lg ${fieldErrors.password ? 'is-invalid' : ''}`}
                  placeholder="********"
                  autoComplete="new-password"
                  required
                />
                {fieldErrors.password && <div className="invalid-feedback">{fieldErrors.password}</div>}
              </div>

              <button type="submit" className="btn btn-primary w-100 py-2 fs-5" disabled={loading}>
                {loading ? 'Creating account...' : 'Sign Up'}
              </button>
            </form>

            <GoogleOauth/>

            <p className="mt-3 text-center ">
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
