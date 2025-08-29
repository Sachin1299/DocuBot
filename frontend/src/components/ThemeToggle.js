import React, { useState, useEffect } from "react";
import { Sun, Moon } from "lucide-react"; // optional icons

function ThemeToggle() {
  // Check saved theme or fallback to light
  const [theme, setTheme] = useState(localStorage.getItem("theme") || "dark");

  // Apply theme whenever it changes
  useEffect(() => {
    document.documentElement.setAttribute("data-theme", theme);
    localStorage.setItem("theme", theme);
  }, [theme]);

  return (
    <button
      className="btn btn-outline-secondary"
      onClick={() => setTheme(theme === "light" ? "dark" : "light")}
    >
      {theme === "light" ? <Moon size={18} /> : <Sun size={18} />}
      {theme === "light" ? " Dark Mode" : " Light Mode"}
    </button>
  );
}

export default ThemeToggle;
