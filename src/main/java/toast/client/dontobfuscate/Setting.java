package toast.client.dontobfuscate;

import com.google.gson.annotations.SerializedName;

public class Setting {
    @SerializedName("Current Mode")
    private String mode;
    @SerializedName("Enabled")
    private Boolean enabled;
    @SerializedName("Current Value")
    private Double value;

    public Setting(String mode) {
        setMode(mode);
    }

    public Setting(Double value) {
        setValue(value);
    }

    public Setting(Boolean enabled) {
        setEnabled(enabled);
    }

    public String getMode() {
        return this.mode;
    }

    public void setMode(String newMode) {
        this.mode = newMode;
        this.value = null;
        this.enabled = null;
    }

    public double getValue() {
        return this.value;
    }

    public void setValue(double newValue) {
        this.mode = null;
        this.value = newValue;
        this.enabled = null;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.mode = null;
        this.value = null;
        this.enabled = enabled;
    }

    public int getType() {
        if (mode != null) return 0;
        else if (value != null) return 1;
        else if (enabled != null) return 2;
        else return 3;
    }
}
