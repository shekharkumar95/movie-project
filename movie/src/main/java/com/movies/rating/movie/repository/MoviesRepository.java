package com.movies.rating.movie.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.movies.rating.movie.entity.Movies;

@Repository
public interface MoviesRepository extends JpaRepository<Movies, String> {

}
