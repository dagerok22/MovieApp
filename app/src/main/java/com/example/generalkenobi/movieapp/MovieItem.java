package com.example.generalkenobi.movieapp;


/**
 * Created by GeneralKenobi on 24.06.2016.
 */
public class MovieItem {

    public MovieItem(String overView,
                     String title,
                     Integer id,
                     Double voteAverage,
                     Double voteCount,
                     String releaseDate,
                     String posterPath,
                     String bg_poster) {
        OverView = overView;
        Title = title;
        this.id = id;
        VoteAverage = voteAverage;
        VoteCount = voteCount;
        ReleaseDate = releaseDate;
        this.posterPath = posterPath;
        this.bgPosterPath = bg_poster;
    }

    public String OverView;
    public String Title;
    public Integer id;
    public Double VoteAverage;
    public Double VoteCount;
    public String ReleaseDate;
    private String posterPath;
    public String bgPosterPath;

    public String getBgPosterPath() {
        return bgPosterPath;
    }

    public void setBgPosterPath(String bgPosterPath) {
        this.bgPosterPath = bgPosterPath;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }
}
