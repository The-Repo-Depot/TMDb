package tushar.letgo.tmdb.common.mvp;

public interface PresenterCache {
    long generateId();

    <T extends Presenter> T getPresenter(long index);

    void setPresenter(long index, Presenter presenter);
}
