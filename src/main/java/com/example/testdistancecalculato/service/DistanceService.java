package com.example.testdistancecalculato.service;

import com.example.testdistancecalculato.entity.City;
import com.example.testdistancecalculato.entity.Distance;
import com.example.testdistancecalculato.repository.DistanceRepository;
import javassist.NotFoundException;

import netscape.javascript.JSException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.*;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;


@Repository
public class DistanceService {

    private final CityService cityService;
    private final DistanceRepository distanceRepository;


    private final String CROWFLIGHT_METHOD = "cf";
    private final String MATRIX_DISTANCE = "md";
    private final String ALL_METHODS = "all";

    @Autowired
    public DistanceService(CityService cityService, DistanceRepository distanceRepository) {
        this.cityService = cityService;
        this.distanceRepository = distanceRepository;
    }

    public ResponseEntity<Collection<Distance>> calculateDistanceCrowfligh(
            String method,
            String from,
            String to) {

        String[] fromCity = from.split(",");
        String[] toCity = to.split(",");

        List<City> fromCityList = new ArrayList<>();
        List<City> toCityList = new ArrayList<>();

        for (String s : fromCity) {
            fromCityList.add(cityService.findCityByName(s));
        }

        for (String s : toCity) {
            toCityList.add(cityService.findCityByName(s));
        }



        switch (method) {
            case CROWFLIGHT_METHOD:
                return getDistanceCrowFlight(fromCityList, toCityList);
            case MATRIX_DISTANCE:
                return getDistanceMatrix(fromCityList, toCityList);
            case ALL_METHODS:
                return getAllDistance(fromCityList, toCityList);
            default:
                throw new JSException("Метода не существует");
        }

    }

    private ResponseEntity<Collection<Distance>> getDistanceMatrix(
            List<City> fromCityList,
            List<City> toCityList) {


        List<Distance> distanceList = new ArrayList<>();

        if (toCityList.size() != fromCityList.size()) {
            throw new JSException("Количество городов Должно быть одинаково");
        } else {
            for (int i = 0; i < toCityList.size(); i++) {

                double distance = distanceMatrix(fromCityList.get(i).getLatitude(),
                        toCityList.get(i).getLatitude(),
                        fromCityList.get(i).getLongitude(),
                        toCityList.get(i).getLongitude());

                createNewDistance(fromCityList, toCityList, distance, distanceList, i);

            }
        }
        return new ResponseEntity<Collection<Distance>>(distanceList, HttpStatus.OK);
    }

    private void createNewDistance(List<City> fromCityList,
                                   List<City> toCityList,
                                   double distance,
                                   List<Distance> distanceList,
                                   int i) {

        Distance newDistance = new Distance(fromCityList.get(i).getName(),
                toCityList.get(i).getName(), distance);

        if (distanceRepository.existsByFromCityAndToCityAndDistance(
                fromCityList.get(i).getName(),
                toCityList.get(i).getName() ,
                distance)) {
            distanceList.add(distanceRepository.findByFromCityAndToCityAndDistance(
                    fromCityList.get(i).getName(),
                    toCityList.get(i).getName() , distance));
        } else {
            distanceList.add(distanceRepository.save(newDistance));
        }
    }

    public ResponseEntity<Collection<Distance>> getDistanceCrowFlight(
            List<City> fromCityList,
            List<City> toCityList) {

        List<Distance> distanceList = new ArrayList<>();

        if (toCityList.size() != fromCityList.size()) {
            throw new JSException("Количество городов Должно быть одинаково");
        } else {
            for (int i = 0; i < fromCityList.size(); i++) {
                double distance = distanceCrowFlight(fromCityList.get(i), toCityList.get(i));
                createNewDistance(fromCityList, toCityList, distance, distanceList, i);

            }
        }

        return new ResponseEntity<Collection<Distance>>(distanceList, HttpStatus.OK);
    }

    private ResponseEntity<Collection<Distance>> getAllDistance(List<City> fromCityList,
                                                                List<City> toCityList) {
        List<Distance> allMethodDistance = new ArrayList<>();

        double crowflightDistance;
        double distanceMatrix;

        if (toCityList.size() != fromCityList.size()) {
            throw new JSException("Количество городов Должно быть одинаково");
        } else {
            for (int i = 0; i < fromCityList.size(); i++) {
                crowflightDistance = distanceCrowFlight(fromCityList.get(i), toCityList.get(i));
                distanceMatrix = distanceMatrix(fromCityList.get(i).getLatitude(),
                        toCityList.get(i).getLatitude(),
                        fromCityList.get(i).getLongitude(),
                        toCityList.get(i).getLongitude());
                createNewDistance(fromCityList, toCityList, crowflightDistance, allMethodDistance, i);
                createNewDistance(fromCityList, toCityList, distanceMatrix, allMethodDistance, i);

            }
        }

        return new ResponseEntity<Collection<Distance>>(allMethodDistance, HttpStatus.OK);
    }

    private boolean ifNotDistance(Double p1, Double p2, Double p3, Double p4) {
        return p1 == null || p2 == null || p3 == null || p4 == null;
    }

    private float distanceCrowFlight(City firstCity, City secondCity) {

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
                double a = pow(Math.sin(dlat / 2), 2)
                        + Math.cos(lat1) * Math.cos(lat2)
                        * pow(Math.sin(dlon / 2), 2);

                c = 2 * Math.asin(sqrt(a));

            }
            return (float) (c * r);
        }

    }
    private float distanceMatrix(double x1, double y1, double x2, double y2) {

        return (float) Math.sqrt(Math.pow(x2 - x1, 2) +
                Math.pow(y2 - y1, 2));

    }

}
