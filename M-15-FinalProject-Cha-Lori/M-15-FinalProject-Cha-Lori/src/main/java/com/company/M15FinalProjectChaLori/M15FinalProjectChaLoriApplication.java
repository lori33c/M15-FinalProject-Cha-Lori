package com.company.M15FinalProjectChaLori;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.InputMismatchException;
import java.util.Scanner;

@SpringBootApplication
public class M15FinalProjectChaLoriApplication {

	public static void main(String[] args) {

		SpringApplication.run(M15FinalProjectChaLoriApplication.class, args);
		int option = 0;
		Scanner scnr = new Scanner(System.in);

		do {
			// Menu
			System.out.println("\n\t\tMain Menu");
			System.out.println("==========================");
			System.out.println("1. Weather in a City");
			System.out.println("2. Location of the International Space Station");
			System.out.println("3. Weather in the Location of the ISS");
			System.out.println("4. Current Cryptocurrency Prices");
			System.out.println("5. Quit");

			// Get User Input
			try {
				System.out.print("Enter your choice: ");
				option = scnr.nextInt();
			}
			catch (InputMismatchException e) {
				System.out.print("You must enter an integer.");
			}

			switch(option) {
				case 1:
					System.out.println("\nWeather in a City");
					System.out.print("Enter a name of city: ");
					String city_name = scnr.next();

					WebClient weather_client = WebClient.create("https://api.openweathermap.org/data/2.5/weather?q=" + city_name + "&appid=14aaf4f5c55fa09f8a2595fd13fef2f6&units=imperial");

					WeatherResponse weatherResponse = null;

					try {
						Mono<WeatherResponse> weather_response = weather_client
								.get()
								.retrieve()
								.bodyToMono(WeatherResponse.class);

						weatherResponse = weather_response.share().block();
					} catch (WebClientResponseException we) {
						int statusCode = we.getRawStatusCode();
						if (statusCode >= 400 && statusCode < 500) {
							System.out.println("Client Error");
						} else if (statusCode >= 500 && statusCode < 600) {
							System.out.println("Server Error");
						}
						System.out.println("Message: " + we.getMessage());
					} catch (Exception e) {
						System.out.println("An error occured: " + e.getMessage());
					}

					if (weatherResponse != null) {
						System.out.println("In " + city_name + " we have: ");
						System.out.println("Current Temperature: " + weatherResponse.main.temp + " Degrees Fahrenheit");
						System.out.println("Weather Conditions: " + weatherResponse.weather[0].main);
					}

					break;
				case 2:
					System.out.println("\nLocation of the International Space Station");

					WebClient iss_client = WebClient.create("http://api.open-notify.org/iss-now.json");

					SpaceResponse spaceResponse = null;

					try {
						Mono<SpaceResponse> iss_response = iss_client
								.get()
								.retrieve()
								.bodyToMono(SpaceResponse.class);

						// Return the current latitude and longitude
						spaceResponse = iss_response.share().block();
					} catch (WebClientResponseException we) {
						int statusCode = we.getRawStatusCode();
						if (statusCode >= 400 && statusCode < 500) {
							System.out.println("Client Error");
						} else if (statusCode >= 500 && statusCode < 600) {
							System.out.println("Server Error");
						}
						System.out.println("Message: " + we.getMessage());
					} catch (Exception e) {
						System.out.println("An error occured: " + e.getMessage());
					}

					double latitude = Double.parseDouble(spaceResponse.iss_position.latitude);
					double longitude = Double.parseDouble(spaceResponse.iss_position.longitude);

					if (spaceResponse != null) {
						// Return the Location of the ISS
						System.out.println("Current Location of the ISS");
						System.out.println("Latitude: " + latitude);
						System.out.println("Longitude: " + longitude);
					}

					// Return the city and country corresponding to those coordinates
					WebClient iss_location_name_client = WebClient.create("https://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon="  + longitude + "&appid=14aaf4f5c55fa09f8a2595fd13fef2f6&&units=imperial");

					WeatherResponse locationWeatherResponse = null;
					try {
						Mono<WeatherResponse> iss_location_name_response = iss_location_name_client
								.get()
								.retrieve()
								.bodyToMono(WeatherResponse.class);

						locationWeatherResponse = iss_location_name_response.share().block();
					} catch (WebClientResponseException we) {
						int statusCode = we.getRawStatusCode();
						if (statusCode >= 400 && statusCode < 500) {
							System.out.println("Client Error");
						} else if (statusCode >= 500 && statusCode < 600) {
							System.out.println("Server Error");
						}
						System.out.println("Message: " + we.getMessage());
					} catch (Exception e) {
						System.out.println("An error occured: " + e.getMessage());
					}

					if (locationWeatherResponse != null) {
						city_name = locationWeatherResponse.name;
						String country_name = locationWeatherResponse.sys.country;

						// if coordinates return null for the country, the app should show that
						// Else print the city and country name corresponding to those coordinates
						if (country_name == null) {
							System.out.println("ISS is not currently in a country.");
						} else {
							System.out.println("The ISS is in " + city_name + ", " + country_name + ".");
						}
					}

					break;
				case 3:
					System.out.println("\nWeather in the Location of the ISS");

					WebClient space_client = WebClient.create("http://api.open-notify.org/iss-now.json");

					SpaceResponse spaceShipResponse = null;

					try {
						Mono<SpaceResponse> space_response = space_client
								.get()
								.retrieve()
								.bodyToMono(SpaceResponse.class);

						spaceShipResponse = space_response.share().block();
					} catch (WebClientResponseException we) {
						int statusCode = we.getRawStatusCode();
						if (statusCode >= 400 && statusCode < 500) {
							System.out.println("Client Error");
						} else if (statusCode >= 500 && statusCode < 600) {
							System.out.println("Server Error");
						}
						System.out.println("Message: " + we.getMessage());
					} catch (Exception e) {
						System.out.println("An error occured: " + e.getMessage());
					}

					latitude = Double.parseDouble(spaceShipResponse.iss_position.latitude);
					longitude = Double.parseDouble(spaceShipResponse.iss_position.longitude);

					if (spaceShipResponse != null) {
						// Return the Location of the ISS
						System.out.println("Current Location of the ISS");
						System.out.println("Latitude: " + latitude);
						System.out.println("Longitude: " + longitude);
					}

					// Return the city and country corresponding to those coordinates
					WebClient iss_weather_client = WebClient.create("https://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon="  + longitude + "&appid=14aaf4f5c55fa09f8a2595fd13fef2f6&&units=imperial");

					WeatherResponse issWeatherResponse = null;

					try {
						Mono<WeatherResponse> iss_weather_response = iss_weather_client
								.get()
								.retrieve()
								.bodyToMono(WeatherResponse.class);

						issWeatherResponse = iss_weather_response.share().block();
					} catch (WebClientResponseException we) {
						int statusCode = we.getRawStatusCode();
						if (statusCode >= 400 && statusCode < 500) {
							System.out.println("Client Error");
						} else if (statusCode >= 500 && statusCode < 600) {
							System.out.println("Server Error");
						}
						System.out.println("Message: " + we.getMessage());
					} catch (Exception e) {
						System.out.println("An error occured: " + e.getMessage());
					}

					if (issWeatherResponse != null) {
						city_name = issWeatherResponse.name;
						String country_name = issWeatherResponse.sys.country;

						// Should return the same thing in option 2
						// And in that location
						if (country_name == null) {
							System.out.println("ISS is not currently in a country.");
						} else {
							System.out.println("The ISS is in " + city_name + ", " + country_name + ".");
						}

						System.out.println("Current Temperature: " + issWeatherResponse.main.temp + " Degrees Fahrenheit");
						System.out.println("Weather Conditions: " + issWeatherResponse.weather[0].main);
					}

					break;
				case 4:
					System.out.println("\nCurrent Cryptocurrency Prices");
					System.out.print("Enter the symbol of a Cryptocurrency (such as BTC or ETH): ");
					String crypto_id = scnr.next();

					WebClient crypto_client = WebClient.create("https://rest.coinapi.io/v1/assets/" + crypto_id + "?apikey=242B0B70-6995-4F32-9775-597810ECA86D");

					CryptoResponse[] cryptoResponse = null;
					try {
						Mono<CryptoResponse[]> crypto_response = crypto_client
								.get()
								.retrieve()
								.bodyToMono(CryptoResponse[].class);

						cryptoResponse = crypto_response.share().block();
					} catch (WebClientResponseException we) {
						int statusCode = we.getRawStatusCode();
						if (statusCode >= 400 && statusCode < 500) {
							System.out.println("Client Error");
						} else if (statusCode >= 500 && statusCode < 600) {
							System.out.println("Server Error");
						}
						System.out.println("Message: " + we.getMessage());
					} catch (Exception e) {
						System.out.println("An error occured: " + e.getMessage());
					}

					System.out.println("name: " + cryptoResponse[0].name);
					System.out.println("asset id: " + cryptoResponse[0].asset_id);
					double price = Double.parseDouble(cryptoResponse[0].price_usd);
					System.out.printf("price: $%.2f", price);
					break;
				case 5:
					System.out.println("Goodbye");
					break;
				default:
					System.out.println("The integer was not between 1 and 5.");
			}
		} while (option != 5);
		System.out.println("Program exited");
	}
}
