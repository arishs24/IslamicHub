import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.prefs.Preferences;
import org.json.JSONArray;
import org.json.JSONObject;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

// Main class for the PrayerTime application
public class PrayerTime extends Application {
    private MediaPlayer mediaPlayer;

    // Main method to launch the application
    public static void main(String[] args) {
        launch(args);
    }

    // Start method to set up the primary stage
    @Override
    public void start(Stage primaryStage) {

        Media azanMedia = new Media(getClass().getResource("assets/azan1.mp3").toExternalForm());
        mediaPlayer = new MediaPlayer(azanMedia);

        primaryStage.setTitle("Muslim Hub"); // Set the title of the window
        primaryStage.setScene(createMainScene(primaryStage)); // Set the scene of the window
        primaryStage.show(); // Display the window
    }

    // Method to create the main scene
    private Scene createMainScene(Stage primaryStage) {
        UserSettings settings = new UserSettings(Preferences.userNodeForPackage(PrayerTime.class));
        Image backgroundImage = new Image("assets/bg.jpeg");
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(675); // Adjust to your scene's width
        backgroundView.setFitHeight(450); // Adjust to your scene's height
        backgroundView.setPreserveRatio(false);

        Image appIcon = new Image("assets/logo.png");
        primaryStage.getIcons().add(appIcon);

        Image logoImage = new Image("assets/logo.png");
        ImageView logoView = new ImageView(logoImage);
        logoView.setFitWidth(150); // Adjust logo width
        logoView.setFitHeight(150); // Adjust logo height
        logoView.setPreserveRatio(true);

        Label titleLabel = new Label("Islamic Hub");
        titleLabel.setStyle("-fx-font-size: 30px; -fx-text-fill: white; -fx-font-weight: bold;"); // Title style

        // Create Start Adhan button
        Button startAzanButton = new Button("Start Adhan");
        startAzanButton.setOnAction(e -> mediaPlayer.play());

        // Create Stop Adhan button
        Button stopAzanButton = new Button("Stop Adhan");
        stopAzanButton.setOnAction(e -> mediaPlayer.stop());

        HBox adhanButtons = new HBox(10, startAzanButton, stopAzanButton);
        adhanButtons.setAlignment(Pos.CENTER_RIGHT);

        VBox prayerLayout = new VBox(10); // Vertical box layout with spacing of 10
        prayerLayout.setAlignment(Pos.TOP_RIGHT); // Align content to the top right
        prayerLayout.setPadding(new Insets(20)); // Set padding around the VBox

        // Create an HBox for the logo and title
        HBox headerBox = new HBox(10, logoView, titleLabel); // 10 is the spacing between logo and title
        headerBox.setAlignment(Pos.CENTER_LEFT);

        // Create and style buttons
        Button settingsButton = styleButton(new Button("Settings"));
        Button nearbyMosquesButton = styleButton(new Button("Nearby Mosques"));
        Button quranButton = styleButton(new Button("Quran"));
        Button halalRestaurantsButton = styleButton(new Button("Halal Restaurants"));

        // Set actions for buttons
        settingsButton.setOnAction(e -> showSettingsPage(primaryStage, settings));
        nearbyMosquesButton.setOnAction(e -> showNearbyMosques(primaryStage, settings));
        quranButton.setOnAction(e -> showQuranPage(primaryStage));
        halalRestaurantsButton.setOnAction(e -> showHalalRestaurantsPage(primaryStage, settings));

        // Add buttons to the layout
        prayerLayout.getChildren().addAll(settingsButton, nearbyMosquesButton, quranButton, halalRestaurantsButton,
                adhanButtons);
        prayerLayout.setAlignment(Pos.CENTER_RIGHT);

        // Add the headerBox to the top of layout
        StackPane root = new StackPane();
        root.getChildren().addAll(backgroundView); // Add backgroundView first, then contentLayout
        root.getChildren().add(headerBox); // Add the headerBox before the prayerLayout
        root.getChildren().add(prayerLayout);
        headerBox.setPadding(new Insets(10, 0, 0, 10)); // Adjust padding for headerBox

        // Retrieve user settings
        String city = settings.getSetting("city");
        String province = settings.getSetting("province");
        String country = settings.getSetting("country");

        // Display prayer times or a message to set location
        if (!city.isEmpty() && !country.isEmpty()) {
            displayPrayerTimes(city, country, province, primaryStage, prayerLayout);
        } else {
            Label locationSettingLabel = new Label("Please set your location in settings.");
            locationSettingLabel.setStyle("-fx-text-fill: white;"); // Set style for the label

            Label northAmericaLabel = new Label("Only places in North America.");
            northAmericaLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;"); // Set style for the label

            prayerLayout.getChildren().addAll(locationSettingLabel, northAmericaLabel);
        }

        return new Scene(root, 675, 450); // Return the created scene
    }

