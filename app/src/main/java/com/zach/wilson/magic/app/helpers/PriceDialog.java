package com.zach.wilson.magic.app.helpers;

import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.zach.wilson.magic.app.R;
import com.zach.wilson.magic.app.models.Card;
import com.zach.wilson.magic.app.models.CardList;
import com.zach.wilson.magic.app.models.Products;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;



public class PriceDialog{
    Card card;
    Dialog deckDialog;
    Context context;
    Products product;
    Dialog priceDialog;
    int currentEdition;
    TextView foil, high, mean, low;
    ImageView share,addToCart, addToDeck, cardInfo;
    LinearLayout layout1, layout2;

    public PriceDialog(Context c, Card card, int currentEdition, Products product){
        this.context = c;
        this.card = card;
        this.product = product;
        this.currentEdition = currentEdition;
        priceDialog = new Dialog(context);
        deckDialog = new Dialog(context);

    }



    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public void showDialog() {
        priceDialog.setContentView(R.layout.deckdialogicons);
        priceDialog.setCanceledOnTouchOutside(true);
        priceDialog.setTitle("Pricing/Options");
        TableLayout t = (TableLayout) priceDialog.findViewById(R.id.pricelayout);
        foil = (TextView) t.findViewById(R.id.foilPrice);
        high = (TextView) t.findViewById(R.id.highPrice);
        mean = (TextView) t.findViewById(R.id.meanPrice);
        low = (TextView) t.findViewById(R.id.lowPrice);
        layout1 = (LinearLayout) priceDialog
                .findViewById(R.id.layout1);
        layout2 = (LinearLayout) priceDialog
                .findViewById(R.id.layout2);
        share = (ImageView) layout1.findViewById(R.id.shareCardAsText);
        addToCart = (ImageView) layout1.findViewById(R.id.addCardToCart);
        addToDeck = (ImageView) layout2.findViewById(R.id.addCardToDeck);
        cardInfo = (ImageView) layout2.findViewById(R.id.cardInfo);


        share.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(
                        Intent.EXTRA_TEXT,
                        "Hey check out: "
                                + card.getName()
                                + "\n"
                                + card.getEditions()[currentEdition].getImage_url()
                                + "\n\n Shared By: Magic Card Viewer on Google Play at:\n"
                                + CardList.urlToAPP
                );
                context.startActivity(
                        Intent.createChooser(intent, "Share"));
            }

        });

        addToDeck.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SharedPreferences prefs = context
                        .getSharedPreferences("DECKSNEW",
                                Context.MODE_PRIVATE);
                Set<String> keys = prefs.getAll().keySet();
                Object[] tempArr = keys.toArray();
                final ArrayList<String> deckNames = new ArrayList<String>();
                String[] keyArr = new String[tempArr.length];
                for (int i = 0; i < tempArr.length; i++) {
                    keyArr[i] = tempArr[i].toString();
                }

                for (int i = 0; i < keyArr.length; i++) {
                    if (keyArr[i].contains("#")) {
                        deckNames.add(prefs.getAll().get(keyArr[i])
                                .toString());
                    }
                }

                Log.i("DECKNAMES", String.valueOf(deckNames.size()));
                deckDialog.setTitle("Choose deck to add to");
                deckDialog.setContentView(R.layout.listview);
                ListView view = (ListView) deckDialog
                        .findViewById(R.id.listing);
                view.setClickable(true);
                deckNames.add("Create a new Deck");
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        context, android.R.layout.simple_list_item_activated_1,
                        deckNames);
                view.setAdapter(adapter);
                view.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1,
                                            int arg2, long arg3) {

                        if (arg2 == deckNames.size() - 1) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(context);

                            alert.setTitle("Creating a new deck!");
                            alert.setMessage("Enter the name of your new deck: ");
                            final EditText input = new EditText(context);
                            alert.setView(input);

                            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    if (input.getText().length() > 0) {
                                        SharedPreferences prefs = context
                                                .getSharedPreferences("DECKSNEW",
                                                        Context.MODE_PRIVATE);
                                            prefs.edit().putString(input.getText().toString() + "#", input.getText().toString()).commit();
                                            prefs.edit().putInt(input.getText().toString() + "SIZE", 1);
                                        String data;
                                        data = card.getName() + "(+)" + card.getId() + "(-)" + card.getEditions()[0].getImage_url();
                                        prefs.edit().putString(input.getText().toString() + "" + 0, data)
                                                .apply();

                                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                                context);

                                        // set title
                                        alertDialogBuilder.setTitle("Success!");
                                        alertDialogBuilder
                                                .setMessage(
                                                        card.getName()
                                                                + " was successfully added to deck\n"
                                                                + input.getText().toString()
                                                )
                                                .setCancelable(false)
                                                .setPositiveButton(
                                                        "OK",
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(
                                                                    DialogInterface dialog,
                                                                    int id) {
                                                                priceDialog.cancel();
                                                                deckDialog.cancel();
                                                                dialog.cancel();
                                                            }
                                                        }
                                                );

                                        // create alert dialog
                                        AlertDialog alertDialog = alertDialogBuilder
                                                .create();

                                        // show it
                                        alertDialog.show();

                                    }
                                }



                            });

                            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    deckDialog.dismiss();
                                    priceDialog.dismiss();
                                }
                            });

                            alert.show();

                        } else {

                            SharedPreferences prefs = context
                                    .getSharedPreferences("DECKSNEW",
                                            Context.MODE_PRIVATE);
                            Set<String> keys = prefs.getAll().keySet();
                            String deck = deckNames.get(arg2);
                            int s = prefs.getInt(deck + "SIZE", 0);
                            s++;
                            String data;
                            data = card.getName() + "(+)" + card.getId() + "(-)" + card.getEditions()[0].getImage_url();
                            prefs.edit().remove(deck + "SIZE").apply();
                            prefs.edit().putInt(deck + "SIZE", s).apply();
                            s--;
                            prefs.edit().putString(deck + "" + s, data)
                                    .apply();
                            deckDialog.dismiss();
                            priceDialog.dismiss();
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                    context);

                            // set title
                            alertDialogBuilder.setTitle("Success!");
                            alertDialogBuilder
                                    .setMessage(
                                            card.getName()
                                                    + " was successfully added to deck\n"
                                                    + deck
                                    )
                                    .setCancelable(false)
                                    .setPositiveButton(
                                            "OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(
                                                        DialogInterface dialog,
                                                        int id) {
                                                    // if this button is
                                                    // clicked, close
                                                    // current activity
                                                    dialog.cancel();
                                                }
                                            }
                                    );

                            // create alert dialog
                            AlertDialog alertDialog = alertDialogBuilder
                                    .create();

                            // show it
                            alertDialog.show();

                        }
                    }
                });
                deckDialog.setCanceledOnTouchOutside(true);
                deckDialog.show();

            }

        });


        addToCart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                Map<String, String> map = new HashMap<String, String>();
                map.put("CARD", card.getName());
                FlurryAgent.logEvent("Card added to cart", map);

                if (CardList.cardsToOrder == null) {
                    CardList.cardsToOrder = new ArrayList<String>();
                }
                if (CardList.currentOrder == null) {
                    CardList.currentOrder = CardList.massCardOrderingBaseURL;
                }
                CardList.cardsToOrder.add(card.getName() + " x 1");
                CardList.currentOrder += "1 " + card.getName() + "||";

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                // set title
                alertDialogBuilder.setTitle("Success!");

                // set dialog message

                alertDialogBuilder
                        .setMessage(
                                card.getName()
                                        + " was successfully added to your cart\n"
                        )
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog, int id) {
                                        // if this button is clicked,
                                        // close
                                        // current activity
                                        dialog.cancel();
                                    }
                                }
                        );

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }

        });

        high.setText("$" + product.getProducts().getHiprice());
        mean.setText("$" + product.getProducts().getAvgprice());
        low.setText("$" + product.getProducts().getLowprice());

        if (!product.getProducts().getFoilavgprice().equals("0")) {
            foil.setText("$" + product.getProducts().getFoilavgprice());
        } else {
            foil.setVisibility(View.GONE);
            TextView temp = (TextView) t.findViewById(R.id.foilPriceWords);
            temp.setVisibility(View.GONE);
        }

        priceDialog.show();

    }


}
