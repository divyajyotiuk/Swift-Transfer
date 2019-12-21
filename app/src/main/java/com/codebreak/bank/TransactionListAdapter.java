package com.codebreak.bank;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.codebreak.bank.model.TxnList;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.Random;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TransactionListAdapter extends FirebaseRecyclerAdapter<TxnList, TransactionListAdapter.TransactionViewHolder> {


    private final int fontSize;
    private final int[] colors;
    private final Random rand;
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */

    int greenColor, redColor;
    public TransactionListAdapter(@NonNull FirebaseRecyclerOptions<TxnList> options, int fontSize, int[] colors) {
        super(options);
        this.fontSize = fontSize;
        this.colors = colors;
         rand = new Random();
        redColor = Color.parseColor("#d50000");
        greenColor = Color.parseColor("#00842c");
    }

    @Override
    protected void onBindViewHolder(@NonNull TransactionViewHolder transactionViewHolder, int i, @NonNull TxnList transaction) {
        transactionViewHolder.date.setText(String.format("%s %s", transaction.getDate(), transaction.getTime()));
        int rand_int1 = rand.nextInt(colors.length);
        String name, amount;

        if(transaction.isMoneyAdded())
        {
            name = transaction.getReceiverName();
            amount = String.format("+%s", transaction.getAmount());

            transactionViewHolder.amount.setTextColor(greenColor);

        }
        else
        {
            name = transaction.getSenderName();
            transactionViewHolder.amount.setTextColor(redColor);
            amount = String.format("-%s", transaction.getAmount());
        }
        transactionViewHolder.name.setText(name);
        transactionViewHolder.amount.setText(amount);
        TextDrawable drawable = TextDrawable.builder().beginConfig()
                .textColor(Color.WHITE)
                .useFont(Typeface.SANS_SERIF)
                .fontSize(Math.round(fontSize)) /* size in px */
                .toUpperCase()
                .endConfig()
                .buildRound(name.split(" ")[0].substring(0,1),colors[rand_int1]);
                transactionViewHolder.avatar.setImageDrawable(drawable);
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TransactionViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction, parent, false));
    }

     class TransactionViewHolder extends RecyclerView.ViewHolder
    {

        TextView name, amount, date;
        ImageView avatar;
        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.avatar);
            name = itemView.findViewById(R.id.name);
            date = itemView.findViewById(R.id.date);
            amount = itemView.findViewById(R.id.amount);
        }
    }
}
