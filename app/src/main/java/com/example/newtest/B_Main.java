package com.example.newtest;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatToggleButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.newtest.adapter.B_CommentAdapter;
import com.example.newtest.adapter.B_InfoAdapter;
import com.example.newtest.adapter.B_StatusAdapter;
import com.example.newtest.api.Charger;
import com.example.newtest.api.ChargerList;
import com.example.newtest.common.AskPermissions;
import com.example.newtest.common.ChargerType;
import com.example.newtest.common.Server;
import com.example.newtest.common.Shared;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.algo.NonHierarchicalDistanceBasedAlgorithm;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.LoginButton;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.exception.KakaoException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import retrofit2.Response;

import static com.example.newtest.common.MarkerByCharger.createBitmapFromMarkerView;
import static com.example.newtest.common.MarkerByCharger.getMarkerViewByManage;

public class B_Main extends AskPermissions implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {

    private final String TAG = getClass().getName();

    private final int REQUEST_ACTIVITY_GOOGLE_LOCATION = 1000;
    private final int REQUEST_ACTIVITY_SEARCH = 2000;
    private final int REQUEST_ACTIVITY_FILTER = 3000;
    private final int REQUEST_ACTIVITY_COMMENT = 4000;
    private final int REQUEST_ACTIVITY_NAV_BOOKMARK = 5000;
    private final int REQUEST_ACTIVITY_NAV_COMMENT = 6000;

    //            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
//            .setInterval(30 * 1000)
//            .setFastestInterval(5 * 1000);
    private LocationRequest mLocationRequest = new LocationRequest()
            .setNumUpdates(1)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    private GoogleApiClient mGoogleApiClient;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;
    private ISessionCallback mKakaoSessionCallback;

