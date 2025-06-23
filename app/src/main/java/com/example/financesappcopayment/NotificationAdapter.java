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

public class NotificationAdapter extends ArrayAdapter<Notification> {
    public NotificationAdapter(Context context, List<Notification> notifications) {
        super(context, 0, notifications);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_notification, parent, false);
        }

        Notification notification = getItem(position);

        ImageView icon = convertView.findViewById(R.id.icon);
        TextView title = convertView.findViewById(R.id.title);
        TextView amount = convertView.findViewById(R.id.amount);
        TextView type = convertView.findViewById(R.id.type);

        // Заполняем данными
        icon.setImageResource(notification.getIconResId());
        title.setText(notification.getTitle());
        amount.setText(notification.getAmount());
        type.setText(notification.getType());

        return convertView;
    }
}
