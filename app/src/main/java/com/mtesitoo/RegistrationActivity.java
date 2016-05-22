/*
 * Copyright 2012 Roman Nurik
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mtesitoo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SyncStats;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.mtesitoo.backend.cache.CategoryCache;
import com.mtesitoo.backend.cache.CountriesCache;
import com.mtesitoo.backend.cache.UnitCache;
import com.mtesitoo.backend.cache.ZoneCache;
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
import com.mtesitoo.backend.service.ProductRequest;
import com.mtesitoo.backend.service.RegistrationRequest;
import com.mtesitoo.backend.service.SellerRequest;
import com.mtesitoo.backend.service.logic.ICategoryRequest;
import com.mtesitoo.backend.service.logic.ICommonRequest;
import com.mtesitoo.backend.service.logic.ICountriesRequest;
import com.mtesitoo.backend.service.logic.ILoginRequest;
import com.mtesitoo.backend.service.logic.IProductRequest;
import com.mtesitoo.backend.service.logic.ICallback;
import com.mtesitoo.backend.model.Product;


import com.mtesitoo.backend.service.logic.IRegistrationRequest;
import com.mtesitoo.backend.service.logic.ISellerRequest;
import com.mtesitoo.model.RegistrationWizard;
import com.tech.freak.wizardpager.model.AbstractWizardModel;
import com.tech.freak.wizardpager.model.ImagePage;
import com.tech.freak.wizardpager.model.ModelCallbacks;
import com.tech.freak.wizardpager.model.Page;
import com.tech.freak.wizardpager.ui.PageFragmentCallbacks;
import com.tech.freak.wizardpager.ui.ReviewFragment;
import com.tech.freak.wizardpager.ui.StepPagerStrip;

import java.util.Date;
import java.util.List;
import android.os.Handler;


import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegistrationActivity extends ActionBarActivity implements
        PageFragmentCallbacks, ReviewFragment.Callbacks, ModelCallbacks {
    Handler myHandler;
    private MyPagerAdapter mPagerAdapter;
    private AbstractWizardModel mWizardModel;
    private List<Page> mCurrentPageSequence;

    private boolean mEditingAfterReview;
    private boolean mConsumePageSelectedEvent;
    Context mContext;

    protected SharedPreferences.Editor mEditor;
    protected SharedPreferences mPrefs;

    @Bind(R.id.next_button)
    Button mNextButton;
    @Bind(R.id.prev_button)
    Button mPrevButton;
    @Bind(R.id.item_pager)
    ViewPager mPager;
    @Bind(R.id.strip)
    StepPagerStrip mStepPagerStrip;

    @OnClick(R.id.next_button)
    public void onNextButtonClick(View view) {

        //todo: loads the infomation of countries
        if (mPager.getCurrentItem() == mCurrentPageSequence.size()) {
            String username = mWizardModel.findByKey(this.getString(R.string.page_username)).getData().getString(Page.SIMPLE_DATA_KEY);
            String firstname = mWizardModel.findByKey(this.getString(R.string.page_firstname)).getData().getString(Page.SIMPLE_DATA_KEY);
            String lastname = mWizardModel.findByKey(this.getString(R.string.page_lastname)).getData().getString(Page.SIMPLE_DATA_KEY);
            String page_password = mWizardModel.findByKey(this.getString(R.string.page_password)).getData().getString(Page.SIMPLE_DATA_KEY);
            String country=mPrefs.getString("SelectedCountries","195");
            String zone=mWizardModel.findByKey(this.getString(R.string.page_Zone)).getData().getString(Page.SIMPLE_DATA_KEY);
            String page_phonenumber = mWizardModel.findByKey(this.getString(R.string.page_phonenumber)).getData().getString(Page.SIMPLE_DATA_KEY);
            String page_email = mWizardModel.findByKey(this.getString(R.string.page_email)).getData().getString(Page.SIMPLE_DATA_KEY);
            String Address1 = mWizardModel.findByKey(this.getString(R.string.page_address1)).getData().getString(Page.SIMPLE_DATA_KEY);
            String Address2 = mWizardModel.findByKey(this.getString(R.string.page_address2)).getData().getString(Page.SIMPLE_DATA_KEY);
            String page_Postcode = mWizardModel.findByKey(this.getString(R.string.page_Postcode)).getData().getString(Page.SIMPLE_DATA_KEY);
            String page_Agree = mWizardModel.findByKey(this.getString(R.string.page_Agree)).getData().getString(Page.SIMPLE_DATA_KEY);


           /* ICountriesCache cache = new CountriesCache(this);
            List<Countries> countries = cache.getCountries();

            for (Countries c : countries) {
                if (c.getName().equals(country)) {
                    country = Integer.toString(c.getId());
                    break;
                }
            }*/
            IZonesCache zonesCache=new ZoneCache(this);
            List<Zone> zones=zonesCache.GetZones();
            for (Zone c : zones) {
                if (c.getName().equals(zone)) {
                    zone = Integer.toString(c.getId());
                    break;
                }
            }


           final Seller seller=new Seller(0, username,firstname, lastname,
                   page_phonenumber , page_email,"Company", Address1, Address2,
                   page_Postcode,"uri",page_password, zone,"1",country);

            IRegistrationRequest registrationService=new RegistrationRequest(this);
            registrationService.submitSeller(seller, new ICallback<Seller>() {
                @Override
                public void onResult(Seller result) {

                }

                @Override
                public void onError(Exception e) {
                    Toast.makeText(mContext,e.getMessage(),Toast.LENGTH_LONG).show();
                }
            });

            myHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 100ms
                    System.out.println("i am holding and waiting");
                    startNewLogin(seller,mContext);
                    finish();
                }
            }, 3000);

        } else {
            if (mEditingAfterReview) {
                mPager.setCurrentItem(mPagerAdapter.getCount() - 1);
            } else {
                mPager.setCurrentItem(mPager.getCurrentItem() + 1);
            }
        }
    }

    @OnClick(R.id.prev_button)
    public void onPrevButtonClick(View view) {
        mPager.setCurrentItem(mPager.getCurrentItem() - 1);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ButterKnife.bind(this);
        mContext=this;
        myHandler = new Handler();
        //System.out.println("i am here");
        //Samuel 18/05/2016
        mPrefs = this.getSharedPreferences("pref", Context.MODE_PRIVATE);
        mEditor = mPrefs.edit();





        mWizardModel = new RegistrationWizard(this);

        if (savedInstanceState != null) {
            mWizardModel.load(savedInstanceState.getBundle("model"));
        }

        mWizardModel.registerListener(this);
        mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        mStepPagerStrip
                .setOnPageSelectedListener(new StepPagerStrip.OnPageSelectedListener() {
                    @Override
                    public void onPageStripSelected(int position) {
                        position = Math.min(mPagerAdapter.getCount() - 1,
                                position);
                        if (mPager.getCurrentItem() != position) {
                            mPager.setCurrentItem(position);
                        }
                    }
                });

        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mStepPagerStrip.setCurrentPage(position);

                if (mConsumePageSelectedEvent) {
                    mConsumePageSelectedEvent = false;
                    return;
                }

                mEditingAfterReview = false;
                updateBottomBar();
            }
        });

        onPageTreeChanged();
        updateBottomBar();
    }

    @Override
    public void onPageTreeChanged() {
        mCurrentPageSequence = mWizardModel.getCurrentPageSequence();
        recalculateCutOffPage();
        mStepPagerStrip.setPageCount(mCurrentPageSequence.size() + 1); // + 1 =
        // review
        // step
        mPagerAdapter.notifyDataSetChanged();
        updateBottomBar();
    }

    private void updateBottomBar() {
        int position = mPager.getCurrentItem();
        if (position == mCurrentPageSequence.size()) {
            mNextButton.setText(R.string.submit);
            mNextButton.setBackgroundResource(R.drawable.finish_background);
            mNextButton.setTextAppearance(this, R.style.TextAppearanceFinish);
        } else {
            mNextButton.setText(mEditingAfterReview ? R.string.summary
                    : R.string.next);
            mNextButton
                    .setBackgroundResource(R.drawable.selectable_item_background);
            TypedValue v = new TypedValue();
            getTheme().resolveAttribute(android.R.attr.textAppearanceMedium, v,
                    true);
            mNextButton.setTextAppearance(this, v.resourceId);
            mNextButton.setEnabled(position != mPagerAdapter.getCutOffPage());
        }

        mPrevButton
                .setVisibility(position <= 0 ? View.INVISIBLE : View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWizardModel.unregisterListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle("model", mWizardModel.save());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        for (Page page : mCurrentPageSequence) {
            if (page instanceof ImagePage) {
                ((ImagePage) page).getPageFragment().onActivityResult(requestCode, resultCode, data);
                break;
            }
        }
    }

    @Override
    public AbstractWizardModel onGetModel() {
        return mWizardModel;
    }

    @Override
    public void onEditScreenAfterReview(String key) {
        for (int i = mCurrentPageSequence.size() - 1; i >= 0; i--) {
            if (mCurrentPageSequence.get(i).getKey().equals(key)) {
                mConsumePageSelectedEvent = true;
                mEditingAfterReview = true;
                mPager.setCurrentItem(i);
                updateBottomBar();
                break;
            }
        }
    }

    @Override
    public void onPageDataChanged(Page page) {
        if (page.isRequired()) {
            if (recalculateCutOffPage()) {
                mPagerAdapter.notifyDataSetChanged();
                updateBottomBar();
            }
        }
    }

    @Override
    public Page onGetPage(String key) {
        return mWizardModel.findByKey(key);
    }

    private boolean recalculateCutOffPage() {
        // Cut off the pager adapter at first required page that isn't completed
        int cutOffPage = mCurrentPageSequence.size() + 1;
        for (int i = 0; i < mCurrentPageSequence.size(); i++) {
            Page page = mCurrentPageSequence.get(i);
            if (page.isRequired() && !page.isCompleted()) {
                cutOffPage = i;
                break;
            }
        }

        if (mPagerAdapter.getCutOffPage() != cutOffPage) {
            mPagerAdapter.setCutOffPage(cutOffPage);
            return true;
        }

        return false;
    }

    public class MyPagerAdapter extends FragmentStatePagerAdapter {
        private int mCutOffPage;
        private Fragment mPrimaryItem;

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            if (i >= mCurrentPageSequence.size()) {
                return new ReviewFragment();
            }

            return mCurrentPageSequence.get(i).createFragment();
        }

        @Override
        public int getItemPosition(Object object) {
            // TODO: be smarter about this
            if (object == mPrimaryItem) {
                // Re-use the current fragment (its position never changes)
                return POSITION_UNCHANGED;
            }

            return POSITION_NONE;
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position,
                                   Object object) {
            super.setPrimaryItem(container, position, object);
            mPrimaryItem = (Fragment) object;
        }

        @Override
        public int getCount() {
            return Math.min(mCutOffPage + 1, mCurrentPageSequence == null ? 1
                    : mCurrentPageSequence.size() + 1);
        }

        public void setCutOffPage(int cutOffPage) {
            if (cutOffPage < 0) {
                cutOffPage = Integer.MAX_VALUE;
            }
            mCutOffPage = cutOffPage;
        }

        public int getCutOffPage() {
            return mCutOffPage;
        }
    }
    public void startNewLogin(Seller seller,final Context mContext)
    {
        // do something for button 1 click
        final Intent intent = new Intent(this, HomeActivity.class);
        final ILoginRequest loginService = new LoginRequest(this);

        loginService.authenticateUser(seller.getUsername(), seller.getmPassword(), new ICallback<String>() {
            @Override
            public void onResult(String result) {

                System.out.println("result---"+result);
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
                    }
                });

                sellerService.getSellerInfo(Integer.parseInt(result), new ICallback<Seller>() {
                    @Override
                    public void onResult(Seller result) {System.out.println("seller result--"+result);
                        intent.putExtra(mContext.getString(R.string.bundle_seller_key), result);
                        mContext.startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });


            }

            @Override
            public void onError(Exception e) {  System.out.println("in onError---");
                Toast.makeText(mContext,e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }

}
