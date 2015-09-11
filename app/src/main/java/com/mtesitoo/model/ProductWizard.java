package com.mtesitoo.model;

/**
 * Created by jackwu on 2015-07-18.
 */

import android.content.Context;

import com.mtesitoo.R;
import com.tech.freak.wizardpager.model.AbstractWizardModel;
import com.tech.freak.wizardpager.model.BranchPage;
import com.tech.freak.wizardpager.model.ImagePage;
import com.tech.freak.wizardpager.model.NumberPage;
import com.tech.freak.wizardpager.model.PageList;
import com.tech.freak.wizardpager.model.SingleFixedChoicePage;
import com.tech.freak.wizardpager.model.TextPage;

public class ProductWizard extends AbstractWizardModel {
    public ProductWizard(Context context) {
        super(context);
    }

    @Override
    protected PageList onNewRootPageList() {

        return new PageList(
                new TextPage(this, this.mContext.getString(R.string.page_name)).setRequired(true),

                new TextPage(this, this.mContext.getString(R.string.page_description)).setRequired(true),

                new ImagePage(this, this.mContext.getString(R.string.page_photo)).setRequired(true),

                new TextPage(this, this.mContext.getString(R.string.page_location)).setRequired(true),

                new BranchPage(this, this.mContext.getString(R.string.page_category))
                        .addBranch(
                                this.mContext.getString(R.string.select_animal),
                                new SingleFixedChoicePage(this, this.mContext.getString(R.string.page_animal_category))
                                        .setChoices(
                                                this.mContext.getString(R.string.select_bees),
                                                this.mContext.getString(R.string.select_dairy),
                                                this.mContext.getString(R.string.select_meat),
                                                this.mContext.getString(R.string.select_fish_seafood)
                                        )
                        )
                        .addBranch(
                                this.mContext.getString(R.string.select_plant),
                                new SingleFixedChoicePage(this, this.mContext.getString(R.string.page_plant_category))
                                        .setChoices(
                                                this.mContext.getString(R.string.select_cereal),
                                                this.mContext.getString(R.string.select_fruits_nuts),
                                                this.mContext.getString(R.string.select_vegetables)
                                        )
                        ),

                new SingleFixedChoicePage(this, this.mContext.getString(R.string.page_si_unit))
                        .setChoices(
                                this.mContext.getString(R.string.select_milligram),
                                this.mContext.getString(R.string.select_gram),
                                this.mContext.getString(R.string.select_kilogram)
                        )
                        .setRequired(true),

                new NumberPage(this, this.mContext.getString(R.string.page_price_per_unit)).setRequired(true),

                new NumberPage(this, this.mContext.getString(R.string.page_quantity)).setRequired(true),

                new TextPage(this, this.mContext.getString(R.string.page_expiration)).setRequired(true));
    }
}