package com.bajaks.RisWebshopApi.dto;

import com.bajaks.RisWebshopApi.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserSearchData {
    private String search;
    private Role role;
    private Integer page;
    private Integer perPage;
}
