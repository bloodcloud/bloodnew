package blood_cloud.com.osahub.app.bloodnew.API;


import blood_cloud.com.osahub.app.bloodnew.Model.Bloodmodel;
import retrofit.Callback;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by anant on 21/08/15.
 */

public interface BloodAPI {
    @POST("/loginPage/loginAnd")
         void getBack(@Query("email") String email, @Query("pass") String pass, Callback<Bloodmodel> response);
}
