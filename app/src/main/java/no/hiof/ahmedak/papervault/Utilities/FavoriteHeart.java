package no.hiof.ahmedak.papervault.Utilities;

import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

/**
 * Handle toggle on and off for Favorite Heart refrence : https://www.youtube.com/watch?v=HD4RfeQFS_E&list=PLgCYzUzKIBE9XqkckEJJA0I1wVKbUAOdv&index=73
 */
public class FavoriteHeart {
    private static final String TAG = "FavoriteHeart";
    private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();
    private ScaleAnimation scaleAnimation;

    public ToggleButton Favoritebtn;



    public FavoriteHeart(ToggleButton favoriteBtn) {
       this.Favoritebtn = favoriteBtn;
    }

    public void ToggleHeart(){


        // add Animation to Toggle button
        scaleAnimation = new ScaleAnimation(0.7f, 1.0f, 0.7f, 1.0f, Animation.RELATIVE_TO_SELF, 0.7f, Animation.RELATIVE_TO_SELF, 0.7f);
        scaleAnimation.setDuration(300);
        scaleAnimation.setInterpolator(ACCELERATE_INTERPOLATOR);
        Favoritebtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                buttonView.startAnimation(scaleAnimation);
            }
        });
    }

}
