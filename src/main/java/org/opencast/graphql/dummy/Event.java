package org.opencast.graphql.dummy;

public class Event {
    public String id;
    public String title;
    public String description;
    public float duration;
    public String seriesID;
    public Track[] tracks;

    public Event(String id, String title, String description, float duration, String seriesID, Track[] tracks) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.duration = duration;
        this.seriesID = seriesID;
        this.tracks = tracks;
    }

    public Series getSeries() {
        return Data.seriesById(this.seriesID);
    }

    public static class Track {
        public String mimetype;
        public String url;
        public float framerate;
        public int width;
        public int height;

        public Track(String mimetype, String url, float framerate, int width, int height) {
            this.mimetype = mimetype;
            this.url = url;
            this.framerate = framerate;
            this.width = width;
            this.height = height;
        }
    }
}
