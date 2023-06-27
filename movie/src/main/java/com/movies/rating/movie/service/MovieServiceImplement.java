package com.movies.rating.movie.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.movies.rating.movie.entity.Movies;
import com.movies.rating.movie.entity.MoviesDTO;
import com.movies.rating.movie.entity.Ratings;
import com.movies.rating.movie.repository.MoviesRepository;
import com.movies.rating.movie.repository.RatingRepository;

@Service
public class MovieServiceImplement implements MoviesService {

	
	@Autowired
	MoviesRepository moviesRepository;

	@Autowired
	RatingRepository ratingRepository;
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	
	/**
	 * Retrieves a list of the 10 longest duration movies from the database.
	 * The method fetches all movies from the database using moviesRepository, sorting them in descending order by runtime,
	 * and then uses Java 8 stream API to limit the result set to the top 10 results.
	 * @return a List of Movies objects representing the 10 longest duration movies
	 */
	@Override
	public List<Movies> longetDurationMovies() {
		
		// Fetching 10 longest duration movies using moviesRepository and using Java 8 stream API to return them as a list.
		List<Movies> listMovies =moviesRepository.findAll(Sort.by(Sort.Direction.DESC,"runtimeMinutes"));		
		return listMovies.stream().limit(10).collect(Collectors.toList());
	}


	/**
	 * Adds a new movie to the database using moviesRepository.
	 * @requestBody movies The Movies object to be added to the database
	 * @return The saved entity after it has been added to the database
	 */
	@Override
	public Movies addNewMovies(Movies movies) {
		// Adding a new movie to the database via moviesRepository and returning the saved entity.
		return moviesRepository.save(movies);
	}

	/**
	 * Retrieves a list of top-rated movies from the database.
	 * The method first retrieves all movies that have an average rating greater than 6.0
	 * using ratingRepository, and then fetches the details of each movie from moviesRepository.
	 * The resulting data is converted into MoviesDTO objects and added to a list before being returned.
	 * @return a List of MoviesDTO objects representing the top-rated movies
	 */

	@Override
	public List<MoviesDTO> getTopRatedMovies() {
		
		// Retrieving all movies that have an average rating greater than 6.0 using ratingRepository
		
		List<Ratings> ratings = ratingRepository.findByAverageRatingGreaterThan(6.0);

        List<MoviesDTO> topRatedMovies = new ArrayList<>();

        for (Ratings rating : ratings) {
        	
        	// For each movie that meets the criteria, we fetch its details from moviesRepository and add it to a MoviesDTO object before
            // adding it to the list of top-rated movies.
        	
            Optional<Movies> movie = moviesRepository.findById(rating.getTconst());

            if (movie.isPresent()) {
                MoviesDTO movieDTO = new MoviesDTO();
                movieDTO.setTconst(movie.get().getTconst());
                movieDTO.setPrimaryTitle(movie.get().getPrimaryTitle());
                movieDTO.setGenres(movie.get().getGenres());
                movieDTO.setAverageRating(rating.getAverageRating());

                topRatedMovies.add(movieDTO);
            }
        }

        return topRatedMovies;
    }
	
	/**
	 * Retrieves movie data from the database and converts it to a JSON string.
	 * The query fetches data about genres, primary titles, number of votes, and total votes
	 * for the top 1000 results. The results are grouped by genre and sorted in descending order.
	 */

	@Override
	public String getMovieData() {
		
		// Sending an SQL query to the database using jdbcTemplate to fetch data about genres, primary titles, number of votes and total votes,
		// then parsing the results and consolidating them into a JSON string.
       
		String sql = "SELECT m.genres AS Genre, GROUP_CONCAT(m.primaryTitle) AS PrimaryTitle ,\n" +
                " GROUP_CONCAT(rt.numVotes) AS NumVotes, \n" +
                "SUM(rt.numVotes) AS TotalVotes FROM movies.movies m \n" +
                "JOIN movies.ratings rt ON m.tconst = rt.tconst \n" +
                " GROUP BY m.genres ORDER BY m.genres DESC \n" +
                "LIMIT 1000;";
         
      

        // map results to JSON format and calculate total number of votes
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
        Map<String, Integer> genreVotes = new HashMap<>();
        StringBuilder sb = new StringBuilder("{\"results\": [");
        String currentGenre = "";
        Double currentGenreTotalVotes = 0.0;
        for (Map<String, Object> row : rows)
        
        {
            String genre = (String) row.get("genre"), primaryTitleStr = (String) row.get("primarytitle");
            String numVotesStr = (String) row.get("numvotes");
            BigDecimal TotalVotesStr = (BigDecimal) row.get("totalvotes");
            Double TotalVotes = TotalVotesStr.doubleValue();
           // int numVotes = 0;
            
               String[] numVotes = numVotesStr.split(",");
               String[] primaryTitle = primaryTitleStr.split(",");
            
            if (!currentGenre.equals(genre)) 
            {
                if (!currentGenre.equals("")) sb.replace(sb.lastIndexOf(", "), sb.length(), "], ")
                        .append("\"total\": ").append(currentGenreTotalVotes).append("}, ");
                currentGenre = genre;
              //  currentGenreTotalVotes = 0.0;
                sb.append("{\"Genre\": \"").append(genre).append("\", \"Movies\": [");
            }
            
            
            for (int i = 0; i < numVotes.length; i++)
            {
            sb.append("{\"PrimaryTitle\": \"").append(primaryTitle[i]).append("\", \"NumVotes\": ").append(numVotes[i])
                    .append("}, ");
            }
            
            currentGenreTotalVotes = TotalVotes;
        }
        
        
        if (sb.length() > 1) sb.replace(sb.lastIndexOf(", "), sb.length(), "], ")
                .append("\"total\": ").append(currentGenreTotalVotes).append("}");
        sb.append("]}");

        // return JSON data and total number of votes as a string
        return sb.toString();
    }
	
	
	
	/**
	 * Updates the runtime for movies based on their genre.
	 * If the movie is a Documentary, its runtime is increased by 15 minutes.
	 * If the movie is an Animation, its runtime is increased by 30 minutes.
	 * For all other genres, the runtime is increased by 45 minutes.
	 */
	@Override
	public void updateRuntimes() {
        String sql = "UPDATE movies.movies SET runtimeMinutes = CASE " +
                     "WHEN genre = 'Documentary' THEN (runtimeMinutes + 15) " +
                     "WHEN genre = 'Animation' THEN (runtimeMinutes + 30) " +
                     "ELSE (runtimeMinutes + 45) END";
        jdbcTemplate.update(sql);
    }
}
