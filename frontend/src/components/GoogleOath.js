



const GoogleOauth = ()=>{

    const handleGoogleRedirect = () => {
        // Top-level navigation for OAuth flow (no XHR/fetch)
        window.location.href =
          "https://localhost:8443/oauth2/authorization/google";
      };
return (
<div className="mt-3">
          <button
            type="button"
            className="btn btn-outline-secondary w-100 rounded-3"
            onClick={handleGoogleRedirect}
          >
            Sign in with Google
          </button>
        </div>

)
}

export default GoogleOauth;