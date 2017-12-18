package bwie.com.jdemo.utils;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ASUS on 2017/12/4.
 */

public class RetrofitHelper2 {
    private static OkHttpClient okHttpClient;
    private static ServiceAPI serviceAPI;
    static {
        initRetrofitHelper();
    }

    private static void initRetrofitHelper() {
        if(okHttpClient == null){
            synchronized (RetrofitHelper2.class){
                if(okHttpClient == null){
                    okHttpClient = new OkHttpClient.Builder()
                            .build();

                }
            }
        }
    }
    public static ServiceAPI getServiceAPI(){
        if(serviceAPI == null){
            synchronized (ServiceAPI.class){
                if(serviceAPI == null){
                    serviceAPI =  RetrofitHelper2.createApi(ServiceAPI.class,UrlUtils.HOST2);
                }
            }
        }
        return serviceAPI;
    }
    public static <T> T createApi(Class<T> tClass,String url){
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(url)
                .build();
        return retrofit.create(tClass);
    }
}
