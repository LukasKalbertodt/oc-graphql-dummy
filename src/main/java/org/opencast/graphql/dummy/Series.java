package org.opencast.graphql.dummy;

import graphql.relay.Connection;
import graphql.schema.DataFetchingEnvironment;

public class Series {
  public String id;
  public String name;
  public String description;

  public Series(String id, String name, String description) {
    this.id = id;
    this.name = name;
    this.description = description;
  }

  public Connection<Event> getEvents(DataFetchingEnvironment env) {
    String sortBy = env.getArgument("sortBy");
    String after = env.getArgument("after");
    int limit = env.getArgumentOrDefault("limit", Integer.MAX_VALUE);

    return Util.eventConnection(sortBy, limit, after, this.id);
  }
}
