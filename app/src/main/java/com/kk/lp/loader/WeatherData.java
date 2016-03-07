package com.kk.lp.loader;

import java.util.List;

/**
 * Created by lipeng on 2016 3-8.
 */
public class WeatherData {


    /**
     * lon : 116.4
     * lat : 39.91
     */

    private CoordEntity coord;
    /**
     * coord : {"lon":116.4,"lat":39.91}
     * weather : [{"id":800,"main":"Clear","description":"晴","icon":"01d"}]
     * base : stations
     * main : {"temp":6,"pressure":1028,"humidity":11,"temp_min":6,"temp_max":6}
     * visibility : 10000
     * wind : {"speed":2}
     * clouds : {"all":0}
     * dt : 1457424000
     * sys : {"type":1,"id":7405,"message":0.011,"country":"CN","sunrise":1457390185,"sunset":1457432056}
     * id : 1816670
     * name : Beijing
     * cod : 200
     */

    private String base;
    /**
     * temp : 6
     * pressure : 1028
     * humidity : 11
     * temp_min : 6
     * temp_max : 6
     */

    private MainEntity main;
    private int visibility;
    /**
     * speed : 2
     */

    private WindEntity wind;
    /**
     * all : 0
     */

    private CloudsEntity clouds;
    private int dt;
    /**
     * type : 1
     * id : 7405
     * message : 0.011
     * country : CN
     * sunrise : 1457390185
     * sunset : 1457432056
     */

    private SysEntity sys;
    private int id;
    private String name;
    private int cod;
    /**
     * id : 800
     * main : Clear
     * description : 晴
     * icon : 01d
     */

    private List<WeatherEntity> weather;

    public void setCoord(CoordEntity coord) {
        this.coord = coord;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public void setMain(MainEntity main) {
        this.main = main;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public void setWind(WindEntity wind) {
        this.wind = wind;
    }

    public void setClouds(CloudsEntity clouds) {
        this.clouds = clouds;
    }

    public void setDt(int dt) {
        this.dt = dt;
    }

    public void setSys(SysEntity sys) {
        this.sys = sys;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public void setWeather(List<WeatherEntity> weather) {
        this.weather = weather;
    }

    public CoordEntity getCoord() {
        return coord;
    }

    public String getBase() {
        return base;
    }

    public MainEntity getMain() {
        return main;
    }

    public int getVisibility() {
        return visibility;
    }

    public WindEntity getWind() {
        return wind;
    }

    public CloudsEntity getClouds() {
        return clouds;
    }

    public int getDt() {
        return dt;
    }

    public SysEntity getSys() {
        return sys;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCod() {
        return cod;
    }

    public List<WeatherEntity> getWeather() {
        return weather;
    }

    public static class CoordEntity {
        private double lon;
        private double lat;

        public void setLon(double lon) {
            this.lon = lon;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLon() {
            return lon;
        }

        public double getLat() {
            return lat;
        }
    }

    public static class MainEntity {
        private int temp;
        private int pressure;
        private int humidity;
        private int temp_min;
        private int temp_max;

        public void setTemp(int temp) {
            this.temp = temp;
        }

        public void setPressure(int pressure) {
            this.pressure = pressure;
        }

        public void setHumidity(int humidity) {
            this.humidity = humidity;
        }

        public void setTemp_min(int temp_min) {
            this.temp_min = temp_min;
        }

        public void setTemp_max(int temp_max) {
            this.temp_max = temp_max;
        }

        public int getTemp() {
            return temp;
        }

        public int getPressure() {
            return pressure;
        }

        public int getHumidity() {
            return humidity;
        }

        public int getTemp_min() {
            return temp_min;
        }

        public int getTemp_max() {
            return temp_max;
        }
    }

    public static class WindEntity {
        private int speed;

        public void setSpeed(int speed) {
            this.speed = speed;
        }

        public int getSpeed() {
            return speed;
        }
    }

    public static class CloudsEntity {
        private int all;

        public void setAll(int all) {
            this.all = all;
        }

        public int getAll() {
            return all;
        }
    }

    public static class SysEntity {
        private int type;
        private int id;
        private double message;
        private String country;
        private int sunrise;
        private int sunset;

        public void setType(int type) {
            this.type = type;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setMessage(double message) {
            this.message = message;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public void setSunrise(int sunrise) {
            this.sunrise = sunrise;
        }

        public void setSunset(int sunset) {
            this.sunset = sunset;
        }

        public int getType() {
            return type;
        }

        public int getId() {
            return id;
        }

        public double getMessage() {
            return message;
        }

        public String getCountry() {
            return country;
        }

        public int getSunrise() {
            return sunrise;
        }

        public int getSunset() {
            return sunset;
        }
    }

    public static class WeatherEntity {
        private int id;
        private String main;
        private String description;
        private String icon;

        public void setId(int id) {
            this.id = id;
        }

        public void setMain(String main) {
            this.main = main;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public int getId() {
            return id;
        }

        public String getMain() {
            return main;
        }

        public String getDescription() {
            return description;
        }

        public String getIcon() {
            return icon;
        }
    }
}
