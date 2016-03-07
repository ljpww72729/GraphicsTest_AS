package com.kk.lp.mvp;

/**
 * Created by lipeng on 2016 3-10.
 */
public interface MainMVP {
    interface RequiredViewOps {
        void showToast(String msg);

        void showAlert(String msg);
    }

    interface PresenterOps {
        void onConfigurationChanged(RequiredViewOps view);
        void onDestory(boolean isChangingConfig);
        void newNote(String textToNote);
        void deleteNote(Note note);
    }

    interface RequiredPresenterOps {
        void onNoteInserted(Note note);
        void onNoteRemoved(Note note);
        void onError(String errorMsg);
    }

    interface ModelOps{
        void insertNote(Note note);
        void removeNote(Note note);
        void onDestroy();
    }

}
