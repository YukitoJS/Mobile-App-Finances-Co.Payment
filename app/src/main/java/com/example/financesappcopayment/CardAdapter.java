package com.example.financesappcopayment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import java.util.List;

public class CardAdapter extends ArrayAdapter<CardItem> {
    public CardAdapter(Context context, List<CardItem> cards) {
        super(context, 0, cards);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_card, parent, false);
        }

        CardItem card = getItem(position);

        TextView cardNumber = convertView.findViewById(R.id.cardNumberTv);
        TextView cardDate = convertView.findViewById(R.id.cardDateTv);
        TextView cardHolderName = convertView.findViewById(R.id.card_holder_nameTv);
        CardView cardView = (CardView) ((LinearLayout) convertView).getChildAt(0);

        // Установка данных
        cardNumber.setText(card.getCardNumber());
        cardDate.setText(card.getExpiryDate());
        cardHolderName.setText(card.getCardHolderName());
        cardView.setBackgroundResource(R.drawable.card_background);

        return convertView;
    }
}
