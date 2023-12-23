package com.amazon.ata.music.playlist.service.helpers;

import com.amazon.ata.music.playlist.service.dynamodb.models.AlbumTrack;
import com.amazon.ata.music.playlist.service.models.SongModel;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class AlbumTrackTestHelper {
    private AlbumTrackTestHelper() {
    }

    public static AlbumTrack generateAlbumTrack(int sequenceNumber) {
        AlbumTrack albumTrack = new AlbumTrack();
        albumTrack.setAsin("asin" + sequenceNumber);
        albumTrack.setTrack_number(sequenceNumber);
        albumTrack.setAlbum_name("album" + sequenceNumber);
        albumTrack.setSong_title("title" + sequenceNumber);
        return albumTrack;
    }

    public static void assertAlbumTracksEqualSongModels(List<AlbumTrack> albumTracks, List<SongModel> songModels) {
        assertEquals(albumTracks.size(),
                     songModels.size(),
                     String.format("Expected album tracks (%s) and song models (%s) to match",
                                   albumTracks,
                                   songModels));
        for (int i = 0; i < albumTracks.size(); i++) {
            assertAlbumTrackEqualsSongModel(
                albumTracks.get(i),
                songModels.get(i),
                String.format("Expected %dth album track (%s) to match corresponding song model (%s)",
                              i,
                              albumTracks.get(i),
                              songModels.get(i)));
        }
    }

    public static void assertAlbumTrackEqualsSongModel(AlbumTrack albumTrack, SongModel songModel) {
        String message = String.format("Expected album track %s to match song model %s", albumTrack, songModel);
        assertAlbumTrackEqualsSongModel(albumTrack, songModel, message);
    }

    public static void assertAlbumTrackEqualsSongModel(AlbumTrack albumTrack, SongModel songModel, String message) {
        assertEquals(albumTrack.getAsin(), songModel.getAsin(), message);
        assertEquals(albumTrack.getTrack_number(), songModel.getTrackNumber(), message);
        assertEquals(albumTrack.getAlbum_name(), songModel.getAlbum(), message);
        assertEquals(albumTrack.getSong_title(), songModel.getTitle(), message);
    }
}
