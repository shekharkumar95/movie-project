package com.movies.rating.movie.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name="ratings")
public class Ratings {
	
	
	@Id
	@Column(name ="tconst")
	private String tconst;
	
	@Column(name ="averagerating")
    private Double averageRating;
	
	
	@Column(name ="numvotes")
    private Integer numVotes;

}
