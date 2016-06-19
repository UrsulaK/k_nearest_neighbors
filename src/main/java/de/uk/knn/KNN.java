package de.uk.knn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KNN {

	private int k = 3;
	
	public static void main(String[] args){
		
		List<Movie> movies = new ArrayList<Movie>();
		movies.add(new Movie("Romance", 3, 104));
		movies.add(new Movie("Romance", 2, 100));
		movies.add(new Movie("Romance", 1, 81));
		movies.add(new Movie("Action", 101, 10));
		movies.add(new Movie("Action", 99, 5));
		movies.add(new Movie("Action", 98, 2));
		
		Movie unknownMovie = new Movie(18,90);
		KNN knn = new KNN();
		List<Movie> nearestNeighbours = knn.findNearestNeighbors(movies, unknownMovie);
		System.out.println("The movie is a " + knn.classifyMovie(unknownMovie, nearestNeighbours).getType());
		
	}
	
	public List<Movie> findNearestNeighbors(List<Movie> knownMovies, Movie unknownMovie){
		
		List<Movie> nearestNeighbours = new ArrayList<Movie>();
		for(Movie movie : knownMovies){
			double result1 = movie.getKicks() - unknownMovie.getKicks();
			result1 *= result1;
			double result2 = movie.getKisses() - unknownMovie.getKisses();
			result2 *= result2;
			
			double result3 = result1 + result2;
			double distance = Math.sqrt(result3);
			movie.setDistance(distance);
		}
		Collections.sort(knownMovies);
		for(int i=0; i<k; i++ ){
			nearestNeighbours.add(knownMovies.get(i));
		}
		return nearestNeighbours;
	}
	
	public Movie classifyMovie(Movie unknownMovie, List<Movie> nearestNeighbours){
		int countAction = 0;
		int countRomance = 0;
		for(Movie movie : nearestNeighbours){
			
			if("Romance".equals(movie.getType())){
				countRomance ++;
			}
			else{
				countAction ++;
			}			
		}
		if(countAction > countRomance){
			unknownMovie.setType("Action");
		}
		else{
			unknownMovie.setType("Romance");
		}
		return unknownMovie;
	}
	
	
}
