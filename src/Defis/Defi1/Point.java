package Defis.Defi1;

public class Point implements java.io.Serializable{
    private double latitude;
    private double longitude;

    public Point(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Point() {
        this.latitude = 0;
        this.longitude = 0;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
