package com.mtesitoo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mtesitoo.backend.cache.AuthorizationCache;
import com.mtesitoo.backend.cache.CategoryCache;
import com.mtesitoo.backend.cache.CountriesCache;
import com.mtesitoo.backend.cache.UnitCache;
import com.mtesitoo.backend.cache.ZoneCache;
import com.mtesitoo.backend.cache.logic.IAuthorizationCache;
import com.mtesitoo.backend.cache.logic.ICategoryCache;
import com.mtesitoo.backend.cache.logic.ICountriesCache;
import com.mtesitoo.backend.cache.logic.IUnitCache;
import com.mtesitoo.backend.cache.logic.IZonesCache;
import com.mtesitoo.backend.model.Category;
import com.mtesitoo.backend.model.Countries;
import com.mtesitoo.backend.model.Seller;
import com.mtesitoo.backend.model.Unit;
import com.mtesitoo.backend.model.Zone;
import com.mtesitoo.backend.service.CategoryRequest;
import com.mtesitoo.backend.service.CommonRequest;
import com.mtesitoo.backend.service.CountriesRequest;
import com.mtesitoo.backend.service.LoginRequest;
import com.mtesitoo.backend.service.SellerRequest;
import com.mtesitoo.backend.service.ZoneRequest;
import com.mtesitoo.backend.service.logic.ICategoryRequest;
import com.mtesitoo.backend.service.logic.ICommonRequest;
import com.mtesitoo.backend.service.logic.ICountriesRequest;
import com.mtesitoo.backend.service.logic.ILoginRequest;
import com.mtesitoo.backend.service.logic.ICallback;
import com.mtesitoo.backend.service.logic.ISellerRequest;
import com.mtesitoo.backend.service.logic.IZoneRequest;

import java.text.Collator;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Context mContext;
    protected SharedPreferences.Editor mEditor;
    protected SharedPreferences mPrefs;
    String[] zonesNames;

    @Bind(R.id.user_name)
    TextView mUsername;
    
    @Bind(R.id.password)
    TextView mPassword;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login: {
                final Intent intent = new Intent(this, HomeActivity.class);
                final ILoginRequest loginService = new LoginRequest(this);

                loginService.authenticateUser(mUsername.getText().toString(), mPassword.getText().toString(), new ICallback<String>() {
                    @Override
                    public void onResult(String result) {

                        Log.d("LOGIN - RESULT", result);
                        ICategoryRequest categoryService = new CategoryRequest(mContext);
                        ISellerRequest sellerService = new SellerRequest(mContext);
                        ICommonRequest commonService = new CommonRequest(mContext);

                        commonService.getLengthUnits(new ICallback<List<Unit>>() {
                            @Override
                            public void onResult(List<Unit> units) {
                                IUnitCache cache = new UnitCache(mContext);
                                cache.storeLengthUnits(units);
                            }

                            @Override
                            public void onError(Exception e) {
                                Log.e("LengthUnits", e.toString());
                            }
                        });

                        commonService.getWeightUnits(new ICallback<List<Unit>>() {
                            @Override
                            public void onResult(List<Unit> units) {
                                IUnitCache cache = new UnitCache(mContext);
                                cache.storeWeightUnits(units);
                            }

                            @Override
                            public void onError(Exception e) {
                                Log.e("WeightUnits", e.toString());
                            }
                        });

                        categoryService.getCategories(new ICallback<List<Category>>() {
                            @Override
                            public void onResult(List<Category> categories) {
                                ICategoryCache cache = new CategoryCache(mContext);
                                cache.storeCategories(categories);
                            }

                            @Override
                            public void onError(Exception e) {
                                Log.e("Categories", e.toString());
                            }
                        });

                        sellerService.getSellerInfo(Integer.parseInt(result), new ICallback<Seller>() {
                            @Override
                            public void onResult(Seller result) {
                                Log.d("Login - Seller Info", result.toString());
                                intent.putExtra(mContext.getString(R.string.bundle_seller_key), result);
                                mContext.startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onError(Exception e) {
                                Log.e("getSellerInfo", e.toString());
                            }
                        });
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e("AuthenticateUser", e.toString());
                        Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
                break;
            }

            case R.id.newUser: {
                //Todo: allow user to pick between seller and buyer profiles

                ICountriesCache cache = new CountriesCache(mContext);
                List<Countries> countries = cache.getCountries();
                final String[] countriesNames = new String[countries.size()];

                for (int i = 0; i < countries.size(); i++) {
                    countriesNames[i] = countries.get(i).getName();
                }

                //Arrays.sort(countriesNames);

                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Where are you from?");
                builder.setItems(countriesNames, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mEditor.putString("SelectedCountries", countriesNames[which]);
                        mEditor.apply();

                        IZoneRequest zoneService = new ZoneRequest(mContext);

                        zoneService.getZones(new ICallback<List<Zone>>() {
                            @Override
                            public void onResult(List<Zone> zones) {

                                IZonesCache zonesCache = new ZoneCache(mContext);
                                zonesCache.storeZones(zones);

                                List<Zone> zones1 = zones;
                                zonesNames = new String[zones1.size()];

                                for (int i = 0; i < zones1.size(); i++) {
                                    zonesNames[i] = zones1.get(i).getName();
                                }

                                final Intent intent = new Intent(mContext, RegistrationActivity.class);
                                mContext.startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onError(Exception e) {
                                Log.e("Zones", e.toString());
                            }
                        });

                    }
                });
                builder.show();
                break;
            }

        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
        Button login = (Button) findViewById(R.id.login);
        login.setOnClickListener(this);
        Button newUser = (Button) findViewById(R.id.newUser);
        newUser.setOnClickListener(this);
        mContext = this;

        mPrefs = this.getSharedPreferences("pref", Context.MODE_PRIVATE);
        mEditor = mPrefs.edit();
        ICountriesRequest countriesService = new CountriesRequest(mContext);

        countriesService.getCountries(new ICallback<List<Countries>>() {
            @Override
            public void onResult(List<Countries> countries) {
                ICountriesCache cache = new CountriesCache(mContext);
                cache.storeCountries(countries);
                String s1 = "";

                for (Countries country : countries) {
                    s1 = s1 + country.getName();
                }
            }

            @Override
            public void onError(Exception e) {
                Log.e("Countries", e.toString());
            }
        });


        final ILoginRequest loginService = new LoginRequest(this);
        loginService.getAuthToken(new ICallback<String>() {
            @Override
            public void onResult(String result) {
                IAuthorizationCache authorizationCache = new AuthorizationCache(mContext);
                if (authorizationCache.getAuthorization() == null) {
                    authorizationCache.storeAuthorization(result);
                }
            }

            @Override
            public void onError(Exception e) {
                Log.e("Login", e.toString());
            }
        });
    }
}