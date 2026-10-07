# Aura_Rng_Game
# AURA RNG 
# A feature-rich Java Swing application featuring an RNG-based aura rolling system, custom 2D visual effects, character avatar modeling, dynamic weather events, upgrades, rebirth mechanics, and a built-in admin developer console.
#------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
# Key Features:
# 42 Unique Auras: Ranging from Common (1 in 2) to ZENITH (1 in 100,000,000,000,000,000).
# Dual-Stage Display:Avatar Showroom: Displays a customizable stick figure avatar styled with dynamic visual particle effects and distinct poses based on the active aura.
# Core Synthesizer: Real-time animated core engine rendering shape-shifting graphics, screen flashes, and camera shake upon rolling rare items.
# Weather Events: Dynamic global events (Solar Flare, Void Storm, Cosmic Alignment) cycling every 10 seconds to multiply roll luck.
# Progressive Economy & Upgrades: Earn Shards based on aura rarity, unlock global luck multipliers, boost auto-roll speeds, and increase shard gain rates.
# Rebirth System: Reset progress to earn persistent rebirth tokens that scale overall luck, starting shards, and shard gain multipliers.
# Aura Codex / Index: Complete tracking of all discovered and locked auras with a dynamic equip option to customize the avatar showroom.
# Developer Admin Console: Built-in modal dialog supporting CLI-style text commands and instant macros for debugging and testing.
# Technical OverviewGUI Framework: Built using pure Java Swing and Graphics2D (no external game engines or frameworks required).
# Render Loop: Uses javax.swing.Timer running at ~60 FPS (16ms interval) driving continuous geometry transformations and particle rendering.
# Thread Safety: All state transitions and UI updates are synchronized using standard swing event dispatch thread paradigms.
# Prerequisites & InstallationRequirementsJava Development Kit (JDK): Version 8 or higher.
# CompilationSave the code into a single file named AuraGame.java, then compile using the Java compiler:Bashjavac AuraGame.java
# Running the ApplicationExecute the compiled byte code:Bashjava AuraGame
# Controls & Gameplay!!! ROLL AURA: Synthesizes a new core and rolls for an aura based on your current luck multiplier.⚙ AUTO: Toggles automatic continuous rolling at the current auto-roll delay speed.EQUIP: Open the lower Aura Index / Codex scroll view and click EQUIP next to any unlocked aura to customize your active avatar graphics.ADMIN PANEL: Opens the Developer Command Console.Developer Admin Console CommandsClick the 🛠 ADMIN PANEL button to open the developer dialog. You can use the quick macro buttons or enter direct text commands:CommandSyntax / ExampleDescriptionShardsgive shards <amount>Grants specified amount of Shards to player balance.Luckgive luck <amount>Increases base luck parameter by specified amount.Rollsgive rolls <amount>Adds specified count directly to total roll statistics.Auragive aura <aura_name>Instantly unlocks and adds 1x of any aura to inventory.Clearclear logFlushes the on-screen live admin console output text.
