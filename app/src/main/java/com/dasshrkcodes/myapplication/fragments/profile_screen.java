package com.dasshrkcodes.myapplication.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dasshrkcodes.myapplication.R;
import com.dasshrkcodes.myapplication.app_info;
import com.dasshrkcodes.myapplication.login_screen;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link profile_screen#newInstance} factory method to
 * create an instance of this fragment.
 */
public class profile_screen extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public profile_screen() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment profile_screen.
     */
    // TODO: Rename and change types and number of parameters
    public static profile_screen newInstance(String param1, String param2) {
        profile_screen fragment = new profile_screen();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    TextView profile_username,profile_email,help_page_contactText,help_page_aboutText;
    ImageView profile_pic;
    ExtendedFloatingActionButton profile_Logout,profile_Login,help_about_Button,help_app_info_Button,
            help_contact_Button;

    Integer help_about_ButtonClickcount = 0;
    Integer help_contact_ButtonClickcount = 0;

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

        View myInflatedView = inflater.inflate(R.layout.fragment_profile_screen, container,false);

        profile_username=myInflatedView.findViewById(R.id.profile_username);
        profile_email=myInflatedView.findViewById(R.id.profile_email);
        profile_pic=myInflatedView.findViewById(R.id.profile_pic);
        profile_Logout=myInflatedView.findViewById(R.id.profile_Logout);
        profile_Login=myInflatedView.findViewById(R.id.profile_Login);

        help_page_contactText=myInflatedView.findViewById(R.id.help_page_contactText);
        help_about_Button=myInflatedView.findViewById(R.id.help_about_Button);
        help_page_aboutText=myInflatedView.findViewById(R.id.help_page_aboutText);
        help_app_info_Button=myInflatedView.findViewById(R.id.help_app_info_Button);
        help_contact_Button=myInflatedView.findViewById(R.id.help_contact_Button);

        help_page_contactText.setMovementMethod(LinkMovementMethod.getInstance());


        profile_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_login = new Intent(getActivity(), login_screen.class);
                startActivity(intent_login);
            }
        });


        profile_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleSignInOptions gso = new GoogleSignInOptions.
                        Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                        build();

                GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
                googleSignInClient.signOut();
                FirebaseAuth.getInstance().signOut();

                Intent intent_logout = new Intent(getActivity(), login_screen.class);
                startActivity(intent_logout);
            }
        });

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(getActivity());

        if (signInAccount != null) {
            profile_Login.setVisibility(View.GONE);
            profile_username.setText(signInAccount.getDisplayName());
            profile_email.setText(signInAccount.getEmail());

            Picasso.get().load(signInAccount.getPhotoUrl()).
                    into(this.profile_pic);

        }else {
            profile_Logout.setVisibility(View.GONE);
        }


        help_about_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                app_info_about_ButtonClick();
            }
        });
        help_page_aboutText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                app_info_about_ButtonClick();
            }
        });

        help_app_info_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), app_info.class));
            }
        });
        help_contact_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TextView textView = (TextView) myInflatedView.findViewById(R.id.help_page_contactText);
                final TextView textView2 = (TextView) myInflatedView.findViewById(R.id.help_page_contactText3);
                if (help_contact_ButtonClickcount.intValue() % 2 == 0) {
                    textView.setVisibility(View.VISIBLE);
                    textView.setAlpha(0.0f);
                    textView.setTranslationY(-200.0f);
                    textView.animate().translationYBy(200.0f).alpha(1.0f).setDuration(500);
                    textView2.setVisibility(View.VISIBLE);
                    textView2.setAlpha(0.0f);
                    textView2.setTranslationY(-200.0f);
                    textView2.animate().translationYBy(200.0f).alpha(1.0f).setDuration(500);
                } else {textView.setVisibility(View.INVISIBLE);
                    textView2.setVisibility(View.INVISIBLE);
                }
                help_contact_ButtonClickcount = Integer.valueOf(help_contact_ButtonClickcount.intValue() + 1);
            }
        });


        return myInflatedView;
    }



    public void app_info_about_ButtonClick() {
        if (this.help_about_ButtonClickcount.intValue() % 2 == 0) {
            help_about_Button.setVisibility(View.VISIBLE);
            help_about_Button.animate().translationYBy(200.0f).alpha(0.0f).setDuration(500).withEndAction(new Runnable() {
                public void run() {
                    help_about_Button.setVisibility(View.GONE);
                    help_page_aboutText.setVisibility(View.VISIBLE);
                    help_page_aboutText.setTranslationY(-200.0f);
                    help_page_aboutText.setAlpha(0.0f);
                    help_page_aboutText.animate().translationYBy(200.0f).alpha(1.0f).setDuration(500);
                }
            });
        } else {
            help_page_aboutText.setVisibility(View.VISIBLE);
            help_page_aboutText.setTranslationY(200.0f);
            help_page_aboutText.setAlpha(1.0f);
            help_page_aboutText.animate().translationYBy(-200.0f).alpha(0.0f).setDuration(500).withEndAction(new Runnable() {
                public void run() {
                    help_page_aboutText.setVisibility(View.GONE);
                }
            });
            help_about_Button.setVisibility(View.VISIBLE);
            help_about_Button.setTranslationY(200.0f);
            help_about_Button.setAlpha(0.0f);
            help_about_Button.animate().translationYBy(-200.0f).alpha(1.0f).setDuration(500);
        }
        this.help_about_ButtonClickcount = Integer.valueOf(this.help_about_ButtonClickcount.intValue() + 1);
    }

}