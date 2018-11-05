package farasense.mobile.model.realm;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class FaraSenseSensorHours extends RealmObject {

    @SerializedName("id")
    Integer id;
    @SerializedName("timestamp")
    Date date;
    @SerializedName("kwh")
    Double kwh;
    @SerializedName("media_power")
    Double mediaPower;
    @SerializedName("time")
    Double time;
    @PrimaryKey
    @SerializedName("long_timestamp")
    Long timestamp;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getKwh() {
        return kwh;
    }

    public void setKwh(Double kwh) {
        this.kwh = kwh;
    }

    public Double getMediaPower() {
        return mediaPower;
    }

    public void setMediaPower(Double mediaPower) {
        this.mediaPower = mediaPower;
    }

    public Double getTime() {
        return time;
    }

    public void setTime(Double time) {
        this.time = time;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