    private final float ADJUST_ZOOM = 11f; // 12.5f
    private boolean exit = false;
    private boolean progress;
    private boolean isFirstConnectAction = false;
    private boolean isFirstLoginAction = true;
    private boolean isHiddenByNoTouch = true;
    private float currentZoom;
    private GoogleMap map;
    private ClusterManager<Charger.Info> cluster;
    private BottomSheet bottomSheet;
    private BottomSheetBehavior sheet;
    private ArrayList<Charger.Info> items;
    private Marker saveMarker;
    private ValueAnimator saveAnimator;
    private ClusterRenderListener listener = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.b_main_nav);
        initView();
        initSheet();
        initLocation();
        initKakaoLogin();

        // loaded station data
        items = Shared.getInfoList(B_Main.this);

        // init GoogleMap
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.b_fragment);
        mapFragment.getMapAsync(B_Main.this);
    }

    private void initView() {
        // Toolbar
        Toolbar toolbar = findViewById(R.id.b_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("충전소 검색");

        // DrawerLayout
        DrawerLayout drawer = findViewById(R.id.b_drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // NavigationView in DrawerLayout
        NavigationView navigationView = findViewById(R.id.b_navigation);
        navigationView.setNavigationItemSelectedListener(this);
        View headerLayout = navigationView.getHeaderView(0);
        headerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kakaoLogin();
            }
        });

        // FloatingActionButton for Location
        FloatingActionButton fab = findViewById(R.id.b_fab_location);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(B_Main.this, PERMISSIONS_LOCATION, PERMISSIONS_RESULT_LOCATION);
            }
        });
    }

    private void initSheet() {
        // BottomSheet
        View front = findViewById(R.id.b_sheet_front);
        front.setVisibility(View.VISIBLE);
        front.setAlpha(1);

        View back = findViewById(R.id.b_sheet_back);
        back.setVisibility(View.GONE);
        back.setAlpha(0);

        View view = findViewById(R.id.b_sheet);
        sheet = BottomSheetBehavior.from(view);
        sheet.setState(BottomSheetBehavior.STATE_HIDDEN);
        sheet.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            View appBarLayout = findViewById(R.id.b_appBarLayout);
            View front = findViewById(R.id.b_sheet_front);
            View back = findViewById(R.id.b_sheet_back);

            @Override
            public void onStateChanged(@NonNull View view, int i) {
                if (i == BottomSheetBehavior.STATE_EXPANDED)
                    bottomSheet.initComment();

                if (i == BottomSheetBehavior.STATE_COLLAPSED) {
                    isHiddenByNoTouch = true;
                    // back sheet, scroll 맨 위로 초기화
                    NestedScrollView nestedScrollView = findViewById(R.id.b_sheet_back_nestedScrollView);
                    nestedScrollView.smoothScrollTo(0, 0);
                }

                if (i == BottomSheetBehavior.STATE_HIDDEN) {
                    reverseAnimator();
                    if (isHiddenByNoTouch)
                        addMarker(); // STATE_COLLAPSED 였을때, 'sheet'가 맵을 가리고 있던 부분에 marker 추가
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {
                float padding;
                if (v > 0) { // PeekHeight 위
                    padding = sheet.getPeekHeight() + (view.getHeight() - sheet.getPeekHeight()) * v;
                    front.animate().alpha(1 - v).setDuration(0).start();
                    back.setVisibility(View.VISIBLE);
                    back.animate().alpha(v).setDuration(0).start();
                } else { // 아래
                    padding = sheet.getPeekHeight() * (1 - Math.abs(v)); // abs: 절대값
                    back.setVisibility(View.GONE);
                }
                int maxPaddingBottom = (view.getHeight() - appBarLayout.getHeight()) / 2;
                map.setPadding(0, dpToPx(85), 0, Math.min(maxPaddingBottom, (int) padding));
            }
        });
    }

    private void initKakaoLogin() {
        mKakaoSessionCallback = new ISessionCallback() {
            @Override
            public void onSessionOpened() {
                List<String> keys = new ArrayList<>();
                keys.add("kakao_account.profile");

                UserManagement.getInstance().me(keys, new MeV2ResponseCallback() {
                    @Override
                    public void onFailure(ErrorResult errorResult) {
                        Log.e(TAG, errorResult.getErrorMessage());
                    }

                    @Override
                    public void onSessionClosed(ErrorResult errorResult) {
                        Log.e(TAG, errorResult.getErrorMessage());
                    }

                    @Override
                    public void onSuccess(MeV2Response response) {
                        Log.e(TAG, "onSuccess1");
                        String id = String.valueOf(response.getId());
                        String nick = response.getKakaoAccount().getProfile().getNickname();
                        String img;
                        if (response.getKakaoAccount().getProfile().getThumbnailImageUrl() != null)
                            img = response.getKakaoAccount().getProfile().getThumbnailImageUrl();
                        else
                            img = "";

                        setLoginState(nick, img);
                        if (isFirstLoginAction) {
                            HashMap<String, String> map = new HashMap<>();
                            map.put("id", id);
                            map.put("nick", nick);
                            map.put("img", img);
                            Shared.setAccount(getApplicationContext(), map);
                            new Server(B_Main.this).saveUser();
                            new Server(B_Main.this).loadBookmark(new Server.OnPostTaskListener() {
                                @Override
                                public void onPostTask() {
                                    checkBookmark();
                                }
                            });
                            isFirstLoginAction = false;
                        }
                    }
                });
            }

            @Override
            public void onSessionOpenFailed(KakaoException exception) {
                Log.e(TAG, "onSessionOpenFailed: " + exception.toString());
            }
        };
        Session.getCurrentSession().addCallback(mKakaoSessionCallback);
    }

    private void initLocation() {
        // check GPS on, off
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
                        SettingsClient client = LocationServices.getSettingsClient(B_Main.this);
                        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
                        // GPS on
                        task.addOnSuccessListener(B_Main.this, new OnSuccessListener<LocationSettingsResponse>() {
                            @Override
                            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                                map.setMyLocationEnabled(true);
                                map.getUiSettings().setMyLocationButtonEnabled(true);

                                MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.b_fragment);

                                if (mapFragment.getView() != null) {
                                    View locationButton = ((View) mapFragment.getView().findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
                                    locationButton.setVisibility(View.GONE);
                                }

                                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
                            }
                        });
                        // GPS off
                        task.addOnFailureListener(B_Main.this, new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                if (isFirstConnectAction) {
                                    LatLng SEOUL = new LatLng(37.576270, 126.976882);
                                    CameraPosition position = new CameraPosition.Builder()
                                            .target(SEOUL)
                                            .zoom(15)
                                            .build();
                                    map.moveCamera(CameraUpdateFactory.newCameraPosition(position));
                                    isFirstConnectAction = false;
                                    return;
                                }

                                if (e instanceof ResolvableApiException) {
                                    try {
                                        ResolvableApiException resolvable = (ResolvableApiException) e;
                                        resolvable.startResolutionForResult(B_Main.this, REQUEST_ACTIVITY_GOOGLE_LOCATION);
                                    } catch (IntentSender.SendIntentException sendEx) {
                                        Log.e(TAG, Objects.requireNonNull(sendEx.getMessage()));
                                    }
                                }
                            }
                        });
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        if (i == CAUSE_NETWORK_LOST)
                            Log.e(TAG, "onConnectionSuspended: Google Play services connection lost. Cause: network lost.");
                        if (i == CAUSE_SERVICE_DISCONNECTED)
                            Log.e(TAG, "onConnectionSuspended: Google Play services connection lost. Cause: service disconnected");
                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Log.e(TAG, "onConnectionFailed: " + connectionResult.getErrorMessage());
                    }
                })
                .build();

        // update location
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    CameraPosition position = new CameraPosition.Builder()
                            .target(latLng)
                            .zoom(15)
                            .build();
                    if (isFirstConnectAction) {
                        map.moveCamera(CameraUpdateFactory.newCameraPosition(position));
                        isFirstConnectAction = false;
                        return;
                    }
                    map.animateCamera(CameraUpdateFactory.newCameraPosition(position));
                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!Session.getCurrentSession().checkAndImplicitOpen())
            setLogoutState();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(mKakaoSessionCallback);
    }

    @Override
    public void onMapReady(final GoogleMap map) {
        this.map = map;
        // 퍼미션 허용 & GPS 켜져있으면 지도에 현재 위치
        // 퍼미션 허용 & GPS 꺼져있으면 지도에 광화문 위치
        if (checkSelfPermissionLocation()) {
            isFirstConnectAction = true;
            connectGoogleApiClient();
        } else {
            // 퍼미션 허용되지 않았으면 지도에 광화문 위치
            LatLng SEOUL = new LatLng(37.576270, 126.976882);
            CameraPosition position = new CameraPosition.Builder()
                    .target(SEOUL)
                    .zoom(15)
                    .build();
            map.moveCamera(CameraUpdateFactory.newCameraPosition(position));
        }

        // 지도 UI 설정
        map.getUiSettings().setMapToolbarEnabled(false);
        map.getUiSettings().setZoomControlsEnabled(false);

        // 지도 축소 제한
        map.setMinZoomPreference(6); // 1: World, 5: Continent, 10: City, 15: Streets, 20: Buildings

        // 지도에 보여지는 지역을 우리나라로 제한
        LatLng latLng1 = new LatLng(33.19027, 126.1236); // left bottom
        LatLng latLng2 = new LatLng(38.62253, 131.8728); // right top
        LatLngBounds KOREA = new LatLngBounds(latLng1, latLng2);
        map.setLatLngBoundsForCameraTarget(KOREA);

        cluster = getClusterManager();
        map.setOnMarkerClickListener(cluster);
        map.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int i) {
                // REASON_API_ANIMATION: zoom control 버튼 조작
                if (i == REASON_DEVELOPER_ANIMATION) // animateCamera or moveCamera
                    reverseAnimator();

                if (i == REASON_GESTURE) {
                    isHiddenByNoTouch = false;
                    sheet.setState(BottomSheetBehavior.STATE_HIDDEN);
                }
            }
        });
        map.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                addMarker();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // REQUEST_ACTIVITY_GOOGLE_LOCATION: ask GPS turn on (GoogleClientAPI)
        if (requestCode == REQUEST_ACTIVITY_GOOGLE_LOCATION) {
            if (resultCode == RESULT_OK) {
                connectGoogleApiClient();
            }
        }

        if (requestCode == REQUEST_ACTIVITY_NAV_BOOKMARK || requestCode == REQUEST_ACTIVITY_NAV_COMMENT) {
            if (Session.getCurrentSession().checkAndImplicitOpen())
                new Server(B_Main.this).loadBookmark(new Server.OnPostTaskListener() {
                    @Override
                    public void onPostTask() {
                        checkBookmark();
                    }
                });
        }

        if (requestCode == REQUEST_ACTIVITY_SEARCH || requestCode == REQUEST_ACTIVITY_NAV_BOOKMARK || requestCode == REQUEST_ACTIVITY_NAV_COMMENT) {
            if (resultCode == RESULT_OK && data != null && data.getExtras() != null) {
                String sid = data.getExtras().getString("sid", "");
                ArrayList<Charger.Info> infoList = Shared.getInfoList(B_Main.this);

                Charger.Info item = null;
                for (Charger.Info info : infoList) {
                    if (sid.equals(info.getSid())) {
                        item = info;
                        break;
                    }
                }
                if (item == null)
                    return;

                final Charger.Info clusterItem = item;
                setClusterRenderListener(new ClusterRenderListener() {
                    @Override
                    public void onClusterRendered() {
                        Marker marker = null;
                        for (Marker a : cluster.getMarkerCollection().getMarkers()) {
                            if (clusterItem.getSid().equals(a.getTag())) {
                                marker = a;
                                break;
                            }
                        }
                        if (marker == null)
                            return;

                        ValueAnimator animator = getAnimator(clusterItem, marker);
                        animator.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationStart(Animator animation, boolean isReverse) {
                                if (isReverse)
                                    return;
                                if (sheet.getState() != BottomSheetBehavior.STATE_COLLAPSED)
                                    sheet.setState(BottomSheetBehavior.STATE_COLLAPSED);
                                bottomSheet = new BottomSheet();
                                bottomSheet.initBottomSheet(clusterItem);
                            }
                        });
                        animator.start();
                        removeClusterRenderListener();
                        saveMarker = marker;
                        saveAnimator = animator;
                    }
                });

                CameraPosition position = new CameraPosition.Builder()
                        .target(clusterItem.getPosition())
                        .zoom(15)
                        .build();
                map.setPadding(0, dpToPx(85), 0, sheet.getPeekHeight());
                map.moveCamera(CameraUpdateFactory.newCameraPosition(position));
            }
        }

        if (requestCode == REQUEST_ACTIVITY_FILTER) {
            if (resultCode == RESULT_OK) { // Filter change
                sheet.setState(BottomSheetBehavior.STATE_HIDDEN);
                new Server(B_Main.this).loadStation(new Server.OnTaskListener() {
                    @Override
                    public void onPreTask() {
                        showProgressBar();
                    }

                    @Override
                    public void onPostTask() {
                        items = Shared.getInfoList(B_Main.this);
                        addMarker();
                    }
                });
            }
        }

        if (requestCode == REQUEST_ACTIVITY_COMMENT) {
            if (resultCode == RESULT_OK) { // posting success
                bottomSheet.initComment();
                Toast.makeText(B_Main.this, "댓글 등록 완료", Toast.LENGTH_SHORT).show();
            }
        }

        Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void allowedPermissionLocation() {
        super.allowedPermissionLocation();
        connectGoogleApiClient();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                Intent intent = new Intent(this, Bb_Search.class);
                startActivityForResult(intent, REQUEST_ACTIVITY_SEARCH);
                return true;
            case R.id.action_filter:
                Bundle bundle = new Bundle();
                bundle.putString("typeCheck", new Gson().toJson(Shared.getTypeCheck(getApplicationContext())));
                bundle.putString("parkCheck", new Gson().toJson(Shared.getParkingCheck(getApplicationContext())));
                bundle.putString("companyCheck", new Gson().toJson(Shared.getCompanyCheck(getApplicationContext())));

                intent = new Intent(this, C_Filter.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, REQUEST_ACTIVITY_FILTER);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setCheckable(false);
        Intent intent;
        switch (item.getItemId()) {
            case R.id.nav_bookmark:
                if (Session.getCurrentSession().checkAndImplicitOpen()) {
                    intent = new Intent(B_Main.this, Nav_Bookmark.class);
                    startActivityForResult(intent, REQUEST_ACTIVITY_NAV_BOOKMARK);
                    break;
                }
                requestLogin();
                break;
            case R.id.nav_comment:
                if (Session.getCurrentSession().checkAndImplicitOpen()) {
                    intent = new Intent(B_Main.this, Nav_Comment.class);
                    startActivityForResult(intent, REQUEST_ACTIVITY_NAV_COMMENT);
                    break;
                }
                requestLogin();
                break;
            case R.id.nav_notice:
                intent = new Intent(B_Main.this, Nav_Notice.class);
                startActivity(intent);
                break;
            case R.id.nav_setting:
                intent = new Intent(B_Main.this, Nav_Setting.class);
                startActivity(intent);
                break;
        }

        DrawerLayout drawer = findViewById(R.id.b_drawerLayout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.b_drawerLayout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }

        if (sheet.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            sheet.setState(BottomSheetBehavior.STATE_COLLAPSED);
            return;
        }

        if (sheet.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
            sheet.setState(BottomSheetBehavior.STATE_HIDDEN);
            return;
        }

        if (exit) {
            finish();
        } else {
            String msg = getString(R.string.string_alertBackBtn);
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3000);
        }
    }

    /**
     * BottomSheet 열려있는 상태에서 로그인하거나 로그아웃 할 때, Bookmark 상태 최신화
     */
    private void checkBookmark() {
        if (sheet.getState() == BottomSheetBehavior.STATE_EXPANDED
                || sheet.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
            bottomSheet.checkBookmark();
        }
    }

    private ClusterManager<Charger.Info> getClusterManager() {
        final ClusterManager<Charger.Info> cluster = new ClusterManager<>(getApplicationContext(), map);
        cluster.setAlgorithm(new NonHierarchicalDistanceBasedAlgorithm<Charger.Info>());
        cluster.setRenderer(new DefaultClusterRenderer<Charger.Info>(getApplicationContext(), map, cluster) {
            @Override
            protected boolean shouldRenderAsCluster(Cluster<Charger.Info> cluster) {
                return currentZoom < ADJUST_ZOOM && 9 < cluster.getSize();
            }

            @Override
            protected void onBeforeClusterItemRendered(Charger.Info item, MarkerOptions markerOptions) {
                View view = getMarkerViewByManage(getApplicationContext(), item);
                Bitmap bitmap = createBitmapFromMarkerView(B_Main.this, view);
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
            }

            @Override
            protected void onClusterItemRendered(Charger.Info clusterItem, Marker marker) {
                marker.setTag(clusterItem.getSid());
            }

            @Override
            public void onMarkerMarking(final boolean running) {
                super.onMarkerMarking(running);
                Log.d(TAG, "ClusterRendering: " + running);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (running) {
                            progress = true;
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (progress) {
                                        showProgressBar();
                                    }
                                }
                            }, 800);
                            return;
                        }
                        progress = false;
                        closeProgressBar();
                        if (listener != null)
                            listener.onClusterRendered();
                    }
                });
            }
        });
        cluster.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<Charger.Info>() {
            @Override
            public boolean onClusterItemClick(final Charger.Info item, final Marker marker) {
                // marker 눌러진 상태에서 같은 marker 또 누를 경우, 아무일 일어나지 않음
                if (saveMarker != null && saveMarker.getId().equals(marker.getId()))
                    return true;

                ValueAnimator animator = getAnimator(item, marker);
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation, boolean isReverse) {
                        if (isReverse)
                            return;
                        if (sheet.getState() != BottomSheetBehavior.STATE_COLLAPSED) {
                            sheet.setState(BottomSheetBehavior.STATE_COLLAPSED);
                            map.setPadding(0, dpToPx(85), 0, sheet.getPeekHeight());
                        }
                        map.animateCamera(CameraUpdateFactory.newLatLng(item.getPosition()), 500, null);
                        bottomSheet = new BottomSheet();
                        bottomSheet.initBottomSheet(item);
                    }
                });
                animator.start();
                saveMarker = marker;
                saveAnimator = animator;
                return true;
            }
        });
        return cluster;
    }

    private void addMarker() {
        if (items == null)
            return;

        cluster.clearItems();
        currentZoom = map.getCameraPosition().zoom;
        if (ADJUST_ZOOM < currentZoom) { // 정해진 줌보다 확대
            LatLngBounds bounds = map.getProjection().getVisibleRegion().latLngBounds;
            ArrayList<Charger.Info> filteredItems = new ArrayList<>();
            for (Charger.Info item : items) {
                if (bounds.contains(item.getPosition())) {
                    filteredItems.add(item);
                    cluster.addItem(item);
                }
            }
            cluster.cluster();
            // 보여지는 지도에 표시할 item 없으면
            // onMarkerMarking() 실행되지 않기 때문에
            // closeProgressBar() 실행되지 않음 ∴ 임의로 추가함
            if (filteredItems.size() == 0)
                closeProgressBar();
            return;
        }

        for (Charger.Info item : items)
            cluster.addItem(item);
        cluster.cluster();

        if (items.size() == 0)
            closeProgressBar();
    }

    private class BottomSheet {

        Charger.Info item;
        AppCompatToggleButton tb_front_bookmark;
        AppCompatToggleButton tb_back_bookmark;

        private void initBottomSheet(Charger.Info item) {
            this.item = item;
            initFrontSheet();
            initBackSheet();
            checkBookmark();
        }

        private void initFrontSheet() {
            TextView tv_snm = findViewById(R.id.b_sheet_front_tv_snm);
            tv_snm.setText(item.getSnm());

            TextView tv_park = findViewById(R.id.b_sheet_front_tv_park);
            tv_park.setText(item.getPark());
            tv_snm.setSelected(true);

            TextView tv_time = findViewById(R.id.b_sheet_tv_utime);
            tv_time.setText(item.getUtime());
            tv_time.setSelected(true);

            ImageView iv_ac3 = findViewById(R.id.b_sheet_front_iv_ac3);
            ImageView iv_cha = findViewById(R.id.b_sheet_front_iv_cha);
            ImageView iv_com = findViewById(R.id.b_sheet_front_iv_combo);
            ImageView iv_single = findViewById(R.id.b_sheet_front_iv_single);
            ImageView iv_super = findViewById(R.id.b_sheet_front_iv_super);
            ImageView iv_h2 = findViewById(R.id.b_sheet_front_iv_h2);

            ArrayList<ImageView> imgList = new ArrayList<>();
            imgList.add(iv_ac3);
            imgList.add(iv_cha);
            imgList.add(iv_com);
            imgList.add(iv_single);
            imgList.add(iv_super);
            imgList.add(iv_h2);

            TextView tv_ac3 = findViewById(R.id.b_sheet_front_tv_ac3);
            TextView tv_cha = findViewById(R.id.b_sheet_front_tv_cha);
            TextView tv_com = findViewById(R.id.b_sheet_front_tv_combo);
            TextView tv_single = findViewById(R.id.b_sheet_front_tv_single);
            TextView tv_super = findViewById(R.id.b_sheet_front_tv_super);
            TextView tv_h2 = findViewById(R.id.b_sheet_front_tv_h2);

            ArrayList<TextView> tvList = new ArrayList<>();
            tvList.add(tv_ac3);
            tvList.add(tv_cha);
            tvList.add(tv_com);
            tvList.add(tv_single);
            tvList.add(tv_super);
            tvList.add(tv_h2);

            new ChargerType(B_Main.this)
                    .setCtp(item.getCtp())
                    .setIvList(imgList)
                    .setTvList(tvList)
                    .initView();

            AppCompatButton acb_path = findViewById(R.id.b_sheet_front_acb_path);
            tb_front_bookmark = findViewById(R.id.b_sheet_front_acb_bookmark);
            AppCompatButton acb_share = findViewById(R.id.b_sheet_front_acb_share);

            acb_path.setOnClickListener(getPathOnClickListener());
            tb_front_bookmark.setOnClickListener(getBookmarkOnClickListener());
            acb_share.setOnClickListener(getShareOnClickListener());
        }

        private void initBackSheet() {
            Toolbar toolbar = findViewById(R.id.b_sheet_back_toolbar);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sheet.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            });

            TextView tv_snm_toolbar = findViewById(R.id.b_sheet_back_tv_snm);
            tv_snm_toolbar.setText(item.getSnm());
            tv_snm_toolbar.setSelected(true);

            ImageView iv_ac3 = findViewById(R.id.b_sheet_back_iv_ac3);
            ImageView iv_cha = findViewById(R.id.b_sheet_back_iv_cha);
            ImageView iv_com = findViewById(R.id.b_sheet_back_iv_combo);
            ImageView iv_single = findViewById(R.id.b_sheet_back_iv_single);
            ImageView iv_super = findViewById(R.id.b_sheet_back_iv_super);
            ImageView iv_h2 = findViewById(R.id.b_sheet_back_iv_h2);

            ArrayList<ImageView> imgList = new ArrayList<>();
            imgList.add(iv_ac3);
            imgList.add(iv_cha);
            imgList.add(iv_com);
            imgList.add(iv_single);
            imgList.add(iv_super);
            imgList.add(iv_h2);

            new ChargerType(B_Main.this)
                    .setCtp(item.getCtp())
                    .setIvList(imgList)
                    .initView();

            AppCompatButton acb_path = findViewById(R.id.b_sheet_back_acb_path);
            tb_back_bookmark = findViewById(R.id.b_sheet_back_acb_bookmark);
            AppCompatButton acb_share = findViewById(R.id.b_sheet_back_acb_share);

            acb_path.setOnClickListener(getPathOnClickListener());
            tb_back_bookmark.setOnClickListener(getBookmarkOnClickListener());
            acb_share.setOnClickListener(getShareOnClickListener());

            RecyclerView recycler = findViewById(R.id.b_sheet_back_recycler_info);
            recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            B_InfoAdapter adapter = new B_InfoAdapter(getApplicationContext(), item);
            recycler.setAdapter(adapter);

            initStatus();
        }

        private void checkBookmark() {
            if (Session.getCurrentSession().checkAndImplicitOpen()) {
                ArrayList<Charger.Bookmark> values = Shared.getBookmark(B_Main.this);
                boolean isBookmark = false;
                for (Charger.Bookmark value : values) {
                    if (item.getSid().equals(value.getSid())) {
                        isBookmark = true;
                        break;
                    }
                }

                if (isBookmark) {
                    tb_front_bookmark.setChecked(true);
                    tb_back_bookmark.setChecked(true);
                } else {
                    tb_front_bookmark.setChecked(false);
                    tb_back_bookmark.setChecked(false);
                }
            } else {
                tb_front_bookmark.setChecked(false);
                tb_back_bookmark.setChecked(false);
            }
        }

        private void initStatus() {
            final ArrayList<Charger.Status> filteredList = filterStatusList();
            final int machineCount = filteredList.size();
            String machineCountString = filteredList.size() + "대";

            TextView tv_count = findViewById(R.id.b_sheet_tv_chargerCount);
            tv_count.setText(machineCountString);

            final RecyclerView recyclerView = findViewById(R.id.b_sheet_recycler_status);
            recyclerView.setLayoutManager(new LinearLayoutManager(B_Main.this, LinearLayoutManager.HORIZONTAL, false));
            recyclerView.setNestedScrollingEnabled(false); // 전체(세로) 스크롤 가능하게함 in 가로 RecyclerView
            recyclerView.setAdapter(new B_StatusAdapter(getApplicationContext(), filteredList, true));

            ImageView iv_refresh = findViewById(R.id.b_sheet_iv_refresh);
            iv_refresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Server(B_Main.this).loadStatus(new Server.OnTaskListener() {
                        ImageView iv_refresh = findViewById(R.id.b_sheet_iv_refresh);

                        @Override
                        public void onPreTask() {
                            iv_refresh.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate));
                            recyclerView.setAdapter(new B_StatusAdapter(getApplicationContext(), machineCount, false));
                        }

                        @Override
                        public void onPostTask() {
                            iv_refresh.clearAnimation();
                            recyclerView.setAdapter(new B_StatusAdapter(getApplicationContext(), filterStatusList(), true));
                        }
                    });
                }
            });
        }

        private ArrayList<Charger.Status> filterStatusList() {
            ArrayList<Charger.Status> statusList = Shared.getStatus(B_Main.this);
            ArrayList<Charger.Status> filteredStatusList = new ArrayList<>();
            for (Charger.Status status : statusList) {
                if (item.getSid().equals(status.getSid()))
                    filteredStatusList.add(status);
            }
            return filteredStatusList;
        }

        private void initComment() {
            TextView tv_commentCount = findViewById(R.id.b_sheet_tv_commentCount);
            tv_commentCount.setText("");

            TextView tv_commentPost = findViewById(R.id.b_sheet_tv_commentPost);
            tv_commentPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Session.getCurrentSession().checkAndImplicitOpen()) {
                        Intent intent = new Intent(getApplicationContext(), Ba_Comment.class);
                        intent.putExtra("sid", item.getSid());
                        intent.putExtra("snm", item.getSnm());
                        intent.putExtra("apply", true);
                        startActivityForResult(intent, REQUEST_ACTIVITY_COMMENT);
                        return;
                    }
                    requestLogin();
                }
            });

            new Server(B_Main.this).loadComment(null, item.getSid(), null, new Server.OnCommentTaskListener() {
                View view_progressBar = findViewById(R.id.b_sheet_constraint_progressBar);
                View view_nothingComment = findViewById(R.id.b_sheet_constraint_nothingComment);
                RecyclerView recycler_comment = findViewById(R.id.b_sheet_recycler_comment);

                @Override
                public void onPreTask() {
                    view_progressBar.setVisibility(View.VISIBLE);
                    view_nothingComment.setVisibility(View.GONE);
                    recycler_comment.setVisibility(View.GONE);
                }

                @Override
                public void onPostTask(Response<ChargerList> response) {
                    assert response.body() != null;
                    ArrayList<Charger.Comment> comments = response.body().getComment();
                    TextView tv_commentCount = findViewById(R.id.b_sheet_tv_commentCount);
                    tv_commentCount.setText(String.valueOf(comments.size()));

                    if (comments.size() == 0) {
                        view_progressBar.setVisibility(View.GONE);
                        view_nothingComment.setVisibility(View.VISIBLE);
                        recycler_comment.setVisibility(View.GONE);
                        return;
                    }

                    view_progressBar.setVisibility(View.GONE);
                    view_nothingComment.setVisibility(View.GONE);
                    recycler_comment.setVisibility(View.VISIBLE);
                    recycler_comment.setLayoutManager(new LinearLayoutManager(B_Main.this));
                    recycler_comment.setAdapter(new B_CommentAdapter(getApplicationContext(), comments));
                }
            });
        }

        private View.OnClickListener getPathOnClickListener() {
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse("geo:0,0?q=" + item.getPosition().latitude + "," + item.getPosition().longitude + "(" + item.getSnm() + ")");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            };
        }

        private View.OnClickListener getBookmarkOnClickListener() {
            return new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CompoundButton toggle = (CompoundButton) view;
                    if (!Session.getCurrentSession().checkAndImplicitOpen()) {
                        toggle.setChecked(false);
                        requestLogin();
                        return;
                    }

                    if (toggle.isChecked()) {
                        new Server(B_Main.this).saveBookmark(item.getSid());
                        tb_front_bookmark.setChecked(true);
                        tb_back_bookmark.setChecked(true);
                    } else {
                        new Server(B_Main.this).deleteBookmark(item.getSid());
                        tb_front_bookmark.setChecked(false);
                        tb_back_bookmark.setChecked(false);
                    }
                }
            };
        }

        private View.OnClickListener getShareOnClickListener() {
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String subject = "[" + item.getSnm() + "] " + item.getAdr();
                    String uri = "http://maps.google.com/maps/search/?api=1&query=" + item.getPosition().latitude + "," + item.getPosition().longitude;

                    Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
                    intent.putExtra(android.content.Intent.EXTRA_TEXT, uri);
                    startActivity(Intent.createChooser(intent, "공유하기"));
                }
            };
        }
    }

    //todo: 'Kakao' 계정으로 로그인 하겠다는 알림 like 카카오지하철
    private void kakaoLogin() {
        LoginButton lb = findViewById(R.id.b_lb_kakao);
        lb.performClick();
    }

    private void requestLogin() {
        new AlertDialog.Builder(B_Main.this)
                .setMessage("로그인이 필요한 기능입니다.")
                .setPositiveButton(getString(R.string.com_kakao_ok_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        kakaoLogin();
                    }
                })
                .setNegativeButton(getString(R.string.com_kakao_cancel_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private void setLoginState(String nick, String img) {
        NavigationView navigationView = findViewById(R.id.b_navigation);
        View headerLayout = navigationView.getHeaderView(0);
        headerLayout.setClickable(false);

        ImageView iv_profile = headerLayout.findViewById(R.id.b_iv_profile);
        if (!img.equals("")) {
            Glide.with(getApplicationContext())
                    .load(img)
                    .apply(RequestOptions.circleCropTransform())
                    .into(iv_profile);
        } else {
            Glide.with(getApplicationContext())
                    .load(R.drawable.kakao_default_profile_image)
                    .apply(RequestOptions.circleCropTransform())
                    .into(iv_profile);
        }

        TextView tv_nick = headerLayout.findViewById(R.id.b_tv_nickname);
        tv_nick.setText(nick);
    }

    private void setLogoutState() {
        NavigationView navigationView = findViewById(R.id.b_navigation);
        View headerLayout = navigationView.getHeaderView(0);
        headerLayout.setClickable(true);

        ImageView iv_profile = headerLayout.findViewById(R.id.b_iv_profile);
        Glide.with(getApplicationContext())
                .load(R.drawable.kakao_default_profile_image)
                .apply(RequestOptions.circleCropTransform())
                .into(iv_profile);

        TextView tv_nickname = headerLayout.findViewById(R.id.b_tv_nickname);
        tv_nickname.setText("로그인을 해주세요.");

        if (Session.getCurrentSession().isOpened()) {
            Session.getCurrentSession().close();
        }
        checkBookmark();
    }

    private void connectGoogleApiClient() {
        if (mGoogleApiClient.isConnected())
            mGoogleApiClient.reconnect();
        else
            mGoogleApiClient.connect();
    }

    private ValueAnimator getAnimator(final Charger.Info item, final Marker marker) {
        ValueAnimator animator = ValueAnimator.ofFloat(1F, 2F);
        animator.setDuration(500);
        animator.setInterpolator(new AnticipateOvershootInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            View view = getMarkerViewByManage(getApplicationContext(), item);
            Bitmap bitmap = createBitmapFromMarkerView(B_Main.this, view);

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap,
                        (int) Math.max(dpToPx(30), dpToPx(40) * valueAnimator.getAnimatedFraction()),
                        (int) Math.max(dpToPx(32), dpToPx(42) * valueAnimator.getAnimatedFraction()),
                        false);
                if (map.getProjection().getVisibleRegion().latLngBounds.contains(marker.getPosition())) {
                    marker.setIcon(BitmapDescriptorFactory.fromBitmap(scaledBitmap));
                }
            }
        });
        return animator;
    }

    private void reverseAnimator() {
        if (saveAnimator != null) {
            saveAnimator.reverse();
            saveAnimator = null;
            saveMarker = null;
        }
    }

    private void showProgressBar() {
        View progressBar = findViewById(R.id.b_progress);
        progressBar.setVisibility(View.VISIBLE);
        if (map != null) {
            map.getUiSettings().setAllGesturesEnabled(false);
        }
    }

    private void closeProgressBar() {
        View progressBar = findViewById(R.id.b_progress);
        progressBar.setVisibility(View.GONE);
        if (map != null) {
            map.getUiSettings().setAllGesturesEnabled(true);
            map.getUiSettings().setRotateGesturesEnabled(false);
        }
    }

    private int dpToPx(float value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, getResources().getDisplayMetrics());
    }

    private interface ClusterRenderListener {
        void onClusterRendered();
    }

    private void setClusterRenderListener(ClusterRenderListener listener) {
        this.listener = listener;
    }

    private void removeClusterRenderListener() {
        listener = null;
    }
}