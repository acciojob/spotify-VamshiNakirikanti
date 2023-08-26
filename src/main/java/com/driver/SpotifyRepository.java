//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.driver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class SpotifyRepository {
    public HashMap<Artist, List<Album>> artistAlbumMap = new HashMap();
    public HashMap<Album, List<Song>> albumSongMap = new HashMap();
    public HashMap<Playlist, List<Song>> playlistSongMap = new HashMap();
    public HashMap<Playlist, List<User>> playlistListenerMap = new HashMap();
    public HashMap<User, Playlist> creatorPlaylistMap = new HashMap();
    public HashMap<User, List<Playlist>> userPlaylistMap = new HashMap();
    public HashMap<Song, List<User>> songLikeMap = new HashMap();
    public HashMap<Artist, Integer> artistLikeMap = new HashMap();
    public List<User> users = new ArrayList();
    public List<Song> songs = new ArrayList();
    public List<Playlist> playlists = new ArrayList();
    public List<Album> albums = new ArrayList();
    public List<Artist> artists = new ArrayList();

    public SpotifyRepository() {
    }

    public User createUser(String name, String mobile) {
        User user = new User(name, mobile);
        this.users.add(user);
        return user;
    }

    public Artist createArtist(String name) {
        Artist artist = new Artist(name);
        this.artists.add(artist);
        return artist;
    }

    public List<Artist> getArtists() {
        return this.artists;
    }

    public Album createAlbum(String title, String artistName) {
        Album album = new Album(title);
        this.albums.add(album);
        if (this.artistAlbumMap.containsKey(artistName)) {
            ((List)this.artistAlbumMap.get(artistName)).add(album);
        } else {
            List<Album> albumList = new ArrayList();
            albumList.add(album);
            this.artistAlbumMap.put(this.getArtist(artistName), albumList);
        }

        return album;
    }

    public Artist getArtist(String name) {
        Iterator var2 = this.artists.iterator();

        Artist artist;
        do {
            if (!var2.hasNext()) {
                return null;
            }

            artist = (Artist)var2.next();
        } while(!artist.getName().equals(name));

        return artist;
    }

    public Song createSong(String title, String albumName, int length) throws Exception {
        Song song = new Song(title, length);
        this.songs.add(song);
        Album album = this.getAlbum(albumName);
        if (this.albumSongMap.containsKey(album)) {
            ((List)this.albumSongMap.get(album)).add(song);
        } else {
            List<Song> songList = new ArrayList();
            songList.add(song);
            this.albumSongMap.put(album, songList);
        }

        return song;
    }

    public Album getAlbum(String name) {
        Iterator var2 = this.albums.iterator();

        Album album;
        do {
            if (!var2.hasNext()) {
                return null;
            }

            album = (Album)var2.next();
        } while(!album.getTitle().equals(name));

        return album;
    }

    public Playlist createPlaylistOnLength(String mobile, String title, int length) throws Exception {
        User user = this.getUserFromMobile(mobile);
        if (user == null) {
            throw new Exception("User does not exist");
        } else {
            Playlist playlist = new Playlist(title);
            this.playlists.add(playlist);
            this.creatorPlaylistMap.put(user, playlist);
            List<Song> songList = new ArrayList();
            Iterator var7 = this.songs.iterator();

            while(var7.hasNext()) {
                Song song = (Song)var7.next();
                if (song.getLength() == length) {
                    songList.add(song);
                }
            }

            List<Playlist> playlistList = new ArrayList();
            playlistList.add(playlist);
            this.userPlaylistMap.put(user, playlistList);
            if (this.playlistListenerMap.containsKey(playlist)) {
                ((List)this.playlistListenerMap.get(playlist)).add(user);
            } else {
                List<User> userList = new ArrayList();
                userList.add(user);
                this.playlistListenerMap.put(playlist, userList);
            }

            return playlist;
        }
    }

    public User getUserFromMobile(String mobile) {
        Iterator var2 = this.users.iterator();

        User user;
        do {
            if (!var2.hasNext()) {
                return null;
            }

            user = (User)var2.next();
        } while(!user.getMobile().equals(mobile));

        return user;
    }

    public Playlist createPlaylistOnName(String mobile, String title, List<String> songTitles) throws Exception {
        User user = this.getUserFromMobile(mobile);
        if (user == null) {
            throw new Exception("User does not exist");
        } else {
            Playlist playlist = new Playlist(title);
            this.playlists.add(playlist);
            this.creatorPlaylistMap.put(user, playlist);
            List<Song> songList = new ArrayList();
            Iterator var7 = songTitles.iterator();

            while(var7.hasNext()) {
                String songTitle = (String)var7.next();
                Song song = this.getSong(title);
                if (song != null) {
                    songList.add(song);
                }
            }

            List<Playlist> playlistList = new ArrayList();
            playlistList.add(playlist);
            this.userPlaylistMap.put(user, playlistList);
            if (this.playlistListenerMap.containsKey(playlist)) {
                ((List)this.playlistListenerMap.get(playlist)).add(user);
            } else {
                List<User> userList = new ArrayList();
                userList.add(user);
                this.playlistListenerMap.put(playlist, userList);
            }

            return playlist;
        }
    }

    public Song getSong(String name) {
        Iterator var2 = this.songs.iterator();

        Song song;
        do {
            if (!var2.hasNext()) {
                return null;
            }

            song = (Song)var2.next();
        } while(!song.getTitle().equals(name));

        return song;
    }

    public Playlist findPlaylist(String mobile, String playlistTitle) throws Exception {
        User user = this.getUserFromMobile(mobile);
        if (user == null) {
            throw new Exception("User does not exist");
        } else {
            Playlist playlist = this.getPlayList(playlistTitle);
            if (playlist == null) {
                throw new Exception("Playlist does not exist");
            } else if (this.creatorPlaylistMap.get(user) == playlist) {
                return playlist;
            } else {
                Iterator var5 = ((List)this.playlistListenerMap.get(playlist)).iterator();

                User u;
                do {
                    if (!var5.hasNext()) {
                        ArrayList userList;
                        if (this.userPlaylistMap.containsKey(user)) {
                            ((List)this.userPlaylistMap.get(user)).add(playlist);
                        } else {
                            userList = new ArrayList();
                            userList.add(playlist);
                            this.userPlaylistMap.put(user, userList);
                        }

                        if (this.playlistListenerMap.containsKey(playlist)) {
                            ((List)this.playlistListenerMap.get(playlist)).add(user);
                        } else {
                            userList = new ArrayList();
                            userList.add(user);
                            this.playlistListenerMap.put(playlist, userList);
                        }

                        return playlist;
                    }

                    u = (User)var5.next();
                } while(u != user);

                return playlist;
            }
        }
    }

    public Playlist getPlayList(String name) {
        Iterator var2 = this.playlists.iterator();

        Playlist playlist;
        do {
            if (!var2.hasNext()) {
                return null;
            }

            playlist = (Playlist)var2.next();
        } while(!playlist.getTitle().equals(name));

        return playlist;
    }

    public Song likeSong(String mobile, String songTitle) throws Exception {
        User user = this.getUserFromMobile(mobile);
        if (user == null) {
            throw new Exception("User does not exist");
        } else {
            Song song = this.getSong(songTitle);
            if (song == null) {
                throw new Exception("Song does not exist");
            } else {
                if (this.songLikeMap.containsKey(song)) {
                    Iterator var5 = ((List)this.songLikeMap.get(song)).iterator();

                    while(var5.hasNext()) {
                        User u = (User)var5.next();
                        if (u == user) {
                            return song;
                        }
                    }

                    ((List)this.songLikeMap.get(song)).add(user);
                    Artist artist = this.getArtistFromSong(song);
                    this.artistLikeMap.put(artist, (Integer)this.artistLikeMap.getOrDefault(artist, 0) + 1);
                }

                return song;
            }
        }
    }

    public Artist getArtistFromSong(Song song) {
        Album album = this.getAlbumFromSong(song);
        Iterator var3 = this.artistAlbumMap.keySet().iterator();

        while(var3.hasNext()) {
            Artist artist = (Artist)var3.next();
            Iterator var5 = ((List)this.artistAlbumMap.get(artist)).iterator();

            while(var5.hasNext()) {
                Album a = (Album)var5.next();
                if (album == a) {
                    return artist;
                }
            }
        }

        return null;
    }

    public Album getAlbumFromSong(Song song) {
        Iterator var2 = this.albumSongMap.keySet().iterator();

        while(var2.hasNext()) {
            Album album = (Album)var2.next();
            Iterator var4 = ((List)this.albumSongMap.get(album)).iterator();

            while(var4.hasNext()) {
                Song s = (Song)var4.next();
                if (s == song) {
                    return album;
                }
            }
        }

        return null;
    }

    public String mostPopularArtist() {
        int max = 0;
        Artist mostFamous = null;
        Iterator var3 = this.artistLikeMap.keySet().iterator();

        while(var3.hasNext()) {
            Artist artist = (Artist)var3.next();
            if ((Integer)this.artistLikeMap.get(artist) > max) {
                max = (Integer)this.artistLikeMap.get(artist);
                mostFamous = artist;
            }
        }

        return mostFamous.getName();
    }

    public String mostPopularSong() {
        int max = 0;
        Song mostFamous = null;
        Iterator var3 = this.songLikeMap.keySet().iterator();

        while(var3.hasNext()) {
            Song song = (Song)var3.next();
            if (((List)this.songLikeMap.get(song)).size() > max) {
                max = ((List)this.songLikeMap.get(song)).size();
                mostFamous = song;
            }
        }

        return mostFamous.getTitle();
    }
}
