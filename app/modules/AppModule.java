package modules;

import com.google.inject.AbstractModule;
import services.LogFileService;

public class AppModule extends AbstractModule {

    @Override
    public void configure() {
        bind(LogFileService.class).asEagerSingleton();
    }
}
