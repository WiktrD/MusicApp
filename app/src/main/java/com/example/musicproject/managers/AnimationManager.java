package com.example.musicproject.managers;

import android.view.View;
import android.animation.Animator;

import com.airbnb.lottie.LottieAnimationView;

public class AnimationManager {
    public static void showLottieAnimation(final LottieAnimationView anim) {
        if (anim == null) return;
        anim.setVisibility(View.VISIBLE);
        anim.playAnimation();
        anim.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                anim.setVisibility(View.GONE);
            }

            @Override public void onAnimationStart(Animator animation) {}
            @Override public void onAnimationCancel(Animator animation) {}
            @Override public void onAnimationRepeat(Animator animation) {}
        });
    }
}
