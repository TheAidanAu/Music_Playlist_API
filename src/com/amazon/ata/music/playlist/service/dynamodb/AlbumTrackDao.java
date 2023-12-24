package com.amazon.ata.music.playlist.service.dynamodb;

import com.amazon.ata.music.playlist.service.dynamodb.models.AlbumTrack;

import com.amazon.ata.music.playlist.service.dynamodb.models.Playlist;
import com.amazon.ata.music.playlist.service.exceptions.AlbumTrackNotFoundException;
import com.amazon.ata.music.playlist.service.exceptions.PlaylistNotFoundException;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

import javax.inject.Inject;

/**
 * Accesses data for an album using {@link AlbumTrack} to represent the model in DynamoDB.
 */
public class AlbumTrackDao {
    private final DynamoDBMapper dynamoDbMapper;

    /**
     * Instantiates an AlbumTrackDao object.
     *
     * @param dynamoDbMapper the {@link DynamoDBMapper} used to interact with the album_track table
     */
    @Inject
    public AlbumTrackDao(DynamoDBMapper dynamoDbMapper) {
        this.dynamoDbMapper = dynamoDbMapper;
    }

    /**
     * Returns the {@link AlbumTrack} corresponding to the specified Album ASIN and Track Number.
     *
     * @param asin the album's ASIN
     * @param track_number the track number on that album
     * @return the stored AlbumKTrack, or null if none was found.
     */
    public AlbumTrack getAlbumTrack(String asin, Integer track_number) {
        AlbumTrack albumTrack = this.dynamoDbMapper.load(AlbumTrack.class, asin, track_number);

        if (albumTrack == null) {
            throw new AlbumTrackNotFoundException("Could not find an album track with the ASIN "
                    + asin + " and with the track number " + track_number.toString());
        }

        return albumTrack;

    }

}
