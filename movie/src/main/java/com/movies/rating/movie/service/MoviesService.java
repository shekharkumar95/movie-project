package com.movies.rating.movie.service;

import java.util.List;

import com.movies.rating.movie.entity.Movies;
import com.movies.rating.movie.entity.MoviesDTO;

public interface MoviesService {

	
	public List<Movies> longetDurationMovies();
	
	public Movies addNewMovies(Movies movies);
	
	public List<MoviesDTO> getTopRatedMovies();
	
	public String getMovieData();
	
	public void updateRuntimes();
}
