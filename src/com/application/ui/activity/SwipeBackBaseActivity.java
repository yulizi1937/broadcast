/**
 * https://github.com/ikew0ng/SwipeBackLayout
 */
package com.application.ui.activity;

import java.io.File;

import android.os.Bundle;
import android.view.View;

import com.application.ui.view.SwipeBackActivityBase;
import com.application.ui.view.SwipeBackActivityHelper;
import com.application.ui.view.SwipeBackLayout;
import com.application.ui.view.SwipeBackUtils;
import com.application.utils.AndroidUtilities;

/**
 * @author Vikalp Patel(VikalpPatelCE)
 *
 */
public class SwipeBackBaseActivity extends BaseActivity implements SwipeBackActivityBase {
    private SwipeBackActivityHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHelper = new SwipeBackActivityHelper(this);
        mHelper.onActivityCreate();
    }
    

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mHelper.onPostCreate();
    }

    @Override
    public View findViewById(int id) {
        View v = super.findViewById(id);
        if (v == null && mHelper != null)
            return mHelper.findViewById(id);
        return v;
    }

    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        return mHelper.getSwipeBackLayout();
    }

    @Override
    public void setSwipeBackEnable(boolean enable) {
        getSwipeBackLayout().setEnableGesture(enable);
    }

    @Override
    public void scrollToFinishActivity() {
        SwipeBackUtils.convertActivityToTranslucent(this);
        getSwipeBackLayout().scrollToFinishActivity();
    }
    
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        AndroidUtilities.exitWindowAnimation(SwipeBackBaseActivity.this);
    }
    
    protected boolean checkIfFileExists(String mFilePath){
    	try{
    		if(new File(mFilePath).exists()){
    			return true;	
    		}else{
    			return false;	
    		}
    	}catch(Exception e){
    		return false;	
    	}
    }
}

