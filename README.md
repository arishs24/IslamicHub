# Muslim Hub
## Purpose: 
The primary purpose of my application is to serve as a comprehensive digital assistant for Muslims, integrating essential aspects of Islamic daily life into a user-friendly and accessible platform. At its core, the application addresses the unique needs of the Muslim community by providing key functionalities such as accurate prayer times, information about nearby mosques and halal restaurants. A significant feature of this application is its capability to personalize experiences based on user preferences and location. Users can set their city, province, and country to receive precise prayer times, ensuring relevance no matter where they are located. The application also includes innovative features such as playing the Adhan (call to prayer). Designed with a clean and intuitive interface, the application strives to be more than just a utility; it seeks to be a daily companion for Muslims.

### How to get it running:
#### Windows Instructions
1. Open a PowerShell terminal in the src folder
2. Enter the following command $javafxPath = "C:\{pathToFolder}\ics4-fpt-semester-1-2023-2024-arishs24\lib\javafx"
3. Enter the following command $javafxLibs = Get-ChildItem -Path $javafxPath -Filter *.jar | ForEach-Object { $_.FullName }
4. Enter the following command $javafxClasspath = $javafxLibs -join ";"
5. Enter the following command javac -cp .`;$javafxClasspath`;C:\{pathToFolder}\ics4-fpt-semester-1-2023-2024-arishs24\json-20231013.jar PrayerTime.java 
(Replace pathToFolder with your path to the folder)
6. Enter the following command java -cp .`;$javafxClasspath`;C:\{pathToFolder}\ics4-fpt-semester-1-2023-2024-arishs24\json-20231013.jar PrayerTime 
(Replace pathToFolder with your path to the folder)

#### Mac instructions
1. Open a terminal in the src folder
2. Enter the following command javac -cp .:/path/to/lib/javafx/*:/path/to/json-20231013.jar PrayerTime.java (Replace path/to/lib with your path to the folder)
3. Enter the following command java -cp .:/path/to/lib/javafx/*:/path/to/json-20231013.jar PrayerTime (Replace path/to/lib with your path to the folder)