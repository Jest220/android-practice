package com.lr3_428_09;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.lr3_428_09.database.SimpleDao;
import com.lr3_428_09.model.SimpleModel;

public class PopupDialogFragment extends DialogFragment {
    public interface OnDialogResultListener {
        void onResult();
    }
    private EditText etValue;
    private Button btnPopupSave;
    private Button btnPopupDelete;
    private SimpleDao simpleDao;
    private boolean isEditMode;
    private OnDialogResultListener listener;
    private SimpleModel model;

    public PopupDialogFragment(SimpleDao simpleDao, boolean isEditMode) {
        this.simpleDao = simpleDao;
        this.isEditMode = isEditMode;
    }

    public PopupDialogFragment(SimpleDao simpleDao, SimpleModel model) {
        this.simpleDao = simpleDao;
        this.isEditMode = true;
        this.model = model;
    }

    public void setListener(OnDialogResultListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_table_edit, container, false);
        initializeViews(view);
        setupButtons();
        return view;
    }

    public void initializeViews(View view) {
        etValue = view.findViewById(R.id.etValue);
        btnPopupSave = view.findViewById(R.id.btnPopupSave);
        btnPopupDelete = view.findViewById(R.id.btnPopupDelete);
        if (isEditMode) {
            etValue.setText(model.getName());
            btnPopupDelete.setVisibility(View.VISIBLE);
        }
    }

    public void setupButtons() {
        btnPopupSave.setOnClickListener(v -> { saveModel(); });
        btnPopupDelete.setOnClickListener(v -> { deleteModel(); });
    }

    private void saveModel() {
        String name = etValue.getText().toString().trim();
        if (name.isEmpty()) {
            Toast.makeText(getContext(), "Введите значение", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isEditMode) {
            simpleDao.update(model.getId(), name);
            Toast.makeText(getContext(), "Запись обновлена", Toast.LENGTH_SHORT).show();
        } else {
            simpleDao.insert(name);
            Toast.makeText(getContext(), "Запись добавлена", Toast.LENGTH_SHORT).show();
        }
        if (listener != null) listener.onResult();
        dismiss();
    }

    private void deleteModel() {
        if (isEditMode) {
            simpleDao.delete(model.getId());
            Toast.makeText(getContext(), "Запись удалена", Toast.LENGTH_SHORT).show();
            if (listener != null) listener.onResult();
            dismiss();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null) {
            // Устанавливаем стиль окна
            getDialog().getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);
            getDialog().getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }
    }
}
