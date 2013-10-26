package fr.masciulli.drinks.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import fr.masciulli.drinks.R;

public class DrinkDetailFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_drink_detail, container, false);

        TextView nameView = (TextView)root.findViewById(R.id.name);
        ImageView imageView = (ImageView)root.findViewById(R.id.image);
        TextView textView = (TextView)root.findViewById(R.id.text);

        Intent intent = getActivity().getIntent();
        String name = intent.getStringExtra("drink_name");
        String imageUrl = intent.getStringExtra("drink_imageurl");

        nameView.setText(name);
        Picasso.with(getActivity()).load(imageUrl).into(imageView);
        for (int i=0; i< 10; i++) {
            textView.setText(textView.getText() + getResources().getString(R.string.loremipsum) + "\n\n");
        }

        return root;
    }
}
