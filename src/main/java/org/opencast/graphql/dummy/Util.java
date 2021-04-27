package org.opencast.graphql.dummy;

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

public class Util {
  /**
   * Returns a connection over all events.
   *
   * @param sortBy Either 'TITLE', 'DURATION' or null.
   * @param limit Maximum number of events included in this connection.
   * @param after Cursor of one event. Only events after that event are included in the
   *              connection. May be null.
   * @param seriesId If not null, only events from that series are included in the connection.
   */
  public static Connection<Event> eventConnection(
      final String sortBy,
      final int limit,
      final String after,
      final String seriesId
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
    } else if (sortBy != null) {
      throw new RuntimeException("'sortBy' has invalid value");
    }

    // Restrict events to requested range
    final var edgesPlusOne = Arrays.stream(events)
        .filter(after == null ? x -> true : skipUntilAfter(event -> event.id.equals(after)))
        .limit(1 + (long)limit)
        .map(event -> (Edge<Event>) new DefaultEdge<>(event, new DefaultConnectionCursor(event.id)))
        .collect(Collectors.toList());
    final var edges = edgesPlusOne.size() == limit + 1 ? edgesPlusOne.subList(0, limit) : edgesPlusOne;

    // Collect page information
    final var startCursor = edges.isEmpty()
        ? null
        : new DefaultConnectionCursor(edges.get(0).getNode().id);
    final var endCursor = edges.isEmpty()
        ? null
        : new DefaultConnectionCursor(edges.get(edges.size() - 1).getNode().id);
    final var hasPreviousPage = after != null;
    final var hasNextPage = edgesPlusOne.size() == limit + 1;
    final var pageInfo = new DefaultPageInfo(startCursor, endCursor, hasPreviousPage, hasNextPage);

    return new DefaultConnection<>(edges, pageInfo);
  }

  private static <T> Predicate<T> skipUntilAfter(Predicate<T> predicate) {
    final boolean[] seen = { false };
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
