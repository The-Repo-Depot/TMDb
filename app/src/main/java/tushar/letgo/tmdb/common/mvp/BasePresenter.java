package tushar.letgo.tmdb.common.mvp;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public abstract class BasePresenter<T> implements Presenter<T> {
    private static final String KEY_WAS_CREATED = "was_created";

    protected T view;

    protected boolean wasViewCreated = false;

    @Override
    public void bindView(T view) {
        this.view = view;
    }

    @Override
    public void unbindView() {
        this.view = null;
    }

    @Override
    public void onCreate(@Nullable PresenterBundle bundle) {
        if (bundle != null) {
            wasViewCreated = bundle.getBoolean(KEY_WAS_CREATED);
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onSaveInstanceState(@NonNull PresenterBundle bundle) {
        wasViewCreated = true;

        bundle.putBoolean(KEY_WAS_CREATED, wasViewCreated);
    }
}
