package com.frenchfriedtechnology.horseandriderscompanion.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;


import com.frenchfriedtechnology.horseandriderscompanion.R;

import timber.log.Timber;

public class CircularAnimUtil {

    public static final long DURATION_MILLIS = 400;
    public static final int MIN_RADIUS = 0;
    private static final int FINISH_NONE = 0, FINISH_SINGLE = 1, FINISH_ALL = 3;

    @SuppressLint("NewApi")
    private static void actionVisible(boolean isShown, final View targetView, float minRadius, long durationMills) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
            if (isShown)
                targetView.setVisibility(View.VISIBLE);
            else
                targetView.setVisibility(View.INVISIBLE);
            return;
        }
        int cx = (targetView.getLeft() + targetView.getRight()) / 2;
        int cy = (targetView.getTop() + targetView.getBottom()) / 2;

        int w = targetView.getWidth();
        int h = targetView.getHeight();

        int maxRadius = (int) Math.sqrt(w * w + h * h) + 1;

        float startRadius, endRadius;
        if (isShown) {
            startRadius = minRadius;
            endRadius = maxRadius;
        } else {
            startRadius = maxRadius;
            endRadius = minRadius;
        }

        Animator anim =
                ViewAnimationUtils.createCircularReveal(targetView, cx, cy, startRadius, endRadius);
        targetView.setVisibility(View.VISIBLE);
        anim.setDuration(durationMills);

        if (!isShown)
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    targetView.setVisibility(View.INVISIBLE);
                }
            });

        anim.start();
    }

    @SuppressLint("NewApi")
    private static void actionOtherVisible(boolean isShow, final View triggerView, final View animView,
                                           float miniRadius, long durationMills) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
            if (isShow)
                animView.setVisibility(View.VISIBLE);
            else
                animView.setVisibility(View.INVISIBLE);
            return;
        }

        int[] tvLocation = new int[2];
        triggerView.getLocationInWindow(tvLocation);
        final int tvCX = tvLocation[0] + triggerView.getWidth() / 2;
        final int tvCY = tvLocation[1] + triggerView.getHeight() / 2;

        int[] avLocation = new int[2];
        animView.getLocationInWindow(avLocation);
        final int avLX = avLocation[0];
        final int avTY = avLocation[1];

        int triggerX = Math.max(avLX, tvCX);
        triggerX = Math.min(triggerX, avLX + animView.getWidth());

        int triggerY = Math.max(avTY, tvCY);
        triggerY = Math.min(triggerY, avTY + animView.getHeight());


        int avW = animView.getWidth();
        int avH = animView.getHeight();

        int rippleCX = triggerX - avLX;
        int rippleCY = triggerY - avTY;

        int maxW = Math.max(rippleCX, avW - rippleCX);
        int maxH = Math.max(rippleCY, avH - rippleCY);
        final int maxRadius = (int) Math.sqrt(maxW * maxW + maxH * maxH) + 1;

        float startRadius, endRadius;
        if (isShow) {
            startRadius = miniRadius;
            endRadius = maxRadius;
        } else {
            startRadius = maxRadius;
            endRadius = miniRadius;
        }

        Animator anim = ViewAnimationUtils.createCircularReveal(
                animView, rippleCX, rippleCY, startRadius, endRadius);
        animView.setVisibility(View.VISIBLE);
        anim.setDuration(durationMills);

        if (!isShow)
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    animView.setVisibility(View.INVISIBLE);
                }
            });

        anim.start();
    }


    private static void startActivityOrFinish(final int finishType, final Activity thisActivity,
                                              final Intent intent, final Integer requestCode,
                                              final Bundle bundle) {
        if (requestCode == null)
            thisActivity.startActivity(intent);
        else if (bundle == null)
            thisActivity.startActivityForResult(intent, requestCode);
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            thisActivity.startActivityForResult(intent, requestCode, bundle);
        } else
            thisActivity.startActivityForResult(intent, requestCode);

        switch (finishType) {
            case FINISH_SINGLE:
                thisActivity.finish();
                break;
            case FINISH_ALL:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    thisActivity.finishAffinity();
                } else
                    thisActivity.finish();
                break;
        }
    }

    @SuppressLint("NewApi")
    private static void actionStartActivity(
            final int finishType, final Activity thisActivity, final Intent intent,
            final Integer requestCode, final Bundle bundle, final View triggerView,
            int colorOrImageRes, long durationMills) {

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
            startActivityOrFinish(finishType, thisActivity, intent, requestCode, bundle);
            return;
        }

        int[] location = new int[2];
        triggerView.getLocationInWindow(location);
        final int cx = location[0] + triggerView.getWidth() / 2;
        final int cy = location[1] + triggerView.getHeight() / 2;
        final ImageView view = new ImageView(thisActivity);
        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        view.setImageResource(colorOrImageRes);
        final ViewGroup decorView = (ViewGroup) thisActivity.getWindow().getDecorView();
        int w = decorView.getWidth();
        int h = decorView.getHeight();
        decorView.addView(view, w, h);

        int maxW = Math.max(cx, w - cx);
        int maxH = Math.max(cy, h - cy);
        final int finalRadius = (int) Math.sqrt(maxW * maxW + maxH * maxH) + 1;

        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);
        int maxRadius = (int) Math.sqrt(w * w + h * h) + 1;
        if (durationMills == DURATION_MILLIS) {
            double rate = 1d * finalRadius / maxRadius;
            durationMills = (long) (DURATION_MILLIS * Math.sqrt(rate));
        }
        final long finalDuration = durationMills;
        anim.setDuration(finalDuration);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                if (requestCode == null)
                    thisActivity.startActivity(intent);
                else if (bundle == null)
                    thisActivity.startActivityForResult(intent, requestCode);
                else
                    thisActivity.startActivityForResult(intent, requestCode, bundle);

                thisActivity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                switch (finishType) {
                    case FINISH_NONE:
                        triggerView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Animator anim =
                                        ViewAnimationUtils.createCircularReveal(view, cx, cy, finalRadius, 0);
                                anim.setDuration(finalDuration);
                                anim.addListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);
                                        try {
                                            decorView.removeView(view);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                anim.start();
                            }
                        }, 1000);
                        break;
                    case FINISH_SINGLE:
                        thisActivity.finish();
                        break;
                    case FINISH_ALL:
                        thisActivity.finishAffinity();
                        break;
                }

            }
        });
        anim.start();
    }


    public static void show(View myView, float startRadius, long durationMills) {
        actionVisible(true, myView, startRadius, durationMills);
    }

    public static void hide(final View myView, float endRadius, long durationMills) {
        actionVisible(false, myView, endRadius, durationMills);
    }

    public static void startActivityForResult(
            final Activity thisActivity, final Intent intent, final Integer requestCode, final Bundle bundle,
            final View triggerView, int colorOrImageRes, long durationMills) {

        actionStartActivity(FINISH_NONE, thisActivity, intent, requestCode, bundle, triggerView, colorOrImageRes, durationMills);
    }

    public static void startActivityThenFinish(
            final Activity thisActivity, final Intent intent, final boolean isFinishAffinity, final View triggerView,
            int colorOrImageRes, long durationMills) {
        int finishType = isFinishAffinity ? FINISH_ALL : FINISH_SINGLE;
        actionStartActivity(finishType, thisActivity, intent, null, null, triggerView, colorOrImageRes, durationMills);
    }

    public static void startActivityForResult(
            Activity thisActivity, Intent intent, Integer requestCode, View triggerView, int colorOrImageRes) {
        startActivityForResult(thisActivity, intent, requestCode, null, triggerView, colorOrImageRes, DURATION_MILLIS);
    }

    public static void startActivity(
            Activity thisActivity, Intent intent, View triggerView, int colorOrImageRes, long durationMills) {
        startActivityForResult(thisActivity, intent, null, null, triggerView, colorOrImageRes, durationMills);
    }

    public static void startActivity(
            Activity thisActivity, Intent intent, View triggerView, int colorOrImageRes) {
        startActivity(thisActivity, intent, triggerView, colorOrImageRes, DURATION_MILLIS);
    }

    public static void startActivity(Activity thisActivity, Class<?> targetClass, View triggerView, int colorOrImageRes) {
        startActivity(thisActivity, new Intent(thisActivity, targetClass), triggerView, colorOrImageRes, DURATION_MILLIS);
    }

    public static void startActivityThenFinish(Activity thisActivity, Intent intent, View triggerView, int colorOrImageRes) {
        startActivityThenFinish(thisActivity, intent, false, triggerView, colorOrImageRes, DURATION_MILLIS);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void enterReveal(View view) {
        int cx = view.getMeasuredWidth() / 2;
        int cy = view.getMeasuredHeight() / 2;

        // get the final radius for the clipping circle
        int finalRadius = Math.max(view.getWidth(), view.getHeight()) / 2;

        // create the animator for this view (the start radius is zero)
        Animator anim =
                ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);
        anim.setInterpolator(new AccelerateInterpolator());
        anim.setDuration(200);
        // make the view visible and start the animation
        view.setVisibility(View.VISIBLE);
        anim.start();
    }

    public static void exitReveal(final View view) {
        Timber.d(" exitReveal() ");

        // get the center for the clipping circle
        int cx = view.getWidth() / 2;
        int cy = view.getHeight() / 2;

// get the initial radius for the clipping circle
        float initialRadius = (float) Math.hypot(cx, cy);

// create the animation (the final radius is zero)
        Animator anim =
                ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius, 0);
        anim.setInterpolator(new AccelerateInterpolator());
        anim.setDuration(200);

