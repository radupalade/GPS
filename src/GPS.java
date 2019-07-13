import java.util.*;

public class GPS {

    private GeoLocationPoint currentLocation;
    private GeoMap geoMap;
    private Map<String, List<City>> pathToEachCity;


    public GPS(GeoMap geoMap) {
        currentLocation = new GeoLocationPoint(0, 0);
        this.geoMap = geoMap;
        pathToEachCity = new HashMap<>();
        for (City each : geoMap.getCities()) {

            pathToEachCity.put(each.getCityName(), new ArrayList<>(Arrays.asList(each)));
        }
    }

    public GeoLocationPoint getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(GeoLocationPoint currentLocation) {
        this.currentLocation = currentLocation;
    }

    public GeoMap getGeoMap() {
        return geoMap;
    }

    public void setGeoMap(GeoMap geoMap) {
        this.geoMap = geoMap;
    }

    public List<City> getDirection(String to) {
        City source = geoMap.getCityAt(currentLocation);

        List<City> visitedCities = new LinkedList<>();
        Map<City, Double> citiesAndDistances = geoMap.initialNeighboursAndDistances(source);
        Queue<City> unvisitedCities = new LinkedList<>();
        unvisitedCities.add(source);

        while (unvisitedCities.isEmpty() == false) {
            City currentCity = unvisitedCities.poll();
            Double distanceToCurrentCity = citiesAndDistances.get(currentCity);

            Map<City, Double> currentCityNeighbours = geoMap.getNeighboursWithDistancesOf(currentCity);

            for (Map.Entry<City, Double> entry : currentCityNeighbours.entrySet()) {
                City neighbourCity = entry.getKey();
                if (visitedCities.contains(neighbourCity)) {
                    continue;
                }
                unvisitedCities.add(neighbourCity);
                Double neighbourDistance = entry.getValue();
                Double oldDistanceToNeighbour = citiesAndDistances.get(neighbourCity);
                if (Double.compare(distanceToCurrentCity + neighbourDistance, oldDistanceToNeighbour) < 0) {

                    citiesAndDistances.put(neighbourCity, distanceToCurrentCity + neighbourDistance);

                    List<City> pathToCurrentCity = new ArrayList<>(
                            pathToEachCity.get(currentCity.getCityName()));

                    pathToCurrentCity.add(neighbourCity);
                    pathToEachCity.put(neighbourCity.getCityName(), pathToCurrentCity);
                }
            }
            visitedCities.add(currentCity);

        }
        System.out.println(citiesAndDistances);
        System.out.println(pathToEachCity);
        return pathToEachCity.get(to);
    }
}



