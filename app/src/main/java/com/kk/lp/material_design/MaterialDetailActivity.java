package com.kk.lp.material_design;

import android.animation.Animator;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kk.lp.BaseActivity;
import com.kk.lp.R;

/**
 * Created by lipeng on 2-19.
 */
public class MaterialDetailActivity extends BaseActivity {
    static String baconTitle = "Bacon";
    static String baconText = "Bacon ipsum dolor amet pork belly meatball kevin spare ribs. Frankfurter swine corned beef meatloaf, strip steak.";
    static String veggieTitle = "Veggie";
    static String veggieText = "Veggies es bonus vobis, proinde vos postulo essum magis kohlrabi welsh onion daikon amaranth tatsoi tomatillo melon azuki bean garlic.";
    MyBottomBehavior bsb;
    View bottom_sheet;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_fab);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        int dp = (int) Resources.getSystem().getDisplayMetrics().density;
        ((CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout)).setTitle(getString(R.string.app_name));
        RecyclerView rv = (RecyclerView) findViewById(R.id.recyclerview);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new RecyclerView.Adapter<ViewHolder>() {
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
                return new ViewHolder(getLayoutInflater().inflate(R.layout.list_item, parent, false));
            }

            @Override
            public void onBindViewHolder(ViewHolder viewHolder, int position) {
                viewHolder.text1.setText(baconTitle);
                viewHolder.text2.setText(baconText);
            }

            @Override
            public int getItemCount() {
                return 10;
            }
        });
        bottom_sheet = findViewById(R.id.bottom_sheet);
        bsb = MyBottomBehavior.from(bottom_sheet);
        bsb.setState(MyBottomBehavior.STATE_HIDDEN);
        bsb.setBottomSheetCallback(new MyBottomBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, @MyBottomBehavior.State int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        ItemClickSupport.addTo(rv).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(final RecyclerView recyclerView, int position, View v) {
                if (position % 2 == 0) {
                    if (bsb.getState() != MyBottomBehavior.STATE_EXPANDED) {
                        bsb.setState(MyBottomBehavior.STATE_EXPANDED);
                    } else {
                        bsb.setState(MyBottomBehavior.STATE_COLLAPSED);
                    }
                } else {
                    BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MaterialDetailActivity.this);
                    bottomSheetDialog.setTitle("bottomSheetDialog");
                    View view = LayoutInflater.from(MaterialDetailActivity.this).inflate(R.layout.popupwindow, null);
                    view.findViewById(R.id.btn_pw).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(MaterialDetailActivity.this, "yes", Toast.LENGTH_SHORT).show();
                        }
                    });
                    bottomSheetDialog.setContentView(view);
                    bottomSheetDialog.show();
                }
            }
        });

        LinearLayout bottom_sheet_content = (LinearLayout) findViewById(R.id.bottom_sheet_content);
        if (bottom_sheet_content != null){
            for (int i = 0; i < bottom_sheet_content.getChildCount(); i++) {
                bottom_sheet_content.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showSnackBar(((TextView)v).getText().toString());
                    }
                });
            }
        }



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
//                System.exit(0);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void showSnackBar(String message){
        Snackbar.make(bottom_sheet, message, Snackbar.LENGTH_SHORT).show();
    }

    private static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView text1;
        TextView text2;
        static int green;
        static int white;

        public ViewHolder(View itemView) {
            super(itemView);
            text1 = (TextView) itemView.findViewById(android.R.id.text1);
            text2 = (TextView) itemView.findViewById(android.R.id.text2);
            itemView.setOnClickListener(this);

            if (green == 0)
                green = itemView.getContext().getResources().getColor(R.color.green);
            if (white == 0)
                white = itemView.getContext().getResources().getColor(R.color.background_material_light);
        }

        @Override
        public void onClick(View view) {
            boolean isVeggie = false;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
                isVeggie = ((ColorDrawable) view.getBackground()) != null && ((ColorDrawable) view.getBackground()).getColor() == green;
            }

            int finalRadius = (int) Math.hypot(view.getWidth() / 2, view.getHeight() / 2);

            if (isVeggie) {
                text1.setText(baconTitle);
                text2.setText(baconText);
                view.setBackgroundColor(white);
            } else {
                Animator anim = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    anim = ViewAnimationUtils.createCircularReveal(view, (int) view.getWidth() / 2, (int) view.getHeight() / 2, 0, finalRadius);
                }
                text1.setText(veggieTitle);
                text2.setText(veggieText);
                view.setBackgroundColor(green);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    anim.start();
                }
            }
        }
    }

}
