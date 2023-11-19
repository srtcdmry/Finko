package com.finko;

import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.finko.component.EditTextValidator;

public class BaseFragment extends Fragment {

    public void setToolbarText(String toolbarText) {
        Toolbar mToolbar = requireActivity().findViewById(R.id.mainToolbar);
        TextView textView = mToolbar.findViewById(R.id.title);
        textView.setText(toolbarText);
    }

    public AppCompatImageButton getToolbarBackButton(){
        Toolbar mToolbar = requireActivity().findViewById(R.id.mainToolbar);
        return mToolbar.findViewById(R.id.toolbarBackButton);
    }

    public void showToast(String message, int toastType, boolean withIcon) {
        if (getActivity() != null){
            ((BaseActivity) requireActivity()).showToast(message, toastType, withIcon);
        }
    }

    public void initProgressDialogInstance() {
        if (getActivity() != null) {
            ((BaseActivity) getActivity()).initProgressDialogInstance();
        }
    }

    public void dismissProgressDialog() {
        if (getActivity() != null) {
            ((BaseActivity) getActivity()).dismissProgressDialog();
        }
    }

    public void showProgressDialog() {
        if (getActivity() != null) {
            ((BaseActivity) getActivity()).showProgressDialog();
        }
    }

    public void showProgressDialog(String message) {
        if (getActivity() != null) {
            ((BaseActivity) getActivity()).showProgressDialog(message);
        }
    }

    public EditTextValidator passwordWatcher(){
        if (getActivity() != null) {
           return ((BaseActivity) getActivity()).passwordWatcher();
        }

        return null;
    }

    public EditTextValidator mailWatcher(){
        if (getActivity() != null) {
            return ((BaseActivity) getActivity()).mailWatcher();
        }

        return null;
    }

    public void logout(){
        if (getActivity() != null){
            ((BaseActivity) requireActivity()).logout();
        }
    }
}
