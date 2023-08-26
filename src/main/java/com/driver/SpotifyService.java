
package com.driver;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SpotifyService {
    SpotifyRepository spotifyRepository = new SpotifyRepository();

    public SpotifyService() {
    }

    public User createUser(String name, String mobile) {
        return this.spotifyRepository.createUser(name, mobile);
    }

    public Artist createArtist(String name) {
        return this.spotifyRepository.createArtist(name);
    }

    public Album createAlbum(String title, String artistName) {
        Artist artist = this.spotifyRepository.getArtist(artistName);
        if (artist == null) {
            this.createArtist(artistName);
        }

        Album album = this.spotifyRepository.createAlbum(title, artistName);
        return album;
    }

    public Song createSong(String title, String albumName, int length) throws Exception {
        Album album = this.spotifyRepository.getAlbum(albumName);
        if (album == null) {
            throw new Exception("Album does not exist");
        } else {
            return this.spotifyRepository.createSong(title, albumName, length);
        }
    }

    public Playlist createPlaylistOnLength(String mobile, String title, int length) throws Exception {
        return this.spotifyRepository.createPlaylistOnLength(mobile, title, length);
    }

    public Playlist createPlaylistOnName(String mobile, String title, List<String> songTitles) throws Exception {
        return this.spotifyRepository.createPlaylistOnName(mobile, title, songTitles);
    }

    public Playlist findPlaylist(String mobile, String playlistTitle) throws Exception {
        return this.spotifyRepository.findPlaylist(mobile, playlistTitle);
    }

    public Song likeSong(String mobile, String songTitle) throws Exception {
        return this.spotifyRepository.likeSong(mobile, songTitle);
    }

    public String mostPopularArtist() {
        return this.spotifyRepository.mostPopularArtist();
    }

    public String mostPopularSong() {
        return this.spotifyRepository.mostPopularSong();
    }
}
