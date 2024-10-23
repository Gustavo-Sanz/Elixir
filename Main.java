package com.elixir;

import java.util.ArrayList;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;
import io.github.cdimascio.dotenv.Dotenv;
import com.merakianalytics.orianna.Orianna;
import com.merakianalytics.orianna.types.core.champion.ChampionRotation;
import com.merakianalytics.orianna.types.core.staticdata.Champion;
import com.merakianalytics.orianna.types.common.Region;
import com.merakianalytics.orianna.types.core.staticdata.Versions;

public class Main {
    public static void main(String[] args) {

        ArrayList<Integer> championIds = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        boolean condition = false;


        // loading dotenv variables
        Dotenv dotenv = Dotenv.load();
        String riotapikey = dotenv.get("API_KEY");

        // setting riot API key
        Orianna.setRiotAPIKey(riotapikey);
        Orianna.setDefaultRegion(Region.NORTH_AMERICA);

        // greeting the user and having them select and option
        System.out.println("\n-------------[Elixir]-------------");
        System.out.println("Please Selection an option below:");
        System.out.println("1.Get current game version & link to patch notes \n2.Get current free champion rotation \n3.Exit program");

        int userSelection = scanner.nextInt();

        while(!condition) {
            switch(userSelection) {
                case 1:
                    //api call to get version
                    final Versions versions = Versions.withRegion(Region.NORTH_AMERICA).get();
    
                    System.out.println("\nHere is the latest patch notes:");
    
                    // getting latest version of league and concatenating it into a link for the user
                    String version = versions.get(0);
                    String formattedVersion = formatVersion(version);
                    String url = "https://www.leagueoflegends.com/en-us/news/game-updates/patch-" + formattedVersion + "-notes/";
                    System.out.println(url);

                    System.out.println("\nPlease select another option or enter 3 to exit:");
                    userSelection = scanner.nextInt();
                    break;
                case 2:
                    //api call to get champion rotation
                    final ChampionRotation rotation = ChampionRotation.withRegion(Region.NORTH_AMERICA).get();
                    System.out.println("\nHeres this weeks free champions:");
                    // convert the ChampionRotation to JSON
                    JSONObject jsonRotation = new JSONObject(rotation.toJSON());
                    JSONArray freeChampionIds = jsonRotation.getJSONArray("freeChampionIds");
    
                    // populate championIds with free champion IDs
                    for(int i = 0; i < freeChampionIds.length(); i++) {
                        championIds.add(freeChampionIds.getInt(i));
                    }
    
                    // print champion names based on their IDs
                    for(int id : championIds) {
                        final Champion champion = Champion.withId(id).get();
                        System.out.println(champion.getName());
                    }

                    System.out.println("\nPlease select another option or enter 3 to exit:");
                    userSelection = scanner.nextInt();
                    break;
                case 3:
                    System.out.println("Program will now exit, thank you for using elixir!");
                    condition = true;
                    break;
                default:
                    System.out.println("\nError: Invalid selection!");
                    System.out.println("Please enter a valid option or enter 3 to exit:");
                    userSelection = scanner.nextInt();
            }
        }
        scanner.close();
        System.exit(0);
    }


    public static String formatVersion(String version) {
        String formattedVersion = version.replace(".", "-");
    
        if (formattedVersion.endsWith("-1")) {
            formattedVersion = formattedVersion.substring(0, formattedVersion.lastIndexOf("-1"));
        }
    
        return formattedVersion;
    }

}
