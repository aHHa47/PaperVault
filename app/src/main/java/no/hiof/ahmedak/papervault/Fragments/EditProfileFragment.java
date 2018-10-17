package no.hiof.ahmedak.papervault.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import no.hiof.ahmedak.papervault.R;

public class EditProfileFragment extends Fragment  {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.layout_edit_profile,container,false);


        // TODO: Make it possible for the user to edit there profile image
        // TODO: Make it possible for the user to edit other information
        // TODO: Display user receipts information.

        return view;
    }
}
