package com.github.avarabyeu.jashing.events;

import com.github.avarabyeu.jashing.core.JashingEvent;

import java.util.List;

/**
 * Complex Graph event for RickshawGraph widget
 * @author Andrei Varabyeu
 */
public class ComplexGraphEvent extends JashingEvent {

    private List<Series> series;

    private String displayedValue;

    public ComplexGraphEvent(List<Series> series) {
        this.series = series;
    }

    public ComplexGraphEvent(List<Series> series, String displayedValue) {
        this.series = series;
        this.displayedValue = displayedValue;
    }

    public List<Series> getSeries() {
        return series;
    }

    public void setSeries(List<Series> series) {
        this.series = series;
    }

    public String getDisplayedValue() {
        return displayedValue;
    }


    public static class Series {
        private String name;
        private List<Point> data;

        public Series(String name, List<Point> data) {
            this.name = name;
            this.data = data;
        }

        public String getName() {
            return name;
        }

        public List<Point> getData() {
            return data;
        }
    }


    public static class Point {
        private Long x;
        private Long y;

        public Point(Long x, Long y) {
            this.x = x;
            this.y = y;
        }

        public Long getX() {
            return x;
        }

        public void setX(Long x) {
            this.x = x;
        }

        public Long getY() {
            return y;
        }

        public void setY(Long y) {
            this.y = y;
        }
    }
}
