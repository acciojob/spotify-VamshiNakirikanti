package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class SpotifyRepository {
    public HashMap<Artist, List<Album>> artistAlbumMap;
    public HashMap<Album, List<Song>> albumSongMap;
    public HashMap<Playlist, List<Song>> playlistSongMap;
    public HashMap<Playlist, List<User>> playlistListenerMap;
    public HashMap<User, Playlist> creatorPlaylistMap;
    public HashMap<User, List<Playlist>> userPlaylistMap;
    public HashMap<Song, List<User>> songLikeMap;
    public HashMap<Artist,Integer> artistLikeMap;

    public List<User> users;
    public List<Song> songs;
    public List<Playlist> playlists;
    public List<Album> albums;
    public List<Artist> artists;

    public SpotifyRepository(){
        //To avoid hitting apis multiple times, initialize all the hashmaps here with some dummy data
        artistAlbumMap = new HashMap<>();
        albumSongMap = new HashMap<>();
        playlistSongMap = new HashMap<>();
        playlistListenerMap = new HashMap<>();
        creatorPlaylistMap = new HashMap<>();
        userPlaylistMap = new HashMap<>();
        songLikeMap = new HashMap<>();
        artistLikeMap = new HashMap<>();

        users = new ArrayList<>();
        songs = new ArrayList<>();
        playlists = new ArrayList<>();
        albums = new ArrayList<>();
        artists = new ArrayList<>();
    }

    public User createUser(String name, String mobile) {
        User user = new User(name,mobile);
        users.add(user);
        return user;
    }

    public Artist createArtist(String name) {
        Artist artist = new Artist(name);
        artists.add(artist);
        artistLikeMap.put(artist,0);
        return artist;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public Album createAlbum(String title, String artistName) {
        Album album = new Album(title);
        albums.add(album);
        if(artistAlbumMap.containsKey(artistName)){
            artistAlbumMap.get(artistName).add(album);
        }
        else{
            List<Album> albumList = new ArrayList<>();
            albumList.add(album);
            artistAlbumMap.put(getArtist(artistName),albumList);
        }
        return album;
    }
    public Artist getArtist(String name){
        for(Artist artist:artists){
            if(artist.getName().equals(name)){
                return artist;
            }
        }
        return null;
    }
    public Song createSong(String title, String albumName, int length) throws Exception{
        Song song = new Song(title,length);
        songs.add(song);
        Album album = getAlbum(albumName);
        if(albumSongMap.containsKey(album)){
            albumSongMap.get(album).add(song);
        }
        else{
            List<Song> songList = new ArrayList<>();
            songList.add(song);
            albumSongMap.put(album,songList);
        }
        return song;
    }

    public Album getAlbum(String name){
        for(Album album : albums){
            if(album.getTitle().equals(name)) {
                return album;
            }
        }
        return null;
    }
    public Playlist createPlaylistOnLength(String mobile, String title, int length) throws Exception {
        User user = getUserFromMobile(mobile);
        if(user==null){
            throw new Exception("User does not exist");
        }
        else{
            Playlist playlist = new Playlist(title);
            playlists.add(playlist);
            creatorPlaylistMap.put(user,playlist);
            List<Song> songList = new ArrayList<>();
            for(Song song:songs){
                if(song.getLength()==length){
                    songList.add(song);
                }
            }
            List<Playlist> playlistList = new ArrayList<>();
            playlistList.add(playlist);
            userPlaylistMap.put(user,playlistList);
            if(playlistListenerMap.containsKey(playlist)){
                playlistListenerMap.get(playlist).add(user);
            }
            else{
                List<User> userList = new ArrayList<>();
                userList.add(user);
                playlistListenerMap.put(playlist,userList);
            }
            return playlist;
        }
    }

    public User getUserFromMobile(String mobile){
        for(User user:users){
            if(user.getMobile().equals(mobile)){
                return user;
            }
        }
        return null;
    }
    public Playlist createPlaylistOnName(String mobile, String title, List<String> songTitles) throws Exception {
        User user = getUserFromMobile(mobile);
        if(user==null){
            throw new Exception("User does not exist");
        }
        else{
            Playlist playlist = new Playlist(title);
            playlists.add(playlist);
            creatorPlaylistMap.put(user,playlist);
            List<Song> songList = new ArrayList<>();
            for(String songTitle:songTitles){
                Song song = getSong(title);
                if(song!=null){
                    songList.add(song);
                }
            }
            List<Playlist> playlistList = new ArrayList<>();
            playlistList.add(playlist);
            userPlaylistMap.put(user,playlistList);
            if(playlistListenerMap.containsKey(playlist)){
                playlistListenerMap.get(playlist).add(user);
            }
            else{
                List<User> userList = new ArrayList<>();
                userList.add(user);
                playlistListenerMap.put(playlist,userList);
            }
            return playlist;
        }
    }
    public Song getSong(String name){
        for(Song song:songs){
            if(song.getTitle().equals(name)){
                return song;
            }
        }
        return null;
    }

    public Playlist findPlaylist(String mobile, String playlistTitle) throws Exception {
        User user = getUserFromMobile(mobile);
        if(user==null){
            throw new Exception("User does not exist");
        }
        else{
            Playlist playlist = getPlayList(playlistTitle);
            if(playlist==null){
                throw new Exception("Playlist does not exist");
            }
            else{
                if(creatorPlaylistMap.get(user)==playlist){
                    return playlist;
                }
                else{
                    for(User u:playlistListenerMap.get(playlist)){
                        if(u==user){
                            return playlist;
                        }
                    }
                    if(userPlaylistMap.containsKey(user)){
                        userPlaylistMap.get(user).add(playlist);
                    }
                    else{
                        List<Playlist> playlistList = new ArrayList<>();
                        playlistList.add(playlist);
                        userPlaylistMap.put(user,playlistList);
                    }
                    if(playlistListenerMap.containsKey(playlist)){
                        playlistListenerMap.get(playlist).add(user);
                    }
                    else{
                        List<User> userList = new ArrayList<>();
                        userList.add(user);
                        playlistListenerMap.put(playlist,userList);
                    }
                    return playlist;
                }
            }
        }
    }

    public Playlist getPlayList(String name){
        for(Playlist playlist : playlists){
            if(playlist.getTitle().equals(name)){
                return playlist;
            }
        }
        return null;
    }
    public Song likeSong(String mobile, String songTitle) throws Exception {
        User user = getUserFromMobile(mobile);
        if(user==null){
            throw new Exception("User does not exist");
        }
        else{
            Song song = getSong(songTitle);
            if(song==null){
                throw new Exception("Song does not exist");
            }
            else{
                if(songLikeMap.containsKey(song)){
                    for(User u:songLikeMap.get(song)){
                        if(u==user){
                            return song;
                        }
                    }
                    songLikeMap.get(song).add(user);
                    Artist artist = getArtistFromSong(song);
                    artistLikeMap.put(artist,artistLikeMap.getOrDefault(artist,0)+1);
                }
                return song;
            }
        }
    }

    public Artist getArtistFromSong(Song song){
        Album album = getAlbumFromSong(song);
        for(Artist artist:artistAlbumMap.keySet()){
            for(Album a:artistAlbumMap.get(artist)){
                if(album==a){
                    return artist;
                }
            }
        }
        return null;
    }
    public Album getAlbumFromSong(Song song){
        for(Album album:albumSongMap.keySet()){
            for(Song s: albumSongMap.get(album)){
                if(s==song){
                    return album;
                }
            }
        }
        return null;
    }
    public String mostPopularArtist() {
        int max=-1;
        Artist mostFamous = null;
        for(Artist artist:artistLikeMap.keySet()){
            if(artistLikeMap.get(artist)>=max){
                max=artistLikeMap.get(artist);
                mostFamous=artist;
            }
        }
        if(mostFamous==null){
            return "";
        }
        return mostFamous.getName();
    }

    public String mostPopularSong() {
        int max=-1;
        Song mostFamous = null;
        for(Song song:songLikeMap.keySet()){
            if(songLikeMap.get(song).size()>=max){
                max=songLikeMap.get(song).size();
                mostFamous = song;
            }
        }
        if(mostFamous==null){
            return "";
        }
        return mostFamous.getTitle();
    }
}
