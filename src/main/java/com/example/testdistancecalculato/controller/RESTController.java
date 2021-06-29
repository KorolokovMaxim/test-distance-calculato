package com.example.testdistancecalculato.controller;


import com.example.testdistancecalculato.entity.City;
import com.example.testdistancecalculato.entity.Distance;
import com.example.testdistancecalculato.service.DistanceService;
import com.example.testdistancecalculato.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@RestController
@RequestMapping(path = "/")
public class RESTController {


    private final CityService cityService;
    private final DistanceService distanceRepositoryImp;

    @Value("${file.upload-dir}")
    String FILE_DIRECTORY;

    @Autowired
    public RESTController(CityService cityService, DistanceService distanceRepositoryImp) {
        this.cityService = cityService;
        this.distanceRepositoryImp = distanceRepositoryImp;
    }


    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<Object>> indexAPI() {
        List<Object> commands = new ArrayList<>();

        commands.add("Показать все города");
        commands.add("http://localhost:8080/show-all-city");
        commands.add("Подсчет методом crowflight");
        commands.add("Пример: http://localhost:8080/get-distance?method=cf&from=Самара,Иркутск&to=Тверь,Москва");
        commands.add("Подсчет методом distance matrix");
        commands.add("Пример: http://localhost:8080/get-distance?method=md&from=Самара,Иркутск&to=Тверь,Москва");
        commands.add("Подсчет двумя способами");
        commands.add("Пример: http://localhost:8080/get-distance?method=all&from=Самара,Иркутск&to=Тверь,Москва");


        return new ResponseEntity<List<Object>>(commands, HttpStatus.OK);
    }


    @RequestMapping(value = "/show-all-city", produces = "application/json")
    public ResponseEntity<Collection<City>> showAllCity() {
        return this.cityService.showAllCity();
    }


    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public void uploadFile(@RequestParam("file") MultipartFile file) throws IOException {

        cityService.uploadFile(file);

        for (City city : cityService.getAllCities().getCities()) {
            cityService.save(city);
        }

    }

    @RequestMapping(value = "/get-distance", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody()
    public ResponseEntity<Collection<Distance>> getDistance(@RequestParam(value = "method") String method,
                                                            @RequestParam(value = "from") String fromCity,
                                                            @RequestParam(value = "to") String toCity) {


        return distanceRepositoryImp.calculateDistanceCrowfligh(method, fromCity, toCity);

    }

}



