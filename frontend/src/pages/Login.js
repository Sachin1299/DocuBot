// File: src/pages/Login.js
import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import "./Login.css";
import GoogleOauth from "../components/GoogleOath";

function Login() {
  const navigate = useNavigate();

  // Form state
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  // UI state
  const [error, setError] = useState("");
  const [showGoogleCta, setShowGoogleCta] = useState(false);
  const [loading, setLoading] = useState(false);

  // Redirect if already authenticated
  useEffect(() => {
    const checkAuth = async () => {
      try {
        const res = await axios.get("https://localhost:8443/api/auth/check", {
          withCredentials: true,
        });
        if (res.status === 200) {
          navigate("/home");
        }
      } catch (err) {
        // Expected when not authenticated; ignore 401/403.
        if (err.response?.status === 401 || err.response?.status === 403) {
          return;
        }
        console.error("Unexpected auth check error:", err);
      }
    };
    checkAuth();
  }, [navigate]);



  const handleLogin = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError("");
    setShowGoogleCta(false);

    try {
      const res = await axios.post(
        "https://localhost:8443/api/auth/login",
        { email, password },
        { withCredentials: true }
      );
      console.log("Login success:", res.status, res.data);
      navigate("/home");
    } catch (err) {
      if (err.response) {
        const { status, data } = err.response;
        const serverCode = data?.code;
        const serverMessage =
          data?.message ||
          data?.error ||
          data?.detail ||
          (typeof data === "string" ? data : null);

        if (status === 409 && serverCode === "ACCOUNT_GOOGLE_ONLY") {
          setError(
            serverMessage ||
              "This account uses Google Sign-In. Please continue with Google."
          );
          setShowGoogleCta(true);
        } else if (status === 401) {
          setError(serverMessage || "Invalid credentials. Please try again.");
        } else if (status === 400) {
          setError(serverMessage || "Bad request. Please check your input.");
        } else if (status === 403) {
          setError(serverMessage || "Not allowed to login. Contact support.");
        } else {
          setError(serverMessage || "Something went wrong. Please try again.");
        }
      } else if (err.request) {
        // Network/CORS/SSL issues: no response received
        setError("Network error. Check connection or certificate trust.");
      } else {
        // Config/runtime error
        setError("Unexpected error. Please try again.");
      }
    } finally {
      setLoading(false);
    }
  };

  const onEmailChange = (e) => {
    setEmail(e.target.value);
    if (error) setError("");
    if (showGoogleCta) setShowGoogleCta(false);
  };

  const onPasswordChange = (e) => {
    setPassword(e.target.value);
    if (error) setError("");
    if (showGoogleCta) setShowGoogleCta(false);
  };

  return (
    <div className="d-flex align-items-center justify-content-center vh-100 login-page">
      <div className="card shadow p-4 rounded-4" style={{ maxWidth: "400px", width: "100%" }}>
        <h2 className="text-center mb-4">Welcome Back 👋</h2>

        {error && <div className="alert alert-danger">{error}</div>}

        {showGoogleCta}

        <form onSubmit={handleLogin}>
          <div className="mb-3">
            <label className="form-label">Email address</label>
            <input
              type="email"
              className="form-control rounded-3"
              value={email}
              onChange={onEmailChange}
              required
              placeholder="Enter your email"
              autoComplete="email"
            />
          </div>

          <div className="mb-3">
            <label className="form-label">Password</label>
            <input
              type="password"
              className="form-control rounded-3"
              value={password}
              onChange={onPasswordChange}
              required
              placeholder="Enter your password"
              autoComplete="current-password"
            />
          </div>

          <button
            type="submit"
            className="btn btn-primary w-100 rounded-3"
            disabled={loading}
          >
            {loading ? "Signing in..." : "Login"}
          </button>
        </form>

        {/* Optional: always-visible Google button for convenience */}
        <GoogleOauth/>

        <p className="text-center mt-3 mb-0">
          Don’t have an account?{" "}
          <span
            className="text-primary"
            style={{ cursor: "pointer" }}
            onClick={() => navigate("/signup")}
          >
            Sign up
          </span>
        </p>
      </div>
    </div>
  );
}

export default Login;
