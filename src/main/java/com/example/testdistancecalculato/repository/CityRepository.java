package com.example.testdistancecalculato.repository;

import com.example.testdistancecalculato.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;



public interface CityRepository extends JpaRepository<City , Long> {

    City findByName(String name);



}
