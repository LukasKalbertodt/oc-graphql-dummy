package org.opencast.graphql.dummy;

import org.springframework.stereotype.Component;
import java.util.Arrays;
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
}