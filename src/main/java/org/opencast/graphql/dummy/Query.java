package org.opencast.graphql.dummy;

import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import graphql.relay.Connection;
import graphql.relay.DefaultConnection;
import graphql.relay.DefaultConnectionCursor;
import graphql.relay.DefaultEdge;
import graphql.relay.DefaultPageInfo;
import graphql.relay.Edge;
import graphql.schema.DataFetchingEnvironment;

@Component
public class Query {
    public static Event getEventById(DataFetchingEnvironment env) {
        String id = env.getArgument("id");
        return Data.events.stream()
                .filter(event -> event.id.equals(id))
                .findFirst()
                .orElse(null);
    }

    public static Series getSeriesById(DataFetchingEnvironment env) {
        String id = env.getArgument("id");
        return Arrays.stream(Data.series)
                .filter(series -> series.id.equals(id))
                .findFirst()
                .orElse(null);
    }

    public static Connection<Event> getEvents(DataFetchingEnvironment env) {
        String sortBy = env.getArgument("sortBy");
        String after = env.getArgument("after");
        int limit = env.getArgumentOrDefault("limit", Integer.MAX_VALUE);

        return eventConnection(sortBy, limit, after, null);
    }

    public static Connection<Event> eventConnection(
        String sortBy,
        int limit,
        String after,
        String seriesId
    ) {
        var events = Data.events.toArray(Event[]::new);

        // Filter by series, if a series is specified
        if (seriesId != null) {
            events = Arrays.stream(events)
                .filter(event -> seriesId.equals(event.seriesID))
                .toArray(Event[]::new);
        }

        // Sort events
        if ("TITLE".equals(sortBy)) {
            Arrays.sort(events, Comparator.comparing(a -> a.title));
        } else if ("DURATION".equals(sortBy)) {
            Arrays.sort(events, Comparator.comparing(a -> a.duration));
        }

        // Restrict events to requested range
        var edgesPlusOne = Arrays.stream(events)
            .filter(after == null ? x -> true : skipUntilAfter(event -> event.id.equals(after)))
            .limit(1 + (long)limit)
            .map(event -> (Edge<Event>) new DefaultEdge<>(event, new DefaultConnectionCursor(event.id)))
            .collect(Collectors.toList());
        var edges = edgesPlusOne.size() == limit + 1 ? edgesPlusOne.subList(0, limit) : edgesPlusOne;

        // Collect page information
        var startCursor = edges.isEmpty()
            ? null
            : new DefaultConnectionCursor(edges.get(0).getNode().id);
        var endCursor = edges.isEmpty()
            ? null
            : new DefaultConnectionCursor(edges.get(edges.size() - 1).getNode().id);
        var hasPreviousPage = after != null;
        var hasNextPage = edgesPlusOne.size() == limit + 1;
        var pageInfo = new DefaultPageInfo(startCursor, endCursor, hasPreviousPage, hasNextPage);

        return new DefaultConnection<>(edges, pageInfo);
    }

    private static <T> Predicate<T> skipUntilAfter(Predicate<T> predicate) {
        boolean[] seen = { false };
        return t -> {
            if (seen[0]) {
                return true;
            }
            if (predicate.test(t)) {
                seen[0] = true;
            }
            return false;
        };
    }
}