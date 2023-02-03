package com.bajaks.RisWebshopApi.dto;

import com.bajaks.RisWebshopApi.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class OrderSearchData {
    private Date from;
    private Date to;
    private String address;
    private Float minTotal;
    private Float maxTotal;
    private Integer page;
    private Integer perPage;

    private User user;
}
