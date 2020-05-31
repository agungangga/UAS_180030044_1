package com.aa183.AnggaPratama;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class ListHolder extends RecyclerView.ViewHolder{

    public TextView tvNm, tvIbu, tvBhs, tvMataUang;
    public CardView cvList;
    public ImageView mListUpload;

    public ListHolder(View itemView){
        super(itemView);
        cvList = itemView.findViewById(R.id.cvList);
        tvNm = itemView.findViewById(R.id.tvNm);
        tvIbu = itemView.findViewById(R.id.tvIbu);
        tvBhs = itemView.findViewById(R.id.tvBhs);
        tvMataUang = itemView.findViewById(R.id.tvMataUang);
        mListUpload = itemView.findViewById(R.id.mListUpload);
    }

}
