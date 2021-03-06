package me.phoboslabs.illuminati.illuminatigracefulshutdown.shutdown.configuration;

import me.phoboslabs.illuminati.illuminatigracefulshutdown.shutdown.ServerSignalHandler;
import me.phoboslabs.illuminati.illuminatigracefulshutdown.shutdown.exception.RequiredValueException;
import me.phoboslabs.illuminati.illuminatigracefulshutdown.shutdown.exception.SignalNotSupportException;
import me.phoboslabs.illuminati.illuminatigracefulshutdown.shutdown.handler.SpringBootShutdownHandler;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.ApplicationContext;

final class ShutdownHandlerConfiguration {

    private static final String SIGNAL_TYPE = "TERM";

    private final ApplicationContext applicationContext;
    private boolean initialized = false;

    ShutdownHandlerConfiguration(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        this.initShutdownHandler();
    }

    private ShutdownHandlerConfiguration initShutdownHandler () {
        if (this.applicationContext == null) {
            System.out.println("check your applicationContext.");
            return this;
        }
        try {
            ServletWebServerApplicationContext embeddedWebApplicationContext = (ServletWebServerApplicationContext) this.applicationContext;
            SpringBootShutdownHandler springBootShutdownHandler = new SpringBootShutdownHandler(embeddedWebApplicationContext);

            ServerSignalHandler.registShutdownHandler(SIGNAL_TYPE, springBootShutdownHandler);
            this.initialized = ServerSignalHandler.isInitialized();
        } catch (SignalNotSupportException sigex) {
            System.out.println("The illuminati grace shutdown filter is failed to initialize.");
            sigex.printStackTrace();
        } catch (RequiredValueException regex) {
            System.out.println("check your springBootShutdownHandler.");
            regex.printStackTrace();
        }
        return this;
    }

    public boolean isInitialized () {
        return this.initialized;
    }
}
