"use client";

import React, { useState } from "react";
import { useRouter } from "next/navigation";

export default function Page() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const router = useRouter();

  const handleSubmit = async (e) => {
    e.preventDefault();
    console.log("Username:", username);
    console.log("Password:", password);

    const myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");
    myHeaders.append("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhY2Nfc29maWFfZ3JpbW0iLCJpYXQiOjE3NDU1MzA5MDksImV4cCI6MTc0NTYxNzMwOX0.W7cYB6IUKLElU2KEZQzYr79VvcCknfenO6bT-aRPyPw");

    const raw = JSON.stringify({
      "username": username,
      "password": password
    });

    const requestOptions = {
      method: "POST",
      headers: myHeaders,
      body: raw,
      redirect: "follow"
    };

    try {
      const response = await fetch("http://localhost:81/api/auth/login", requestOptions);
      if ((response.status === 400) || (response.status === 401)) {
        setErrorMessage("Invalid username or password. Please try again.");
      } else {
        const result = await response.json(); // Parse JSON
        console.log("Login response:", result);
        localStorage.setItem("authToken", result.token);
        console.log(result);
        // Simulate login success and navigate to the dashboard
        router.push("/chicken");
      }
    } catch (error) {
      console.error(error);
      setErrorMessage("An error occurred. Please try again later.");
    }
  };

  return (
    <div style={styles.container}>
      <div style={styles.header}>
        <h1 style={styles.appName}>Chicken App</h1>
      </div>
      <form onSubmit={handleSubmit} style={styles.form}>
        {errorMessage && <div style={styles.error}>{errorMessage}</div>}
        <div style={styles.inputGroup}>
          <label htmlFor="username" style={styles.label}>
            Username
          </label>
          <input
            type="text"
            id="username"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            style={styles.input}
            required
          />
        </div>
        <div style={styles.inputGroup}>
          <label htmlFor="password" style={styles.label}>
            Password
          </label>
          <input
            type="password"
            id="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            style={styles.input}
            required
          />
        </div>
        <button type="submit" style={styles.button}>
          Login
        </button>
        <button type="button" style={{ ...styles.button, ...styles.registerButton }}>
          Register
        </button>
      </form>
    </div>
  );
}

const styles = {
  container: {
    display: "flex",
    flexDirection: "column",
    justifyContent: "center",
    alignItems: "center",
    height: "90vh", // Cut the top 10% of the image
    width: "100%", // Full width
    backgroundImage: "url('/login_background.gif')", // Background image
    backgroundSize: "cover",
    backgroundPosition: "center top", // Cut the top 10%
    padding: "0", // Remove padding
    margin: "0", // Remove margin
  },
  header: {
    marginBottom: "5%",
  },
  appName: {
    color: "#FFB347", // Orange color for the app name
    fontSize: "10vw",
    textAlign: "center",
    fontWeight: "bold",
    textShadow: "2px 2px #FFD700", // Golden shadow for a fun effect
  },
  form: {
    backgroundColor: "rgba(139, 69, 19, 0.9)", // Slightly transparent dark brown
    padding: "5%",
    borderRadius: "8px",
    boxShadow: "0 4px 8px rgba(0, 0, 0, 0.2)",
    width: "90%",
    maxWidth: "90%",
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
  },
  title: {
    color: "#FFF3E0", // Light color for the title
    textAlign: "center",
    marginBottom: "5%",
  },
  inputGroup: {
    marginBottom: "5%",
    backgroundColor: "#A0522D", // Brighter brown for the input sections
    padding: "2%",
    borderRadius: "4px",
    width: "100%",
    boxShadow: "0 2px 4px rgba(0, 0, 0, 0.2)",
  },
  label: {
    display: "block",
    marginBottom: "2%",
    color: "#FFF3E0", // Light color for the labels
    fontWeight: "bold",
  },
  input: {
    width: "100%",
    padding: "2%",
    border: "1px solid #FFB347", // Orange border
    borderRadius: "4px",
    fontSize: "1rem",
    color: "#2A3D45", // Reverted text color to the original
    backgroundColor: "#FFF3E0", // Reverted background color to the original
  },
  button: {
    width: "100%",
    padding: "3%",
    backgroundColor: "#FFD700", // Golden color for the login button
    color: "#2A3D45",
    border: "none",
    borderRadius: "4px",
    fontSize: "1rem",
    fontWeight: "bold",
    cursor: "pointer",
    transition: "background-color 0.3s",
    marginBottom: "2%",
  },
  registerButton: {
    backgroundColor: "#FFB347", // Orange color for the register button
  },
  error: {
    color: "#FF6347", // Tomato color for error messages
    marginBottom: "5%",
    textAlign: "center",
  },
};
