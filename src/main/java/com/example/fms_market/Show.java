package com.example.fms_market;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.sql.Date;
import java.util.List;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Movie.class, name = "movie"),
        @JsonSubTypes.Type(value = Series.class, name = "series")
})
public abstract class Show {
    private int id;
    private String title;
    private Date release_date;
    private int duration;
    private List<Cast> cast;
    private List<String> genres;
    private Director director;
    private List<String> language;
    private double imdb_score;
    private String country;
    private long budget;
    private long revenue;
    private String poster;
    private String type;
    private String description; // New field for show description

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setDate(Date date) {
        this.release_date = date;
    }

    public Date getDate() {
        return release_date;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }

    public void setCast(List <Cast>cast) {
        this.cast = cast;
    }

    public List getCast() {
        return cast;
    }

    public void setGenres(List<String>genres) {
        this.genres = genres;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setDirector(Director director) {
        this.director = director;
    }

    public Director getDirector() {
        return director;
    }

    public void setLanguage(List<String> language) {
        this.language = language;
    }

    public List<String> getLanguage() {
        return language;
    }

    public void setImdb_score(double imdb_score) {
        this.imdb_score = imdb_score;
    }

    public double getImdb_score() {
        return imdb_score;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    public void setBudget(long budget) {
        this.budget = budget;
    }

    public long getBudget() {
        return budget;
    }

    public void setRevenue(long revenue) {
        this.revenue = revenue;
    }

    public long getRevenue() {
        return revenue;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getPoster() {
        return poster;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }



}

