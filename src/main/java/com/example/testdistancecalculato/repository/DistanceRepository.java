package com.example.testdistancecalculato.repository;

import com.example.testdistancecalculato.entity.Distance;
import org.springframework.data.jpa.repository.JpaRepository;




public interface DistanceRepository extends JpaRepository<Distance , Long> {


        Boolean existsByFromCityAndToCityAndDistance(String from , String to , Double distance );

        Distance findByFromCityAndToCityAndDistance(String from , String to , Double distance);


}
