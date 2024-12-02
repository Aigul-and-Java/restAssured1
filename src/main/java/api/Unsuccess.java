package api;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Unsuccess {
    private String error;

    @JsonCreator
    public Unsuccess(@JsonProperty("error") String error) {
        this.error = error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

}
