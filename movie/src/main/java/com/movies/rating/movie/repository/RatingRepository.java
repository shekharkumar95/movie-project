package com.movies.rating.movie.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.movies.rating.movie.entity.Ratings;

public interface RatingRepository extends JpaRepository<Ratings, String> {

	List<Ratings> findByAverageRatingGreaterThan(double averageRating);

}
