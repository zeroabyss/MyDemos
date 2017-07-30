package zero.maptest;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.inner.Point;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.utils.route.BaiduMapRoutePlan;
import com.baidu.mapapi.utils.route.RouteParaOption;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends Activity {

    private int start_position=0;
    private int end_position=0;
    private int bad_start_position=0;
    private int bad_end_positon=0;

    private EditText bad_start_EditText;
    private EditText bad_end_EditText;
    private Button bad_bt;
    private Button cal;

    private MapView mapView;
    private BaiduMap baiduMap;
    private EditText start;
    private EditText end;
    private Button button;
    private static int INF=Integer.MAX_VALUE;
    private SQLiteDatabase  db;
    private BitmapDescriptor bitmapDescriptor;
    private int[][] matrix;
    private int begin=0;
    private int endl=2;

    private int bad1=0;
    private int bad2=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main2);
        mapView= (MapView) findViewById(R.id.map_view1);
        baiduMap=mapView.getMap();
        baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        baiduMap.setTrafficEnabled(true);

        start= (EditText) findViewById(R.id.startEdit_Text);
        end=(EditText) findViewById(R.id.end_edit_text);
        bad_start_EditText= (EditText) findViewById(R.id.bad_start);
        bad_end_EditText=(EditText)findViewById(R.id.bad_end);

        button= (Button) findViewById(R.id.cal_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_position=1;
                end_position=1;
                Toast.makeText(Main2Activity.this,"请选择起点",Toast.LENGTH_SHORT).show();
                baiduMap.clear();
                getAll();
            }
        });
        matrix=init();
        findViewById(R.id.bad_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bad_start_position=1;
                bad_end_positon=1;
                Toast.makeText(Main2Activity.this,"请选择毁坏路径的起点",Toast.LENGTH_SHORT).show();
                baiduMap.clear();
                getAll();
            }
        });

        findViewById(R.id.cal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Integer> list= new Floyd(13).select(matrix,begin,endl);
                getSort(list);
                matrix=init();
                bad_start_EditText.setText("");
                bad_end_EditText.setText("");
                start.setText("");
                end.setText("");
            }
        });




        LatLng cenpt = new LatLng(18.281339,109.519526);
        //定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(cenpt)
                .zoom(18)
                .build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化


        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        baiduMap.setMapStatus(mMapStatusUpdate);

        bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.icon_marka);
        db=new Sql(this,1).getWritableDatabase();


        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (end_position==1&&start_position==0){
                    endl=Integer.parseInt(marker.getTitle())-1;
                    Log.e("szc",marker.getPosition().longitude+" , "+Double.toString(marker.getPosition().latitude));
                    end.setText(marker.getPosition().longitude+" , " +Double.toString(marker.getPosition().latitude));
                    end_position=0;
                }
                if (start_position==1){
                    begin=Integer.parseInt(marker.getTitle())-1;
                    Log.e("szc",Double.toString(marker.getPosition().latitude));
                    start.setText(marker.getPosition().longitude+" , " +Double.toString(marker.getPosition().latitude));
                    start_position=0;
                    Toast.makeText(getApplicationContext(),"请选择终点",Toast.LENGTH_SHORT).show();
                    Log.e("start",start_position+"");
                    Log.e("start",end_position+"");
                }
                if (bad_end_positon==1&&bad_start_position==0){
                    Log.e("szc",marker.getPosition().longitude+" , "+Double.toString(marker.getPosition().latitude));
                    bad_end_EditText.setText(marker.getPosition().longitude+" , " +Double.toString(marker.getPosition().latitude));
                    bad_end_positon=0;
                    bad2=Integer.parseInt(marker.getTitle())-1;
                    if (matrix[bad1][bad2]!=INF){
                        Toast.makeText(getApplicationContext(),"路段正确",Toast.LENGTH_SHORT).show();
                        matrix[bad1][bad2]=INF;
                        matrix[bad2][bad1]=INF;
                    }else{
                        Toast.makeText(getApplicationContext(),"不存在路段",Toast.LENGTH_SHORT).show();
                        bad_end_EditText.setText("");
                        bad_start_EditText.setText("");
                    }
                }
                if (bad_start_position==1){

                    Log.e("szc",Double.toString(marker.getPosition().latitude));
                    bad_start_EditText.setText(marker.getPosition().longitude+" , " +Double.toString(marker.getPosition().latitude));
                    bad_start_position=0;
                    bad1=Integer.parseInt(marker.getTitle())-1;
                    Toast.makeText(getApplicationContext(),"请选择毁坏路段终点",Toast.LENGTH_SHORT).show();
                }


                return false;
            }
        });


    }

    private void getAll(){
        Cursor cursor=db.query("marker",
                null,
                null,
                null,
                null,
                null,
                null);
        if(cursor.moveToFirst()){
            do {
                double la=cursor.getDouble(cursor.getColumnIndex("latitude"));
                double lon=cursor.getDouble(cursor.getColumnIndex("longtitude"));
                int num=cursor.getInt(cursor.getColumnIndex("name"));
                LatLng point=new LatLng(lon,la);
                OverlayOptions options = new MarkerOptions()//设置一个覆盖标注点 拥有位置和图标
                        .position(point)
                        .icon(bitmapDescriptor)
                        .zIndex(5)
                        .title(Integer.toString(num))
                        .draggable(true);
                baiduMap.addOverlay(options);
            }while(cursor.moveToNext());
        }
        cursor.close();
    }

    private void getSort(List<Integer> list) {
        ArrayList<Marker> markers = new ArrayList<Marker>();
        BitmapDescriptor custom1 = BitmapDescriptorFactory.fromResource(R.drawable.icon_road_blue_arrow);
        List<BitmapDescriptor> customList = new ArrayList<BitmapDescriptor>();
        customList.add(custom1);

        List<LatLng> points = new ArrayList<LatLng>();
        List<Integer> index = new ArrayList<Integer>();
        if (list.isEmpty()){
            Toast.makeText(getApplicationContext(),"没有寻找到路径",Toast.LENGTH_SHORT).show();
            return;
        }
        for (int num : list) {
            Cursor cursor = db.query("marker",
                    null,
                    "name=?",
                    new String[]{Integer.toString(num + 1)},
                    null,
                    null,
                    null);
            if (cursor.moveToFirst()) {
                int num1 = cursor.getInt(cursor.getColumnIndex("name"));
                double la = cursor.getDouble(cursor.getColumnIndex("latitude"));
                double lon = cursor.getDouble(cursor.getColumnIndex("longtitude"));
                Log.e("abyss" + num, num1 + " " + la + " " + lon);
                LatLng point = new LatLng(lon, la);
                points.add(point);
                index.add(0);
                OverlayOptions options = new MarkerOptions()//设置一个覆盖标注点 拥有位置和图标
                        .position(point)
                        .icon(bitmapDescriptor)
                        .zIndex(5)
                        .draggable(true);
                markers.add((Marker) baiduMap.addOverlay(options));
            }
            cursor.close();
        }

        //构造对象
        OverlayOptions ooPolyline = new PolylineOptions().width(15).color(0xAAFF0000).points(points).customTextureList(customList).textureIndex(index);
//添加到地图
        baiduMap.addOverlay(ooPolyline);
    }

    private int[][] init(){
        int[][] matrix={
                {INF,400,400,INF,INF, INF,INF,INF,INF,INF, INF,INF,INF},
                {400,INF,INF,INF,INF, INF,250,INF,INF,170, INF,INF,INF},
                {400,INF,INF,INF,INF, INF,INF,160,INF,INF, INF,INF,INF},
                {INF,INF,INF,INF,INF, 265,INF,240,INF,260, INF,180,INF},
                {INF,INF,INF,INF,INF, INF,376,INF,INF,INF, INF,INF,INF},

                {INF,INF,INF,265,INF, INF,INF,INF,INF,INF, 220,INF,200},
                {INF,250,INF,INF,376, INF,INF,INF,INF,INF, 150,INF,INF},
                {INF,INF,160,240,INF, INF,INF,INF,240,INF, INF,INF,INF},
                {INF,INF,INF,INF,INF, INF,INF,240,INF,215, INF,INF,INF},
                {INF,170,INF,260,INF, INF,INF,INF,215,INF, 255,INF,INF},

                {INF,INF,INF,INF,INF, 220,150,INF,INF,255, INF,INF,INF},
                {INF,INF,INF,180,INF, INF,INF,INF,INF,INF, INF,INF,270},
                {INF,INF,INF,INF,INF, 200,INF,INF,INF,INF, INF,270,INF},
        };
        return matrix;
    }



}
