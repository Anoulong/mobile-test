package com.quickseries.restaurant;

import com.quickseries.data.Restaurant;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.http.GET;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Anou on 2017-10-14.
 */

public class RestaurantPresenter implements RestaurantContract.Presenter {


    public Retrofit retrofit;
    RestaurantContract.View mView;

    public RestaurantPresenter(Retrofit retrofit, RestaurantContract.View mView) {
        this.retrofit = retrofit;
        this.mView = mView;
    }

    @Override
    public void loadRestaurants() {
        retrofit.create(GetRestaurantOperation.class).fetchRestaurants().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Observer<List<Restaurant>>() {
                    @Override
                    public void onCompleted() {
                        mView.showComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showError(e.getMessage());
                    }

                    @Override
                    public void onNext(List<Restaurant> restaurants) {
                        mView.showRestaurant(restaurants);
                    }
                });
    }

    public interface GetRestaurantOperation {
        @GET("/restaurants.json")
        Observable<List<Restaurant>> fetchRestaurants();
    }
}