package hierachyconfig;

import lombok.Data;

import java.io.Serializable;

@Data
public class ColorRGB implements Serializable {
    int r;
    int g;
    int b;

    public ColorRGB(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }
}
