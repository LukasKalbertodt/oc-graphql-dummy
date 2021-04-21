package org.opencast.graphql.dummy;

import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.Comparator;
import java.util.OptionalInt;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.swing.text.html.Option;

import graphql.relay.Connection;
import graphql.relay.DefaultConnection;
import graphql.relay.DefaultConnectionCursor;
import graphql.relay.DefaultEdge;
import graphql.relay.DefaultPageInfo;
import graphql.schema.DataFetcher;

@Component
public class GraphQLDataFetchers {
    public DataFetcher getEventById() {
        return dataFetchingEnvironment -> {
            String id = dataFetchingEnvironment.getArgument("id");
            return Arrays.stream(Data.events)
                    .filter(event -> event.id.equals(id))
                    .findFirst()
                    .orElse(null);
        };
    }

    public DataFetcher getSeriesById() {
        return dataFetchingEnvironment -> {
            String id = dataFetchingEnvironment.getArgument("id");
            return Arrays.stream(Data.series)
                    .filter(series -> series.id.equals(id))
                    .findFirst()
                    .orElse(null);
        };
    }

    public DataFetcher getEvents() {
        return dataFetchingEnvironment -> {
            String sortBy = dataFetchingEnvironment.getArgument("sortBy");
            String after = dataFetchingEnvironment.getArgument("after");
            int limit = dataFetchingEnvironment.getArgumentOrDefault("limit", Integer.MAX_VALUE);

            return eventConnection(sortBy, limit, after, null);
        };
    }

    public static Connection eventConnection(String sortBy, int limit, String after, String seriesId) {
        var events = Data.events.clone();

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
            .map(event -> new DefaultEdge<Event>(event, new DefaultConnectionCursor(event.id)))
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

        return new DefaultConnection(edges, pageInfo);
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