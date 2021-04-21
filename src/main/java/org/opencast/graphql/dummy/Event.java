package org.opencast.graphql.dummy;

import java.util.Arrays;

public class Event {
  public String id;
  public String title;
  public String description;
  public float duration;
  private String seriesID;

  public Event(String id, String title, String description, float duration, String seriesID) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.duration = duration;
    this.seriesID = seriesID;
  }

  public Series getSeries() {
    return Arrays.stream(Data.series)
        .filter(series -> series.id.equals(this.seriesID))
        .findFirst()
        .orElse(null);
  }
}
