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

public class TransactionAdapter extends ArrayAdapter<Transaction> {

    public TransactionAdapter(Context context, List<Transaction> transactions) {
        super(context, 0, transactions);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_transaction, parent, false);
        }

        Transaction transaction = getItem(position);

        ImageView icon = convertView.findViewById(R.id.icon);
        TextView title = convertView.findViewById(R.id.title);
        TextView amount = convertView.findViewById(R.id.amount);
        TextView type = convertView.findViewById(R.id.type);

        // Заполняем данными
        icon.setImageResource(transaction.getIconResId());
        title.setText(transaction.getTitle());
        amount.setText(transaction.getAmount());
        type.setText(transaction.getType());

        // Цвет суммы в зависимости от типа транзакции
        if (transaction.getAmount().startsWith("-")) {
            amount.setTextColor(Color.parseColor("#1D3A70"));
        } else {
            amount.setTextColor(Color.parseColor("#1DAB87"));
        }

        return convertView;
    }
}
