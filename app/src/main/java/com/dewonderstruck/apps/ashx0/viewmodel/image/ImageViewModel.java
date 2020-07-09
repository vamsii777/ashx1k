package com.dewonderstruck.apps.ashx0.viewmodel.image;

import com.dewonderstruck.apps.ashx0.repository.image.ImageRepository;
import com.dewonderstruck.apps.ashx0.utils.AbsentLiveData;
import com.dewonderstruck.apps.ashx0.viewmodel.common.PSViewModel;
import com.dewonderstruck.apps.ashx0.viewobject.Image;
import com.dewonderstruck.apps.ashx0.viewobject.common.Resource;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

/**
 * Created by Vamsi Madduluri on 12/8/17.
 * Contact Email : vamsii.wrkhost@gmail.com
 */


public class ImageViewModel extends PSViewModel {


    //region Variables

    // Get Image Video List
    private final LiveData<Resource<List<Image>>> imageListLiveData;
    private MutableLiveData<TmpDataHolder> imageParentObj = new MutableLiveData<>();

    public String id;
    public List<Image> newsImageList;
    public String imgId;
    public String imgType;

    //endregion


    //region Constructors

    @Inject
    ImageViewModel(ImageRepository repository) {

        imageListLiveData = Transformations.switchMap(imageParentObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            return repository.getImageList(obj.imgType, obj.imageParentId);
        });

    }

    //endregion


    //region Methods

    public void setImageParentId(String imageType, String imageParentId) {
        TmpDataHolder tmpDataHolder = new TmpDataHolder(imageType, imageParentId);
        this.imageParentObj.setValue(tmpDataHolder);
    }

    public LiveData<Resource<List<Image>>> getImageListLiveData() {
        return imageListLiveData;
    }


    static class TmpDataHolder {
        String imgType ;
        String imageParentId ;

        public TmpDataHolder(String imgType, String imageParentId) {
            this.imgType = imgType;
            this.imageParentId = imageParentId;
        }
    }

    //endregion

}
