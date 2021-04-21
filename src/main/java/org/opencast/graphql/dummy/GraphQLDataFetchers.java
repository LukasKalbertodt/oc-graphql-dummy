package org.opencast.graphql.dummy;

import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.Comparator;
import java.util.OptionalInt;
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
            //String after = dataFetchingEnvironment.getArgument("after");
            //int count = dataFetchingEnvironment.getArgument("count");

            var events = Data.events.clone();
            if (sortBy.equals("TITLE")) {
                Arrays.sort(events, Comparator.comparing(a -> a.title));
            } else if (sortBy.equals("DURATION")) {
                Arrays.sort(events, Comparator.comparing(a -> a.duration));
            }

            var edges = Arrays.stream(events)
                    .map(event -> new DefaultEdge<Event>(event, new DefaultConnectionCursor(event.id)))
                    .collect(Collectors.toList());
            
            var pageInfo = new DefaultPageInfo(
                new DefaultConnectionCursor(edges.get(0).getNode().id),
                new DefaultConnectionCursor(edges.get(edges.size() - 1).getNode().id),
                false,
                false
            );

            return new DefaultConnection(edges, pageInfo);
        };
    }
}