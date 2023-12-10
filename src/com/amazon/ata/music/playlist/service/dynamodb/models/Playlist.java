package com.amazon.ata.music.playlist.service.dynamodb.models;

import com.amazon.ata.music.playlist.service.converters.AlbumTrackLinkedListConverter;

import com.amazon.ata.music.playlist.service.exceptions.InvalidAttributeValueException;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a record/item in the playlists table.
 */
@DynamoDBTable(tableName = "playlists")
public class Playlist {
    private String id;
    private String name;
    private String customerId;
    private Integer songCount;
    // The music playlist client will provide a non-empty list of tags
    // or null in the request to indicate no tags were provided
    private Set<String> tags = new HashSet<>();
    //FIXME The music playlist client will provide a non-empty list of tags
    // or null in the request to indicate no tags were provided.
    private List<AlbumTrack> songList = new ArrayList<>();

    @DynamoDBHashKey(attributeName = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @DynamoDBAttribute(attributeName = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        validateField("Playlist Name", name);
        this.name = name;
    }

    @DynamoDBAttribute(attributeName = "customerId")
    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        validateField("Customer ID", customerId);
        this.customerId = customerId;
    }

    @DynamoDBAttribute(attributeName = "songCount")
    public Integer getSongCount() {
        return songCount;
    }

    public void setSongCount(Integer songCount) {
        this.songCount = songCount;
    }

    @DynamoDBAttribute(attributeName = "tags")
    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    // PARTICIPANTS: You do not need to modify the songList getters/setters or annotations
    @DynamoDBTypeConverted(converter = AlbumTrackLinkedListConverter.class)
    @DynamoDBAttribute(attributeName = "songList")
    public List<AlbumTrack> getSongList() {
        return songList;
    }

    public void setSongList(List<AlbumTrack> songList) {
        this.songList = songList;
    }

    //Helper Methods
    // This is to check the characters in the customer ID and the playlist name fields
    public void validateField(String fieldName, String fieldValue) {
        String invalidCharacters = "\"'\\";

        if (fieldValue != null) {
            // check if the field value contains any of the invalid characters
            if (fieldValue.chars().anyMatch(c -> invalidCharacters.indexOf(c) >= 0) ) {
                throw new InvalidAttributeValueException(fieldName + "has invalid characters");
            }
        }
    }
}
