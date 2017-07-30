package zero.maptest;

import com.baidu.mapapi.model.LatLng;

/**
 * Created by Aiy on 2017/2/22.
 */

public class LatlngDemo {
    private String name;
    private int num;
    private LatLng latLng;
    public LatlngDemo(double longitude,double latitude ,int num){
        latLng=new LatLng(longitude,latitude);
        this.num=num;

    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLongitude() {
        return latLng.longitude;
    }
    public LatLng getLatlng(){
        return latLng;
    }

    public double getLatitude() {
        return latLng.latitude;
    }


    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
