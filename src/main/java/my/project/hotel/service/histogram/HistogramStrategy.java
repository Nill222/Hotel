package my.project.hotel.service.histogram;

import java.util.Map;

public interface HistogramStrategy {
    Map<String, Long> computeHistogram();
}
