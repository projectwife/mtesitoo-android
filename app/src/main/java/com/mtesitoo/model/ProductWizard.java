package com.mtesitoo.model;

/**
 * Created by jackwu on 2015-07-18.
 */

import android.content.Context;

import com.mtesitoo.R;
import com.mtesitoo.backend.cache.CategoryCache;
import com.mtesitoo.backend.cache.logic.ICategoryCache;
import com.mtesitoo.backend.model.Category;
import com.tech.freak.wizardpager.model.AbstractWizardModel;
import com.tech.freak.wizardpager.model.BranchPage;
import com.tech.freak.wizardpager.model.DecimalPage;
import com.tech.freak.wizardpager.model.ImagePage;
import com.tech.freak.wizardpager.model.NumberPage;
import com.tech.freak.wizardpager.model.PageList;
import com.tech.freak.wizardpager.model.SingleFixedChoicePage;
import com.tech.freak.wizardpager.model.TextPage;

import java.util.ArrayList;
import java.util.List;

public class ProductWizard extends AbstractWizardModel {
    public ProductWizard(Context context) {
        super(context);
    }

    @Override
    protected PageList onNewRootPageList() {
        ICategoryCache cache = new CategoryCache(mContext);
        List<Category> categories = cache.getCategories();
        String[] categoryNames = new String[categories.size()];

        for (int i = 0; i < categories.size(); i++) {
            categoryNames[i] = categories.get(i).getName();
        }

        return new PageList(
                new TextPage(this, mContext.getString(R.string.page_name)).setRequired(true),

                new TextPage(this, mContext.getString(R.string.page_description)).setRequired(true),

                new ImagePage(this, mContext.getString(R.string.page_photo)).setRequired(true),

//                new TextPage(this, mContext.getString(R.string.page_location)).setRequired(true),

                new SingleFixedChoicePage(this, mContext.getString(R.string.page_category))
                        .setChoices(categoryNames),

/*                new SingleFixedChoicePage(this, mContext.getString(R.string.page_si_unit))
                        .setChoices(
                                this.mContext.getString(R.string.select_milligram),
                                this.mContext.getString(R.string.select_gram),
                                this.mContext.getString(R.string.select_kilogram)
                        )
                        .setRequired(true),
*/
                new DecimalPage(this, mContext.getString(R.string.page_price_per_unit)).setRequired(true),

                new NumberPage(this, mContext.getString(R.string.page_quantity)).setRequired(true));

//                new TextPage(this, mContext.getString(R.string.page_expiration)).setRequired(true));
    }
}