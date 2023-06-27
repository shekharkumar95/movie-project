package com.movies.rating.movie.entity;

import lombok.Data;

@Data
public class MoviesDTO {
	private String tconst;
	private String primaryTitle;
	private String genres;
	private Double averageRating;
}
