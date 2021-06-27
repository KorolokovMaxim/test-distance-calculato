package com.example.testdistancecalculato.repository;

import com.example.testdistancecalculato.entity.Cities;
import com.example.testdistancecalculato.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Map;


public interface CityRepository extends JpaRepository<City , Long> {

    City findByName(String name);



}
