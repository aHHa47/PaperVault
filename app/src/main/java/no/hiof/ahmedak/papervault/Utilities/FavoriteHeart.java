package no.hiof.ahmedak.papervault.Utilities;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

/**
 * Handle toggle on and off for Favorite Heart refrence : https://www.youtube.com/watch?v=HD4RfeQFS_E&list=PLgCYzUzKIBE9XqkckEJJA0I1wVKbUAOdv&index=73
 */
public class FavoriteHeart {
    private static final String TAG = "FavoriteHeart";
    private static final DecelerateInterpolator DECELERATE_INTERPOLATOR = new DecelerateInterpolator();
    private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();

    public ImageView whiteHeart, orangeHeart;



    public FavoriteHeart(ImageView whiteHeart, ImageView orangeHeart) {
        this.whiteHeart = whiteHeart;
        this.orangeHeart = orangeHeart;
    }

    public void ToggleHeart(){
        Log.d(TAG, "ToggleHeart: Toggling Heart");

        AnimatorSet animatorSet = new AnimatorSet();

        if(orangeHeart.getVisibility() == View.VISIBLE){
            Log.d(TAG, "ToggleHeart: Toggle off ");
            orangeHeart.setScaleX(0.1f);
            orangeHeart.setScaleY(0.1f);

            ObjectAnimator objectAnimatorY = ObjectAnimator.ofFloat(orangeHeart,"scaleY",1f,0f);
            objectAnimatorY.setDuration(300);
            objectAnimatorY.setInterpolator(ACCELERATE_INTERPOLATOR);
            ObjectAnimator objectAnimatorX = ObjectAnimator.ofFloat(orangeHeart,"scaleX",1f,0f);
            objectAnimatorX.setDuration(300);
            objectAnimatorX.setInterpolator(ACCELERATE_INTERPOLATOR);

            orangeHeart.setVisibility(View.GONE);
            whiteHeart.setVisibility(View.VISIBLE);

            animatorSet.playTogether(objectAnimatorY,objectAnimatorX);

        }
       else if(orangeHeart.getVisibility() == View.GONE){
            Log.d(TAG, "ToggleHeart: Toggle on ");
            orangeHeart.setScaleX(0.1f);
            orangeHeart.setScaleY(0.1f);

            ObjectAnimator objectAnimatorY = ObjectAnimator.ofFloat(orangeHeart,"scaleY",0.1f,1f);
            objectAnimatorY.setDuration(300);
            objectAnimatorY.setInterpolator(DECELERATE_INTERPOLATOR);
            ObjectAnimator objectAnimatorX = ObjectAnimator.ofFloat(orangeHeart,"scaleX",0.1f,1f);
            objectAnimatorX.setDuration(300);
            objectAnimatorX.setInterpolator(DECELERATE_INTERPOLATOR);

            orangeHeart.setVisibility(View.VISIBLE);
            whiteHeart.setVisibility(View.GONE);

            animatorSet.playTogether(objectAnimatorY,objectAnimatorX);

        }

        animatorSet.start();
    }

}
