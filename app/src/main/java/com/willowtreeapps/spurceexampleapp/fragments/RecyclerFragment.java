/*
 *     Spruce
 *
 *     Copyright (c) 2017 WillowTree, Inc.
 *     Permission is hereby granted, free of charge, to any person obtaining a copy
 *     of this software and associated documentation files (the "Software"), to deal
 *     in the Software without restriction, including without limitation the rights
 *     to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *     copies of the Software, and to permit persons to whom the Software is
 *     furnished to do so, subject to the following conditions:
 *     The above copyright notice and this permission notice shall be included in
 *     all copies or substantial portions of the Software.
 *     THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *     IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *     FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *     AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *     LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *     OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *     THE SOFTWARE.
 *
 */

package com.willowtreeapps.spurceexampleapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.willowtreeapps.spruce.Spruce;
import com.willowtreeapps.spruce.SpruceAnimator;
import com.willowtreeapps.spruce.animation.DefaultAnimations;
import com.willowtreeapps.spruce.sort.DefaultSort;
import com.willowtreeapps.spurceexampleapp.R;
import com.willowtreeapps.spurceexampleapp.model.ExampleData;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.willowtreeapps.spruce.exclusion.ExclusionHelper.R_L_MODE;


public class RecyclerFragment extends Fragment {

    private RecyclerView recyclerView;
    private CheckBox excludeView;
    private SpruceAnimator spruceAnimator;

    public static RecyclerFragment newInstance() {
        return new RecyclerFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater,
                             ViewGroup container, @Nullable Bundle savedInstanceState) {
        recyclerView = container.findViewById(R.id.recycler);
        excludeView = container.findViewById(R.id.view_exclusion);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext()) {
            @Override
            public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
                super.onLayoutChildren(recycler, state);
                initSpruce();
            }
        };

        // Mock data objects
        List<ExampleData> placeHolderList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            placeHolderList.add(new ExampleData());
        }

        recyclerView.setAdapter(new RecyclerAdapter(placeHolderList));
        recyclerView.setLayoutManager(linearLayoutManager);

        return inflater.inflate(R.layout.recycler_fragment, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (spruceAnimator != null) {
            spruceAnimator.start();
        }
    }

    private void initSpruce() {
        spruceAnimator = new Spruce.SpruceBuilder(recyclerView)
                .sortWith(new DefaultSort(100))
                .excludeViews(getExcludedViews(), R_L_MODE)
                .animateWith(DefaultAnimations.dynamicFadeIn(recyclerView),
                        DefaultAnimations.dynamicTranslationUpwards(recyclerView))
                .start();
    }

    /**
     * getExcludedViews method gives the positions to be excluded.
     *
     * @return position list.
     */
    private List<Integer> getExcludedViews() {
        List<Integer> positions = new ArrayList<>();
        if (excludeView.isChecked()) {
            positions.add(1);
            positions.add(4);
            positions.add(7);
        }
        return positions;
    }

    private class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

        List<ExampleData> placeholderList;

        RecyclerAdapter(List<ExampleData> placeholderList) {
            this.placeholderList = placeholderList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RelativeLayout view = (RelativeLayout) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.view_placeholder, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return placeholderList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            RelativeLayout placeholderView;

            ViewHolder(View itemView) {
                super(itemView);
                placeholderView = itemView.findViewById(R.id.placeholder_view);
                placeholderView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                initSpruce();
            }
        }

    }
}
