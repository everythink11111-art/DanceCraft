# DanceCraft — Minecraft Java 1.21.11 / Fabric

Client-side cosmetic animation mod. It does not send packets, modify server state, or require server installation.

## Controls
- **B** — toggle the dance for your own player model.
- **N** — toggle the dance for the player currently under your crosshair.
- Change keys in **Options → Controls → Key Binds → DanceCraft**.

The animation is local-only. Other players on a third-party server will not see your dance, and they will not know you toggled their local rendering. You can locally animate other player models on your screen.

## Requirements
- Minecraft Java Edition **1.21.11**
- Fabric Loader **0.18.1 or newer**
- Fabric API **0.141.1+1.21.11**
- Java **21**
- Fabric Loom **1.14-SNAPSHOT** (Gradle downloads it during the build)

## Build
Open this folder as a Gradle project in IntelliJ IDEA, or install Gradle and run:

```bash
gradle build
```

The distributable JAR will be in `build/libs/` (the file without `-dev`).

If Gradle asks for a JDK, select Java 21.

## Notes
- This first build is a source project, not a precompiled JAR.
- The supplied clip is a stylized 2D game animation. This implementation translates its broad raised-arm, swaying-body and alternating-leg motion into Minecraft's humanoid model; it is not a frame-perfect 3D recreation.
- This is a cosmetic client mod. It does not bypass server permissions or anti-cheat, and it cannot make remote players see the animation without their client or server supporting it.

## Build on GitHub (no local JDK required)

1. Create a new empty GitHub repository.
2. Upload the contents of this project, including the hidden `.github/workflows/build.yml` file.
3. Open the repository's **Actions** tab.
4. Select **Build DanceCraft** and click **Run workflow**, or push a commit.
5. When the run finishes, open the run and download the `dancecraft-build` artifact.
6. Put the resulting mod JAR in your Minecraft `mods` folder alongside Fabric API.

GitHub Actions installs Temurin Java 21 on its hosted runner, so Java does not need to be installed on your own PC. The workflow uses Gradle 9.2.1 and publishes the JAR files as a downloadable artifact.
