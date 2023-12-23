package com.amazon.ata.music.playlist.service.converters;

import com.amazon.ata.music.playlist.service.dynamodb.models.AlbumTrack;
import com.amazon.ata.music.playlist.service.models.PlaylistModel;
import com.amazon.ata.music.playlist.service.dynamodb.models.Playlist;
import com.amazon.ata.music.playlist.service.models.SongModel;

import java.util.ArrayList;
import java.util.List;

public class ModelConverter {
    /**
     * Converts a provided {@link Playlist} into a {@link PlaylistModel} representation.
     * @param playlist the playlist to convert
     * @return the converted playlist
     */
    public PlaylistModel toPlaylistModel(Playlist playlist) {
        return PlaylistModel.builder()
            .withId(playlist.getId())
                .withName(playlist.getName())
                .withCustomerId(playlist.getCustomerId())
                .withSongCount(playlist.getSongCount())
                .withTags(playlist.getTags()).build();
    }

    /**
     * Converts a provided {@link AlbumTrack} into a {@link SongModel} representation.
     * @param albumTrack the AlbumTrack to convert
     * @return the converted AlbumTrack
     */
    public SongModel toSongModel(AlbumTrack albumTrack) {
        return SongModel.builder()
                .withAsin(albumTrack.getAsin())
                .withTrackNumber(albumTrack.getTrack_number())
                .withAlbum(albumTrack.getAlbum_name())
                .withTitle(albumTrack.getSong_title())
                .build();
    }

    public List<SongModel> toSongModelList(List<AlbumTrack> albumTrackList) {
        List<SongModel> songModelList = new ArrayList<>();

        for (AlbumTrack song: albumTrackList) {
            songModelList.add(toSongModel(song));
        }
        return songModelList;
    }
}
