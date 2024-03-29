package com.amazon.ata.music.playlist.service.activity;

import com.amazon.ata.music.playlist.service.converters.ModelConverter;
import com.amazon.ata.music.playlist.service.dynamodb.models.AlbumTrack;
import com.amazon.ata.music.playlist.service.dynamodb.models.Playlist;
import com.amazon.ata.music.playlist.service.exceptions.InvalidAttributeValueException;
import com.amazon.ata.music.playlist.service.models.requests.CreatePlaylistRequest;
import com.amazon.ata.music.playlist.service.models.results.CreatePlaylistResult;
import com.amazon.ata.music.playlist.service.models.PlaylistModel;
import com.amazon.ata.music.playlist.service.dynamodb.PlaylistDao;

import com.amazon.ata.music.playlist.service.util.MusicPlaylistServiceUtils;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Implementation of the CreatePlaylistActivity for the MusicPlaylistService's CreatePlaylist API.
 *
 * This API allows the customer to create a new playlist with no songs.
 */
public class CreatePlaylistActivity implements RequestHandler<CreatePlaylistRequest, CreatePlaylistResult> {
    private final Logger log = LogManager.getLogger();
    private final PlaylistDao playlistDao;

    /**
     * Instantiates a new CreatePlaylistActivity object.
     *
     * @param playlistDao PlaylistDao to access the playlists table.
     */
    @Inject
    public CreatePlaylistActivity(PlaylistDao playlistDao) {
        this.playlistDao = playlistDao;
    }

    /**
     * This method handles the incoming request by persisting a new playlist
     * with the provided playlist name and customer ID from the request.
     * <p>
     * It then returns the newly created playlist.
     * <p>
     * If the provided playlist name or customer ID has invalid characters, throws an
     * InvalidAttributeValueException
     *
     * @param createPlaylistRequest request object containing the playlist name and customer ID
     *                              associated with it
     * @return createPlaylistResult result object containing the API defined {@link PlaylistModel}
     */
    @Override
    public CreatePlaylistResult handleRequest(final CreatePlaylistRequest createPlaylistRequest, Context context) {
        log.info("Received CreatePlaylistRequest {}", createPlaylistRequest);

        String requestedPlaylistName = createPlaylistRequest.getName();
        if (!MusicPlaylistServiceUtils.isValidString(requestedPlaylistName)) {
            throw new InvalidAttributeValueException(
                    "This Playlist Name contains invalid characters"
            );
        }

        String requestedCustomerId = createPlaylistRequest.getCustomerId();
        if (!MusicPlaylistServiceUtils.isValidString(requestedCustomerId)) {
            throw new InvalidAttributeValueException(
                    "This Customer ID contains invalid characters"
            );
        }

        //DONEFIXME look at the guided projects for the CreateTopicMessageHandler class
        // Create a Playlist object and save it
        Playlist newPlaylist = new Playlist();
        newPlaylist.setId(MusicPlaylistServiceUtils.generatePlaylistId());
        newPlaylist.setName(requestedPlaylistName);
        newPlaylist.setCustomerId(requestedCustomerId);
        newPlaylist.setSongCount(0);
        // Initialize an empty songList before storing it to DynamoDB
        // Be careful you have to include the AlbumTrack type
        newPlaylist.setSongList(new ArrayList<AlbumTrack>());
        //TODO
        // The Music Playlist Client will provide a non-empty list of tags
        // or null IN THE REQUEST to indicate no tags were provided
        // NO EMPTY LIST
        List<String> requestedTags = createPlaylistRequest.getTags();
        // Single point of setting the tags property, making it easier to understand the flow.
        Set<String> playlistTags;
        // Instead, the HashSet is instantiated separately based on the condition
        // Checked if the requestedTags list is not only null but also not empty before creating the HashSet.
        if (requestedTags != null && !requestedTags.isEmpty()) {
            playlistTags = new HashSet<>(requestedTags);
        } else {
            playlistTags = null;
        }

        newPlaylist.setTags(playlistTags);

        playlistDao.savePlaylist(newPlaylist);

        // The following lines are implemented because of the class diagrams and the GetPlaylistActivity class
        PlaylistModel newPlaylistModel = new ModelConverter().toPlaylistModel(newPlaylist);

        return CreatePlaylistResult.builder()
                .withPlaylist(newPlaylistModel)
                .build();
    }
}
