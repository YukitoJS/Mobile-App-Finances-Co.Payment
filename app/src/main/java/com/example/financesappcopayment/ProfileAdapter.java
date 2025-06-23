package com.example.financesappcopayment;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ProfileAdapter extends ArrayAdapter<Profile> {
    public ProfileAdapter(Context context, List<Profile> profiles) {
        super(context, 0, profiles);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_profile, parent, false);
        }

        Profile profile = getItem(position);

        ImageView icon = convertView.findViewById(R.id.icon);
        TextView title = convertView.findViewById(R.id.title);
        ImageView btn = convertView.findViewById(R.id.btn);

        // Заполняем данными
        icon.setImageResource(profile.getIconResId());
        title.setText(profile.getTitle());
        btn.setImageResource(profile.getIconId());

        return convertView;
    }
}
