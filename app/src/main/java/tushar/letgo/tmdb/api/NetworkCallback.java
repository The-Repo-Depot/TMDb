package tushar.letgo.tmdb.api;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import timber.log.Timber;

/**
 * Created by Tushar on 6/16/17.
 */

public abstract class NetworkCallback<M> extends Subscriber<M> {

    private static final String TAG = NetworkCallback.class.getName();

    public abstract void onSuccess(M model);

    public abstract void onFailure(String message);

    public abstract void onFinish();

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            int code = httpException.code();
            String message = httpException.getMessage();
//            Timber.tag(TAG).d("code : " + code);
            onFailure(message);
        } else {
            onFailure(e.getMessage());
        }
        onFinish();
    }

    @Override
    public void onNext(M model) {
        onSuccess(model);
    }

    @Override
    public void onCompleted() {
        onFinish();
    }
}