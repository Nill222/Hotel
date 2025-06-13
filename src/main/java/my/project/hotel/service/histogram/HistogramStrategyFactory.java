package my.project.hotel.service.histogram;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class HistogramStrategyFactory {

    private final Map<String, HistogramStrategy> strategies;

    public HistogramStrategy getStrategy(String param) {
        return strategies.get(param + "HistogramStrategy");
    }
}

