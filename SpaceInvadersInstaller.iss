[Setup]
; Información básica del instalador
AppName=Space Invaders
AppVersion=1.0
DefaultDirName={pf}\Space Invaders
DefaultGroupName=Space Invaders
OutputDir=dist
OutputBaseFilename=SpaceInvadersInstaller
Compression=lzma
SolidCompression=yes

[Files]
; Archivos a incluir en el instalador
Source: "SpaceInvaders.exe"; DestDir: "{app}"; Flags: ignoreversion
Source: "SpaceInvaders.jar"; DestDir: "{app}"; Flags: ignoreversion
Source: "player.png"; DestDir: "{app}"; Flags: ignoreversion
Source: "alien.png"; DestDir: "{app}"; Flags: ignoreversion
Source: "background.png"; DestDir: "{app}"; Flags: ignoreversion
Source: "music.wav"; DestDir: "{app}"; Flags: ignoreversion
Source: "shoot.wav"; DestDir: "{app}"; Flags: ignoreversion
Source: "explosion.wav"; DestDir: "{app}"; Flags: ignoreversion
Source: "gameover.wav"; DestDir: "{app}"; Flags: ignoreversion
Source: "victory.wav"; DestDir: "{app}"; Flags: ignoreversion
Source: "icon.ico"; DestDir: "{app}"; Flags: ignoreversion

[Icons]
; Crear iconos en el menú de inicio y escritorio
Name: "{group}\Space Invaders"; Filename: "{app}\SpaceInvaders.exe"
Name: "{commondesktop}\Space Invaders"; Filename: "{app}\SpaceInvaders.exe"
