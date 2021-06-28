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
import java.util.Collection;


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


    @RequestMapping()
    public ResponseEntity<String> indexAPI() {
        return new ResponseEntity<String>("Index page", HttpStatus.OK);
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
                                                            @RequestParam(value = "from") String toCity,
                                                            @RequestParam(value = "to") String fromCity) {


        return distanceRepositoryImp.calculateDistanceCrowfligh(method, toCity, fromCity);

    }

}



