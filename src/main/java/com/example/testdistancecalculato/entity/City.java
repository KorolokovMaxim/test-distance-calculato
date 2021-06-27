package com.example.testdistancecalculato.entity;



import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.xml.bind.annotation.*;


@Entity
@XmlRootElement(name = "city")
@Data
@EqualsAndHashCode
@XmlAccessorType(XmlAccessType.FIELD)
public class City {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @XmlAttribute(required = true)
    private Long id;

    @Column(name = "name")
    @XmlElement
    private String name;

    @Column(name = "latitude")
    @XmlElement
    @JsonIgnore
    private Double latitude;

    @Column(name = "longitude")
    @XmlElement
    @JsonIgnore
    private Double longitude;


    public City() {
    }

    public City(Long id , String name){
        this.id = id;
        this.name = name;
    }

    public City(Long id, String name, Double latitude, Double longitude) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }


}
