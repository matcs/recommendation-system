package main.java.com.firstratings;

import edu.duke.FileResource;
import main.java.com.model.Movie;
import main.java.com.model.Rater;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.util.ArrayList;
import java.util.HashMap;

public class FirstRatings {

    public static void main(String[] args) {
        loadCasesComedy();
        loadCasesMore150Minutes();
        loadDirectors();
        numberOfRatingsForRater();
        maxNumRating(numberOfRatingsForRater());
    }

    public static ArrayList<Movie> loadMovies (String filename) {
        ArrayList<Movie> movieData = new ArrayList<Movie> ();

        FileResource fr = new FileResource("data/" + filename + ".csv");
        CSVParser parser = fr.getCSVParser();

        for (CSVRecord record: parser) {
            String currentID = record.get(0);
            String currentTitle = record.get(1);
            String currentYear = record.get(2);
            String currentCountry = record.get(3);
            String currentGenre = record.get(4);
            String currentDirector = record.get(5);
            int currentMinutes = Integer.parseInt(record.get(6));
            String currentPoster = record.get(7);

            Movie currentMovie = new Movie(currentID, currentTitle, currentYear, currentGenre, currentDirector,
                    currentCountry, currentPoster, currentMinutes);

            movieData.add(currentMovie);
        }

        return movieData;
    }

    public static void loadCasesComedy(){
        ArrayList<Movie> moviesRating = loadMovies("ratedmoviesfull");
        int count = 0;
        for(Movie movies : moviesRating){
            if(movies.getGenres().contains("Comedy")){
                count++;
            }
        }
        System.out.println("Number of comedy movies: " + count);
    }

    public static void loadCasesMore150Minutes(){
        ArrayList<Movie> moviesRating = loadMovies("ratedmoviesfull");
        int count = 0;
        for(Movie movies : moviesRating) {
            if(movies.getMinutes()>150){
                count++;
            }
        }
        System.out.println("Number movies with more than 150 minutes: " + count);
    }

    public static void loadDirectors(){
        ArrayList<Movie> moviesRating = loadMovies("ratedmoviesfull");
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        for(Movie movies : moviesRating){
            String directors [] = movies.getDirector().split(",");
            for (String director : directors ) {
                director = director.trim();
                if (! map.containsKey(director)) {
                    map.put(director, 1);
                } else {
                    map.put(director, map.get(director) + 1);
                }
            }
        }
        int maxNumOfMovies = 0;
        for (String director : map.keySet()) {
            if (map.get(director) > maxNumOfMovies) {
                maxNumOfMovies = map.get(director);
            }
        }
        System.out.println("Max number of by one director: "+maxNumOfMovies);

        ArrayList<String> directors = new ArrayList<String> ();
        for (String director : map.keySet()) {
            if (map.get(director) == maxNumOfMovies) {
                directors.add(director);
            }
        }

        System.out.println("Director with more movies: " + directors);
    }

    public static ArrayList<Rater> loadRaters (String filename) {
        ArrayList<Rater> ratersData = new ArrayList<Rater> ();
        ArrayList<String> listOfIDs = new ArrayList<String> ();

        FileResource fr = new FileResource("data/" + filename + ".csv");
        CSVParser parser = fr.getCSVParser();

        for (CSVRecord record : parser) {
            String currentRaterID = record.get(0);
            String currentMovieID = record.get(1);
            double currentMovieRating = Double.parseDouble(record.get(2));

            if (! listOfIDs.contains(currentRaterID)) {
                Rater currentRater = new Rater(currentRaterID);
                ratersData.add(currentRater);
                currentRater.addRating(currentMovieID, currentMovieRating);

            } else {
                for (int k=0; k < ratersData.size(); k++) {
                    if (ratersData.get(k).getID().equals(currentRaterID)) {
                        ratersData.get(k).addRating(currentMovieID, currentMovieRating);
                    }
                }
            }

            listOfIDs.add(currentRaterID);
        }

        return ratersData;
    }

    public static HashMap<String, HashMap<String, Double>> numberOfRatingsForRater() {
        ArrayList<Rater> raters = loadRaters("ratings");
        HashMap<String, HashMap<String, Double>> hashmap = new HashMap<String, HashMap<String, Double>>();
        for (Rater rater : raters) {
            HashMap<String, Double> ratings = new HashMap<String, Double>();
            ArrayList<String> itemsRated = rater.getItemsRated();

            for (int i = 0; i < itemsRated.size(); i++) {
                String movieID = itemsRated.get(i);
                double movieRating = rater.getRating(movieID);

                ratings.put(movieID, movieRating);
            }
            hashmap.put(rater.getID(), ratings);
        }

        String raterID = "193";
        int ratingsSize = hashmap.get("193").size();
        System.out.println("Number of ratings for the rater " + raterID + " : " + ratingsSize);
        return hashmap;
    }

    public static void maxNumRating(HashMap<String, HashMap<String, Double>> hashmap){
        int maxNumOfRatings = 0;
        for (String key : hashmap.keySet()) {

            int currAmountOfRatings = hashmap.get(key).size();

            if (currAmountOfRatings > maxNumOfRatings) {
                maxNumOfRatings = currAmountOfRatings;
            }
        }
        System.out.println("Maximum number of ratings by any rater : " + maxNumOfRatings);

        ArrayList<String> raterWithMaxNumOfRatings = new ArrayList<String> ();
        for (String key : hashmap.keySet()) {
            int currAmountOfRatings = hashmap.get(key).size();

            if (maxNumOfRatings == currAmountOfRatings) {
                raterWithMaxNumOfRatings.add(key);
            }
        }
        System.out.println("Rater with the most number of ratings : " + raterWithMaxNumOfRatings);

        String movieID = "1798709";
        int numOfRatings = 0;
        for (String key : hashmap.keySet()) {
            if(hashmap.get(key).containsKey(movieID)) {
                numOfRatings +=1;
            }
        }
        System.out.println("Number of ratings movie " + movieID + " has : " + numOfRatings);

        ArrayList<String> uniqueMovies = new ArrayList<String> ();
        for (String key : hashmap.keySet()) {
            for (String currMovieID : hashmap.get(key).keySet()) {
                if (! uniqueMovies.contains(currMovieID)) {
                    uniqueMovies.add(currMovieID);
                }
            }
        }
        System.out.println("Total unique value of movies that were rated : " + uniqueMovies.size());
    }

}


