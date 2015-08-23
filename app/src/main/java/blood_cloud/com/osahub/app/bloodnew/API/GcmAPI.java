package blood_cloud.com.osahub.app.bloodnew.API;


import blood_cloud.com.osahub.app.bloodnew.Model.GcmModel;
import retrofit.Callback;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by anant on 21/08/15.
 */
public interface GcmAPI {
    @POST("/GCMNotification")
        void putGCM(@Query("shareRegId") String bol, @Query("regId") String regId, Callback<GcmModel> response);
}
