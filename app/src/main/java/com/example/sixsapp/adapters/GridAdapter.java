package com.example.sixsapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sixsapp.R;
import com.example.sixsapp.activities.QuestionnaireActivity;
import com.example.sixsapp.pojo.Category;
import com.example.sixsapp.utilis.Global;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.GridHolder> {
    public Context context;
    public Activity MainActivity;
    private List<Category> categories;
    private static final int REQUEST_CODE_REFRESH_ACTIVITY = 1;
    public GridAdapter(Context context, List<Category> categories, Activity activity) {
        this.context = context;
        this.categories = categories;
        this.MainActivity = activity;
    }

    @NonNull
    @Override
    public GridHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_menu, parent, false);
        return new GridHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GridHolder holder, int position) {
        Category category = categories.get(position);
        String categoryName = category.getCategoryName();
        holder.textView.setText(categoryName);

        int colorValue = android.graphics.Color.parseColor(category.getCategoryColor());
        holder.card.getBackground().setTint(colorValue);

        if(category.isDisable()){
            holder.card.setEnabled(false);
            holder.mLockIcon.setVisibility(View.VISIBLE);
            holder.mChip.setVisibility(View.VISIBLE);
        }

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Global.SelectedCategory = category;
                Intent intent = new Intent(context, QuestionnaireActivity.class);
                MainActivity.startActivityForResult(intent, REQUEST_CODE_REFRESH_ACTIVITY);
                //context.startActivity(new Intent(context, QuestionnaireActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    static class GridHolder extends RecyclerView.ViewHolder {
        TextView textView;
        MaterialCardView card;
        ImageView mLockIcon;
        LinearLayout mChip;
        GridHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.title_textView);
            card = itemView.findViewById(R.id.card);
            mLockIcon = itemView.findViewById(R.id.lock_icon);
            mChip = itemView.findViewById(R.id.chip);
        }
    }
}
