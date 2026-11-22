package com.example.musicproject.fragmentsModels.MainMenu.settings.language;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.musicproject.R;

import java.util.Locale;

public class LanguageFragment extends Fragment {

    private static final float ALPHA_ACTIVE = 1.0f;
    private static final float ALPHA_INACTIVE = 0.3f;

    private ImageButton plBtn, enBtn, deBtn;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_language, container, false);

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

        // Recreate the Activity so the change is visible immediately
        if (getActivity() != null) {
            getActivity().recreate();
        }
    }

    private void updateFlagsUI(String activeLang) {
        plBtn.setAlpha(activeLang.equals("pl") ? ALPHA_ACTIVE : ALPHA_INACTIVE);
        enBtn.setAlpha(activeLang.equals("en") ? ALPHA_ACTIVE : ALPHA_INACTIVE);
        deBtn.setAlpha(activeLang.equals("de") ? ALPHA_ACTIVE : ALPHA_INACTIVE);
    }
}
