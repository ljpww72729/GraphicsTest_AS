package com.kk.lp.mvp;

/**
 * Created by lipeng on 2016 3-10.
 */
public class MainModel implements MainMVP.ModelOps {

    // Presenter reference
    private MainMVP.RequiredPresenterOps mPresenter;

    public MainModel(MainPresenter mPresenter) {
        this.mPresenter = mPresenter;
    }

    // Insert Note in DB
    @Override
    public void insertNote(Note note) {
        // data business logic
        // ...
        mPresenter.onNoteInserted(note);
    }

    // Removes Note from DB
    @Override
    public void removeNote(Note note) {
        // data business logic
        // ...
        mPresenter.onNoteRemoved(note);
    }

    @Override
    public void onDestroy() {

    }
}
