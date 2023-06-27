package com.movies.rating.movie.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.movies.rating.movie.entity.Movies;
import com.movies.rating.movie.entity.MoviesDTO;
import com.movies.rating.movie.service.MoviesService;

@RestController
@RequestMapping("/api/v1")
public class MovieController {

	
	@Autowired
	MoviesService moviesService;
	
	
	/*
	 * This Api for get Longest Duration Movies
	 * 
	 * 
	 * */
	@GetMapping("/longest-duration-movies")
	public List<Movies> getLongestMovies()
	{
		List<Movies> topMovies = moviesService.longetDurationMovies();
		
		return topMovies;
	}
	
	/*
	 * 
	 * For insert a new Movies in movies table
	 * */
	
	@PostMapping("/new-movie")
	public String  addMovies(@RequestBody Movies movies)
	{
		Movies newMovies = moviesService.addNewMovies(movies);
		
		return "Success";
		
	}
	
	/*
	 * 
	 * to Fetch Top Movies
	 * */
	@GetMapping("/top-rated-movies")
	public List<MoviesDTO> topMovies()
	{
		return moviesService.getTopRatedMovies();
	}
	
	
	/*
	 * Give list of movies with genre
	 * 
	 * */
	@GetMapping("/genre-movies-with-subtotals")
	public String genereMovies()
	{
		return moviesService.getMovieData();
	}
		
	
	/*
	 * increse duration as per genres
	 */
	
	@PostMapping("/update-runtimes")
    public ResponseEntity<String> updateRuntimes() {
        try {
            moviesService.updateRuntimes();
            return ResponseEntity.ok("Movie runtimes updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("An error occurred while updating movie runtimes: " + e.getMessage());
        }
    }
	
	
	
}
