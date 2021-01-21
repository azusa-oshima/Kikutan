package com.mlt.tango.library;

public class MusicLibrary_Sub2 {
//    private TreeMap<String, MediaMetadataCompat> music = new TreeMap<>();
//    private HashMap<String, Integer> albumRes = new HashMap<>();
//    private HashMap<String, String> musicFileName = new HashMap<>();
//
////    public MusicLibrary(){
////    }
//
//    public static void init() {
//        createMediaMetadataCompat(
//                "0",
//                "Jazz in Paris",
//                "Media Right Productions",
//                "Jazz & Blues",
//                "Jazz",
//                10,
//                TimeUnit.SECONDS,
//                "mb_word_1.m4a",
//                R.drawable.album_jazz_blues,
//                "album_jazz_blues");
//        createMediaMetadataCompat(
//                "1",
//                "The Coldest Shoulder22",
//                "The 126ers",
//                "Youtube Audio Library Rock 2",
//                "Rock",
//                160,
//                TimeUnit.SECONDS,
//                "mb_word_2.m4a",
//                R.drawable.album_youtube_audio_library_rock_2,
//                "album_youtube_audio_library_rock_2");
//        createMediaMetadataCompat(
//                "2",
//                "The Coldest Shoulder",
//                "The 126ers",
//                "Youtube Audio Library Rock 2",
//                "Rock",
//                160,
//                TimeUnit.SECONDS,
//                "mb_word_3.m4a",
//                R.drawable.album_youtube_audio_library_rock_2,
//                "album_youtube_audio_library_rock_2");
//    }
//
//
//    public String getRoot() {
//        return "root";
//    }
//
//    private String getAlbumArtUri(String albumArtResName) {
//        return ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
//                BuildConfig.APPLICATION_ID + "/drawable/" + albumArtResName;
//    }
//
//    public String getMusicFilename(String mediaId) {
//        return musicFileName.containsKey(mediaId) ? musicFileName.get(mediaId) : null;
//    }
//
//    private int getAlbumRes(String mediaId) {
//        return albumRes.containsKey(mediaId) ? albumRes.get(mediaId) : 0;
//    }
//
//    public Bitmap getAlbumBitmap(Context context, String mediaId) {
//        return BitmapFactory.decodeResource(context.getResources(),
//                getAlbumRes(mediaId));
//    }
//
//    public List<MediaBrowserCompat.MediaItem> getMediaItems() {
//        List<MediaBrowserCompat.MediaItem> result = new ArrayList<>();
//        for (MediaMetadataCompat metadata : music.values()) {
//            result.add(
//                    new MediaBrowserCompat.MediaItem(
//                            metadata.getDescription(), MediaBrowserCompat.MediaItem.FLAG_PLAYABLE));
//        }
//        return result;
//    }
//
//    public MediaMetadataCompat getMetadata(Context context, String mediaId) {
//        MediaMetadataCompat metadataWithoutBitmap = music.get(mediaId);
//        Bitmap albumArt = getAlbumBitmap(context, mediaId);
//
//        // Since MediaMetadataCompat is immutable, we need to create a copy to set the album art.
//        // We don't set it initially on all queueItems so that they don't take unnecessary memory.
//        MediaMetadataCompat.Builder builder = new MediaMetadataCompat.Builder();
//        for (String key :
//                new String[]{
//                        MediaMetadataCompat.METADATA_KEY_MEDIA_ID,
//                        MediaMetadataCompat.METADATA_KEY_ALBUM,
//                        MediaMetadataCompat.METADATA_KEY_ARTIST,
//                        MediaMetadataCompat.METADATA_KEY_GENRE,
//                        MediaMetadataCompat.METADATA_KEY_TITLE
//                }) {
//            builder.putString(key, metadataWithoutBitmap.getString(key));
//        }
//        builder.putLong(
//                MediaMetadataCompat.METADATA_KEY_DURATION,
//                metadataWithoutBitmap.getLong(MediaMetadataCompat.METADATA_KEY_DURATION));
//        builder.putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, albumArt);
//        return builder.build();
//    }
//
//    private static void createMediaMetadataCompat(
//            String mediaId,
//            String title,
//            String artist,
//            String album,
//            String genre,
//            long duration,
//            TimeUnit durationUnit,
//            String musicFilename,
//            int albumArtResId,
//            String albumArtResName) {
//        music.put(
//                mediaId,
//                new MediaMetadataCompat.Builder()
//                        .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, mediaId)
//                        .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, album)
//                        .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, artist)
//                        .putLong(MediaMetadataCompat.METADATA_KEY_DURATION,
//                                TimeUnit.MILLISECONDS.convert(duration, durationUnit))
//                        .putString(MediaMetadataCompat.METADATA_KEY_GENRE, genre)
//                        .putString(
//                                MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI,
//                                getAlbumArtUri(albumArtResName))
//                        .putString(
//                                MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON_URI,
//                                getAlbumArtUri(albumArtResName))
//                        .putString(MediaMetadataCompat.METADATA_KEY_TITLE, title)
//                        .build());
//        albumRes.put(mediaId, albumArtResId);
//        musicFileName.put(mediaId, musicFilename);
//    }
}
