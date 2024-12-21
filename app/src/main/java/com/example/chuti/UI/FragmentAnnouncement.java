package com.example.chuti.UI;

import static com.example.chuti.FragmentManager.FragmentManager.replaceFragment;
import static com.example.chuti.Handlers.DateFormatterHandlers.CurrentOffsetTimeParser;
import static com.example.chuti.Handlers.SMessageHandler.SAlertError;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.chuti.FragmentMain;
import com.example.chuti.Model.Announcement;
import com.example.chuti.Model.ServiceResponseViewModel;
import com.example.chuti.R;
import com.example.chuti.Security.BaseURL;
import com.example.chuti.Security.Services;
import com.example.chuti.Security.SharedPref;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentAnnouncement extends Fragment {

    Services retrofitApiInterface;
    Gson gson;
    String token, accountID, companyID;
    SpotsDialog spotsDialog;
    ServiceResponseViewModel serviceResponseViewModel = new ServiceResponseViewModel();
    Spinner catalogSpinner;
    String userID, appKey, periodYear;
    Toolbar toolbar;
    RecyclerView annaouncementRecyclerView;
    AnnouncementAdapter announcementAdapter;
    ChipNavigationBar bottomNavigationView;
    List<Announcement> announcementList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_announcement, container, false);

        retrofitApiInterface = BaseURL.getRetrofit().create(Services.class);
        SharedPref.init(getContext());
        gson = new Gson();
        spotsDialog = new SpotsDialog(getContext(), R.style.Custom);
        appKey = SharedPref.read("appKey", "");
        token = SharedPref.read("token", "");
        companyID = SharedPref.read("companyID", "");
        accountID = SharedPref.read("accountID", "");
        userID = SharedPref.read("uId", "");

        toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        toolbar.setTitle("My Leave Requests");
        toolbar.setSubtitle("");
        toolbar.setNavigationOnClickListener(v -> {
            toolbar.setTitle(R.string.chuti);
            replaceFragment(new FragmentMain(), getContext());
        });

        bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationView);

        catalogSpinner = root.findViewById(R.id.catalogSpinner);
        annaouncementRecyclerView = root.findViewById(R.id.annaouncementRecyclerView);

        List<Integer> yearList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();

        int currentYear = calendar.get(Calendar.YEAR);

        // Add the current year and the last three years to the list
        for (int i = 0; i < 3; i++) {
            yearList.add(currentYear - i);
        }

        if (!yearList.isEmpty()) {
            ArrayAdapter<Integer> dataAdapter = new ArrayAdapter<>(
                    getContext(),
                    android.R.layout.simple_spinner_item,
                    yearList
            );
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            catalogSpinner.setAdapter(dataAdapter);

            // Set item selection listener
            catalogSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    periodYear = catalogSpinner.getItemAtPosition(position).toString();
                    Announcement();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Do nothing if no selection is made
                }
            });

            return root;
        }

        return root;
    }

    private void Announcement() {
        try {
            Call<List<Announcement>> getContToLocCall = retrofitApiInterface.GetAnnouncementsAsync("Bearer" + " " + token, appKey, companyID, Integer.parseInt(periodYear));
            getContToLocCall.enqueue(new Callback<List<Announcement>>() {
                @Override
                public void onResponse(Call<List<Announcement>> call, Response<List<Announcement>> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                spotsDialog.dismiss();
                                announcementList = new ArrayList<>();
                                announcementList = response.body();
                                // Sort the list in descending order
                                Collections.sort(announcementList, (u1, u2) -> {
                                    return u2.getAnnouncementID() - u1.getAnnouncementID(); // Descending order
                                });
                                announcementAdapter = new AnnouncementAdapter(getContext(), announcementList);
                                LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                                annaouncementRecyclerView.setLayoutManager(mLayoutManager);
                                annaouncementRecyclerView.setAdapter(announcementAdapter);
                                bottomNavigationView.showBadge(R.id.announcement, response.body().size());
                            }
                        } else {
                            if (response.errorBody() != null) {
                                spotsDialog.dismiss();
                                gson = new GsonBuilder().create();
                                try {
                                    serviceResponseViewModel = gson.fromJson(response.errorBody().string(), ServiceResponseViewModel.class);
                                    SAlertError(serviceResponseViewModel.getMessage(), getContext());
                                    annaouncementRecyclerView.setAdapter(null);
                                    announcementAdapter.equals("");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    } catch (Exception e) {
                        spotsDialog.dismiss();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<List<Announcement>> call, Throwable t) {
                    SAlertError(t.getMessage(), getContext());
                    spotsDialog.dismiss();
                }
            });
        } catch (Exception e) {
            spotsDialog.dismiss();
            e.printStackTrace();
        }
    }

    public static class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.ViewHolder> {
        Context context;
        List<Announcement> announcementList;

        public AnnouncementAdapter(Context context, List<Announcement> announcementList) {
            this.context = context;
            this.announcementList = announcementList;
        }

        @NonNull
        @Override
        public AnnouncementAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.annaouncement_recycalerview, parent, false);
            return new AnnouncementAdapter.ViewHolder(view);
        }

        @Override
        @RequiresApi(api = Build.VERSION_CODES.O)
        public void onBindViewHolder(@NonNull AnnouncementAdapter.ViewHolder holder, int position) {
            final Announcement leaveRequestsViewModel = announcementList.get(position);

            try {

                try {
                    holder.txtAnnouncementText.setText(leaveRequestsViewModel.getAnnouncementText());
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

                try {
                    holder.txtAnnouncementTitle.setText(leaveRequestsViewModel.getAnnouncementTitle());
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

                try {
                    holder.txtPublishedDate.setText(CurrentOffsetTimeParser(leaveRequestsViewModel.getPublishedDate()));
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public int getItemCount() {
            return announcementList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView
                    txtPublishedDate,
                    txtAnnouncementTitle,
                    txtAnnouncementText;
            ImageView arrow_button;

            LinearLayout itemHiddenView, itemLayout;


            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                arrow_button = itemView.findViewById(R.id.arrow_button);
                itemHiddenView = itemView.findViewById(R.id.itemHiddenView);
                itemLayout = itemView.findViewById(R.id.itemLayout);
                txtPublishedDate = itemView.findViewById(R.id.txtPublishedDate);
                txtAnnouncementTitle = itemView.findViewById(R.id.txtAnnouncementTitle);
                txtAnnouncementText = itemView.findViewById(R.id.txtAnnouncementText);

                itemView.findViewById(R.id.arrow_button).setOnClickListener(v -> {
                    if (itemHiddenView.getVisibility() == View.VISIBLE) {
                        TransitionManager.beginDelayedTransition(itemLayout, new AutoTransition());
                        itemHiddenView.setVisibility(View.GONE);
                        arrow_button.setImageResource(R.drawable.icon_expand_more_24);
                    } else {
                        TransitionManager.beginDelayedTransition(itemLayout, new AutoTransition());
                        itemHiddenView.setVisibility(View.VISIBLE);
                        arrow_button.setImageResource(R.drawable.icon_expand_less_24);
                    }
                });
            }
        }
    }

}