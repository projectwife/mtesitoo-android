package com.mtesitoo.model;

/**
 * Created by jackwu on 2015-07-18.
 */

import android.content.Context;

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

                new TextPage(this, "Name").setRequired(true),

                new TextPage(this, "Description").setRequired(true),

                new ImagePage(this, "Photo").setRequired(true),

                new TextPage(this, "Location").setRequired(true),

                new BranchPage(this, "Category")
                        .addBranch(
                                "Animal",
                                new SingleFixedChoicePage(this, "Animal Category")
                                        .setChoices("Bees", "Dairy",
                                                "Meat", "Fish and Seafood"))
                        .addBranch(
                                "Plant",
                                new SingleFixedChoicePage(this, "Plant Category")
                                        .setChoices("Cereal", "Fruits & Nuts",
                                                "Vegetables")),

                new SingleFixedChoicePage(this, "SI Unit").setChoices("Milligram", "Gram ", "Kilogram")
                        .setRequired(true),

                new NumberPage(this, "Price per Unit").setRequired(true),

                new NumberPage(this, "Quantity").setRequired(true),

                new TextPage(this, "Expiration").setRequired(true));
    }
}
