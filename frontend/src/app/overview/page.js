"use client";

import React, { useEffect, useState } from "react";
import { useRouter } from "next/navigation";

export default function Dashboard() {
  const router = useRouter();
  const [challenges, setChallenges] = useState([]);
  const [errorMessage, setErrorMessage] = useState("");

  useEffect(() => {
    const fetchChallenges = async () => {
      const myHeaders = new Headers();
      const token = localStorage.getItem("authToken"); // Use saved token
      myHeaders.append("Authorization", `Bearer ${token}`);

      const requestOptions = {
        method: "GET",
        headers: myHeaders,
        redirect: "follow"
      };

      try {
        const response = await fetch("http://localhost:81/api/challenges", requestOptions);
        if (response.ok) {
          const data = await response.json();
          setChallenges(data);
        } else {
          setErrorMessage("Failed to fetch challenges. Please try again.");
        }
      } catch (error) {
        console.error(error);
        setErrorMessage("An error occurred. Please try again later.");
      }
    };

    fetchChallenges();
  }, []);

  return (
    <div style={styles.container}>
      {/* Challenges Section */}
      <div style={styles.challenges}>
        <h2 style={styles.challengesTitle}>Challenges</h2>
        <p style={styles.challengesText}>Complete your daily tasks to earn rewards!</p>
      </div>

      {/* Challenge Containers */}
      <div style={styles.challengeContainer}>
        {challenges.map((challenge) => (
          <div key={challenge.id} style={styles.challengeBox}>
            <h3 style={styles.challengeTitle}>{challenge.title}</h3>
            <p style={styles.challengeDescription}>{challenge.description}</p>
            <p style={styles.challengeStatus}>Status: {challenge.status}</p>
            <div style={styles.progressContainer}>
              <div style={{ ...styles.progressBar, width: `${(challenge.spentAmount / challenge.limit) * 100}%` }}></div>
            </div>
            <p style={styles.progressText}>{challenge.spentAmount} / {challenge.limit}</p>
          </div>
        ))}
      </div>

      {/* Error Message */}
      {errorMessage && <div style={styles.error}>{errorMessage}</div>}

      {/* Navigation Buttons */}
      <div style={styles.navbar}>
        <button style={{ ...styles.navButton, ...styles.activeButton }}>Challenges</button>
        <button style={styles.navButton} onClick={() => router.push("/chicken")}>
          Chicken
        </button>
      </div>
    </div>
  );
}

const styles = {
  container: {
    position: "relative",
    display: "flex",
    flexDirection: "column",
    justifyContent: "flex-start", // Align content to the top
    alignItems: "center",
    height: "100vh",
    width: "100vw",
    backgroundImage: "url('/challengebg2.gif')", // Background image
    backgroundSize: "cover",
    backgroundPosition: "center",
    color: "#FFF3F0", // Text color
    overflow: "hidden",
    padding: "1rem",
  },
  challenges: {
    width: "100%",
    padding: "1rem",
    backgroundColor: "#35D8D5", // Updated background color
    borderRadius: "8px",
    textAlign: "center",
    marginBottom: "1rem",
  },
  challengesTitle: {
    fontSize: "1.5rem",
    fontWeight: "bold",
    color: "#FFF3F0", // Text color for contrast
    marginBottom: "0.5rem",
  },
  challengesText: {
    fontSize: "1rem",
    color: "#FFF3F0", // Text color for contrast
  },
  challengeContainer: {
    width: "100%",
    display: "flex",
    flexDirection: "column",
    gap: "1rem", // Space between challenge boxes
    marginTop: "1rem", // Move containers further up
  },
  challengeBox: {
    width: "100%",
    backgroundColor: "#8C7474", // Updated container color
    borderRadius: "16px", // Increased rounding
    border: "1px solid #FFF3F0", // Slight outline
    padding: "1rem",
    boxShadow: "0 2px 4px rgba(0, 0, 0, 0.1)", // Subtle shadow for depth
  },
  challengeTitle: {
    fontSize: "1.2rem",
    fontWeight: "bold",
    color: "#FFF3F0", // Updated text color
    marginBottom: "0.5rem",
  },
  challengeDescription: {
    fontSize: "1rem",
    color: "#FFF3F0", // Updated text color
  },
  challengeStatus: {
    fontSize: "1rem",
    color: "#FFF3F0", // Updated text color
  },
  progressContainer: {
    width: "100%",
    backgroundColor: "#e0e0e0",
    borderRadius: "25px",
    overflow: "hidden",
    margin: "10px 0",
  },
  progressBar: {
    height: "20px",
    backgroundColor: "#76c7c0",
    transition: "width 0.5s",
  },
  progressText: {
    fontSize: "1rem",
    color: "#FFF3F0",
    textAlign: "center",
  },
  navbar: {
    display: "flex",
    justifyContent: "space-between",
    position: "absolute",
    bottom: 0,
    left: 0,
    width: "100%",
    zIndex: 1,
  },
  navButton: {
    flex: 1,
    padding: "1rem",
    margin: 0, // No margin between buttons
    backgroundColor: "#B5E43C", // Highlight color for buttons
    color: "#2A3D45",
    border: "none",
    fontWeight: "bold",
    cursor: "pointer",
    textAlign: "center",
    transition: "background-color 0.3s",
  },
  activeButton: {
    backgroundColor: "#3B7447", // Updated active button color
    color: "#FFF3F0", // Text color for contrast
  },
  error: {
    color: "#FF6347", // Tomato color for error messages
    marginBottom: "5%",
    textAlign: "center",
  },
};
