package com.example.gamepedia.GameFiles;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "GAMES")
public class GameItem {
    @PrimaryKey
    @NonNull
    public String id;

    @ColumnInfo(name = "NAME")
    public String name;

    @ColumnInfo(name = "IMAGE")
    public String image;

    @ColumnInfo(name = "DESCRIPTION")
    public String description;

    @ColumnInfo(name = "RATING")
    public String rating;

    @ColumnInfo(name = "METACRITIC")
    public String metacritic;

    @ColumnInfo(name = "RELEASE_DATE")
    public String releaseDate;

    @ColumnInfo(name = "FAVORITE")
    public boolean favorite;

    public GameItem(@NonNull String id, String name, String image, String description, String rating, String metacritic, String releaseDate, boolean favorite) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.description = description;
        this.rating = rating;
        this.metacritic = metacritic;
        this.releaseDate = releaseDate;
        this.favorite = favorite;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getMetacritic() {
        return metacritic;
    }

    public void setMetacritic(String metacritic) {
        this.metacritic = metacritic;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
}

