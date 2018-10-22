package farasense.mobile.model.realm;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class FaraSenseSensor extends RealmObject {

    @SerializedName("id")
    Integer id;
    @SerializedName("amper")
    Double amper;
    @SerializedName("power")
    Double power;
    @SerializedName("timestamp")
    Date date;
    @PrimaryKey
    @SerializedName("long_timestamp")
    Long timestamp;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public double getAmper() {
        return amper;
    }

    public void setAmper(double amper) {
        this.amper = amper;
    }

    public double getPower() {
        return power;
    }

    public void setPower(double power) {
        this.power = power;
    }

}
