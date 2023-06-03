package lv.nixx.poc.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Statistics {
    private long count;
}
