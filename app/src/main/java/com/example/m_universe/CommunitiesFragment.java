package com.example.m_universe;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CommunitiesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommunitiesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView sports, dance, gamers, programmers, music;
    Button j1, j2, j3, j4, j5;


    public CommunitiesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CommunitiesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CommunitiesFragment newInstance(String param1, String param2) {
        CommunitiesFragment fragment = new CommunitiesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_communities, container, false);

        sports = view.findViewById(R.id.sportsTV);
        dance = view.findViewById(R.id.danceTV);
        gamers = view.findViewById(R.id.gamersTV);
        programmers = view.findViewById(R.id.programmersTV);
        music = view.findViewById(R.id.musicTV);

        j1 = view.findViewById(R.id.join_button1);
        j2 = view.findViewById(R.id.join_button2);
        j3 = view.findViewById(R.id.join_button3);
        j4 = view.findViewById(R.id.join_button4);
        j5 = view.findViewById(R.id.join_button5);

        sports.setOnClickListener(v -> openSportsActivity());
        dance.setOnClickListener(v -> openDanceActivity());
        gamers.setOnClickListener(v -> openGamersActivity());
        programmers.setOnClickListener(v -> openProgrammersActivity());
        music.setOnClickListener(v -> openMusicActivity());

        j1.setOnClickListener(v -> openSportsActivity());
        j2.setOnClickListener(v -> openDanceActivity());
        j3.setOnClickListener(v -> openGamersActivity());
        j4.setOnClickListener(v -> openProgrammersActivity());
        j5.setOnClickListener(v -> openMusicActivity());


        return view;
    }

    private void openSportsActivity() {
        Intent intent = new Intent(requireActivity(), SportsActivity.class);
        startActivity(intent);
    }

    private void openDanceActivity() {
        Intent intent = new Intent(requireActivity(), dance.class);
        startActivity(intent);
    }

    private void openGamersActivity() {
        Intent intent = new Intent(requireActivity(), gamers.class);
        startActivity(intent);
    }

    private void openProgrammersActivity() {
        Intent intent = new Intent(requireActivity(), programmers.class);
        startActivity(intent);
    }

    private void openMusicActivity() {
        Intent intent = new Intent(requireActivity(), com.example.m_universe.music.class);
        startActivity(intent);
    }
}