package org.opencast.graphql.dummy;

import java.util.ArrayList;
import java.util.Arrays;

public class Data {
    public static int nextId = 1000;

    public static ArrayList<Event> events = new ArrayList<>(Arrays.asList(
        new Event("01234567-abcd-abcd-abcd-000000000001",
            "My Neighbor Totoro",
            "A cute movie about childhood",
            5280,
            "76543210-abcd-abcd-abcd-000000000001",
            new Event.Track[] {
                new Event.Track("video/x-matroska", "https://example.com/totoro.mkv", 24, 1920, 1080)
            }),
        new Event("01234567-abcd-abcd-abcd-000000000002",
            "Spirited Away",
            "Animated fantasy movie with a strong theme of environmentalism",
            7500,
            "76543210-abcd-abcd-abcd-000000000001",
            new Event.Track[] {
                new Event.Track("video/x-matroska", "https://example.com/chihiro.mkv", 24, 1920, 1080)
            }),
        new Event("01234567-abcd-abcd-abcd-000000000003",
            "Princess Mononoke",
            "Another masterpiece.",
            7980,
            "76543210-abcd-abcd-abcd-000000000001",
            new Event.Track[]  {
                new Event.Track("video/x-matroska", "https://example.com/mononoke.mkv", 24, 1920, 1080)
            }),
        new Event("01234567-abcd-abcd-abcd-000000000010",
            "The Holographic Principle",
            "Pretty easy stuff",
            384.2f,
            null,
            new Event.Track[] {
                new Event.Track("video/mp4", "https://example.com/holo-presenter.mp4", 24, 1280, 720),
                new Event.Track("video/mp4", "https://example.com/holo-presentation.mp4", 24, 1920, 1080)
            }),
        new Event("01234567-abcd-abcd-abcd-000000000011",
            "Finite Fields",
            null,
            534.7f,
            null,
            new Event.Track[] {
                new Event.Track("video/mp4", "https://example.com/math-presenter.mp4", 15, 800, 600),
                new Event.Track("video/mp4", "https://example.com/math-presentation.mp4", 30, 1920, 1080)
            }),
        new Event("01234567-abcd-abcd-abcd-000000000020",
            "Spring",
            "Doggo and Trees",
            464,
            "76543210-abcd-abcd-abcd-000000000002",
            new Event.Track[] {
                new Event.Track("video/webm", "https://example.com/spring.webm", 23.97f, 2048, 858),
            }),
        new Event("01234567-abcd-abcd-abcd-000000000021",
            "Cosmos Laundromaut",
            null,
            730,
            "76543210-abcd-abcd-abcd-000000000002",
            new Event.Track[] {
                new Event.Track("video/webm", "https://example.com/cosmos.webm", 23.97f, 2048, 858),
            })
    ));

    public static Series[] series = {
        new Series("76543210-abcd-abcd-abcd-000000000001", "Studio Ghibli Movies", null),
        new Series("76543210-abcd-abcd-abcd-000000000002", "Blender Movies",
            "Blender is a software for 3D modeling, animation, rendering and more. This series "
                + "contains movies created with Blender"),
    };
}
