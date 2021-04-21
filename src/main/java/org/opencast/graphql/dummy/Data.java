package org.opencast.graphql.dummy;

public class Data {
    public static Event[] events = {
        new Event("01234567-abcd-abcd-abcd-000000000001",
            "My Neighbor Totoro",
            "A cute movie about childhood",
            5280,
            "76543210-abcd-abcd-abcd-000000000001"),
        new Event("01234567-abcd-abcd-abcd-000000000002",
            "Spirited Away",
            "Animated fantasy movie with a strong theme of environmentalism",
            7500,
            "76543210-abcd-abcd-abcd-000000000001"),
        new Event("01234567-abcd-abcd-abcd-000000000003",
            "My Neighbor Totoro",
            "A cute movie about childhood",
            5280,
            "76543210-abcd-abcd-abcd-000000000001"),
        new Event("01234567-abcd-abcd-abcd-000000000010",
            "The Holographic Principle",
            "Pretty easy stuff",
            384.2f,
            null),
        new Event("01234567-abcd-abcd-abcd-000000000011",
            "Finite Fields",
            null,
            534.7f,
            null),
        new Event("01234567-abcd-abcd-abcd-000000000020",
            "Spring",
            "Doggo and Trees",
            234.5f,
            "76543210-abcd-abcd-abcd-000000000002"),
        new Event("01234567-abcd-abcd-abcd-000000000021",
            "Cosmos Laundromaut",
            null,
            432,
            "76543210-abcd-abcd-abcd-000000000002"),
    };

    public static Series[] series = {
        new Series("76543210-abcd-abcd-abcd-000000000001", "Studio Ghibli Movies", null),
        new Series("76543210-abcd-abcd-abcd-000000000002", "Blender Movies",
            "Blender is a software for 3D modeling, animation, rendering and more. This series "
                + "contains movies created with Blender"),
    };
}
