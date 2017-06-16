package tushar.letgo.tmdb.common;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GenericLoaderFactory;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.stream.BaseGlideUrlLoader;
import com.bumptech.glide.load.model.stream.StreamModelLoader;
import com.bumptech.glide.module.GlideModule;

import java.io.InputStream;

import javax.inject.Inject;

import okhttp3.OkHttpClient;
import tushar.letgo.tmdb.api.ImageService;

/**
 * Created by Tushar on 6/13/17.
 */

public class GlideSetting implements GlideModule {

    @Inject
    OkHttpClient okHttpClient;

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {

    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        App.get(context).getAppComponent().inject(this);

        glide.register(String.class, InputStream.class, new ImageLoader.Factory());
        glide.register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(okHttpClient));
    }

    private static class ImageLoader extends BaseGlideUrlLoader<String> {

        public ImageLoader(Context context) {
            super(context);
        }

        @Override
        protected String getUrl(String imagePath, int width, int height) {
            return ImageService.buildPosterUrl(imagePath, width);
        }

        public static class Factory implements ModelLoaderFactory<String, InputStream> {
            @Override
            public StreamModelLoader<String> build(Context context, GenericLoaderFactory factories) {
                return new ImageLoader(context);
            }

            @Override
            public void teardown() {
                // skip
            }
        }
    }
}
