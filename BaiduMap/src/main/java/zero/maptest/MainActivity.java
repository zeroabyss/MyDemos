package zero.maptest;

import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.LogoPosition;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.route.BaiduMapRoutePlan;
import com.baidu.mapapi.utils.route.RouteParaOption;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private Overlay myOverlay;
    private MapView mapView;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());//初始化地图
        setContentView(R.layout.activity_main);
        mapView = (MapView) findViewById(R.id.map_view);
        final BaiduMap baiduMap = mapView.getMap();//设置地图
        baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);//设置为普通地图
        baiduMap.setTrafficEnabled(true);//交通图
        // baiduMap.setBaiduHeatMapEnabled(true);热力图
        mapView.setLogoPosition(LogoPosition.logoPostionRightTop);//设置logo位置

        LatLng point = new LatLng(39.963175, 116.400244);//设置一个点
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.icon_marka);
        //标记图片
        OverlayOptions options = new MarkerOptions()//设置一个覆盖标注点 拥有位置和图标
                .position(point)
                .icon(bitmapDescriptor)
                .zIndex(20)
                .draggable(true);

        final Marker myOverlay = (Marker) baiduMap.addOverlay(options);//添加到地图

        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Button bt=new Button(getApplicationContext());
                TextView textView=new TextView(getApplicationContext());
                textView.setText("zeroabyss");
                InfoWindow infoWindow=new InfoWindow(BitmapDescriptorFactory.fromView(textView), marker.getPosition(), -47, new InfoWindow.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick() {

                    }
                });
                baiduMap.showInfoWindow(infoWindow);

                return true;
            }
        });
        baiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                baiduMap.hideInfoWindow();
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });

        baiduMap.setOnMarkerDragListener(new BaiduMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {

            }

            @Override
            public void onMarkerDragStart(Marker marker) {

            }
        });


      /*  //画图
        LatLng pt1 = new LatLng(39.93923, 116.357428);
        LatLng pt2 = new LatLng(39.91923, 116.327428);
        LatLng pt3 = new LatLng(39.89923, 116.347428);
        LatLng pt4 = new LatLng(39.89923, 116.367428);
        LatLng pt5 = new LatLng(39.91923, 116.387428);
        List<LatLng> pts=new ArrayList<LatLng>();
        pts.add(pt1);
        pts.add(pt2);
        pts.add(pt3);
        pts.add(pt4);
        pts.add(pt5);
        OverlayOptions overlayOptions=new PolygonOptions()//多边形
                .points(pts)
                .stroke(new Stroke(5,0xAA00FF00))
                .fillColor( 0xAA00FF00);
        baiduMap.addOverlay(overlayOptions);
*/
        //构造纹理资源
        BitmapDescriptor custom1 = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_road_red_arrow);
        BitmapDescriptor custom2 = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_road_green_arrow);
        BitmapDescriptor custom3 = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_road_blue_arrow);
// 定义点
        LatLng pt1 = new LatLng(39.93923, 116.357428);
        LatLng pt2 = new LatLng(39.91923, 116.327428);
        LatLng pt3 = new LatLng(39.89923, 116.347428);
        LatLng pt4 = new LatLng(39.89923, 116.367428);
        LatLng pt5 = new LatLng(39.91923, 116.387428);

/*//构造纹理队列
        List<BitmapDescriptor>customList = new ArrayList<BitmapDescriptor>();
        customList.add(custom1);
        customList.add(custom2);
        customList.add(custom3);

        List<LatLng> points = new ArrayList<LatLng>();
        List<Integer> index = new ArrayList<Integer>();
        points.add(pt1);//点元素
        index.add(0);//设置该点的纹理索引
        points.add(pt2);//点元素
        index.add(0);//设置该点的纹理索引
        points.add(pt3);//点元素
        index.add(1);//设置该点的纹理索引
        points.add(pt4);//点元素
        index.add(2);//设置该点的纹理索引
        points.add(pt5);//点元素
//构造对象
        OverlayOptions ooPolyline = new PolylineOptions().width(15).color(0xAAFF0000).points(points).customTextureList(customList).textureIndex(index);
//添加到地图
        baiduMap.addOverlay(ooPolyline);*/

//        List<LatLng> points = new ArrayList<LatLng>();
//        points.add(new LatLng(39.93923, 116.357428));
//        points.add(new LatLng(39.91923, 116.327428));
//        points.add(new LatLng(39.89923, 116.347428));
//        points.add(new LatLng(39.89923, 116.367428));
//        points.add(new LatLng(39.91923, 116.387428));
//
//        List<Integer> colors = new ArrayList<>();
//        colors.add(Integer.valueOf(Color.BLUE));
//        colors.add(Integer.valueOf(Color.RED));
//        colors.add(Integer.valueOf(Color.YELLOW));
//        colors.add(Integer.valueOf(Color.GREEN));
//
//        OverlayOptions line = new PolylineOptions()
//                .width(10)
//                .colorsValues(colors)
//                .points(points);
//        baiduMap.addOverlay(line);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }




}
