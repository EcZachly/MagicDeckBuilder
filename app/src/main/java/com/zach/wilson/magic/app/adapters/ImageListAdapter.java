package com.zach.wilson.magic.app.adapters;

import java.util.ArrayList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.zach.wilson.magic.app.R;
import com.zach.wilson.magic.app.models.Card;
import com.zach.wilson.magic.app.models.Edition;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageListAdapter extends BaseAdapter{

	DisplayImageOptions options;
	String[] imageUrls;
	ImageLoader imageLoader;
	LayoutInflater inflater;
	
	
	
	
	private ArrayList<Edition> cardEditions;
	private Card card;
	
	public Card getCard() {
		return card;
	}

	public void setCard(Card card) {
		this.card = card;
	}

	public ArrayList<Edition> getCardEditions() {
		return cardEditions;
	}

	public void setCardEditions(ArrayList<Edition> cardEditions) {
		this.cardEditions = cardEditions;
	}

	public ImageListAdapter(LayoutInflater inflater, Card card, DisplayImageOptions options){		
		this.inflater = inflater;
		this.options = options;
		this.card = card;	
		cardEditions = new ArrayList<Edition>();
		for(int i = 0; i < card.getEditions().length; i++){
			Edition e = card.getEditions()[i];
		
			
		if(e.getSet().startsWith("Vintage") || e.getSet().startsWith("Masters") || e.getSet().startsWith("Duels of the Planeswalkers")){
			//Log.i("SETV", e.getSet());
		}
		else{
		    if(!e.getImage_url().substring(0, (e.getImage_url().length() - 4)).endsWith("/0")){
                cardEditions.add(e);
            }

		}

		


		}
		imageLoader = ImageLoader.getInstance();
	}
	
	@Override
	public int getCount() {
		return cardEditions.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;
		final ViewHolder holder;
		if (convertView == null) {
			view = inflater.inflate(R.layout.imagelayout, parent, false);
			holder = new ViewHolder();
			holder.image = (ImageView) view.findViewById(R.id.image);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		imageLoader.displayImage(cardEditions.get(position).getImage_url(), holder.image, options, new SimpleImageLoadingListener() {
			@Override
			public void onLoadingStarted(String imageUri, View view) {
				
			}

			@Override
			public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
				String message = null;
				switch (failReason.getType()) {
					case IO_ERROR:
						message = "Input/Output error";
						break;
					case DECODING_ERROR:
						message = "Image can't be decoded";
						break;
					case NETWORK_DENIED:
						message = "Downloads are denied";
						break;
					case OUT_OF_MEMORY:
						message = "Out Of Memory error";
						break;
					case UNKNOWN:
						message = "Unknown error";
						break;
				}
			}
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			}
		});
		return view;
	}
	
	private static class ViewHolder {
		ImageView image;
	}
}




