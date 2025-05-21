"use client";

import React, { useState } from "react";
import { useRouter } from "next/navigation";

export default function Dashboard() {
  const router = useRouter();
  const [experience, setExperience] = useState(0);
  const [level, setLevel] = useState(5); // Hardcoded level for now
  const [experienceNeeded, setExperienceNeeded] = useState(100);
  const [showLevelUp, setShowLevelUp] = useState(false); // State to control the Level Up GIF visibility
  const [rewardClaimed, setRewardClaimed] = useState(false); // State to track if the reward is claimed

  const handleExperienceChange = (value) => {
    let newExperience = experience + value;
    if (newExperience >= experienceNeeded) {
      newExperience -= experienceNeeded;
      setLevel(level + 1);
      setExperienceNeeded(Math.floor(experienceNeeded * 1.2));

      // Show the Level Up GIF for 5 seconds
      setShowLevelUp(true);
      setTimeout(() => setShowLevelUp(false), 5000);
    }
    setExperience(newExperience);
  };

  const handleClaimReward = () => {
    handleExperienceChange(150); // Add 100 XP
    setRewardClaimed(true); // Mark the reward as claimed
  };

  // Determine which GIF to show based on the level
  const getGif = () => {
    if (level <= 5) {
      return "/egg.gif";
    } else if (level <= 9) {
      return "/chickling_bg.gif";
    } else {
      return "/chicken.gif";
    }
  };

  return (
    <div style={styles.container}>
      {/* Background Image */}
      <div style={styles.background}></div>

      {/* Level Up GIF */}
      {showLevelUp && <img src="/lvl_up.gif" alt="Level Up" style={styles.levelUpGif} />}

      {/* Level Indicator */}
      <div style={styles.levelDisplay}>Level: {level}</div>

      {/* Experience Bar */}
      <div style={styles.experienceContainer}>
        <div style={{ ...styles.experienceBar, width: `${(experience / experienceNeeded) * 100}%` }}></div>
      </div>

      {/* Dynamic GIF */}
      <img src={getGif()} alt="Dynamic GIF" style={styles.gif} />

      {/* Claim Reward Button */}
      {!rewardClaimed && (
        <div style={styles.buttonContainer}>
          <button style={styles.experienceButton} onClick={handleClaimReward}>
            Claim Reward!
          </button>
        </div>
      )}

      {/* Navigation Buttons */}
      <div style={styles.navbar}>
        <button style={styles.navButton} onClick={() => router.push("/overview")}>
          Challenges
        </button>
        <button style={{ ...styles.navButton, ...styles.activeButton }}>Chicken</button>
      </div>
    </div>
  );
}

const styles = {
  container: {
    position: "relative", // To position child elements relative to this container
    display: "flex",
    flexDirection: "column",
    justifyContent: "space-between",
    alignItems: "center",
    height: "100vh",
    width: "100vw",
    overflow: "hidden",
  },
  background: {
    position: "absolute",
    top: 0,
    left: 0,
    width: "100%",
    height: "100%",
    backgroundImage: "url('/background2.jpg')", // Path to the background image
    backgroundSize: "cover",
    backgroundPosition: "center",
    zIndex: -1, // Ensure it is the furthest back layer
  },
  levelUpGif: {
    position: "absolute",
    top: "10px",
    left: "50%",
    transform: "translateX(-50%)",
    width: "350px", // Adjust size as needed
    height: "auto",
    zIndex: 1, // Ensure it appears above other elements
  },
  gif: {
    width: "400px",
    height: "auto",
    position: "absolute",
    bottom: "150px", // Move the GIF higher above the navigation buttons
    left: "50%",
    transform: "translateX(-50%)",
    zIndex: 0, // Ensure it appears above the background
  },
  buttonContainer: {
    display: "flex",
    justifyContent: "center",
    marginBottom: "500px", // Add space below the experience bar
    zIndex: 2, // Ensure buttons appear above other elements
  },
  experienceButton: {
    padding: "0.5rem 1rem",
    margin: "0 0.5rem",
    backgroundColor: "#FFA500", // Green button
    color: "#FFF",
    border: "none",
    borderRadius: "4px",
    fontWeight: "bold",
    cursor: "pointer",
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
  experienceContainer: {
    width: "80%",
    backgroundColor: "#e0e0e0",
    borderRadius: "25px",
    overflow: "hidden",
    margin: "20px auto 10px", // Add margin below the level indicator
    zIndex: 0,
  },
  experienceBar: {
    height: "30px",
    backgroundColor: "#D41E3C",
    transition: "width 0.5s",
  },
  levelDisplay: {
    position: "absolute",
    top: "20px",
    left: "50%",
    transform: "translateX(-50%)",
    color: "#3B7447",
    fontSize: "1.5rem",
    fontWeight: "bold",
    zIndex: 1,
  },
};
