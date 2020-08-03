package com.example.newsapp1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PostAdapter extends ArrayAdapter<Post> {

    public PostAdapter(Context context, List<Post> posts) {
        super(context, 0, posts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        Post currentPost = getItem(position);

        TextView sectionNameView = (TextView) listItemView.findViewById(R.id.pillar_name);
        assert currentPost != null;
        sectionNameView.setText(currentPost.getSectionName());

        TextView dateView = (TextView) listItemView.findViewById(R.id.date);
        SimpleDateFormat defaultDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        SimpleDateFormat formattedDate = new SimpleDateFormat("dd MMMM yyyy HH:mm");
        try {
            Date myDate = defaultDateFormat.parse(currentPost.getDate());
            dateView.setText(formattedDate.format(myDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        TextView author = (TextView) listItemView.findViewById(R.id.author);
        if (currentPost.getAuthorFirstName() == null && currentPost.getAuthorLastName() == null){
            author.setVisibility(View.GONE);
        } else if (currentPost.getAuthorFirstName() == null){
            author.setText("By: " + currentPost.getAuthorLastName());
        } else if (currentPost.getAuthorLastName() == null){
            author.setText("By: " + currentPost.getAuthorFirstName());
        } else {
            author.setText("By: " + currentPost.getAuthorFirstName() + " " + currentPost.getAuthorLastName());
        }

        TextView titleNameView = (TextView) listItemView.findViewById(R.id.title);
        titleNameView.setText(currentPost.getTitle());

        return listItemView;
    }
}

