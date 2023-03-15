package hierachyconfig;

import com.intellij.openapi.options.ConfigurableBase;
import org.jetbrains.annotations.NotNull;

public class MyConfigEntry extends ConfigurableBase<MyConfigUI, MyConfigurable> {
    private final MyConfigurable settings;

    public MyConfigEntry() {
        this(MyConfigurable.getInstance());
    }

    public MyConfigEntry(@NotNull MyConfigurable settings) {
        super("hierachyconfig", "hierachyconfig.settings", "hierachyconfig");
        this.settings = settings;
    }

    @NotNull
    @Override
    protected MyConfigurable getSettings() {
        return settings;
    }

    @Override
    protected MyConfigUI createUi() {
        return new MyConfigUI(settings);
    }
}
