package com.mtesitoo.model;

import android.content.Context;

import com.mtesitoo.R;

import com.mtesitoo.backend.cache.ZoneCache;
import com.mtesitoo.backend.cache.logic.IZonesCache;
import com.mtesitoo.backend.model.Zone;
import com.tech.freak.wizardpager.model.AbstractWizardModel;
import com.tech.freak.wizardpager.model.PageList;
import com.tech.freak.wizardpager.model.SingleFixedChoicePage;
import com.tech.freak.wizardpager.model.TextPage;

import java.util.List;

/**
 * Created by Administrator on 2016/5/6 0006.
 */
public class RegistrationWizard extends AbstractWizardModel {

   // private Context mContext;

    public RegistrationWizard(Context context) {
        super(context);
    }

    @Override
    protected PageList onNewRootPageList() {

        IZonesCache zonesCache=new ZoneCache(mContext);
        List<Zone> zones= zonesCache.GetZones();
        String[] zonesNames=new String[zones.size()];

        for (int i = 0; i <zones.size() ; i++) {
            zonesNames[i]=zones.get(i).getName();
        }
         //Agree
        String[] agreeNames=new String[2];;
        agreeNames[0]="Yes";
        agreeNames[1]="No";

        return new PageList(

                new TextPage(this, mContext.getString(R.string.page_username)).setRequired(true),
                new TextPage(this, mContext.getString(R.string.page_firstname)).setRequired(true),
                new TextPage(this, mContext.getString(R.string.page_lastname)).setRequired(true),
                new TextPage(this, mContext.getString(R.string.page_password)).setRequired(true),
                new TextPage(this, mContext.getString(R.string.page_email)).setRequired(true),
                new TextPage(this, mContext.getString(R.string.page_phonenumber)).setRequired(true),
                //CountriesCodes
                new TextPage(this, mContext.getString(R.string.page_address1)).setRequired(true),
                new TextPage(this, mContext.getString(R.string.page_address2)).setRequired(true),
                new TextPage(this, mContext.getString(R.string.page_Postcode)).setRequired(true),
                new SingleFixedChoicePage(this, mContext.getString(R.string.page_Zone))
                        .setChoices(zonesNames),
                new SingleFixedChoicePage(this, mContext.getString(R.string.page_Agree))
                .setChoices(agreeNames)
               );

    }
}
