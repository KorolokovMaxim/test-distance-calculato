package com.example.testdistancecalculato.jaxb;

import com.example.testdistancecalculato.entity.Cities;
import com.example.testdistancecalculato.entity.City;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;


public class jaxbWriter {

    public static void main(String[] args)  {

        City city = new City(1L , "Samara" , 53.2000700 ,50.1500000 );
        City moscow = new City(2L ,"Moscow" , 53.2000700 , 50.1500000);
        City tver = new City(3L ,"Tver" , 56.852 , 35.9217);
        City irkutsk = new City(4L ,"Irkutsk" , 52.29778 , 104.29639);

        Cities cities = new Cities();
        cities.getCities().add(city);
        cities.getCities().add(moscow);
        cities.getCities().add(tver);
        cities.getCities().add(irkutsk);


        File file = new File("file.xml");
        try {
            JAXBContext context = JAXBContext.newInstance(Cities.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT , true);
            marshaller.marshal(cities ,file);
            marshaller.marshal(cities , System.out);

        } catch (JAXBException  e) {
            e.printStackTrace();
        }


    }

}
