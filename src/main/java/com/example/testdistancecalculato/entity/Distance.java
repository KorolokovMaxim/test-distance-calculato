package com.example.testdistancecalculato.entity;


import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@EqualsAndHashCode
@Entity
@Table(name = "distance")
public class Distance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "from_city")
    private String fromCity;
    @Column(name = "to_city" )
    private String toCity;
    @Column(name = "distance")
    private Double distance;

    public Distance(String fromCity , String toCity , Double distance){
        this.fromCity = fromCity;
        this.toCity = toCity;
        this.distance = distance;
    }

    public Distance() {

    }
}
