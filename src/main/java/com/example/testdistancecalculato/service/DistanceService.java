package com.example.testdistancecalculato.service;

import com.example.testdistancecalculato.entity.City;
import com.example.testdistancecalculato.entity.Distance;
import com.example.testdistancecalculato.exceptions.GlobalExceptionHandler;
import com.example.testdistancecalculato.repository.DistanceRepository;
import javassist.NotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.*;


@Repository
public class DistanceService {

    private final CityService cityService;
    private final DistanceRepository distanceRepository;


    private final String CROWFLIGHT_METHOD = "cf";
    private final String MATRIX_DISTANCE = "md";

    @Autowired
    public DistanceService(CityService cityService, DistanceRepository distanceRepository) {
        this.cityService = cityService;
        this.distanceRepository = distanceRepository;
    }

    public ResponseEntity<Collection<Distance>> calculateDistanceCrowfligh(
            String method,
            String from,
            String to) {


        String[] toCity = to.split(",");
        String[] fromCity = from.split(",");
        List<City> toCityList = new ArrayList<>();
        List<City> fromCityList = new ArrayList<>();

        for (String s : toCity) {
            toCityList.add(cityService.findCityByName(s));
        }
        for (String s : fromCity) {
            fromCityList.add(cityService.findCityByName(s));
        }

        if (method.equals(CROWFLIGHT_METHOD)) {
            return getDistanceCrowFlight(toCityList, fromCityList);

        } else if (method.equals(MATRIX_DISTANCE)) {
            return getDistanceMatrix(toCityList, fromCityList);
        }
        return null;
    }

    private ResponseEntity<Collection<Distance>> getDistanceMatrix(
            List<City> toCityList,
            List<City> fromCityList) {

        double distance;
        Collection<Distance> distanceList = new ArrayList<>();

        if (toCityList.size() != fromCityList.size()) {
            Collection<Distance> error = new ArrayList<>();
            error.add(new Distance("Количество городов", "Должно быть одинаково", 0D));
            return new ResponseEntity<Collection<Distance>>(error, HttpStatus.OK);
        } else {

            for (int i = 0; i < toCityList.size(); i++) {

                distance = distanceCrowFlight(fromCityList.get(i), toCityList.get(i));
                Distance newDistance = new Distance(fromCityList.get(i).getName(),
                        toCityList.get(i).getName(), distance);

                if (distanceRepository.existsByFromCityAndToCity(fromCityList.get(i).getName(), toCityList.get(i).getName())) {
                    distanceList.add(distanceRepository.findByFromCityAndToCity(fromCityList.get(i).getName(), toCityList.get(i).getName()));
                } else {

                    distanceList.add(distanceRepository.save(newDistance));

                }

            }

            System.out.println(Arrays.toString(distanceList.toArray()));

        }


        return new ResponseEntity<Collection<Distance>>(distanceList, HttpStatus.OK);
    }

    private boolean ifNotDistance(Double p1, Double p2, Double p3, Double p4) {
        return p1 == null || p2 == null || p3 == null || p4 == null;
    }

    public ResponseEntity<Collection<Distance>> getDistanceCrowFlight(
            List<City> toCityList,
            List<City> fromCityList) {

        Collection<Distance> distanceList = new ArrayList<>();
        double distance;

        if (toCityList.size() != fromCityList.size()) {
            Collection<Distance> error = new ArrayList<>();
            error.add(new Distance("Количество городов", "Должно быть одинаково", 0D));
            return new ResponseEntity<Collection<Distance>>(error, HttpStatus.OK);
        } else {

            if (fromCityList.size() > 1) {
//                distanceList.add(new Distance(
//                        "В этом методе нельзя добовлять больше одной пары ",
//                        "для добовления многих городов воспользуйтесь методов md " +
//                                "(?method=md&from_city_list&to_city_list) ",
//                        0D));

                return new ResponseEntity<Collection<Distance>>(distanceList, HttpStatus.OK);

            } else {
                distance = distanceCrowFlight(fromCityList.get(0), toCityList.get(0));

                Distance newDistance = new Distance(fromCityList.get(0).getName(),
                        toCityList.get(0).getName(), distance);

                if (distanceRepository.existsByFromCityAndToCity(fromCityList.get(0).getName(), toCityList.get(0).getName())) {
                    distanceList.add(distanceRepository.findByFromCityAndToCity(fromCityList.get(0).getName(), toCityList.get(0).getName()));
                } else {

                    distanceList.add(distanceRepository.save(newDistance));
                }
            }
        }
        return new ResponseEntity<Collection<Distance>>(distanceList, HttpStatus.OK);
    }

    private double distanceCrowFlight(City firstCity, City secondCity) {

        if (firstCity == null || secondCity == null) {
            return 0;
        } else {
            double c = 0;
            double r = 6371;
            if (ifNotDistance(firstCity.getLatitude(), firstCity.getLongitude()
                    , secondCity.getLatitude()
                    , secondCity.getLongitude())) {
                try {
                    throw new NotFoundException("Not Found Distance " +
                            firstCity.getName() + " " +
                            firstCity.getLatitude() + " " +
                            firstCity.getLongitude() + secondCity.getName() + " "
                            + secondCity.getLongitude() + " "
                            + secondCity.getLatitude());
                } catch (NotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                double lon1 = Math.toRadians(firstCity.getLongitude());
                double lon2 = Math.toRadians(secondCity.getLongitude());
                double lat1 = Math.toRadians(firstCity.getLatitude());
                double lat2 = Math.toRadians(secondCity.getLatitude());
                System.out.println(lon1 + " " + lon2 + " " + lat1 + " " + lat2);

                double dlon = lon2 - lon1;
                double dlat = lat2 - lat1;
                double a = Math.pow(Math.sin(dlat / 2), 2)
                        + Math.cos(lat1) * Math.cos(lat2)
                        * Math.pow(Math.sin(dlon / 2), 2);

                c = 2 * Math.asin(Math.sqrt(a));

            }
            return (c * r);
        }

    }


}
