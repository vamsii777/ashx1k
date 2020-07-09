package com.dewonderstruck.apps.ashx0.viewmodel.aboutus;

import com.dewonderstruck.apps.Config;
import com.dewonderstruck.apps.ashx0.repository.aboutus.AboutUsRepository;
import com.dewonderstruck.apps.ashx0.utils.AbsentLiveData;
import com.dewonderstruck.apps.ashx0.viewmodel.common.PSViewModel;
import com.dewonderstruck.apps.ashx0.viewobject.AboutUs;
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

/**
 * Created by Vamsi Madduluri on 12/30/17.
 * Contact Email : vamsii.wrkhost@gmail.com
 */

public class AboutUsViewModel extends PSViewModel {


    //region Variables

    // Get AboutUs
    private final LiveData<Resource<AboutUs>> aboutUsLiveData;
    private MutableLiveData<String> aboutUsObj = new MutableLiveData<>();

    public String aboutId;
    //endregion


    //region Constructors

    @Inject
    AboutUsViewModel(AboutUsRepository repository) {

        aboutUsLiveData = Transformations.switchMap(aboutUsObj, newsId -> {
            if (newsId.isEmpty()) {
                return AbsentLiveData.create();
            }
            return repository.getAboutUs(Config.API_KEY);
        });

    }

    //endregion


    //region Public Methods

    public void setAboutUsObj(String aboutUsObj) {
        this.aboutUsObj.setValue(aboutUsObj);
    }

    public LiveData<Resource<AboutUs>> getAboutUsData() {
        return aboutUsLiveData;
    }

    //endregion

}
