package com.amazon.ata.music.playlist.service.dynamodb.models;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

/**
 * Represents a record in the album_tracks table.
 */
@DynamoDBTable(tableName = "album_tracks")
public class AlbumTrack {
    private String asin;
    private String albumName;
    private Integer trackNumber;
    private String songTitle;

    @DynamoDBHashKey
    public String getAsin() {
        return asin;
    }

    public void setAsin(String asin) {
        this.asin = asin;
    }

    @DynamoDBRangeKey
    public Integer getTrack_number() {
        return trackNumber;
    }

    public void setTrack_number(Integer trackNumber) {
        this.trackNumber = trackNumber;
    }

    @DynamoDBAttribute(attributeName = "album_name")
    public String getAlbum_name() {
        return albumName;
    }

    public void setAlbum_name(String albumName) {
        this.albumName = albumName;
    }

    @DynamoDBAttribute(attributeName = "song_title")
    public String getSong_title() {
        return songTitle;
    }

    public void setSong_title(String songTitle) {
        this.songTitle = songTitle;
    }
}
