package com.software.tempe.mandirinews.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.software.tempe.mandirinews.NewsActivity;
import com.software.tempe.mandirinews.R;
import com.software.tempe.mandirinews.constant.Constant;
import com.software.tempe.mandirinews.model.Article;
import com.software.tempe.mandirinews.utility.DateUtility;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DataViewAdapter extends RecyclerView.Adapter<DataViewAdapter.ViewHolder> {

    private List<Article> articleArrayList;
    private Context context;
    private int recent_position = -1;

    public DataViewAdapter(List<Article> articleArrayList, Context context) {
        this.articleArrayList = articleArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_news, viewGroup, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, @SuppressLint("RecyclerView") int i) {
        String news_title = articleArrayList.get(i).getTitle();
        if (news_title.endsWith("- CNN"))    {
            news_title = news_title.replace("- CNN", "");
        } else if (news_title.endsWith(" - First")) {
            news_title = news_title.replace(" - First", "");
        }

        viewHolder.newsTitleTxtView.setText(news_title);

        try {
            Picasso.get()
                    .load(articleArrayList.get(i).getUrlToImage())
                    .centerCrop()
                    .resize(500, 400)
                    .error(R.mipmap.ic_launcher)
                    .into(viewHolder.imgNews);
        } catch (Exception e)   {
            System.out.println("message: " + e.getMessage());
        }


        viewHolder.newsPublishedAt.setText(" \u2022 " + DateUtility.DateFormat(articleArrayList.get(i).getPublishedAt()));

        viewHolder.newsLastSeenTxtView.setText(DateUtility.DateToTimeFormat(articleArrayList.get(i).getPublishedAt()));

        if (i > recent_position)    {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.item_animation_fall_down);
            viewHolder.news_item_card.startAnimation(animation);
            recent_position = i;
        }

    }



    @Override
    public int getItemCount() {
        return articleArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView newsTitleTxtView;
        private TextView newsLastSeenTxtView;
        private TextView newsPublishedAt;
        private ImageView imgNews;
        private CardView news_item_card;

        AssetManager assetManager = context.getApplicationContext().getAssets();
        Typeface airbnb_cereal_bold = Typeface.createFromAsset(assetManager, "font/airbnbcereal_bold.ttf");

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            newsTitleTxtView = itemView.findViewById(R.id.newsTitleTxtView);
            newsLastSeenTxtView = itemView.findViewById(R.id.newsLastSeenTxtView);
            newsPublishedAt = itemView.findViewById(R.id.newsPublishedAt);
            imgNews = itemView.findViewById(R.id.imgNews);
            news_item_card = itemView.findViewById(R.id.news_item_card);

            newsTitleTxtView.setTypeface(airbnb_cereal_bold);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            String headline = articleArrayList.get(getAdapterPosition()).getTitle();
            if (headline.endsWith(" - CNN")) {
                headline = headline.replace(" - CNN", "");
            } else if (headline.endsWith(" - First")) {
                headline = headline.replace(" - First", "");
            }

            String description = articleArrayList.get(getAdapterPosition()).getDescription();
            String date = articleArrayList.get(getAdapterPosition()).getPublishedAt();
            String imgURL = articleArrayList.get(getAdapterPosition()).getUrlToImage();
            String URL = articleArrayList.get(getAdapterPosition()).getUrl();

            Intent intent = new Intent(context, NewsActivity.class);

            intent.putExtra(Constant.INTENT_HEADLINE, headline);
            intent.putExtra(Constant.INTENT_DESCRIPTION, description);
            intent.putExtra(Constant.INTENT_DATE, date);
            intent.putExtra(Constant.INTENT_IMG_URL, imgURL);
            intent.putExtra(Constant.INTENT_ARTICLE_URL, URL);

            context.startActivity(intent);

            // ((Activity) mContext).overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        }
    }
}
