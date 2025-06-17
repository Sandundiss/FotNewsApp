package com.example.fotnewsapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import java.util.Arrays;
import java.util.List;

public class AcademicFragment extends Fragment {
    private ViewPager2 viewPager;
    private LinearLayout dotsLayout;
    private final List<Integer> slideImages = Arrays.asList(
            R.drawable.academic0,
            R.drawable.academic1,
            R.drawable.academic2,
            R.drawable.academic3
    );


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_academic, container, false);

        viewPager = view.findViewById(R.id.slideshowViewPager);
        dotsLayout = view.findViewById(R.id.dotsLayout);

        setupSlideshow();

        return view;
    }

    private void setupSlideshow() {
        viewPager.setAdapter(new SlideAdapter(slideImages));


        setupDotsIndicator();


        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                int currentPosition = viewPager.getCurrentItem();
                if (currentPosition == slideImages.size() - 1) {
                    viewPager.setCurrentItem(0);
                } else {
                    viewPager.setCurrentItem(currentPosition + 1);
                }
                boolean b = handler.postDelayed(this, 3000);// Change slide every 3 seconds
            }
        };
        handler.postDelayed(runnable, 3000);

        // Clean up when view is destroyed
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                updateDotsIndicator(position);
            }
        });
    }

    private void setupDotsIndicator() {
        ImageView[] dots = new ImageView[slideImages.size()];
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new ImageView(getContext());
            dots[i].setImageDrawable(ContextCompat.getDrawable(requireContext(),
                    R.drawable.dot_unselected));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(8, 0, 8, 0);
            dotsLayout.addView(dots[i], params);
        }
        updateDotsIndicator(0);
    }

    private void updateDotsIndicator(int position) {
        int childCount = dotsLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView imageView = (ImageView) dotsLayout.getChildAt(i);
            if (i == position) {
                imageView.setImageDrawable(ContextCompat.getDrawable(requireContext(),
                        R.drawable.dot_selected));
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(requireContext(),
                        R.drawable.dot_unselected));
            }
        }
    }
}