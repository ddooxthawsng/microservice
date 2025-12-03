package thangdm12.statistic_service.model;

import java.util.Date;

import lombok.Data;

@Data
public class StatisticDTO {
    private Long id;
    private String message;
    private Date createdDate;
}
