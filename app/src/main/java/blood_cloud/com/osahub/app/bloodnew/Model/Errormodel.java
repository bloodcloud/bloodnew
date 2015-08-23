package blood_cloud.com.osahub.app.bloodnew.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by anant on 22/08/15.
 */
public class Errormodel {


        @SerializedName("code")
        private Integer code;

        @SerializedName("error_message")
        private String strMessage;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getStrMessage() {
        return strMessage;
    }

    public void setStrMessage(String strMessage) {
        this.strMessage = strMessage;
    }

    public Errormodel(String strMessage, Integer code) {
        this.strMessage = strMessage;
        this.code = code;
    }

    public Errormodel(String strMessage) {
        this.strMessage = strMessage;
    }
}
