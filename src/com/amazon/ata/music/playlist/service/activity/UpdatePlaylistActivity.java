package com.amazon.ata.music.playlist.service.activity;

import com.amazon.ata.music.playlist.service.converters.ModelConverter;
import com.amazon.ata.music.playlist.service.dynamodb.models.Playlist;
import com.amazon.ata.music.playlist.service.exceptions.InvalidAttributeChangeException;
import com.amazon.ata.music.playlist.service.exceptions.InvalidAttributeValueException;
import com.amazon.ata.music.playlist.service.models.PlaylistModel;
import com.amazon.ata.music.playlist.service.models.requests.UpdatePlaylistRequest;
import com.amazon.ata.music.playlist.service.models.results.UpdatePlaylistResult;
import com.amazon.ata.music.playlist.service.dynamodb.PlaylistDao;

import com.amazon.ata.music.playlist.service.util.MusicPlaylistServiceUtils;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

/**
 * Implementation of the UpdatePlaylistActivity for the MusicPlaylistService's UpdatePlaylist API.
 *
 * This API allows the customer to update their saved playlist's information.
 */
public class UpdatePlaylistActivity implements RequestHandler<UpdatePlaylistRequest, UpdatePlaylistResult> {
    private final Logger log = LogManager.getLogger();
    private final PlaylistDao playlistDao;

    /**
     * Instantiates a new UpdatePlaylistActivity object.
     *
     * @param playlistDao PlaylistDao to access the playlist table.
     */
    public UpdatePlaylistActivity(PlaylistDao playlistDao) {
        this.playlistDao = playlistDao;
    }

    /**
     * This method handles the incoming request by retrieving the playlist, updating it,
     * and persisting the playlist.
     * <p>
     * It then returns the updated playlist.
     * <p>
     * If the playlist does not exist, this should throw a PlaylistNotFoundException.
     * <p>
     * If the provided playlist name or customer ID has invalid characters, throws an
     * InvalidAttributeValueException
     * <p>
     * If the request tries to update the customer ID,
     * this should throw an InvalidAttributeChangeException
     *
     * @param updatePlaylistRequest request object containing the playlist ID, playlist name, and customer ID
     *                              associated with it
     * @return updatePlaylistResult result object containing the API defined {@link PlaylistModel}
     */
    @Override
    public UpdatePlaylistResult handleRequest(final UpdatePlaylistRequest updatePlaylistRequest, Context context) {
        log.info("Received UpdatePlaylistRequest {}", updatePlaylistRequest);

        // Validate the Playlist's name and Customer ID for security concerns
        String requestedPlaylistName = updatePlaylistRequest.getName();
        if (!MusicPlaylistServiceUtils.isValidString(requestedPlaylistName)) {
            throw new InvalidAttributeValueException(
                    "This Playlist Name contains invalid characters"
            );
        }

        String requestedCustomerId = updatePlaylistRequest.getCustomerId();
        if (!MusicPlaylistServiceUtils.isValidString(requestedCustomerId)) {
            throw new InvalidAttributeValueException(
                    "This Customer ID contains invalid characters"
            );
        }

        // 2 steps: load and save
        // Step 1: retrieving/loading the playlist,
        String requestedId = updatePlaylistRequest.getId();
        // The getPlaylist method will throw a PlaylistNotFoundException if the Playlist does NOT exist
        Playlist playlistToUpdate = playlistDao.getPlaylist(requestedId);

        // Check if the existing Playlist's Customer ID is the same as the Customer ID input by the user
        if (playlistToUpdate.getCustomerId().equals(requestedCustomerId)) {
            // if both Customer IDs are the same, we can update/save the new Playlist Name
            playlistToUpdate.setName(requestedPlaylistName);
        } else {
            // or we will throw an InvalidAttributeChangeException to make sure that the customer ID are the same
            throw new InvalidAttributeChangeException("You should provide the Customer Id that this playlist belongs to!");
        }

        // Step 2: Saving/Updating the playlist
        playlistDao.savePlaylist(playlistToUpdate);

        PlaylistModel playlistModelToUpdate = new ModelConverter().toPlaylistModel(playlistToUpdate);

        return UpdatePlaylistResult.builder()
                .withPlaylist(playlistModelToUpdate)
                .build();
    }
}
