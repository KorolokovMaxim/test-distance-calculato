package com.example.testdistancecalculato.repository;

import com.example.testdistancecalculato.entity.City;
import com.example.testdistancecalculato.entity.Distance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface DistanceRepository extends JpaRepository<Distance , Long> {


        Boolean existsByFromCityAndToCity(String from , String to );

        Distance findByFromCityAndToCity(String from , String to);


}
