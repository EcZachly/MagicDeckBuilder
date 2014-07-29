package com.zach.wilson.magic.app.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zach.wilson.magic.app.R.drawable;
import com.zach.wilson.magic.app.R.id;
import com.zach.wilson.magic.app.R.layout;
import com.zach.wilson.magic.app.adapters.LazyAdapter;
import com.zach.wilson.magic.app.helpers.DeckBrewClient;
import com.zach.wilson.magic.app.models.Card;
import com.zach.wilson.magic.app.models.CardList;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

//DONE INJECTING
public class AdvancedSearchFragment extends Fragment {
    @InjectView(id.mainType)
    Spinner spinner;

    @InjectView(id.formatParameter)
    Spinner formatSpinner;
    @InjectView(id.rarityParameter)
    Spinner raritySpinner;
    @InjectView(id.advList)
    ListView advList;
    @InjectView(id.ANDOR)
    RadioGroup ANDOR;
    @InjectView(id.AND)
    RadioButton and;
    @InjectView(id.OR)
    RadioButton or;
    ListView list;
    @InjectView(id.advLayout)
    RelativeLayout advLayout;
    @InjectView(id.MultiColor)
    RadioButton multicolor;

    @InjectView(id.COLORS)
    LinearLayout colorLayout;
    OnClickListener colorListener;
    @InjectView(id.removeSubmit)
    Button removeSubmit;
    @InjectView(id.addFormatParameter)
    Button addFormat;


    Card[] cardsFromSearch;

    ImageLoader imageLoader;
    ProgressDialog dialog;
    ArrayList<String> textContainsParameters;
    ArrayList<String> typeContainsParameters;
    ArrayList<String> mainTypeContainsParameters;
    ArrayList<String> advPotentialNames;
    ArrayList<String> formatContainsParameters;
    ArrayList<String> rarityContainsParameters;
    boolean AO;
    static String[] formats = {"Standard", "Modern", "Vintage", "Legacy",
            "Commander"};
    static boolean[] colorsChecked = {false, false, false, false, false};
    static String[] colorChecker = {"red", "green", "black", "blue", "white"};
    ImageView[] colors;
    ArrayAdapter<String> formatAdapter;
    ArrayAdapter<String> advAdapter;
    Dialog priceDialog;
    boolean blnMulticolor;
    Context context;
    View view;
    Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(layout.advancedsearchview, null);
        this.view = v;
        ButterKnife.inject(this, v);
        dialog = new ProgressDialog(v.getContext());
        this.activity = getActivity();
        context = getActivity().getBaseContext();
        priceDialog = new Dialog(context);
        imageLoader = ImageLoader.getInstance();
        setUpAdvancedSearch(v);

