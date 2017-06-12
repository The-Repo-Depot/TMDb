package tushar.letgo.tmdb.dipendency;

import javax.inject.Singleton;

import dagger.Component;
import tushar.letgo.tmdb.common.GlideSetting;

/**
 * Created by Tushar on 6/12/17.
 */

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    void inject(GlideSetting glideSetting);
}
