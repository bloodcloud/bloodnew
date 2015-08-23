package blood_cloud.com.osahub.app.bloodnew.Model;

import com.google.gson.annotations.Expose;

/**
 * Created by anant on 21/08/15.
 */
public class GcmModel {
    @Expose
    private String action;


    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