        return v;
    }

    public void setUpAdvancedSearch(View v) {
        // SETTING UP ADVANCED SEARCH
        advPotentialNames = new ArrayList<String>();
        textContainsParameters = new ArrayList<String>();
        typeContainsParameters = new ArrayList<String>();
        formatContainsParameters = new ArrayList<String>();
        mainTypeContainsParameters = new ArrayList<String>();
        rarityContainsParameters = new ArrayList<String>();
        formatAdapter = new ArrayAdapter(context, layout.spinner_item,
                formats);
        formatSpinner.setAdapter(formatAdapter);

        addFormat.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String l = formatSpinner.getSelectedItem().toString();
                formatContainsParameters.add(l.toLowerCase());
                InputMethodManager inputManager = (InputMethodManager) getActivity()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);

                //check if no view has focus:
                View vw = getActivity().getCurrentFocus();
                if (vw == null)
                    return;

                inputManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });

        Button addT = (Button) v.findViewById(id.addTypeParameter);
        addT.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                EditText text = (EditText) view.findViewById(id.typeContains);
                typeContainsParameters.add(text.getText().toString().toLowerCase());
                text.setText("");
                InputMethodManager inputManager = (InputMethodManager) getActivity()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);

                //check if no view has focus:
                View vw = getActivity().getCurrentFocus();
                if (vw == null)
                    return;

                inputManager.hideSoftInputFromWindow(vw.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

            }

        });
        Button add = (Button) v.findViewById(id.addTextParameter);
        add.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                EditText text = (EditText) view.findViewById(id.textContains);
                textContainsParameters.add(text.getText().toString());
                text.setText("");
                InputMethodManager inputManager = (InputMethodManager) getActivity()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);

                //check if no view has focus:
                View vw = getActivity().getCurrentFocus();
                if (vw == null)
                    return;

                inputManager.hideSoftInputFromWindow(vw.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

            }

        });
        advAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_list_item_1, advPotentialNames);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(context,
                layout.spinner_item, CardList.types);
        spinner.setAdapter(spinnerAdapter);

        Button t = (Button) v.findViewById(id.addMainTypeParameter);
        t.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String w = spinner.getSelectedItem().toString();
                mainTypeContainsParameters.add(w.toLowerCase());
            }

        });
        ArrayAdapter<String> rarityAdapter = new ArrayAdapter<String>(context,
                layout.spinner_item, CardList.rarities);
        raritySpinner.setAdapter(rarityAdapter);
        Button s = (Button) v.findViewById(id.addRarityParameter);
        s.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String w = raritySpinner.getSelectedItem().toString();
                rarityContainsParameters.add(w.toLowerCase());
            }

        });
        colors = new ImageView[5];
        colors[0] = (ImageView) v.findViewById(id.redManaImage);
        colors[1] = (ImageView) v.findViewById(id.greenManaImage);
        colors[2] = (ImageView) v.findViewById(id.blackManaImage);
        colors[3] = (ImageView) v.findViewById(id.blueManaImage);
        colors[4] = (ImageView) v.findViewById(id.whiteManaImage);
        colors[0]
                .setContentDescription(String.valueOf(drawable.red_inactive));
        colors[1].setContentDescription(String
                .valueOf(drawable.green_inactive));
        colors[2].setContentDescription(String
                .valueOf(drawable.black_inactive));
        colors[3].setContentDescription(String
                .valueOf(drawable.blue_inactive));
        colors[4].setContentDescription(String
                .valueOf(drawable.white_inactive));

        colorListener = new OnClickListener() {

            @Override
            public void onClick(View v) {
                ImageView view = (ImageView) v;

                if (Integer.parseInt(v.getContentDescription().toString()) == drawable.red_inactive) {
                    view.setImageResource(drawable.red_active);
                    view.setContentDescription(String
                            .valueOf(drawable.red_active));

                } else if (Integer.parseInt(v.getContentDescription()
                        .toString()) == drawable.green_inactive) {
                    view.setImageResource(drawable.green_active);
                    view.setContentDescription(String
                            .valueOf(drawable.green_active));
                } else if (Integer.parseInt(v.getContentDescription()
                        .toString()) == drawable.black_inactive) {
                    view.setImageResource(drawable.black_active);
                    view.setContentDescription(String
                            .valueOf(drawable.black_active));
                } else if (Integer.parseInt(v.getContentDescription()
                        .toString()) == drawable.blue_inactive) {
                    view.setImageResource(drawable.blue_active);
                    view.setContentDescription(String
                            .valueOf(drawable.blue_active));
                } else if (Integer.parseInt(v.getContentDescription()
                        .toString()) == drawable.white_inactive) {
                    view.setImageResource(drawable.white_active);
                    view.setContentDescription(String
                            .valueOf(drawable.white_active));
                } else if (Integer.parseInt(v.getContentDescription()
                        .toString()) == drawable.red_active) {
                    view.setImageResource(drawable.red_inactive);
                    view.setContentDescription(String
                            .valueOf(drawable.red_inactive));

                } else if (Integer.parseInt(v.getContentDescription()
                        .toString()) == drawable.green_active) {
                    view.setImageResource(drawable.green_inactive);
                    view.setContentDescription(String
                            .valueOf(drawable.green_inactive));
                } else if (Integer.parseInt(v.getContentDescription()
                        .toString()) == drawable.black_active) {

                    view.setImageResource(drawable.black_inactive);
                    view.setContentDescription(String
                            .valueOf(drawable.black_inactive));
                } else if (Integer.parseInt(v.getContentDescription()
                        .toString()) == drawable.blue_active) {
                    view.setImageResource(drawable.blue_inactive);
                    view.setContentDescription(String
                            .valueOf(drawable.blue_inactive));
                } else if (Integer.parseInt(v.getContentDescription()
                        .toString()) == drawable.white_active) {
                    view.setImageResource(drawable.white_inactive);
                    view.setContentDescription(String
                            .valueOf(drawable.white_inactive));
                }

            }

        };

        for (ImageView view : colors) {
            view.setOnClickListener(colorListener);
        }

        and = (RadioButton) v.findViewById(id.AND);
        and.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (and.isChecked()) {
                    colorLayout.setVisibility(View.VISIBLE);
                    and.setButtonDrawable(drawable.active_black);
                    multicolor.setButtonDrawable(drawable.inactive_black);
                    or.setButtonDrawable(drawable.inactive_black);
                    int i = 0;
                    for (ImageView view : colors) {

                        switch (i) {

                            case 0:
                                view.setImageResource(drawable.red_inactive);
                                view.setContentDescription(String
                                        .valueOf(drawable.red_inactive));
                                break;
                            case 1:
                                view.setImageResource(drawable.green_inactive);
                                view.setContentDescription(String
                                        .valueOf(drawable.green_inactive));

                                break;

                            case 2:
                                view.setImageResource(drawable.black_inactive);
                                view.setContentDescription(String
                                        .valueOf(drawable.black_inactive));
                                break;

                            case 3:
                                view.setImageResource(drawable.blue_inactive);
                                view.setContentDescription(String
                                        .valueOf(drawable.blue_inactive));
                                break;

                            case 4:
                                view.setImageResource(drawable.white_inactive);
                                view.setContentDescription(String
                                        .valueOf(drawable.white_inactive));
                                break;

                        }

                        i++;
                    }


                } else {

                }

            }

        });
        multicolor = (RadioButton) v.findViewById(id.MultiColor);
        multicolor.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (multicolor.isChecked()) {
                    colorLayout.setVisibility(View.VISIBLE);
                    multicolor.setButtonDrawable(drawable.active_black);
                    or.setButtonDrawable(drawable.inactive_black);
                    and.setButtonDrawable(drawable.inactive_black);
                    int i = 0;
                    for (ImageView view : colors) {

                        switch (i) {

                            case 0:
                                view.setImageResource(drawable.red_inactive);
                                view.setContentDescription(String
                                        .valueOf(drawable.red_inactive));
                                break;
                            case 1:
                                view.setImageResource(drawable.green_inactive);
                                view.setContentDescription(String
                                        .valueOf(drawable.green_inactive));

                                break;

                            case 2:
                                view.setImageResource(drawable.black_inactive);
                                view.setContentDescription(String
                                        .valueOf(drawable.black_inactive));
                                break;

                            case 3:
                                view.setImageResource(drawable.blue_inactive);
                                view.setContentDescription(String
                                        .valueOf(drawable.blue_inactive));
                                break;

                            case 4:
                                view.setImageResource(drawable.white_inactive);
                                view.setContentDescription(String
                                        .valueOf(drawable.white_inactive));
                                break;

                        }

                        i++;
                    }
                }
            }

        });
        or = (RadioButton) v.findViewById(id.OR);
        or.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (or.isChecked()) {
                    int i = 0;
                    for (ImageView view : colors) {

                        switch (i) {
                            case 0:
                                view.setImageResource(drawable.red_unavailable);
                                view.setContentDescription(String
                                        .valueOf(drawable.red_unavailable));
                                break;
                            case 1:
                                view.setImageResource(drawable.green_unavailable);
                                view.setContentDescription(String
                                        .valueOf(drawable.green_unavailable));

                                break;

                            case 2:
                                view.setImageResource(drawable.black_unavailable);
                                view.setContentDescription(String
                                        .valueOf(drawable.black_unavailable));
                                break;

                            case 3:
                                view.setImageResource(drawable.blue_unavailable);
                                view.setContentDescription(String
                                        .valueOf(drawable.blue_unavailable));
                                break;

                            case 4:
                                view.setImageResource(drawable.white_unavailable);
                                view.setContentDescription(String
                                        .valueOf(drawable.white_unavailable));
                                break;

                        }

                        i++;
                    }
                    multicolor.setButtonDrawable(drawable.inactive_black);
                    or.setButtonDrawable(drawable.active_black);
                    and.setButtonDrawable(drawable.inactive_black);
                }
            }

        });

        removeSubmit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Fragment l = new AdvancedSearchFragment();
                getFragmentManager().beginTransaction().replace(id.content_frame, l).commit();


            }


        });


        Button submit = (Button) v.findViewById(id.advSubmit);
        submit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                removeSubmit.setVisibility(View.VISIBLE);
                advPotentialNames.clear();

                for (int i = 0; i < 5; i++) {
                    colorsChecked[i] = false;
                }


                int z = ANDOR.getCheckedRadioButtonId();
                if (z == and.getId()) {
                    for (int i = 0; i < 5; i++) {
                        if (Integer.parseInt(colors[i].getContentDescription()
                                .toString()) == drawable.red_active) {
                            colorsChecked[i] = true;
                        } else if (Integer.parseInt(colors[i]
                                .getContentDescription().toString()) == drawable.green_active) {
                            colorsChecked[i] = true;
                        } else if (Integer.parseInt(colors[i]
                                .getContentDescription().toString()) == drawable.black_active) {
                            colorsChecked[i] = true;
                        } else if (Integer.parseInt(colors[i]
                                .getContentDescription().toString()) == drawable.blue_active) {
                            colorsChecked[i] = true;
                        } else if (Integer.parseInt(colors[i]
                                .getContentDescription().toString()) == drawable.white_active) {
                            colorsChecked[i] = true;
                        }
                    }
                    AO = true;
                    blnMulticolor = false;
                } else if (z == or.getId()) {
                    AO = false;
                    blnMulticolor = false;

                } else if (z == multicolor.getId()) {
                    blnMulticolor = true;
                } else {
                    Toast.makeText(
                            context,
                            "Must select either\nInclude Color or Ignore Color",
                            Toast.LENGTH_LONG).show();

                }
                ArrayList<String> colorsT = new ArrayList<String>();
                for (int i = 0; i < colors.length; i++) {
                    if (colorsChecked[i]) {
                        colorsT.add(colorChecker[i]);
                    }
                }

                DeckBrewClient.getAPI().getCardsFromAdvAttributes(mainTypeContainsParameters, colorsT, typeContainsParameters, textContainsParameters, formatContainsParameters, rarityContainsParameters, blnMulticolor, new Callback<List<Card>>() {
                    @Override
                    public void success(List<Card> cards, Response response) {
                        cardsFromSearch = new Card[cards.size()];
                        for(int i = 0; i < cardsFromSearch.length; i++){
                            cardsFromSearch[i] = cards.get(i);
                        }
                        list = (ListView) view.findViewById(id.advList);
                        list.setAdapter(new LazyAdapter(activity,cardsFromSearch, getActivity().getLayoutInflater()));
                        list.setOnItemClickListener(new OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> arg0, View v, int arg2,
                                                    long arg3) {
                                String viewURL = cardsFromSearch[arg2].getEditions()[0]
                                        .getImage_url();
                                CardList.selectedCard = cardsFromSearch[arg2];
                                CardCarouselFragment fragment = new CardCarouselFragment();
                                Bundle args = new Bundle();
                                args.putSerializable("CARDS FROM ADV SEARCH", cardsFromSearch);
                                args.putBoolean("FROM ADV SEARCH", true);
                                args.putInt("CURRENT ITEM", arg2);
                                fragment.setArguments(args);
                                getFragmentManager().beginTransaction()
                                        .replace(id.content_frame, fragment)
                                        .commit();

                            }

                        });

                        list.setVisibility(View.VISIBLE);


                    }


                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
            }


        });


    }
}





