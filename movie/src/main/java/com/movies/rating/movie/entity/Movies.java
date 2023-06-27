package com.movies.rating.movie.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name ="movies" )
public class Movies {
	
	  @Id
	  @Column(name ="tconst")
	  private String tconst;
	  
	  @Column(name ="titletype")
	  private String titleType;
	  
	  @Column(name ="primarytitle")
	  private String primaryTitle;
	  
	  @Column(name ="runtimeminutes")
	  private Integer runtimeMinutes;
	  
	  @Column(name ="genres")
	  private String genres;
}