    // Method to style buttons
    private Button styleButton(Button button) {
        button.setMinWidth(150); // Set minimum width
        button.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;"); // Set style
        return button; // Return the styled button
    }

    // Method to create a back button
    private Button createBackButton(Stage primaryStage) {
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> primaryStage.setScene(createMainScene(primaryStage))); // Set action to go back to
                                                                                           // main scene
        return backButton; // Return the created button
    }

    // Method to show the halal restaurants page
    private void showHalalRestaurantsPage(Stage primaryStage, UserSettings settings) {

        // Retrieve user settings
        String city = settings.getSetting("city");
        String country = settings.getSetting("country");

        VBox mainLayout = new VBox(10); // Vertical box layout
        Button backButton = createBackButton(primaryStage); // Create a back button
        mainLayout.getChildren().add(backButton); // Add back button to layout

        VBox dynamicContentLayout = new VBox(10); // Layout for dynamic content
        mainLayout.getChildren().add(dynamicContentLayout); // Add dynamic layout to main layout

        Scene halalRestaurantsScene = new Scene(mainLayout, 675, 450); // Create a new scene

        // Fetch and display halal restaurants if city and country are set
        if (!city.isEmpty() && !country.isEmpty()) {
            fetchAndDisplayHalalRestaurants(city, country, dynamicContentLayout);
        } else {
            dynamicContentLayout.getChildren().add(new Label("Please set your city and country in settings."));
        }

        primaryStage.setScene(halalRestaurantsScene); // Set the scene to the stage
    }

    // Method to fetch and display halal restaurants
    private void fetchAndDisplayHalalRestaurants(String city, String country, VBox halalRestaurantsLayout) {
        HttpClient httpClient = HttpClient.newHttpClient(); // Create a new HTTP client
        // Construct API URL
        String apiUrl = "https://api.tomtom.com/search/2/search/halal%20restaurants%20in%20" + city.replaceAll(" ", "")
                + "%2C%20" + country.replaceAll(" ", "")
                + ".json?minFuzzyLevel=1&maxFuzzyLevel=2&categorySet=7315&view=Unified&relatedPois=off&key=ZdGWmcuKgGHSgClGDmLmkjETGeTGHAKF";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .build(); // Build the HTTP request

        // Send the request asynchronously and process the response
        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(responseBody -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(responseBody); // Parse the response body
                        JSONArray results = jsonResponse.getJSONArray("results"); // Get results array

                        updateHalalRestaurantsLayout(halalRestaurantsLayout, results); // Update the layout with results
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                })
                .exceptionally(e -> {
                    e.printStackTrace();
                    return null;
                });
    }

    // Method to update the halal restaurants layout
    private void updateHalalRestaurantsLayout(VBox halalRestaurantsLayout, JSONArray results) {
        javafx.application.Platform.runLater(() -> {
            halalRestaurantsLayout.getChildren().clear(); // Clear existing content
            for (int i = 0; i < results.length(); i++) {
                JSONObject result = results.getJSONObject(i); // Get each result

                // Extract details from the result
                String name = result.getJSONObject("poi").getString("name");
                String address = result.getJSONObject("address").getString("freeformAddress");
                String phone = result.getJSONObject("poi").optString("phone", "No phone number available");

                // Create labels for each detail
                Label nameLabel = new Label("Name: " + name);
                Label addressLabel = new Label("Address: " + address);
                Label phoneLabel = new Label("Phone: " + phone);

                // Create a box for each restaurant and add it to the layout
                VBox restaurantBox = new VBox(5, nameLabel, addressLabel, phoneLabel);
                restaurantBox.setStyle(
                        "-fx-padding: 10; -fx-border-style: solid inside; -fx-border-width: 2; -fx-border-insets: 5; -fx-border-radius: 5; -fx-border-color: gray;");

                halalRestaurantsLayout.getChildren().add(restaurantBox);
            }
        });
    }

    // Method to show the Quran page
    private void showQuranPage(Stage primaryStage) {
        ComboBox<Surah> surahComboBox = new ComboBox<>(); // Create a ComboBox for Surahs
        fetchSurahList(surahComboBox); // Fetch and populate the Surah list

        TextArea surahText = new TextArea(); // Create a TextArea for Surah text
        surahText.setEditable(false); // Make the TextArea non-editable

        // Set an action when a Surah is selected
        surahComboBox.setOnAction(e -> {
            Surah selectedSurah = surahComboBox.getSelectionModel().getSelectedItem();
            fetchAndDisplaySurah(selectedSurah.getNumber(), surahText);
        });

        VBox quranLayout = new VBox(10, surahComboBox, surahText); // Create a layout for the Quran page
        Scene quranScene = new Scene(quranLayout, 675, 600); // Create a new scene
        primaryStage.setScene(quranScene); // Set the scene to the stage

        Button backButton = createBackButton(primaryStage); // Create a back button
        quranLayout.getChildren().add(backButton); // Add the back button to the layout
    }

    // Method to fetch the list of Surahs
    private void fetchSurahList(ComboBox<Surah> surahComboBox) {
        HttpClient httpClient = HttpClient.newHttpClient(); // Create a new HTTP client
        String apiUrl = "http://api.alquran.cloud/v1/quran/en.asad"; // API URL for Surah list

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .build(); // Build the HTTP request

        // Send the request asynchronously and process the response
        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(responseBody -> {
                    JSONObject jsonResponse = new JSONObject(responseBody); // Parse the response body
                    JSONObject data = jsonResponse.getJSONObject("data"); // Get the data object
                    JSONArray surahs = data.getJSONArray("surahs"); // Get the Surahs array

                    javafx.application.Platform.runLater(() -> {
                        for (int i = 0; i < surahs.length(); i++) {
                            JSONObject surah = surahs.getJSONObject(i); // Get each Surah
                            int number = surah.getInt("number"); // Get Surah number
                            String name = surah.getString("englishName"); // Get Surah name
                            surahComboBox.getItems().add(new Surah(number, name)); // Add Surah to ComboBox
                        }
                    });
                })
                .exceptionally(e -> {
                    e.printStackTrace();
                    return null;
                });
    }

    // Method to fetch and display a specific Surah from the Quran
    private void fetchAndDisplaySurah(int surahNumber, TextArea surahText) {
        // Create a new HTTP client
        HttpClient httpClient = HttpClient.newHttpClient();

        // Define the API URL for fetching the Surah
        String apiUrl = "http://api.alquran.cloud/v1/surah/" + surahNumber + "/en.asad";

        // Build the HTTP request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .build();

        // Send the request asynchronously
        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(responseBody -> {
                    try {
                        // Parse the response body to JSON
                        JSONObject jsonResponse = new JSONObject(responseBody);
                        JSONObject data = jsonResponse.getJSONObject("data");
                        JSONArray ayahs = data.getJSONArray("ayahs");

                        // StringBuilder to accumulate Surah content
                        StringBuilder surahContent = new StringBuilder();
                        for (int i = 0; i < ayahs.length(); i++) {
                            JSONObject ayah = ayahs.getJSONObject(i);

                            // Append each Ayah's number and text
                            surahContent.append(ayah.getInt("numberInSurah"))
                                    .append(". ")
                                    .append(ayah.getString("text"))
                                    .append("\n");
                        }

                        // Update the TextArea in the JavaFX Application Thread
                        javafx.application.Platform.runLater(() -> {
                            surahText.setText(surahContent.toString());
                        });
                    } catch (Exception e) {
                        // Print stack trace in case of an exception
                        e.printStackTrace();
                    }
                })
                .exceptionally(e -> {

                    // Handle exceptions
                    e.printStackTrace();
                    return null;
                });
    }

    // Method to show the halal restaurants page
    private void showNearbyMosques(Stage primaryStage, UserSettings settings) {

        // Retrieve user settings
        String city = settings.getSetting("city");
        String country = settings.getSetting("country");
        String province = settings.getSetting("province");

        VBox mainLayout = new VBox(10); // Vertical box layout
        Button backButton = createBackButton(primaryStage); // Create a back button
        mainLayout.getChildren().add(backButton); // Add back button to layout

        VBox dynamicContentLayout1 = new VBox(10); // Layout for dynamic content
        mainLayout.getChildren().add(dynamicContentLayout1); // Add dynamic layout to main layout

        Scene nearbyMosquesScene = new Scene(mainLayout, 675, 450); // Create a new scene

        // Fetch and display halal restaurants if city and country are set
        if (!city.isEmpty() && !country.isEmpty()) {
            fetchAndDisplayNearbyMosques(city, country, province, dynamicContentLayout1);
        } else {
            dynamicContentLayout1.getChildren().add(new Label("Please set your city and country in settings."));
        }
        primaryStage.setScene(nearbyMosquesScene); // Set the scene to the stage
    }

    // Method to fetch and display halal restaurants
    private void fetchAndDisplayNearbyMosques(String city, String country, String province, VBox nearbyMosquesLayout) {
        HttpClient httpClient = HttpClient.newHttpClient(); // Create a new HTTP client
        // Construct API URL
        String apiUrl = "https://api.tomtom.com/search/2/search/mosque%20in%20" + city.replaceAll(" ", "") + "%2C%20"
                + province.replaceAll(" ", "") + "%2C%20" + country.replaceAll(" ", "")
                + ".json?minFuzzyLevel=1&maxFuzzyLevel=2&view=Unified&relatedPois=off&key=ZdGWmcuKgGHSgClGDmLmkjETGeTGHAKF";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .build(); // Build the HTTP request

        // Send the request asynchronously and process the response
        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(responseBody -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(responseBody); // Parse the response body
                        JSONArray results = jsonResponse.getJSONArray("results"); // Get results array

                        updateNearbyMosques(nearbyMosquesLayout, results); // Update the layout with results
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                })
                .exceptionally(e -> {
                    e.printStackTrace();
                    return null;
                });
    }

    // Method to update the halal restaurants layout
    private void updateNearbyMosques(VBox nearbyMosquesLayout, JSONArray results) {
        javafx.application.Platform.runLater(() -> {
            nearbyMosquesLayout.getChildren().clear(); // Clear existing content
            for (int i = 0; i < results.length(); i++) {
                JSONObject result = results.getJSONObject(i); // Get each result

                // Extract details from the result
                String name = result.getJSONObject("poi").getString("name");
                String address = result.getJSONObject("address").getString("freeformAddress");
                String phone = result.getJSONObject("poi").optString("phone", "No phone number available");

                // Create labels for each detail
                Label nameLabel = new Label("Name: " + name);
                Label addressLabel = new Label("Address: " + address);
                Label phoneLabel = new Label("Phone: " + phone);

                // Create a box for each restaurant and add it to the layout
                VBox mosquesBox = new VBox(5, nameLabel, addressLabel, phoneLabel);
                mosquesBox.setStyle(
                        "-fx-padding: 10; -fx-border-style: solid inside; -fx-border-width: 2; -fx-border-insets: 5; -fx-border-radius: 5; -fx-border-color: gray;");

                nearbyMosquesLayout.getChildren().add(mosquesBox);
            }
        });
    }

    // Method to show the settings page
    private void showSettingsPage(Stage primaryStage, UserSettings settings) {
        // Create text fields for city, country, and province settings
        TextField cityTextField = new TextField(settings.getSetting("city"));
        cityTextField.setPromptText("Enter your city");

        TextField provinceTextField = new TextField(settings.getSetting("province"));
        provinceTextField.setPromptText("Enter your state/province");

        TextField countryTextField = new TextField(settings.getSetting("country"));
        countryTextField.setPromptText("Enter your country");

        // Create a save button and define its action
        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            // Save settings when clicked
            settings.saveSetting("city", cityTextField.getText());
            settings.saveSetting("province", provinceTextField.getText());
            settings.saveSetting("country", countryTextField.getText());

            // Switch to the main scene
            primaryStage.setScene(createMainScene(primaryStage));
        });

        // Create a clear button and define its action
        Button clearButton = new Button("Clear");
        clearButton.setOnAction(e -> {
            // Clear settings when clicked
            settings.clearSetting("city");
            settings.clearSetting("province");
            settings.clearSetting("country");

            // Clear text fields
            cityTextField.clear();
            provinceTextField.clear();
            countryTextField.clear();
        });

        // Layout for the settings page
        VBox settingsLayout = new VBox(10, cityTextField, provinceTextField, countryTextField, saveButton, clearButton);
        // Create and set the scene for settings
        Scene settingsScene = new Scene(settingsLayout, 675, 450);
        primaryStage.setScene(settingsScene);

        // Add a back button to the settings layout
        Button backButton = createBackButton(primaryStage);
        settingsLayout.getChildren().add(backButton);
    }

    // Method to display prayer times
    private void displayPrayerTimes(String city, String country, String province, Stage primaryStage,
            VBox prayerLayout) {
        // Get the current date
        LocalDate currentDate = LocalDate.now();

        // Create labels for city and country
        Label cityLabel = new Label("City: " + city);
        Label countryLabel = new Label("Country: " + country);

        // Style the labels
        cityLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: black; -fx-font-weight: bold;");
        countryLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: black; -fx-font-weight: bold;");

        prayerLayout.getChildren().addAll(cityLabel, countryLabel);

        // Format the date
        String date = currentDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

        // Define the method for prayer times calculation
        int method = 2;

        // Build the API URL for fetching prayer times
        String apiUrl = String.format(
                "http://api.aladhan.com/v1/timingsByCity/%s?city=%s&country=%s&state=%s&method=%d", date,
                city.replaceAll(" ", ""), country.replaceAll(" ", ""),
                province.replaceAll(" ", ""), method);

        // Create a new HTTP client
        HttpClient httpClient = HttpClient.newHttpClient();

        // Build the HTTP request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .build();

        // Send the request asynchronously
        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(responseBody -> {
                    // Parse the response body to JSON
                    JSONObject jsonResponse = new JSONObject(responseBody);
                    JSONObject data = jsonResponse.getJSONObject("data");
                    JSONObject timings = data.getJSONObject("timings");

                    // Extract prayer times
                    String[] prayers = { timings.getString("Fajr"), timings.getString("Dhuhr"),
                            timings.getString("Asr"), timings.getString("Maghrib"), timings.getString("Isha") };

                    Date dateObject = new Date(date, prayers);

                    // Update the prayer layout with the timings
                    updatePrayerLayout(prayerLayout, dateObject);
                })
                .exceptionally(e -> {
                    // Handle exceptions
                    e.printStackTrace();
                    return null;
                });
    }

    // Method to update the prayer layout with prayer times
    private void updatePrayerLayout(VBox prayerLayout, Date date) {

        Label dateLabel = new Label("Today's Date: " + date.getDate());
        // Create labels for each prayer time
        String[] prayers = date.getPrayers();
        Label fajrLabel = new Label("Fajr: " + prayers[0]);
        Label dhuhrLabel = new Label("Dhuhr: " + prayers[1]);
        Label asrLabel = new Label("Asr: " + prayers[2]);
        Label maghribLabel = new Label("Maghrib: " + prayers[3]);
        Label ishaLabel = new Label("Isha: " + prayers[4]);

        dateLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: white; -fx-font-weight: bold;"); // Title style
        fajrLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: white; -fx-font-weight: bold;"); // Title style
        dhuhrLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: white; -fx-font-weight: bold;"); // Title style
        asrLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: white; -fx-font-weight: bold;"); // Title style
        maghribLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: white; -fx-font-weight: bold;"); // Title style
        ishaLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: white; -fx-font-weight: bold;"); // Title style

        // Update the prayer layout in the JavaFX Application Thread
        javafx.application.Platform.runLater(() -> {
            prayerLayout.getChildren().addAll(fajrLabel, dhuhrLabel, asrLabel, maghribLabel, ishaLabel, dateLabel);
        });
    }
}
