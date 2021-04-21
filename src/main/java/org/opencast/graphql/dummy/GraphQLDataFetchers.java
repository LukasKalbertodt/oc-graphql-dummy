package org.opencast.graphql.dummy;

import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.Comparator;
import java.util.OptionalInt;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

            // Sort events
            var events = Data.events.clone();
            if (sortBy.equals("TITLE")) {
                Arrays.sort(events, Comparator.comparing(a -> a.title));
            } else if (sortBy.equals("DURATION")) {
                Arrays.sort(events, Comparator.comparing(a -> a.duration));
            }

            // Restrict events to requested range
            int skip = after == null ? 0 : IntStream.range(0, events.length)
                .filter(i -> events[i].id.equals(after))
                .map(i -> i + 1)
                .findFirst()
                .orElse(events.length - 1);
            var edges = Arrays.stream(events)
                .skip(skip)
                .limit(limit)
                .map(event -> new DefaultEdge<Event>(event, new DefaultConnectionCursor(event.id)))
                .collect(Collectors.toList());

            // Collect page information
            var startCursor = edges.isEmpty() ? null : edges.get(0).getNode().id;
            var endCursor = edges.isEmpty() ? null : edges.get(edges.size() - 1).getNode().id;
            var hasPreviousPage = skip != 0;
            var hasNextPage = limit - 1 < events.length;
            var pageInfo = new DefaultPageInfo(
                new DefaultConnectionCursor(startCursor),
                new DefaultConnectionCursor(endCursor),
                hasPreviousPage,
                hasNextPage
            );

            return new DefaultConnection(edges, pageInfo);
        };
    }
}