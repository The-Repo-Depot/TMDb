package tushar.letgo.tmdb.common.mvp;

import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import tushar.letgo.tmdb.common.base.BaseActivity;

public abstract class PresenterActivity extends BaseActivity implements PresenterCache {
    private static final String NEXT_ID_KEY = "next-presenter-id";

    private NonConfigurationInstance nonConfigurationInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        nonConfigurationInstance =
                (NonConfigurationInstance) getLastCustomNonConfigurationInstance();
        if (nonConfigurationInstance == null) {
            long seed;
            if (savedInstanceState == null) {
                seed = 0;
            } else {
                seed = savedInstanceState.getLong(NEXT_ID_KEY);
            }
            nonConfigurationInstance = new NonConfigurationInstance(seed);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(NEXT_ID_KEY, nonConfigurationInstance.nextId.get());
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return nonConfigurationInstance;
    }

    @Override
    public long generateId() {
        return nonConfigurationInstance.nextId.getAndIncrement();
    }

    @SuppressWarnings("unchecked") // Handled internally
    @Override
    public final <T extends Presenter> T getPresenter(long index) {
        T p;
        try {
            p = (T) nonConfigurationInstance.presenters.get(index);
        } catch (ClassCastException e) {
            throw new RuntimeException(e);
        }
        return p;
    }

    @Override
    public void setPresenter(long index, Presenter presenter) {
        nonConfigurationInstance.presenters.put(index, presenter);
    }

    private static class NonConfigurationInstance {
        private Map<Long, Presenter> presenters;
        private AtomicLong nextId;

        public NonConfigurationInstance(long seed) {
            presenters = new HashMap<>();
            nextId = new AtomicLong(seed);
        }
    }
}
