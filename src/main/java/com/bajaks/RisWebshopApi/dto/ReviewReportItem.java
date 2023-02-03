package com.bajaks.RisWebshopApi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewReportItem {
    private String username;
    private String comment;
    private Integer rating;
}
