package com.bajaks.RisWebshopApi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
@Table(name = "order_table")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "order",cascade = CascadeType.PERSIST)
    @JsonManagedReference
    private List<OrderItem> orderItems;

    private String address;
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    private Boolean delivered = Boolean.FALSE;

    @Temporal(TemporalType.TIMESTAMP)
    private Date deliveryTime;

    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id",nullable = false)
    @JsonManagedReference
    private User user;


    public Float total(){
        return orderItems.stream().reduce((float) 0,(current, orderItem) -> current+orderItem.getUnitPrice()*orderItem.getQuantity(), Float::sum);
    }



}
