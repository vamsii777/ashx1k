package com.dewonderstruck.apps.ashx0.ui.contactus;

import com.dewonderstruck.apps.ashx0.repository.contactus.ContactUsRepository;
import com.dewonderstruck.apps.ashx0.ui.common.BackgroundTaskHandler;

/**
 * Created by Vamsi Madduluri on 7/2/18.
 * Contact Email : vamsii.wrkhost@gmail.com
 * Website : http://www.dewonderstruck.com
 */

public class ContactUsBackgroundTaskHandler extends BackgroundTaskHandler{

    private final ContactUsRepository repository;

    public ContactUsBackgroundTaskHandler(ContactUsRepository repository) {
        super();
        this.repository = repository;
    }


    public void postContactUs(String apiKey, String contactName, String contactEmail, String contactDesc, String contactPhone) {

        unregister();

        holdLiveData = repository.postContactUs(apiKey, contactName, contactEmail, contactDesc, contactPhone);
        loadingState.setValue(new LoadingState(true, null));

        //noinspection ConstantConditions
        holdLiveData.observeForever(this);
    }
}
