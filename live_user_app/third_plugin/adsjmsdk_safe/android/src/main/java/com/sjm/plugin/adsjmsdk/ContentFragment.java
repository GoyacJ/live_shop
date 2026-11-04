package com.sjm.plugin.adsjmsdk;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.sjm.sjmsdk.ad.SjmContentAd;
import com.sjm.sjmsdk.ad.SjmContentAdListener;

/**
 * author:hm
 * date: 2022/4/8
 */
public class ContentFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_content_list,null);
//        loadSplash("J4961952628");
        return view;
    }
    public void loadContent(String adId, SjmContentAdListener listener){
//        Log.e(TAG, "onMethodCall:call.method111=getView111");
        SjmContentAd contentAd= new SjmContentAd(getActivity(),listener, adId);

        contentAd.showAd(R.id.main_frame_layout,getChildFragmentManager());
    }

}
