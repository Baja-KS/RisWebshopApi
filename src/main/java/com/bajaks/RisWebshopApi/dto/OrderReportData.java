package com.bajaks.RisWebshopApi.dto;

import com.bajaks.RisWebshopApi.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class OrderReportData {
    private Date from;
    private Date to;
    private String address;
    private Float minTotal;
    private Float maxTotal;

    public Map<String, Object> toMap(){
        ObjectMapper om = new ObjectMapper();
        Map<String,Object> map = om.convertValue(this,Map.class);
        map.values().remove(null);
        return map;
    }
}
