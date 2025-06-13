package com.example.musicproject.fragmentsModels.MainMenu.settings.langauge;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.musicproject.R;

import java.util.Locale;

public class langaugeFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_langauge, container, false);

        Button plBtn = view.findViewById(R.id.instrument_organs_button);
        Button enBtn = view.findViewById(R.id.instrument_piano_button);
        Button deBtn = view.findViewById(R.id.language_ger);

        plBtn.setOnClickListener(v -> setLocale("pl"));
        enBtn.setOnClickListener(v -> setLocale("en"));
        deBtn.setOnClickListener(v -> setLocale("de"));

        return view;
    }

    private void setLocale(String langCode) {
        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);
        Resources res = getResources();
        Configuration config = new Configuration(res.getConfiguration());
        config.setLocale(locale);
        res.updateConfiguration(config, res.getDisplayMetrics());

        // Odśwież Activity, żeby zmiana była widoczna od razu:
        if (getActivity() != null) {
            getActivity().recreate();
        }
    }
}
