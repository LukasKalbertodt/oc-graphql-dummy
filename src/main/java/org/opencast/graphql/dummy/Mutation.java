package org.opencast.graphql.dummy;

import java.util.Map;

import graphql.schema.DataFetchingEnvironment;

public class Mutation {
  public static Event addEvent(DataFetchingEnvironment env) {
    Map<String, Object> input = env.getArgument("event");
    var title = (String) input.get("title");
    var description = (String) input.get("description");
    var seriesId = (String) input.get("seriesId");
    var duration = (Double) input.get("duration");
    if (title == null) {
      throw new RuntimeException("'title' must not be null");
    }
    if (duration == null) {
      throw new RuntimeException("'duration' must not be null");
    }

    if (seriesId != null && Data.seriesById(seriesId) == null) {
      throw new RuntimeException("'seriesId' is invalid: no such series exists!");
    }

    // This breaks when `nextId > 9999`, is susceptible to data races and is broken in a lot
    // of other ways :P
    var id = "01234567-abcd-abcd-abcd-00000000" + Data.nextId;
    Data.nextId += 1;
    var event = new Event(id, title, description, duration.floatValue(), seriesId, new Event.Track[] {});
    Data.events.add(event);

    return event;
  }
}
