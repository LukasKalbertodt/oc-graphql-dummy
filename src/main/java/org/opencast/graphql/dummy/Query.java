package org.opencast.graphql.dummy;

import org.springframework.stereotype.Component;

import graphql.relay.Connection;
import graphql.schema.DataFetchingEnvironment;

@Component
public class Query {
  public static Event getEventById(DataFetchingEnvironment env) {
    String id = env.getArgument("id");
    return Data.eventById(id);
  }

  public static Series getSeriesById(DataFetchingEnvironment env) {
    String id = env.getArgument("id");
    return Data.seriesById(id);
  }

  public static Connection<Event> getEvents(DataFetchingEnvironment env) {
    String sortBy = env.getArgument("sortBy");
    String after = env.getArgument("after");
    int limit = env.getArgumentOrDefault("limit", Integer.MAX_VALUE);

    return Util.eventConnection(sortBy, limit, after, null);
  }
}