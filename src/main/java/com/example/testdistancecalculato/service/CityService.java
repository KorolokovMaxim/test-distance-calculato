package com.example.testdistancecalculato.service;

import com.example.testdistancecalculato.entity.Cities;
import com.example.testdistancecalculato.entity.City;
import com.example.testdistancecalculato.repository.CityRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

import java.io.IOException;
import java.util.*;


@Service
public class CityService {

    private final CityRepository cityRepository;
    MultipartFile uploadFile;

    @Value("${file.upload-dir}")
    String FILE_DIRECTORY;


    private File file;

    @Autowired
    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;

    }


    public void uploadFile(MultipartFile inputFile) throws IOException {
      inputFile.transferTo(new File(FILE_DIRECTORY+inputFile.getOriginalFilename()));
        uploadFile = inputFile;
        file = new File(FILE_DIRECTORY+uploadFile.getOriginalFilename());
    }


    public ResponseEntity<Collection<City>>  showAllCity() {

       return new ResponseEntity<>(cityRepository.findAll() , HttpStatus.OK);
    }



    public Cities getAllCities() {
        Cities cities = new Cities();

        try {
            JAXBContext context = JAXBContext.newInstance(Cities.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            cities = (Cities) unmarshaller.unmarshal(file);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return cities;
    }


    public City findCityByName(String name) {

        return cityRepository.findByName(name);
    }

    public void save(City city) {
        cityRepository.save(city);
    }
}
