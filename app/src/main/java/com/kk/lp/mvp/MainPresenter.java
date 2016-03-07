package com.kk.lp.mvp;

import java.lang.ref.WeakReference;
import java.util.Date;

/**
 * Created by lipeng on 2016 3-10.
 */
public class MainPresenter implements MainMVP.RequiredPresenterOps, MainMVP.PresenterOps {

    // Layer View reference
    private WeakReference<MainMVP.RequiredViewOps> mView;
    // Layer Model reference
    private MainMVP.ModelOps mModel;
    private boolean mIsChangingConfig;

    public MainPresenter(MainMVP.RequiredViewOps mView) {
        this.mView = new WeakReference<>(mView);
        this.mModel = new MainModel(this);
    }

    /**
     * Called from {@link MainModel}
     * when a Note is inserted successfully
     */
    @Override
    public void onNoteInserted(Note note) {
        mView.get().showToast("New register added at " + note.getDate());
    }

    /**
     * Receives call from {@link MainModel}
     * when Note is removed
     */
    @Override
    public void onNoteRemoved(Note note) {
        mView.get().showToast("Note removed");
    }

    @Override
    public void onError(String errorMsg) {

    }

    /**
     * Sent from Activity after a configuration changes
     *
     * @param view View reference
     */
    @Override
    public void onConfigurationChanged(MainMVP.RequiredViewOps view) {
        mView = new WeakReference<>(view);
    }

    /**
     * Receives {@link MainActivity#onDestroy()} event
     *
     * @param isChangingConfig Config change state
     */
    @Override
    public void onDestory(boolean isChangingConfig) {
        mView = null;
        mIsChangingConfig = isChangingConfig;
        if (!isChangingConfig) {
            mModel.onDestroy();
        }
    }

    /**
     * Called by user interaction from {@link MainActivity}
     * creates a new Note
     */
    @Override
    public void newNote(String textToNote) {
        Note note = new Note();
        note.setText(textToNote);
        note.setDate(new Date());
        mModel.insertNote(note);
    }

    /**
     * Called from {@link MainActivity},
     * Removes a Note
     */
    @Override
    public void deleteNote(Note note) {
        mModel.removeNote(note);
    }
}
