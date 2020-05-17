package com.example.food;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.nav_view)
    BottomNavigationView navView;

    private Fragment[] fragments;
    private int lastfragment = 0;

    String TAG ="sssssss"  ;


    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        fragments=new Fragment[]{new HomeFragment(),new FavFragment(),new MineFragment()};
        getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragments[0]).show(fragments[0]).commit();

        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_home:
                        //这里因为需要对3个fragment进行切换
                        //start
                        if (lastfragment != 0) {
                            switchFragment(lastfragment, 0);
                            lastfragment = 0;
                        }
                        //end
                        //如果只是想测试按钮点击，不管fragment的切换，可以把start到end里面的内容去掉
                        return true;
                    case R.id.navigation_fav:
                        if (lastfragment != 1) {
                            switchFragment(lastfragment, 1);
                            lastfragment = 1;
                        }
                        return true;
                    case R.id.navigation_mine:
                        if (lastfragment != 2) {
                            switchFragment(lastfragment, 2);
                            lastfragment = 2;
                        }
                        return true;
                    default:
                        break;
                }
                return false;
            }
        });

        initLocationListener();

    }

    private void initLocationListener() {
//初始化定位
        AMapLocationClient mLocationClient = new AMapLocationClient(getApplicationContext());
        AMapLocationClientOption mLocationOption=new AMapLocationClientOption();
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        //设置单次定位
        mLocationOption.setOnceLocation(true);
        //获取3s内最精确的一次定位结果
        mLocationOption.setOnceLocationLatest(true);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //声明定位回调监听器

        mLocationClient.setLocationOption(mLocationOption);
        AMapLocationListener mLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                Log.i(TAG, "aMapLocation:" + aMapLocation.getAddress());
                //获取纬度
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
//可在其中解析amapLocation获取相应内容。
                        Log.i(TAG, "aMapLocation:" + aMapLocation.getPoiName());
                        search("050000",aMapLocation.getCityCode(),aMapLocation);
                    } else {
                        //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                        Log.i(TAG, "location Error, ErrCode:"
                                + aMapLocation.getErrorCode() + ", errInfo:"
                                + aMapLocation.getErrorInfo());
                    }
                }
            }
        };
//设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
//启动定位
        mLocationClient.startLocation();
    }



    private void switchFragment(int lastfragment, int index) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //隐藏上个Fragment
        transaction.hide(fragments[lastfragment]);
        if (fragments[index].isAdded() == false) {
            transaction.add(R.id.frame, fragments[index]);

        }
        transaction.show(fragments[index]).commitAllowingStateLoss();
    }

    public void search(String keyWord,String cityCode,AMapLocation aMapLocation){
        PoiSearch.Query query = new PoiSearch.Query("", keyWord, cityCode);
//keyWord表示搜索字符串，
//第二个参数表示POI搜索类型，二者选填其一，选用POI搜索类型时建议填写类型代码，码表可以参考下方（而非文字）
//cityCode表示POI搜索区域，可以是城市编码也可以是城市名称，也可以传空字符串，空字符串代表全国在全国范围内进行搜索
        query.setPageSize(10);// 设置每页最多返回多少条poiitem
        query.setPageNum(1);//设置查询页码
        PoiSearch poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
            @Override
            public void onPoiSearched(PoiResult poiResult, int i) {
                Log.i(TAG, "getLatitude:"+aMapLocation.getCityCode()+aMapLocation.getLatitude()+"--"+aMapLocation.getLongitude());
                for (int j = 0; j <poiResult.getPois().size() ; j++) {
                    PoiItem poiItem = poiResult.getPois().get(j);
                    Log.i(TAG, "OnPoiSearchListener:"
                            +"1"+ poiItem.getTitle()
                            +poiItem.getProvinceName()+poiItem.getCityName()+ poiItem.getAdName()+poiItem.getSnippet()
                            +poiItem.getTypeDes()
                    );
                }
            }

            @Override
            public void onPoiItemSearched(PoiItem poiItem, int i) {

            }
        });
//   0851     26.559846--106.725175
        poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(aMapLocation.getLatitude(),
                aMapLocation.getLongitude()), 1000));//设置周边搜索的中心点以及半径

        poiSearch.searchPOIAsyn();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!compositeDisposable.isDisposed())
            compositeDisposable.dispose();
    }

}