// make the view invisible when the animation is done
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setVisibility(View.INVISIBLE);
            }
        });

// start the animation
        anim.start();
    }

    public static void switchReveal(View hideView, final View showView) {
        exitReveal(hideView);
        showView.postDelayed(new Runnable() {
            @Override
            public void run() {
                enterReveal(showView);
            }
        }, 200);
    }

    public static void dialogReveal(final View view, boolean reveal, final AlertDialog dialog) {
        int w = view.getWidth();
        int h = view.getHeight();
        float maxRadius = (float) Math.sqrt(w * w / 4 + h * h / 4);

        if (reveal) {
            Animator revealAnimator = ViewAnimationUtils.createCircularReveal(view,
                    w / 2, h / 2, 0, maxRadius);

            view.setVisibility(View.VISIBLE);
            revealAnimator.start();

        } else {

            Animator anim =
                    ViewAnimationUtils.createCircularReveal(view, w / 2, h / 2, maxRadius, 0);

            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    dialog.dismiss();
                    view.setVisibility(View.INVISIBLE);

                }
            });

            anim.start();
        }
    }


    public static void show(View myView) {
        show(myView, MIN_RADIUS, DURATION_MILLIS);
    }

    public static void hide(View myView) {
        hide(myView, MIN_RADIUS, DURATION_MILLIS);
    }

    public static void showOther(View triggerView, View otherView) {
        actionOtherVisible(true, triggerView, otherView, MIN_RADIUS, DURATION_MILLIS);
    }

    public static void hideOther(View triggerView, View otherView) {
        actionOtherVisible(false, triggerView, otherView, MIN_RADIUS, DURATION_MILLIS);
    }

    /**
     * Animation that pulses selected Button then does a circular reveal before switching Activities
     */
    private void pulseAnimation(final View view, final Class<?> newClass, final Intent intent, final View root, Activity activity) {

        hideOther(view, root);

        ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(view,
                PropertyValuesHolder.ofFloat("scaleX", 1.2f),
                PropertyValuesHolder.ofFloat("scaleY", 1.2f));
        scaleDown.setDuration(200);
        scaleDown.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (intent != null) {
                    startActivity(activity, intent, view, R.color.colorAccent);
                } else {
                    startActivity(activity, newClass, view, R.color.colorAccent);
                }
            }
        });

        scaleDown.setInterpolator(new FastOutSlowInInterpolator());
        scaleDown.setRepeatCount(1);
        scaleDown.setRepeatMode(ObjectAnimator.REVERSE);
        scaleDown.start();

    }
}
