package com.example.musicproject.fragmentsModels.MainMenu.settings.langauge;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.musicproject.R;

import java.util.Locale;

public class langaugeFragment extends Fragment {
    private ImageButton plBtn, enBtn, deBtn;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_langauge, container, false);

         plBtn = view.findViewById(R.id.flag_pl);
         enBtn = view.findViewById(R.id.flag_en);
         deBtn = view.findViewById(R.id.flag_de);

        plBtn.setOnClickListener(v -> setLocale("pl"));
        enBtn.setOnClickListener(v -> setLocale("en"));
        deBtn.setOnClickListener(v -> setLocale("de"));

        String currentLang = Locale.getDefault().getLanguage();
        updateFlagsUI(currentLang);

        return view;
    }

    private void setLocale(String langCode) {
        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);
        Resources res = getResources();
        Configuration config = new Configuration(res.getConfiguration());
        config.setLocale(locale);
        res.updateConfiguration(config, res.getDisplayMetrics());

        updateFlagsUI(langCode);

        // Odśwież Activity, żeby zmiana była widoczna od razu:
        if (getActivity() != null) {
            getActivity().recreate();
        }
    }
    private void updateFlagsUI(String activeLang) {
        plBtn.setAlpha(activeLang.equals("pl") ? 1.0f : 0.3f);
        enBtn.setAlpha(activeLang.equals("en") ? 1.0f : 0.3f);
        deBtn.setAlpha(activeLang.equals("de") ? 1.0f : 0.3f);
    }
}
